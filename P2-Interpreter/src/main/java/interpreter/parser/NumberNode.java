package interpreter.parser;

import interpreter.lexer.Lexer;

public class NumberNode extends ASTNode{
    public final int value;
    Lexer.Token numberToken;

    public NumberNode(Lexer.Token numberToken) {
        this.numberToken = numberToken;
        this.value = Integer.parseInt(numberToken.value);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "N{" + value + "}");
    }
}
