package interpreter.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Lexer implements Iterable<Lexer.Token> {
    private final String input;
    private final List<Token> tokens;
    private int current;

    public Lexer(String input) throws LexerException {
        this.input = input;
        this.tokens = new ArrayList<Token>();
        this.current = 0;
        tokenize();
    }

    private void tokenize() throws LexerException {
        while (current < input.length()) {
            char ch = input.charAt(current);
            switch (ch) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    current++;
                    break;
                case '=':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.EQUAL, "=="));
                        current += 2;
                    } else {
                        tokens.add(new Token(TokenType.ASSIGNMENT, "="));
                        current++;
                    }
                    break;
                case '!':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.NOT_EQUAL, "!="));
                        current += 2;
                    } else {
                        throw new LexerException("Unsupported character: " + ch);
                    }
                    break;
                case '>':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.GREATER_EQUAL, ">="));
                        current += 2;
                    } else {
                        tokens.add(new Token(TokenType.GREATER_THAN, ">"));
                        current++;
                    }
                    break;
                case '<':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.LESS_EQUAL, "<="));
                        current += 2;
                    } else {
                        tokens.add(new Token(TokenType.LESS_THAN, "<"));
                        current++;
                    }
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    tokens.add(new Token(TokenType.OPERATOR, Character.toString(ch)));
                    current++;
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, "{"));
                    current++;
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, "}"));
                    current++;
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "("));
                    current++;
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")"));
                    current++;
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";"));
                    current++;
                    break;
                default:
                    if (isDigit(ch)) {
                        tokens.add(new Token(TokenType.NUMBER, readNumber()));
                    } else if (isAlpha(ch)) {
                        String identifier = readIdentifier();
                        tokens.add(new Token(deriveTokenType(identifier), identifier));
                    } else {
                        throw new LexerException("Unsupported character: " + ch);
                    }
            }
        }
    }

    private char peek() {
        return (current + 1 < input.length()) ? input.charAt(current + 1) : '\0';
    }

    private TokenType deriveTokenType(String identifier) {
        return switch (identifier) {
            case "if" -> TokenType.IF;
            case "else" -> TokenType.ELSE;
            case "print" -> TokenType.PRINT;
            default -> TokenType.IDENTIFIER;
        };
    }

    private String readIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && isAlphaNumeric(input.charAt(current))) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private String readNumber() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && isDigit(input.charAt(current))) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    @Override
    public void forEach(Consumer<? super Token> action) {
        for (Token token : tokens) {
            action.accept(token);
        }
    }

    public static class Token {
        final TokenType type;
        final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public enum TokenType {
        EQUAL, ASSIGNMENT, NOT_EQUAL, GREATER_EQUAL, GREATER_THAN, LESS_EQUAL, LESS_THAN, OPERATOR,
        LBRACE, RBRACE, SEMICOLON, IF, ELSE, PRINT, IDENTIFIER, LPAREN, RPAREN, NUMBER
    }
}
