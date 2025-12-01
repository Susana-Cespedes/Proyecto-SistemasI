import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    private List<Token> tokens;
    private int current = 0;

    // Tabla de símbolos para análisis semántico (Nombre de variable)
    private Set<String> symbolTable = new HashSet<>();

    // Buffer para el código intermedio generado
    private StringBuilder outputCode = new StringBuilder();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Objetivo: Procesar el programa completo
    public String parse() {

        while (!isAtEnd()) {
            statement();
        }
        return outputCode.toString();
    }

    // Regla: Statement -> Declaracion | Impresion
    private void statement() {
        if (match(TokenType.INT)) {
            declaration();
        } else if (match(TokenType.PRINT)) {
            printStatement();
        } else {
            throw new RuntimeException("Esperaba 'int' o 'print' pero encontré: " + peek().type);
        }
    }

    // Regla: Declaracion -> int ID = Expresion ;
    private void declaration() {
        // El token anterior fue INT, ahora esperamos ID
        Token idToken = consume(TokenType.ID, "Esperaba nombre de variable");

        // Validación Semántica 1: ¿Ya existe la variable?
        if (symbolTable.contains(idToken.value)) {
            throw new RuntimeException("Error Semántico: Variable '" + idToken.value + "' ya declarada.");
        }
        symbolTable.add(idToken.value); // Registrar variable

        consume(TokenType.ASSIGN, "Esperaba '='");
        expression(); // Genera código para calcular el valor
        consume(TokenType.SEMICOLON, "Esperaba ';'");

        // Generación de Código: Guardar el valor de la pila en la variable
        emit("STORE " + idToken.value);
    }

    // Regla: Impresion -> print Expresion ;
    private void printStatement() {
        expression(); // Genera código para calcular lo que se va a imprimir
        consume(TokenType.SEMICOLON, "Esperaba ';'");
        emit("PRINT");
    }

    // Regla Simplificada de Expresión (Solo sumas y restas básicas para el ejemplo)
    private void expression() {
        term();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            Token operator = advance();
            term();
            // Generación de Código: Operación
            if (operator.type == TokenType.PLUS) emit("ADD");
            else emit("SUB");
        }
    }

    private void term() {
        if (check(TokenType.NUMBER)) {
            Token num = advance();
            emit("PUSH " + num.value);
        } else if (check(TokenType.ID)) {
            Token id = advance();
            // Validación Semántica 2: ¿La variable existe?
            if (!symbolTable.contains(id.value)) {
                throw new RuntimeException("Error Semántico: Variable '" + id.value + "' no declarada.");
            }
            emit("LOAD " + id.value);
        } else {
            throw new RuntimeException("Esperaba número o variable");
        }
    }

    // --- Métodos Auxiliares ---
    private void emit(String instruction) {
        outputCode.append(instruction).append("\n");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message);
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }
}
