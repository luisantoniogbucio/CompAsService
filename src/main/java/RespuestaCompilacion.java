/**
 * DTO para respuestas de compilación.
 */
public class RespuestaCompilacion {
    
    private boolean exitoso;
    private Double resultado;
    private String mensaje;
    private int tokensGenerados;
    private long tiempoMs;
    
    public RespuestaCompilacion() {}
    
    public static RespuestaCompilacion exito(double resultado, int tokens, long tiempo) {
        RespuestaCompilacion resp = new RespuestaCompilacion();
        resp.exitoso = true;
        resp.resultado = resultado;
        resp.tokensGenerados = tokens;
        resp.tiempoMs = tiempo;
        resp.mensaje = "Compilación exitosa";
        return resp;
    }
    
    public static RespuestaCompilacion error(String mensaje, long tiempo) {
        RespuestaCompilacion resp = new RespuestaCompilacion();
        resp.exitoso = false;
        resp.mensaje = mensaje;
        resp.tiempoMs = tiempo;
        return resp;
    }
    
    public boolean isExitoso() {
        return exitoso;
    }
    
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
    
    public Double getResultado() {
        return resultado;
    }
    
    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public int getTokensGenerados() {
        return tokensGenerados;
    }
    
    public void setTokensGenerados(int tokensGenerados) {
        this.tokensGenerados = tokensGenerados;
    }
    
    public long getTiempoMs() {
        return tiempoMs;
    }
    
    public void setTiempoMs(long tiempoMs) {
        this.tiempoMs = tiempoMs;
    }
}
