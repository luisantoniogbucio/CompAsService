import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Recolector de métricas específicas del dominio de compilación.
 * Thread-safe usando estructuras concurrentes.
 */
public class MetricasCompilacion {
    
    private final long tiempoInicioMs;
    
    private final AtomicLong totalRequests;
    private final AtomicLong requestsExitosos;
    private final AtomicLong requestsFallidos;
    private final AtomicLong sumaLatenciasMs;
    
    private final Map<String, AtomicLong> lenguajesUtilizados;
    private final AtomicLong totalTokensProcesados;
    private final AtomicLong erroresSintacticos;
    
    public MetricasCompilacion() {
        this.tiempoInicioMs = System.currentTimeMillis();
        this.totalRequests = new AtomicLong(0);
        this.requestsExitosos = new AtomicLong(0);
        this.requestsFallidos = new AtomicLong(0);
        this.sumaLatenciasMs = new AtomicLong(0);
        this.lenguajesUtilizados = new ConcurrentHashMap<>();
        this.totalTokensProcesados = new AtomicLong(0);
        this.erroresSintacticos = new AtomicLong(0);
    }
    
    public void registrarCompilacionExitosa(String lenguaje, int tokens, long latenciaMs) {
        totalRequests.incrementAndGet();
        requestsExitosos.incrementAndGet();
        sumaLatenciasMs.addAndGet(latenciaMs);
        totalTokensProcesados.addAndGet(tokens);
        
        lenguajesUtilizados
            .computeIfAbsent(lenguaje, k -> new AtomicLong(0))
            .incrementAndGet();
    }
    
    public void registrarCompilacionFallida(String lenguaje, long latenciaMs) {
        totalRequests.incrementAndGet();
        requestsFallidos.incrementAndGet();
        sumaLatenciasMs.addAndGet(latenciaMs);
        erroresSintacticos.incrementAndGet();
        
        lenguajesUtilizados
            .computeIfAbsent(lenguaje, k -> new AtomicLong(0))
            .incrementAndGet();
    }
    
    public ResultadoMetricas generarSnapshot() {
        ResultadoMetricas resultado = new ResultadoMetricas();
        
        long tiempoActivoMs = System.currentTimeMillis() - tiempoInicioMs;
        resultado.setTiempoActivoMs(tiempoActivoMs);
        
        long total = totalRequests.get();
        if (total > 0) {
            double latenciaPromedio = (double) sumaLatenciasMs.get() / total;
            resultado.setLatenciaPromedioMs(latenciaPromedio);
            
            double segundos = tiempoActivoMs / 1000.0;
            double throughput = segundos > 0 ? total / segundos : 0;
            resultado.setThroughput(throughput);
        } else {
            resultado.setLatenciaPromedioMs(0);
            resultado.setThroughput(0);
        }
        
        resultado.setTotalRequests(total);
        resultado.setRequestsExitosos(requestsExitosos.get());
        resultado.setRequestsFallidos(requestsFallidos.get());
        
        double tasaError = total > 0 
            ? (double) requestsFallidos.get() / total * 100 
            : 0;
        resultado.setTasaError(tasaError);
        
        Map<String, Long> distribucionLenguajes = new HashMap<>();
        lenguajesUtilizados.forEach((lenguaje, contador) -> 
            distribucionLenguajes.put(lenguaje, contador.get()));
        resultado.setLenguajesUtilizados(distribucionLenguajes);
        
        resultado.setTotalTokensProcesados(totalTokensProcesados.get());
        resultado.setErroresSintacticos(erroresSintacticos.get());
        
        return resultado;
    }
}
