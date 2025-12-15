/**
 * main de la aplicación CaaS compiler as a service, o compilador como servicio
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */

public class Main {    
    private static final int PUERTO_DEFECTO = 8080;

    /**
 * Clase principal de la aplicación CaaS (Compiler as a Service).
 * 
 * <p>CaaS es un servicio de compilación que expone un API REST para evaluar
 * expresiones aritméticas y proporcionar telemetría del sistema. Esta aplicación
 * implementa el patrón de arquitectura de microservicios para compilación.</p>
 * 
 * <p>El servicio proporciona tres endpoints principales:</p>
 * <ul>
 *   <li><b>POST /api/compilar</b> - Compila y evalúa expresiones aritméticas</li>
 *   <li><b>GET /api/metricas</b> - Obtiene métricas de uso y rendimiento</li>
 *   <li><b>GET /api/salud</b> - Verifica el estado del servicio (health check)</li>
 * </ul>
 * 
 * <p><b>Ejemplo de uso desde línea de comandos:</b></p>
 * <pre>
 * // Iniciar en puerto por defecto (8080)
 * java Main
 * 
 * // Iniciar en puerto personalizado
 * java Main 9090
 * </pre>
 * 
 * <p><b>Ejemplo de petición HTTP:</b></p>
 * <pre>
 * curl -X POST http://localhost:8080/api/compilar \
 *   -H 'Content-Type: application/json' \
 *   -d '{"expresion": "2 + 3 * 4", "lenguaje": "ARITMETICA"}'
 * </pre>
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 * @version 1.0
 * @since 2024
 * @see ServicioCompilador
 */
public class Main {
    
    /**
     * Puerto por defecto en el que escucha el servidor si no se especifica otro.
     * El valor estándar es 8080, comúnmente usado para aplicaciones web.
     */
    private static final int PUERTO_DEFECTO = 8080;
    
    /**
     * Punto de entrada principal de la aplicación CaaS.
     * 
     * Este método inicializa y arranca el servicio de compilación, configurando
     * el puerto de escucha y mostrando información relevante sobre los endpoints
     * disponibles y ejemplos de uso.
     * @param args argumentos de línea de comandos.
     * @throws InterruptedException si el hilo principal es interrumpido mientras espera.                             
     * @throws Exception si ocurre un error al inicializar o arrancar el servicio.
     */
    public static void main(String[] args) {
        int puerto=PUERTO_DEFECTO;
        
        if (args.length>0){
            try {
                puerto = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Puerto inválido: " + args[0]);
                System.err.println("Usando puerto por defecto: " + PUERTO_DEFECTO);
            }
        }
        
        try {
            ServicioCompilador servicio = new ServicioCompilador(puerto);
            servicio.iniciar();
            System.out.println("Compilador como Servicio con Telemetría");
            System.out.println("Proyecto 02 - Compiladores");
            System.out.println();
            System.out.println("Servidor escuchando en puerto: " + puerto);
            System.out.println();
            System.out.println("Endpoints disponibles:");
            System.out.println("  POST http://localhost:" + puerto + "/api/compilar");
            System.out.println("  GET  http://localhost:" + puerto + "/api/metricas");
            System.out.println("  GET  http://localhost:" + puerto + "/api/salud");
            System.out.println();
            System.out.println("Ejemplo de uso:");
            System.out.println("  curl -X POST http://localhost:" + puerto + "/api/compilar \\");
            System.out.println("    -H 'Content-Type: application/json' \\");
            System.out.println("    -d '{\"expresion\": \"2 + 3 * 4\", \"lenguaje\": \"ARITMETICA\"}'");
            System.out.println();
            System.out.println("Presiona Ctrl C para detener el servicio");
            
            Thread.currentThread().join();
            
        } catch (Exception e) {

            System.err.println("Err al iniciar el servicio: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);

        }
    }
}
