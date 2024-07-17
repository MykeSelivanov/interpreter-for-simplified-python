package interpreter.parser;

import interpreter.lexer.Lexer;

public class BinaryOpNode extends ASTNode{
    public ASTNode left;
    public ASTNode right;
    public Lexer.Token operationToken;

    public BinaryOpNode(ASTNode left, ASTNode right, Lexer.Token operationToken) {
        this.left = left;
        this.right = right;
        this.operationToken = operationToken;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Op{" + operationToken.value + "}");
        left.print(indent + "  ");
        right.print(indent + "  ");
    }
}
