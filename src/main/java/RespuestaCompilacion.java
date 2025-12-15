/**
 * DTO para respuestas de compilación.
 */
public class RespuestaCompilacion {
    
    private boolean exitoso;
    private Double resultado;
    private String mensaje;
    private int tokensGenerados;
    private long tiempoMs;

    /**
     * Constructor por defecto sin parámetros.
     * Requerido para serialización/deserialización JSON.
     */
    public RespuestaCompilacion() {}

    /**
     * Método para crear una respuesta de compilación exitosa.
     * 
     * @param resultado valor numérico resultado de la evaluación
     * @param tokens cantidad de tokens generados en el análisis
     * @param tiempo tiempo en milisegundos de la operación
     * @return instancia de RespuestaCompilacion configurada como exitosa
     */
    public static RespuestaCompilacion exito(double resultado, int tokens, long tiempo) {
        RespuestaCompilacion resp = new RespuestaCompilacion();
        resp.exitoso = true;
        resp.resultado = resultado;
        resp.tokensGenerados = tokens;
        resp.tiempoMs = tiempo;
        resp.mensaje = "Compilación exitosa";
        return resp;
    }

    /**
     * Método para crear una respuesta de compilación fallida.
     * 
     * @param mensaje descripción del error ocurrido
     * @param tiempo tiempo en milisegundos hasta que ocurrió el fallo
     * @return instancia de RespuestaCompilacion configurada como error
     */
    public static RespuestaCompilacion error(String mensaje, long tiempo) {
        RespuestaCompilacion resp = new RespuestaCompilacion();
        resp.exitoso = false;
        resp.mensaje = mensaje;
        resp.tiempoMs = tiempo;
        return resp;
    }

    /**
     * @return true si la compilación fue exitosa, false en caso contrario
     */
    public boolean isExitoso() {
        return exitoso;
    }

    /**
     * @param exitoso establece si la compilación fue exitosa
     */
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    /**
     * @return resultado numérico de la evaluación, null si hubo error
     */
    public Double getResultado() {
        return resultado;
    }

    /**
     * @param resultado establece el valor numérico del resultado
     */
    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    /**
     * @return mensaje descriptivo del resultado o error
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje establece el mensaje descriptivo
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return cantidad de tokens generados durante el análisis
     */
    public int getTokensGenerados() {
        return tokensGenerados;
    }

    /**
     * @param tokensGenerados establece la cantidad de tokens generados
     */
    public void setTokensGenerados(int tokensGenerados) {
        this.tokensGenerados = tokensGenerados;
    }

    /**
     * @return tiempo en milisegundos de la operación
     */
    public long getTiempoMs() {
        return tiempoMs;
    }

    /**
     * @param tiempoMs establece el tiempo en milisegundos
     */
    public void setTiempoMs(long tiempoMs) {
        this.tiempoMs = tiempoMs;
    }
}
