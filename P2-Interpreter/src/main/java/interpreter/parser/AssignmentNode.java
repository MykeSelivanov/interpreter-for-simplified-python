package interpreter.parser;

public class AssignmentNode extends ASTNode {
    String identifier;
    ASTNode expr;

    public AssignmentNode(String identifier, ASTNode expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Assignment: " + identifier + " = ");
        expr.print(indent + "  ");
    }
}
