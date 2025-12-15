/**
 * Solicitudes de compilación.
 */
public class SolicitudCompilacion {
    
    private String expresion;
    private String lenguaje;
    
    public SolicitudCompilacion() {}
    
    public SolicitudCompilacion(String expresion, String lenguaje) {
        this.expresion = expresion;
        this.lenguaje = lenguaje;
    }
    
    public String getExpresion() {
        return expresion;
    }
    
    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }
    
    public String getLenguaje() {
        return lenguaje;
    }
    
    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }
    
    public boolean esValida() {
        return expresion != null && !expresion.trim().isEmpty() &&
               lenguaje != null && !lenguaje.trim().isEmpty();
    }
}
