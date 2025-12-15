import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Este compilador implementa un analizador léxico y sintáctico para evaluar
 * expresiones aritméticas con los operadores básicos (+, -, *, /) y paréntesis.</p>
 * 
 * <p>Gramática soportada:</p>
 * <pre>
 *   Expr   ::= Term (('+' | '-') Term)*
 *   Term   ::= Factor (('*' | '/') Factor)*
 *   Factor ::= Numero | '(' Expr ')'
 * </pre>
 * 
 */
public class CompiladorExpresiones {
    
    private String entrada;
    private int posicionActual;
    private List<Token> tokens;
    private int indiceParseo;

     /**
     * Compila y evalúa una expresión aritmética.
     * 
     * @param expresion la cadena que contiene la expresión aritmética a evaluar
     * @return el resultado numérico de evaluar la expresión
     * @throws Exception si la expresión es inválida, está mal formada
     *                   
     */
    public double compilar(String expresion) throws Exception {
        this.entrada = expresion;
        this.posicionActual = 0;
        this.tokens = new ArrayList<>();
        this.indiceParseo = 0;
        
        analizarLexico();
        double resultado = parsearExpresion();
        
        if (indiceParseo < tokens.size() - 1) {
            throw new Exception("Expresión mal formada: tokens sobrantes");
        }
        
        return resultado;
    }

      /**
     * Realiza el análisis léxico de la expresión de entrada.
     * 
     * Este método recorre la cadena de entrada carácter por carácter,
     * identificando y clasificando los tokens: números, operadores, paréntesis.
     * @throws Exception si se encuentra un carácter no reconocido en la entrada
     */
    private void analizarLexico() throws Exception {
        while (posicionActual < entrada.length()) {
            char c = entrada.charAt(posicionActual);
            
            if (Character.isWhitespace(c)) {
                posicionActual++;
                continue;
            }
            
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numero = new StringBuilder();
                int inicio = posicionActual;
                
                while (posicionActual < entrada.length() && 
                       (Character.isDigit(entrada.charAt(posicionActual)) || 
                        entrada.charAt(posicionActual) == '.')) {
                    numero.append(entrada.charAt(posicionActual));
                    posicionActual++;
                }
                
                tokens.add(new Token(Token.Tipo.NUMERO, numero.toString(), inicio));
                continue;
            }
            
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(new Token(Token.Tipo.OPERADOR, String.valueOf(c), posicionActual));
                posicionActual++;
                continue;
            }
            
            if (c == '(' || c == ')') {
                tokens.add(new Token(Token.Tipo.PARENTESIS, String.valueOf(c), posicionActual));
                posicionActual++;
                continue;
            }
            
            throw new Exception("Carácter no reconocido en posición " + posicionActual + ": " + c);
        }
        
        tokens.add(new Token(Token.Tipo.FIN, "", posicionActual));
    }

    /**
     * Parsea y evalúa una expresión aritmética completa.
     * 
     * <p>Implementa la regla gramatical: Expr ::= Term (('+' | '-') Term)*</p>
     * @return el resultado numérico de evaluar la expresión
     * @throws Exception si la expresión está mal formada o incompleta
     * 
     */
    private double parsearExpresion() throws Exception {
        double resultado = parsearTermino();
        
        while (indiceParseo < tokens.size()) {
            Token token = tokens.get(indiceParseo);
            
            if (token.getTipo() != Token.Tipo.OPERADOR) {
                break;
            }
            
            String op = token.getValor();
            if (!op.equals("+") && !op.equals("-")) {
                break;
            }
            
            indiceParseo++;
            double derecha = parsearTermino();
            
            if (op.equals("+")) {
                resultado += derecha;
            } else {
                resultado -= derecha;
            }
        }
        
        return resultado;
    }

    /**
     * Parsea y evalúa un término aritmético.
     * 
     * <p>Implementa la regla gramatical: Term ::= Factor (('*' | '/') Factor)*</p>
     * 
     * @return el resultado numérico de evaluar el término
     * @throws Exception si el término está mal formado, incompleto, 
     *                   o si se intenta realizar una división por cero
     */
    private double parsearTermino() throws Exception {
        double resultado = parsearFactor();
        
        while (indiceParseo < tokens.size()) {
            Token token = tokens.get(indiceParseo);
            
            if (token.getTipo() != Token.Tipo.OPERADOR) {
                break;
            }
            
            String op = token.getValor();
            if (!op.equals("*") && !op.equals("/")) {
                break;
            }
            
            indiceParseo++;
            double derecha = parsearFactor();
            
            if (op.equals("*")) {
                resultado *= derecha;
            } else {
                if (derecha == 0) {
                    throw new Exception("División por cero");
                }
                resultado /= derecha;
            }
        }
        
        return resultado;
    }

    /**
     * Parsea y evalúa un factor aritmético.
     * 
     * Implementa la regla gramatical: Factor ::= Numero | '(' Expr ')'
     * 
     * @return el resultado numérico de evaluar el factor
     * @throws Exception si el factor está mal formado, si se encuentra un token inesperado,
     *                   si hay paréntesis sin cerrar, o si el número no es válido
     *
     */
    private double parsearFactor() throws Exception {
        if (indiceParseo >= tokens.size()) {
            throw new Exception("Expresión incompleta");
        }
        
        Token token = tokens.get(indiceParseo);
        
        if (token.getTipo() == Token.Tipo.NUMERO) {
            indiceParseo++;
            try {
                return Double.parseDouble(token.getValor());
            } catch (NumberFormatException e) {
                throw new Exception("Número mal formado: " + token.getValor());
            }
        }
        
        if (token.getTipo() == Token.Tipo.PARENTESIS && token.getValor().equals("(")) {
            indiceParseo++;
            double resultado = parsearExpresion();
            
            if (indiceParseo >= tokens.size() || 
                !tokens.get(indiceParseo).getValor().equals(")")) {
                throw new Exception("Paréntesis sin cerrar");
            }
            
            indiceParseo++;
            return resultado;
        }
        
        throw new Exception("Factor esperado en posición " + token.getPosicion());
    }

    /**
     * Obtiene la lista de tokens generados durante el análisis léxico.
     * 
     * @return lista inmutable de tokens identificados en la expresión
     * 
     */
    public List<Token> getTokens() {
        return tokens;
    }
}
