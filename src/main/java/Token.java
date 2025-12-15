/**
 * Representa un componente lexico en el proceso de compilacion.
 */
public class Token {
    
    public enum Tipo {
        NUMERO,      // 0-9+
        OPERADOR,    // +, -, *, /
        PARENTESIS,  // (, )
        FIN          // Finaliza la entrada.
    }
    
    private final Tipo tipo;
    private final String valor;
    private final int posicion;
    
    public Token(Tipo tipo, String valor, int posicion) {
        this.tipo = tipo;
        this.valor = valor;
        this.posicion = posicion;
    }
    
    public Tipo getTipo() {
        return tipo;
    }
    
    public String getValor() {
        return valor;
    }
    
    public int getPosicion() {
        return posicion;
    }
    
    @Override
    public String toString() {
        return String.format("Token{tipo=%s, valor='%s', pos=%d}", 
                           tipo, valor, posicion);
    }
}
