/**
 * Representa un componente léxico en el proceso de compilación.
 * Inmutable y contiene tipo, valor y posición del token en la entrada.
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */
public class Token {

    /**
     * Enumeración de tipos de tokens reconocidos por el compilador.
     */
    public enum Tipo {
        NUMERO,      // 0-9+
        OPERADOR,    // +, -, *, /
        PARENTESIS,  // (, )
        FIN          // Finaliza la entrada.
    }
    
    private final Tipo tipo;
    private final String valor;
    private final int posicion;

    /**
     * Constructor que crea un token inmutable.
     * 
     * @param tipo tipo del token
     * @param valor contenido textual del token
     * @param posicion posición en la entrada donde se encontró
     */
    public Token(Tipo tipo, String valor, int posicion) {
        this.tipo = tipo;
        this.valor = valor;
        this.posicion = posicion;
    }

    /**
     * @return tipo del token
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @return valor textual del token
     */
    public String getValor() {
        return valor;
    }

    /**
     * @return posición del token en la entrada
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Representación en string del token para depuración.
     * 
     * @return string formateado con tipo, valor y posición
     */
    @Override
    public String toString() {
        return String.format("Token{tipo=%s, valor='%s', pos=%d}", 
                           tipo, valor, posicion);
    }
}
