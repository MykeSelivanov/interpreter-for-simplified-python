package interpreter.interpreterimplementation;

import interpreter.parser.*;
import java.util.*;

public class Interpreter {
    private final Map<String, Integer> valueTable = new HashMap<>();

    public int interpret(ASTNode node) {
        return visit(node);
    }

    private int visit(ASTNode node) {
        if (node instanceof BinaryOpNode binaryOpNode) {
            int left = visit(binaryOpNode.left);
            int right = visit(binaryOpNode.right);
            switch (binaryOpNode.operationToken.value) {
                case "+" -> { return left + right; }
                case "-" -> { return left - right; }
                case "*" -> { return left * right; }
                case "/" -> { return left / right; }
                case ">" -> { return left > right ? 1 : 0; }
                case "<" -> { return left < right ? 1 : 0; }
                case ">=" -> { return left >= right ? 1 : 0; }
                case "<=" -> { return left <= right ? 1 : 0; }
                case "==" -> { return left == right ? 1 : 0; }
                case "!=" -> { return left != right ? 1 : 0; }
                default -> throw new InterpreterException("Unexpected operator: " + binaryOpNode.operationToken);
            }
        } else if (node instanceof NumberNode numberNode) {
            return numberNode.value;
        } else if (node instanceof AssignmentNode assignmentNode) {
            int value = visit(assignmentNode.expr);
            valueTable.put(assignmentNode.identifier, value);
            return value;
        } else if (node instanceof IdentifierNode identifierNode) {
            String varName = identifierNode.name;
            if (!valueTable.containsKey(varName)) {
                throw new InterpreterException("Variable not defined: " + varName);
            }
            return valueTable.get(varName);
        } else if (node instanceof IfStatementNode ifStatementNode) {
            if (visit(ifStatementNode.condition) != 0) {
                return visit(ifStatementNode.ifBlock);
            } else if (ifStatementNode.elseBlock != null) {
                return visit(ifStatementNode.elseBlock);
            }
        } else if (node instanceof PrintStatementNode printStatementNode) {
            String varName = printStatementNode.identifier;
            if (!valueTable.containsKey(varName)) {
                throw new InterpreterException("Variable not defined: " + varName);
            }
            System.out.println(valueTable.get(varName));
            return valueTable.get(varName);
        } else if (node instanceof StatementListNode statementListNode) {
            int result = 0;
            for (ASTNode statement : statementListNode.statements) {
                result = visit(statement);
            }
            return result;
        } else {
            throw new InterpreterException("Unexpected AST Node: " + node.getClass().getCanonicalName());
        }
        return 0;
    }
}
