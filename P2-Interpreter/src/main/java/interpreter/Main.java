package interpreter;

import interpreter.lexer.Lexer;
import interpreter.lexer.LexerException;
import interpreter.parser.ASTNode;
import interpreter.parser.Parser;
import interpreter.parser.ParserException;

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
            lexer.forEach(System.out::println);
            Parser parser = new Parser(lexer.tokens);
            ASTNode rootNode = parser.parse();
            rootNode.print("");
        } catch (LexerException | ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
