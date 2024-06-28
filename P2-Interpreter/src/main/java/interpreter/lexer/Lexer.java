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

    private void tokenize() {
        while (current < input.length()) {
            char ch = input.charAt(current);
            switch (ch) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    current++;
                    break;
            }
        }
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    @Override
    public void forEach(Consumer<? super Token> action) {
        for (Token token : tokens) {
            action.accept(token);
        }    }


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

    }
}
