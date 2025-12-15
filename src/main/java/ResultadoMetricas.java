import java.util.Map;

/**
 * DTO para exportar métricas acumuladas del sistema.
 */
public class ResultadoMetricas {
    
    private long tiempoActivoMs;
    private double latenciaPromedioMs;
    private double throughput;
    
    private long totalRequests;
    private long requestsExitosos;
    private long requestsFallidos;
    private double tasaError;
    
    private Map<String, Long> lenguajesUtilizados;
    private long totalTokensProcesados;
    private long erroresSintacticos;
    
    public ResultadoMetricas() {}
    
    public long getTiempoActivoMs() {
        return tiempoActivoMs;
    }
    
    public void setTiempoActivoMs(long tiempoActivoMs) {
        this.tiempoActivoMs = tiempoActivoMs;
    }
    
    public double getLatenciaPromedioMs() {
        return latenciaPromedioMs;
    }
    
    public void setLatenciaPromedioMs(double latenciaPromedioMs) {
        this.latenciaPromedioMs = latenciaPromedioMs;
    }
    
    public double getThroughput() {
        return throughput;
    }
    
    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }
    
    public long getTotalRequests() {
        return totalRequests;
    }
    
    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }
    
    public long getRequestsExitosos() {
        return requestsExitosos;
    }
    
    public void setRequestsExitosos(long requestsExitosos) {
        this.requestsExitosos = requestsExitosos;
    }
    
    public long getRequestsFallidos() {
        return requestsFallidos;
    }
    
    public void setRequestsFallidos(long requestsFallidos) {
        this.requestsFallidos = requestsFallidos;
    }
    
    public double getTasaError() {
        return tasaError;
    }
    
    public void setTasaError(double tasaError) {
        this.tasaError = tasaError;
    }
    
    public Map<String, Long> getLenguajesUtilizados() {
        return lenguajesUtilizados;
    }
    
    public void setLenguajesUtilizados(Map<String, Long> lenguajesUtilizados) {
        this.lenguajesUtilizados = lenguajesUtilizados;
    }
    
    public long getTotalTokensProcesados() {
        return totalTokensProcesados;
    }
    
    public void setTotalTokensProcesados(long totalTokensProcesados) {
        this.totalTokensProcesados = totalTokensProcesados;
    }
    
    public long getErroresSintacticos() {
        return erroresSintacticos;
    }
    
    public void setErroresSintacticos(long erroresSintacticos) {
        this.erroresSintacticos = erroresSintacticos;
    }
}
