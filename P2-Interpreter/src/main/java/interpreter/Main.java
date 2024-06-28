package interpreter;

import interpreter.lexer.Lexer;
import interpreter.lexer.LexerException;

public class Main {
    public static void main(String[] args) {
        String input = """
                x = 5;
                if (x > 3) {
                    y = x + 2;
                } else {
                    y = x * (2 + 3);
                }
                print y;
                """;

        try {
            Lexer lexer = new Lexer(input);
            lexer.forEach(token -> System.out.println(token));
        } catch (LexerException e) {
            throw new RuntimeException(e);
        }
    }
}
