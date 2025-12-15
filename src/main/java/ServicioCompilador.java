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
 * Servicio REST con endpoints de compilación.
 */
public class ServicioCompilador {
    
    private final HttpServer servidor;
    private final Gson gson;
    private final TelemetriaManager telemetria;
    
    public ServicioCompilador(int puerto) throws IOException {
        this.servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        this.gson = new Gson();
        this.telemetria = TelemetriaManager.getInstance();
        
        configurarEndpoints();
        telemetria.log("INFO", "Servicio inicializado en puerto " + puerto);
    }
    
    private void configurarEndpoints() {
        servidor.createContext("/api/compilar", new HandlerCompilar());
        servidor.createContext("/api/metricas", new HandlerMetricas());
        servidor.createContext("/api/salud", new HandlerSalud());
    }
    
    public void iniciar() {
        servidor.start();
        telemetria.log("INFO", "Servidor iniciado");
    }
    
    public void detener() {
        servidor.stop(0);
        telemetria.log("INFO", "Servidor detenido");
    }
    
    private class HandlerCompilar implements HttpHandler {
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
    
    private class HandlerMetricas implements HttpHandler {
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
    
    private class HandlerSalud implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String respuesta = "{\"estado\":\"activo\",\"version\":\"1.0\"}";
            enviarRespuesta(exchange, 200, respuesta);
        }
    }
    
    private String leerCuerpo(InputStream input) throws IOException {
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }
    
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
