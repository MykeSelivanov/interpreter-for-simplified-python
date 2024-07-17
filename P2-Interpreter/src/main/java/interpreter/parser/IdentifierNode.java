package interpreter.parser;

public class IdentifierNode extends ASTNode{
    public final String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Id{" + name + "}");
    }
}
