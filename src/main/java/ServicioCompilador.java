import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.opentelemetry.api.trace.Span;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * Servicio REST que expone endpoints HTTP para compilación de expresiones.
 * Implementa tres endpoints principales: compilar, métricas y salud.
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */
public class ServicioCompilador {
    
    private final HttpServer servidor;
    private final Gson gson;
    private final TelemetriaManager telemetria;

    /**
     * Constructor que crea e inicializa el servidor HTTP.
     * 
     * @param puerto número de puerto donde escuchará el servidor
     * @throws IOException si no se puede crear el servidor en el puerto especificado
     */
    public ServicioCompilador(int puerto) throws IOException {
        this.servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        this.gson = new Gson();
        this.telemetria = TelemetriaManager.getInstance();
        
        configurarEndpoints();
        telemetria.log("INFO", "Servicio inicializado en puerto " + puerto);
    }

    /**
     * Configura los tres endpoints REST del servicio.
     * POST /api/compilar, GET /api/metricas, GET /api/salud
     */
    private void configurarEndpoints() {
        servidor.createContext("/api/compilar", new HandlerCompilar());
        servidor.createContext("/api/metricas", new HandlerMetricas());
        servidor.createContext("/api/salud", new HandlerSalud());
    }

    /**
     * Inicia el servidor HTTP y comienza a aceptar peticiones.
     */
    public void iniciar() {
        servidor.start();
        telemetria.log("INFO", "Servidor iniciado");
    }

    /**
     * Detiene el servidor HTTP inmediatamente.
     */
    public void detener() {
        servidor.stop(0);
        telemetria.log("INFO", "Servidor detenido");
    }

    /**
     * Handler interno para el endpoint POST /api/compilar.
     * Recibe expresiones aritméticas, las compila y retorna el resultado.
     */
    private class HandlerCompilar implements HttpHandler {
        
        /**
         * Procesa peticiones POST de compilación.
         * 
         * @param exchange objeto HttpExchange con la petición y respuesta HTTP
         * @throws IOException si hay error al leer o escribir la respuesta
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "{\"error\":\"Método no permitido\"}");
                return;
            }
            
            long inicioMs = System.currentTimeMillis();
            Span span = telemetria.iniciarSpan("compilar_expresion");
            
            try {
                String cuerpo = leerCuerpo(exchange.getRequestBody());
                SolicitudCompilacion solicitud = gson.fromJson(cuerpo, SolicitudCompilacion.class);
                
                if (solicitud == null || !solicitud.esValida()) {
                    long tiempoMs = System.currentTimeMillis() - inicioMs;
                    RespuestaCompilacion resp = RespuestaCompilacion.error(
                        "Solicitud inválida", tiempoMs);
                    telemetria.registrarCompilacionFallida("DESCONOCIDO", tiempoMs);
                    enviarRespuesta(exchange, 400, gson.toJson(resp));
                    telemetria.finalizarSpanExitoso(span);
                    return;
                }
                
                CompiladorExpresiones compilador = new CompiladorExpresiones();
                double resultado = compilador.compilar(solicitud.getExpresion());
                
                long tiempoMs = System.currentTimeMillis() - inicioMs;
                int tokens = compilador.getTokens().size();
                
                telemetria.registrarCompilacionExitosa(
                    solicitud.getLenguaje(), tokens, tiempoMs);
                
                RespuestaCompilacion resp = RespuestaCompilacion.exito(
                    resultado, tokens, tiempoMs);
                
                enviarRespuesta(exchange, 200, gson.toJson(resp));
                telemetria.finalizarSpanExitoso(span);
                
            } catch (JsonSyntaxException e) {
                long tiempoMs = System.currentTimeMillis() - inicioMs;
                RespuestaCompilacion resp = RespuestaCompilacion.error(
                    "JSON mal formado", tiempoMs);
                telemetria.registrarCompilacionFallida("DESCONOCIDO", tiempoMs);
                enviarRespuesta(exchange, 400, gson.toJson(resp));
                telemetria.finalizarSpanConError(span, e);
                
            } catch (Exception e) {
                long tiempoMs = System.currentTimeMillis() - inicioMs;
                RespuestaCompilacion resp = RespuestaCompilacion.error(
                    "Error: " + e.getMessage(), tiempoMs);
                telemetria.registrarCompilacionFallida("ARITMETICA", tiempoMs);
                enviarRespuesta(exchange, 422, gson.toJson(resp));
                telemetria.finalizarSpanConError(span, e);
            }
        }
    }

    /**
     * Handler interno para el endpoint GET /api/metricas.
     * Retorna estadísticas acumuladas del sistema.
     */
    private class HandlerMetricas implements HttpHandler {

        /**
         * Procesa peticiones GET de métricas.
         * 
         * @param exchange objeto HttpExchange con la petición y respuesta HTTP
         * @throws IOException si hay error al escribir la respuesta
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "{\"error\":\"Método no permitido\"}");
                return;
            }
            
            ResultadoMetricas metricas = telemetria.getMetricas().generarSnapshot();
            enviarRespuesta(exchange, 200, gson.toJson(metricas));
        }
    }

    /**
     * Handler interno para el endpoint GET /api/salud.
     * Verifica que el servicio esté activo (health check).
     */
    private class HandlerSalud implements HttpHandler {
        /**
         * Procesa peticiones GET de verificación de salud.
         * 
         * @param exchange objeto HttpExchange con la petición y respuesta HTTP
         * @throws IOException si hay error al escribir la respuesta
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String respuesta = "{\"estado\":\"activo\",\"version\":\"1.0\"}";
            enviarRespuesta(exchange, 200, respuesta);
        }
    }

    /**
     * Lee el cuerpo completo de una petición HTTP.
     * 
     * @param input stream de entrada de la petición HTTP
     * @return string con el contenido del cuerpo en UTF-8
     * @throws IOException si hay error al leer el stream
     */
    private String leerCuerpo(InputStream input) throws IOException {
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Envía una respuesta HTTP en formato JSON.
     * 
     * @param exchange objeto HttpExchange para enviar la respuesta
     * @param codigo código de estado HTTP (200, 400, 422, etc.)
     * @param json contenido de la respuesta en formato JSON
     * @throws IOException si hay error al escribir la respuesta
     */
    private void enviarRespuesta(HttpExchange exchange, int codigo, String json) 
            throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(codigo, bytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
