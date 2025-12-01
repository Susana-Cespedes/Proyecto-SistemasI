import java.util.*;
import java.util.regex.*;

class Lexer {
    private String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }

            if (Character.isDigit(current)) {
                tokens.add(new Token(TokenType.NUMBER, consumeWhile(Character::isDigit)));
            } else if (Character.isLetter(current)) {
                String word = consumeWhile(Character::isLetter);
                switch (word) {
                    case "int":
                        tokens.add(new Token(TokenType.INT, word));
                        break;
                    case "print":
                        tokens.add(new Token(TokenType.PRINT, word));
                        break;
                    default:
                        tokens.add(new Token(TokenType.ID, word));
                        break;
                }
            } else {
                switch (current) {
                    case '=':
                        tokens.add(new Token(TokenType.ASSIGN, "="));
                        break;
                    case '+':
                        tokens.add(new Token(TokenType.PLUS, "+"));
                        break;
                    case '-':
                        tokens.add(new Token(TokenType.MINUS, "-"));
                        break;
                    case ';':
                        tokens.add(new Token(TokenType.SEMICOLON, ";"));
                        break;
                    default:
                        throw new RuntimeException("Caracter desconocido: " + current);
                }
                pos++;
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private String consumeWhile(java.util.function.Predicate<Character> condition) {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && condition.test(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }
}
