package com.yijiezhen.calculator;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testSingleNum() {
        Lexer lexer = new Lexer("1");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node.isLeaf == true);
        assert(((NumericNode) node).number == 1);
    }

    @Test
    public void testSimpleAdd() {
        Lexer lexer = new Lexer("1+2");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node instanceof BinaryOperatorNode);
        BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
        assert(binaryOperatorNode.isLeaf == false);
        assert(binaryOperatorNode.left instanceof NumericNode);
        NumericNode left = (NumericNode)binaryOperatorNode.left;
        NumericNode right = (NumericNode)binaryOperatorNode.right;
        assert(left.number == 1);
        assert(right.number == 2);
    }

    @Test
    public void testAddAndMultiply() {
        Lexer lexer = new Lexer("1+2 * 3");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node instanceof BinaryOperatorNode);
        BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
        assert(binaryOperatorNode.isLeaf == false);
        assert(binaryOperatorNode.left instanceof NumericNode);
        NumericNode left = (NumericNode)binaryOperatorNode.left;
        assert(left.number == 1);
        BinaryOperatorNode right = (BinaryOperatorNode)binaryOperatorNode.right;
        NumericNode rightLeft = (NumericNode) right.left;
        NumericNode rightRight = (NumericNode) right.right;
        assert(rightLeft.number == 2);
        assert(rightRight.number == 3);
    }

    @Test
    public void testWithParen() {
        Lexer lexer = new Lexer("(1+2) * 3");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node instanceof BinaryOperatorNode);
        BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
        assert(binaryOperatorNode.isLeaf == false);
        assert(binaryOperatorNode.right instanceof NumericNode);
        NumericNode right = (NumericNode)binaryOperatorNode.right;
        assert(right.number == 3);
        BinaryOperatorNode left = (BinaryOperatorNode)binaryOperatorNode.left;
        NumericNode leftLeft = (NumericNode) left.left;
        NumericNode leftRight = (NumericNode) left.right;
        assert(leftLeft.number == 1);
        assert(leftRight.number == 2);
    }

    @Test
    public void testIf() {
        Lexer lexer = new Lexer("if(true) 1 else 2*3 + 2");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node instanceof NumericNode);
        assert(((NumericNode)node).number == 1);
    }

    @Test
    public void testElse() {
        Lexer lexer = new Lexer("if(false) 1 else (1+2) * 3");
        Parser parser = new Parser(lexer);
        AstNode node = parser.expression();
        assert(node instanceof BinaryOperatorNode);
        BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
        assert(binaryOperatorNode.isLeaf == false);
        assert(binaryOperatorNode.right instanceof NumericNode);
        NumericNode right = (NumericNode)binaryOperatorNode.right;
        assert(right.number == 3);
        BinaryOperatorNode left = (BinaryOperatorNode)binaryOperatorNode.left;
        NumericNode leftLeft = (NumericNode) left.left;
        NumericNode leftRight = (NumericNode) left.right;
        assert(leftLeft.number == 1);
        assert(leftRight.number == 2);
    }
}
