import java.util.ArrayList;
import java.util.List;

/**
 * Compilador simple de expresiones aritméticas.
 * Gramática:
 *   Expr   ::= Term (('+' | '-') Term)*
 *   Term   ::= Factor (('*' | '/') Factor)*
 *   Factor ::= Numero | '(' Expr ')'
 */
public class CompiladorExpresiones {
    
    private String entrada;
    private int posicionActual;
    private List<Token> tokens;
    private int indiceParseo;
    
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
    
    public List<Token> getTokens() {
        return tokens;
    }
}
