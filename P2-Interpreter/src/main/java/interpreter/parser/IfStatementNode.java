package interpreter.parser;

public class IfStatementNode extends ASTNode {
    public ASTNode condition;
    public ASTNode ifBlock;
    public ASTNode elseBlock;

    public IfStatementNode(ASTNode condition, ASTNode ifBlock, ASTNode elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "If Statement");
        System.out.println(indent + "  Condition:");
        condition.print(indent + "    ");
        System.out.println(indent + "  If Block:");
        ifBlock.print(indent + "    ");
        if (elseBlock != null) {
            System.out.println(indent + "  Else Block:");
            elseBlock.print(indent + "    ");
        }
    }
}
