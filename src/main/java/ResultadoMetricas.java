import java.util.Map;

/**
 * DTO (Data Transfer Object) para exportar métricas acumuladas del sistema.
 * Contiene estadísticas de rendimiento, uso y errores del servicio de compilación.
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
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

    /**
     * Constructor por defecto sin parámetros.
     * Requerido para serialización/deserialización JSON.
     */
    public ResultadoMetricas() {}

    /**
     * @return tiempo activo del servicio en milisegundos
     */
    public long getTiempoActivoMs() {
        return tiempoActivoMs;
    }

    /**
     * @param tiempoActivoMs establece el tiempo activo en milisegundos
     */
    public void setTiempoActivoMs(long tiempoActivoMs) {
        this.tiempoActivoMs = tiempoActivoMs;
    }

    /**
     * @return latencia promedio en milisegundos
     */
    public double getLatenciaPromedioMs() {
        return latenciaPromedioMs;
    }

    /**
     * @param latenciaPromedioMs establece la latencia promedio
     */
    public void setLatenciaPromedioMs(double latenciaPromedioMs) {
        this.latenciaPromedioMs = latenciaPromedioMs;
    }

    /**
     * @return throughput en requests por segundo
     */
    public double getThroughput() {
        return throughput;
    }

    /**
     * @param throughput establece el throughput del sistema
     */
    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    /**
     * @return total de requests recibidos
     */
    public long getTotalRequests() {
        return totalRequests;
    }

    /**
     * @param totalRequests establece el total de requests
     */
    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    /**
     * @return cantidad de compilaciones exitosas
     */
    public long getRequestsExitosos() {
        return requestsExitosos;
    }

    /**
     * @param requestsExitosos establece la cantidad de éxitos
     */
    public void setRequestsExitosos(long requestsExitosos) {
        this.requestsExitosos = requestsExitosos;
    }

    /**
     * @return cantidad de compilaciones fallidas
     */
    public long getRequestsFallidos() {
        return requestsFallidos;
    }

    /**
     * @param requestsFallidos establece la cantidad de fallos
     */
    public void setRequestsFallidos(long requestsFallidos) {
        this.requestsFallidos = requestsFallidos;
    }

    /**
     * @return tasa de error en porcentaje (0-100)
     */
    public double getTasaError() {
        return tasaError;
    }

    /**
     * @param tasaError establece la tasa de error
     */
    public void setTasaError(double tasaError) {
        this.tasaError = tasaError;
    }

    /**
     * @return mapa con lenguajes y cantidad de usos
     */
    public Map<String, Long> getLenguajesUtilizados() {
        return lenguajesUtilizados;
    }

    /**
     * @param lenguajesUtilizados establece el mapa de lenguajes
     */
    public void setLenguajesUtilizados(Map<String, Long> lenguajesUtilizados) {
        this.lenguajesUtilizados = lenguajesUtilizados;
    }

    /**
     * @return total de tokens procesados
     */
    public long getTotalTokensProcesados() {
        return totalTokensProcesados;
    }

    /**
     * @param totalTokensProcesados establece el total de tokens
     */
    public void setTotalTokensProcesados(long totalTokensProcesados) {
        this.totalTokensProcesados = totalTokensProcesados;
    }

    /**
     * @return cantidad de errores sintácticos detectados
     */
    public long getErroresSintacticos() {
        return erroresSintacticos;
    }

    /**
     * @param erroresSintacticos establece la cantidad de errores
     */
    public void setErroresSintacticos(long erroresSintacticos) {
        this.erroresSintacticos = erroresSintacticos;
    }
}
