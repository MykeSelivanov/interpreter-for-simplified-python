package interpreter.parser;

import interpreter.lexer.Lexer;

import java.util.List;

public class Parser {
    private final List<Lexer.Token> tokens;
    private int currentPos;
    private Lexer.Token currentToken;

    public Parser(List<Lexer.Token> tokens) {
        this.tokens = tokens;
        currentPos = 0;
        currentToken = tokens.get(currentPos);
    }

    public ASTNode parse() {
        return program();
    }

    private ASTNode program() {
        StatementListNode statements = new StatementListNode();
        while (currentToken != null) {
            statements.addStatement(statement());
        }
        return statements;
    }

    private ASTNode statement() {
        ASTNode result;
        if (currentToken.type == Lexer.TokenType.IDENTIFIER) {
            result = assignment();
        } else if (currentToken.type == Lexer.TokenType.IF) {
            result = ifStatement();
        } else if (currentToken.type == Lexer.TokenType.PRINT) {
            result = printStatement();
        } else {
            throw new ParserException("Unexpected token: " + currentToken);
        }

        if (currentToken != null && currentToken.type != Lexer.TokenType.SEMICOLON) {
            throw new ParserException("Expected semicolon after statement");
        }
        consume(Lexer.TokenType.SEMICOLON);

        return result;
    }

    private ASTNode assignment() {
        Lexer.Token identifier = currentToken;
        consume(Lexer.TokenType.IDENTIFIER);
        consume(Lexer.TokenType.ASSIGNMENT);
        ASTNode expr = expression();
        return new AssignmentNode(identifier.value, expr);
    }

    private ASTNode ifStatement() {
        consume(Lexer.TokenType.IF);
        consume(Lexer.TokenType.LPAREN);
        ASTNode condition = relationalExpression();
        consume(Lexer.TokenType.RPAREN);
        consume(Lexer.TokenType.LBRACE);

        ASTNode ifBlock = program();

        consume(Lexer.TokenType.RBRACE); // Consume the closing brace for the if block

        // Check if there's an else block
        ASTNode elseBlock = null;
        if (currentToken != null && currentToken.type == Lexer.TokenType.ELSE) {
            consume(Lexer.TokenType.ELSE);

            // Check if the else block is another if statement (else if)
            if (currentToken.type == Lexer.TokenType.IF) {
                elseBlock = ifStatement(); // handle else if
            } else {
                // Otherwise, it's a normal block
                elseBlock = block(); // handle else
            }
        }

        return new IfStatementNode(condition, ifBlock, elseBlock);
    }

    private ASTNode block() {
        consume(Lexer.TokenType.LBRACE);
        ASTNode blockStatements = program();
        consume(Lexer.TokenType.RBRACE);
        return blockStatements;
    }

    private ASTNode printStatement() {
        consume(Lexer.TokenType.PRINT);
        Lexer.Token identifier = currentToken;
        consume(Lexer.TokenType.IDENTIFIER);
        return new PrintStatementNode(identifier.value);
    }

    private ASTNode relationalExpression() {
        ASTNode node = expression();
        while (currentToken != null && isRelationalOperator(currentToken.type)) {
            Lexer.Token token = currentToken;
            consume(currentToken.type);
            node = new BinaryOpNode(node, expression(), token);
        }
        return node;
    }

    private boolean isRelationalOperator(Lexer.TokenType type) {
        return type == Lexer.TokenType.GREATER_THAN || type == Lexer.TokenType.LESS_THAN ||
                type == Lexer.TokenType.GREATER_EQUAL || type == Lexer.TokenType.LESS_EQUAL ||
                type == Lexer.TokenType.EQUAL || type == Lexer.TokenType.NOT_EQUAL;
    }

    private ASTNode expression() {
        ASTNode node = term();
        while (currentToken != null && (currentToken.type == Lexer.TokenType.OPERATOR) &&
                (currentToken.value.equals("+") || currentToken.value.equals("-"))) {
            Lexer.Token token = currentToken;
            consume(currentToken.type);
            node = new BinaryOpNode(node, term(), token);
        }
        return node;
    }

    private ASTNode term() {
        ASTNode node = factor();
        while (currentToken != null && (currentToken.type == Lexer.TokenType.OPERATOR) &&
                (currentToken.value.equals("*") || currentToken.value.equals("/"))) {
            Lexer.Token token = currentToken;
            consume(currentToken.type);
            node = new BinaryOpNode(node, factor(), token);
        }
        return node;
    }

    private ASTNode factor() {
        Lexer.Token token = currentToken;

        if (token.type == Lexer.TokenType.NUMBER) {
            consume(Lexer.TokenType.NUMBER);
            return new NumberNode(token);
        } else if (token.type == Lexer.TokenType.IDENTIFIER) {
            consume(Lexer.TokenType.IDENTIFIER);
            return new IdentifierNode(token.value);
        } else if (token.type == Lexer.TokenType.LPAREN) {
            consume(Lexer.TokenType.LPAREN);
            ASTNode node = relationalExpression();
            consume(Lexer.TokenType.RPAREN);
            return node;
        }
        throw new ParserException("Unexpected token found for Factor: " + token);
    }

    private void consume(Lexer.TokenType type) {
        if (currentToken != null && currentToken.type == type) {
            currentPos++;
            if (currentPos < tokens.size()) {
                currentToken = tokens.get(currentPos);
            } else {
                currentToken = null;
            }
        } else {
            throw new ParserException("Unexpected token: " + currentToken + " expected: " + type);
        }
    }

    public static class ParserException extends RuntimeException {
        public ParserException(String s) {
            super(s);
        }
    }
}
