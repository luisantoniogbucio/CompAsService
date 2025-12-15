/**
 * main de la aplicación CaaS compiler as a service, o compilador como servicio
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */

public class Main {    
    private static final int PUERTO_DEFECTO = 8080;
    
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
