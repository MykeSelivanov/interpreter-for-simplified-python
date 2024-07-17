package interpreter.parser;

public class PrintStatementNode extends ASTNode{
    public String identifier;

    public PrintStatementNode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Print: " + identifier);
    }
}
