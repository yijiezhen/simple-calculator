package com.yijiezhen.calculator;

abstract class AstNode {
    boolean isLeaf = false;
    public abstract double eval();
}

class NumericNode extends AstNode {
    int number;
    public NumericNode(int n) {
        number = n;
        isLeaf = true;
    }

    @Override
    public double eval() {
        return number;
    }
}

class BinaryOperatorNode extends AstNode {
    char operator;
    AstNode left;
    AstNode right;
    public BinaryOperatorNode(char operator, AstNode left, AstNode right) {
        this.operator = operator;
        this.isLeaf = false;
        this.left = left;
        this.right = right;
    }

    @Override
    public double eval() {
        double value = 0;
        switch (operator) {
            case '+': value = left.eval() + right.eval(); break;
            case '-': value = left.eval() - right.eval(); break;
            case '*': value = left.eval() * right.eval(); break;
            case '/': value = left.eval() / right.eval(); break;
        }
        return value;
    }
}

// expression: if (true|false) {expression} else {expression} | term { ("+" | "-") term }
// factor : NUMBER | "(" expression ")"
// term: factor { ("*" | "/") factor }
//
public class Parser {
    private Lexer lexer;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    // expression: term { ("+" | "-") term }
    public AstNode expression(){
        if (lexer.peek(0).equals(KeywordToken.IF)) {
            lexer.read(); // If
            if (!lexer.peek(0).equals(ParenthesisToken.leftParenthesis)) {
                throw new ParseException("expect left parenthesis");
            }
            lexer.read(); // (
            Token token = lexer.peek(0);
            if (token.equals(KeywordToken.TRUE) ||
                    token.equals(KeywordToken.FALSE)) {
                AstNode ifNode, elseNode;
                lexer.read(); // true | false
                lexer.read(); // )
                ifNode = expression();
                lexer.read(); // else
                elseNode = expression();
                if (token.equals(KeywordToken.TRUE)) {
                    return ifNode;
                } else {
                    return elseNode;
                }
            }
            throw new ParseException("unexpected parse exception");
        } else {
            AstNode left = term();
            while (lexer.peek(0).equals(OperatorToken.addOperator) ||
                    lexer.peek(0).equals(OperatorToken.minusOperator)) {
                OperatorToken token = (OperatorToken) lexer.read();
                AstNode right = term();
                left = new BinaryOperatorNode(token.operator, left, right);
            }
            return left;
        }
    }

    // factor : NUMBER | "(" expression ")"
    public AstNode factor() {
        if (lexer.peek(0).equals(ParenthesisToken.leftParenthesis)) {
            lexer.read();
            AstNode expression = expression();
            if (!lexer.peek(0).equals(ParenthesisToken.rightParenthesis)) {
                throw new ParseException("expect right parenthesis");
            }
            lexer.read();
            return expression;
        } else {
            Token token = lexer.read();
            if (!(token instanceof NumToken)) {
                throw new ParseException("expect numeric node");
            } else {
                NumToken numToken = (NumToken) token;
                return new NumericNode(numToken.num);
            }
        }
    }

    // term: factor { ("*" | "/") factor }
    public AstNode term() {
        AstNode left = factor();
        while(lexer.peek(0).equals(OperatorToken.multipleOperator) ||
                lexer.peek(0).equals(OperatorToken.divideOperator)) {
            OperatorToken token = (OperatorToken) lexer.read();
            AstNode right = factor();
            left = new BinaryOperatorNode(token.operator, left, right);
        }
        return left;
    }


}
