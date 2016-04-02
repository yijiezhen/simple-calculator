package com.yijiezhen.calculator;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testRead1() {
        Lexer lexer = new Lexer("1+3");
        assert (lexer.read().equals(new NumToken(1)));
        assert (lexer.read().equals(new OperatorToken('+')));
        assert (lexer.read().equals(new NumToken(3)));
    }

    @Test
    public void testRead2() {
        Lexer lexer = new Lexer(" 1 * (2 + 3) - 8 / 3");
        assert (lexer.read().equals(new NumToken(1)));
        assert (lexer.read().equals(new OperatorToken('*')));
        assert (lexer.peek(0).equals(new ParenthesisToken('(')));
        assert (lexer.peek(1).equals(new NumToken(2)));
    }

    @Test
    public void testIf() {
        Lexer lexer = new Lexer("if ( true) 1 else 1 + 1");
        assert (lexer.read().equals(KeywordToken.IF));
        assert (lexer.read().equals(ParenthesisToken.leftParenthesis));
        assert (lexer.read().equals(KeywordToken.TRUE));
        assert (lexer.read().equals(ParenthesisToken.rightParenthesis));
        assert (lexer.read().equals(new NumToken(1)));
    }

}