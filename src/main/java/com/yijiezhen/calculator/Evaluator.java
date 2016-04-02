package com.yijiezhen.calculator;

import java.util.Scanner;

public class Evaluator {
    public double evaluate(String str) {
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        return parser.expression().eval();
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Evaluator evaluator = new Evaluator();
        while (true) {
            String line = scanner.nextLine();
            System.out.println(evaluator.evaluate(line));
        }
    }
}
