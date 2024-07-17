package interpreter;

import interpreter.interpreterimplementation.Interpreter;
import interpreter.lexer.Lexer;
import interpreter.lexer.LexerException;
import interpreter.parser.ASTNode;
import interpreter.parser.Parser;
import interpreter.parser.ParserException;
import interpreter.semanticanalazyer.SemanticAnalyzer;

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
            System.out.println("\n=== Lexer ===\n");
            Lexer lexer = new Lexer(input);
            lexer.forEach(System.out::println);

            System.out.println("\n=== Parser ===\n");
            Parser parser = new Parser(lexer.tokens);
            ASTNode rootNode = parser.parse();
            rootNode.print("");

            System.out.println("\n=== Semantic Analysis ===\n");
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            semanticAnalyzer.analyze(rootNode);
            System.out.println("Semantic analysis passed.");

            System.out.println("\n=== Interpreter ===\n");
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(rootNode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
