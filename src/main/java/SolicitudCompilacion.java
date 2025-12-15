/**
 * DTO (Data Transfer Object) para encapsular solicitudes de compilación.
 * Contiene la expresión a compilar y el lenguaje objetivo.
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */
public class SolicitudCompilacion {
    
    private String expresion;
    private String lenguaje;

    /**
     * Constructor por defecto sin parámetros.
     * Requerido para serialización/deserialización JSON.
     */
    public SolicitudCompilacion() {}

    /**
     * Constructor con parámetros para crear una solicitud completa.
     * 
     * @param expresion expresión aritmética a compilar
     * @param lenguaje lenguaje de la expresión
     */
    public SolicitudCompilacion(String expresion, String lenguaje) {
        this.expresion = expresion;
        this.lenguaje = lenguaje;
    }

    /**
     * @return expresión aritmética a compilar
     */
    public String getExpresion() {
        return expresion;
    }

    /**
     * @param expresion establece la expresión a compilar
     */
    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    /**
     * @return lenguaje de la expresión
     */
    public String getLenguaje() {
        return lenguaje;
    }

    /**
     * @param lenguaje establece el lenguaje
     */
    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    /**
     * Valida que la solicitud tenga expresión y lenguaje no vacíos.
     * 
     * @return true si ambos campos son válidos, false en caso contrario
     */
    public boolean esValida() {
        return expresion != null && !expresion.trim().isEmpty() &&
               lenguaje != null && !lenguaje.trim().isEmpty();
    }
}
