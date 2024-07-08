package interpreter.parser;

import java.util.ArrayList;
import java.util.List;

public class StatementListNode extends ASTNode{
    private final List<ASTNode> statements;

    public StatementListNode() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(ASTNode statement) {
        statements.add(statement);
    }

    @Override
    public void print(String indent) {
        for (ASTNode statement : statements) {
            statement.print(indent);
        }
    }
}
