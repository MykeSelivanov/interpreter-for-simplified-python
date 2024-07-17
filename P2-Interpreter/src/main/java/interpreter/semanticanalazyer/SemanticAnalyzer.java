package interpreter.semanticanalazyer;

import interpreter.parser.*;
import java.util.*;

public class SemanticAnalyzer {

    private Stack<Map<String, Boolean>> scopes = new Stack<>();
    private Set<String> globalScope = new HashSet<>();

    public void analyze(ASTNode node) {
        scopes.push(new HashMap<>());
        visit(node);
        scopes.pop();
    }

    private void visit(ASTNode node) {
        if (node instanceof BinaryOpNode binaryOpNode) {
            visit(binaryOpNode.left);
            visit(binaryOpNode.right);
        } else if (node instanceof NumberNode) {
            // Nothing to do for number nodes
        } else if (node instanceof AssignmentNode assignmentNode) {
            String varName = assignmentNode.identifier;
            visit(assignmentNode.expr);
            defineVariable(varName);
        } else if (node instanceof IdentifierNode identifierNode) {
            String varName = identifierNode.name;
            if (!isVariableDefined(varName)) {
                throw new SemanticException("Variable not defined: " + varName);
            }
        } else if (node instanceof IfStatementNode ifStatementNode) {
            visit(ifStatementNode.condition);
            // Process the if-block with a new scope
            scopes.push(new HashMap<>());
            visit(ifStatementNode.ifBlock);
            scopes.pop();
            // Process the else-block with a new scope, if it exists
            if (ifStatementNode.elseBlock != null) {
                scopes.push(new HashMap<>());
                visit(ifStatementNode.elseBlock);
                scopes.pop();
            }
        } else if (node instanceof PrintStatementNode printStatementNode) {
            String varName = printStatementNode.identifier;
            if (!isVariableDefined(varName)) {
                throw new SemanticException("Variable not defined: " + varName);
            }
        } else if (node instanceof StatementListNode statementListNode) {
            for (ASTNode statement : statementListNode.statements) {
                visit(statement);
            }
        } else {
            throw new SemanticException("Unexpected AST Node: " + node.getClass().getCanonicalName());
        }
    }

    private void defineVariable(String varName) {
        scopes.peek().put(varName, true);
        globalScope.add(varName); // Ensure the variable is recognized globally
    }

    private boolean isVariableDefined(String varName) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(varName)) {
                return true;
            }
        }
        return globalScope.contains(varName);
    }
}
