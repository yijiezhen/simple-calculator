package com.yijiezhen.calculator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Token {
    public static Token END = new Token(){};
}

class NumToken extends Token {
    int num;
    public NumToken(int num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof NumToken)) return false;
        final NumToken that = (NumToken) other;
        return this.num == that.num;
    }

    @Override
    public int hashCode() {
        return this.num;
    }
}

class OperatorToken extends Token {
    public static OperatorToken addOperator = new OperatorToken('+');
    public static OperatorToken minusOperator = new OperatorToken('-');
    public static OperatorToken multipleOperator = new OperatorToken('*');
    public static OperatorToken divideOperator = new OperatorToken('/');

    char operator;
    public OperatorToken(char operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof OperatorToken)) return false;
        final OperatorToken that = (OperatorToken) other;
        return this.operator == that.operator;
    }

    @Override
    public int hashCode() {
        return this.operator;
    }
}

class ParenthesisToken extends Token {
    public static ParenthesisToken leftParenthesis = new ParenthesisToken('(');
    public static ParenthesisToken rightParenthesis = new ParenthesisToken(')');

    char parenthesis;
    public ParenthesisToken(char parenthesis) {
        this.parenthesis = parenthesis;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ParenthesisToken)) return false;
        final ParenthesisToken that = (ParenthesisToken) other;
        return this.parenthesis == that.parenthesis;
    }

    @Override
    public int hashCode() {
        return this.parenthesis;
    }
}

class KeywordToken extends Token {

    public static KeywordToken IF = new KeywordToken("if");
    public static KeywordToken ELSE = new KeywordToken("else");
    public static KeywordToken TRUE = new KeywordToken("true");
    public static KeywordToken FALSE = new KeywordToken("false");

    String keyword;
    public KeywordToken(String keyword) {
        this.keyword = keyword;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof KeywordToken)) return false;
        final KeywordToken that = (KeywordToken) other;
        return this.keyword.equals(that.keyword);
    }
    @Override
    public int hashCode() {
        return this.keyword.hashCode();
    }
}

public class Lexer {

    private Pattern pattern = Pattern.compile("(\\s+)|([0-9]+)|([\\+\\-\\*/])|([\\(\\)])|(if|else|true|false)");

    private ArrayList<Token> tokens = new ArrayList<Token>();

    public Lexer(String content) {
        Matcher matcher = pattern.matcher(content);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int start = 0;
        int end = content.length();

        while (start < end) {
            matcher.region(start, end);
            if(matcher.lookingAt()) {
                addToken(tokens, matcher);
                start = matcher.end();
            } else {
                throw new ParseException("Error to parse " + content);
            }
        }
    }

    public Token read() {
        if(tokens.size() > 0) {
            return tokens.remove(0);
        } else {
            return Token.END;
        }
    }

    public Token peek(int i) {
        if (i >= 0 && i < tokens.size()) {
            return tokens.get(i);
        } else {
            return Token.END;
        }
    }

    private void addToken(ArrayList<Token> tokens, Matcher matcher) {
        String m1 = matcher.group(1);
        if (m1 == null) {
            String m2 = matcher.group(2);
            String m3 = matcher.group(3);
            String m4 = matcher.group(4);
            String m5 = matcher.group(5);
            if (m2 != null) {
                tokens.add(new NumToken(Integer.valueOf(m2)));
            } else if (m3 != null) {
                tokens.add(new OperatorToken(m3.charAt(0)));
            } else if (m4 != null) {
                tokens.add(new ParenthesisToken(m4.charAt(0)));
            } else if (m5 != null) {
                tokens.add(new KeywordToken(m5));
            }
        }
    }
}

