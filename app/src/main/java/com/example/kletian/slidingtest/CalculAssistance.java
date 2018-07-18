package com.example.kletian.slidingtest;

import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by Marcelo Ponciano on 13/07/2018.
 */
public class CalculAssistance {

    public static Stack<String> operatorStack = new Stack<>();
    public String result;

    public CalculAssistance() {

    }

    public void setResult(String s) {
        this.result = calcSY(shuntingYard(s));
    }

    public String getResult() {
        return this.result;
    }

    private enum associativity {
        right,
        left
    }

    public static associativity getTokenAssociativity(precedence p) {
        switch (p) {
            case SOM:
                return associativity.left;
            case MENOS:
                return associativity.left;
            case MULTIPLICA:
                return associativity.left;
            case DIVIDE:
                return associativity.left;
            case POT:
                return associativity.right;
            case PERCENT:
                return associativity.left;
            case RAIZ:
                return associativity.left;
            case FATORIAL:
                return associativity.right;
            default:
                return associativity.left;
        }
    }

    private enum precedence {
        SOM(1),
        MENOS(1),
        DIVIDE(2),
        MULTIPLICA(2),
        PERCENT(3),
        POT(3),
        RAIZ(3),
        FUNC(6),
        FATORIAL(7),
        OP(7);

        private int precedenceNum;

        precedence(int precedenceNum) {
            this.precedenceNum = precedenceNum;
        }
    }

    public static precedence getToken(String symbol) {
        switch (symbol) {
            case "+":
                return precedence.SOM;
            case "-":
                return precedence.MENOS;
            case "*":
                return precedence.MULTIPLICA;
            case "/":
                return precedence.DIVIDE;
            case "^":
                return precedence.POT;
            case "%":
                return precedence.PERCENT;
            case "Raiz":
                return precedence.RAIZ;
            case "sin":
                return precedence.FUNC;
            case "cos":
                return precedence.FUNC;
            case "tan":
                return precedence.FUNC;
            case "max":
                return precedence.FUNC;
            case "log":
                return precedence.FUNC;
            case "!":
                return precedence.FATORIAL;
            default:
                return precedence.OP;
        }
    }

    public static HashMap<String, precedence> opMap = new HashMap<String, precedence>() {{
        put("+", precedence.SOM);
        put("-", precedence.MENOS);
        put("*", precedence.MULTIPLICA);
        put("/", precedence.DIVIDE);
        put("^", precedence.POT);
        put("%", precedence.PERCENT);
        put("sin", precedence.FUNC);
        put("max", precedence.FUNC);
        put("tan", precedence.FUNC);
        put("cos", precedence.FUNC);
        put("log", precedence.FUNC);
        put("Raiz", precedence.RAIZ);
        put("!", precedence.FATORIAL);
    }};

    //
    //
    // There are 2 methods for performing numeric operations. One receives 2 double type parameters, and the other receives only 1
    // That's because there are operations that requires only 1 number, like the ones described in the method
    public static boolean oneNumberOperation(String token) {
        return !(opMap.containsKey(token) && !token.equals("sin") && !token.equals("cos") && !token.equals("tan") && !token.equals("Raiz") && !token.equals("!") && !token.equals("log") && !token.equals("%"));
    }

    public static boolean isPercent(String token) {
        return opMap.containsKey(token) && token.equals("%");
    }

    public static boolean isNumber(String n) {
        Pattern p = Pattern.compile("[0-9]");
        java.util.regex.Matcher m = p.matcher(n);
        return m.find();
    }

    public static boolean returnHigher(String tokenAtual, String tokenPilha) {
        return opMap.containsKey(tokenAtual) && opMap.get(tokenAtual).precedenceNum > opMap.get(tokenPilha).precedenceNum;
    }

    public static boolean returnEqualPrecedence(String tokenAtual, String tokenPilha) {
        return opMap.containsKey(tokenAtual) && opMap.get(tokenAtual).precedenceNum == opMap.get(tokenPilha).precedenceNum;
    }

    public static boolean isFunction(String k) {
        return opMap.containsKey(k) && opMap.get(k).precedenceNum == 6;
    }

    public static double calculaFatorial(double d) {
        double num = d;
        while (d > 1) {
            num = num * (d - 1);
            d--;
        }
        return num;

    }

    public static double calc(double first, double second, String operation) {
        switch (operation) {
            case "+":
                return first + second;
            case "-":
                return second - first;
            case "*":
                return first * second;
            case "/":
                return second / first;
            case "^":
                return Math.pow(second, first);
            case "%":
                return (first * second) / 100;
            case "max":
                return Math.max(first, second);
            default:
                return 1;
        }
    }

    public static double calc(double first, String operation) {
        switch (operation) {
            case "sin":
                return Math.sin(Math.toRadians(first));
            case "cos":
                return Math.cos(Math.toRadians(first));
            case "tan":
                return Math.tan(Math.toRadians(first));
            case "Raiz":
                return Math.sqrt(first);
            case "!":
                return calculaFatorial(first);
            case "log":
                return Math.log10(first);
            default:
                return 0;
        }
    }

    public static String calcSY(String s) {
        s = s.trim();
        double[] numeros = new double[2];

        Stack<String> newStack = new Stack<>();
        for (String token : s.split(" ")) {
            if (isNumber(token)) {
                newStack.push(token);
            } else {
                if (opMap.containsKey(token) && !oneNumberOperation(token)) {
                    numeros[0] = Double.parseDouble(newStack.pop());
                    numeros[1] = Double.parseDouble(newStack.pop());
                    newStack.push(Double.toString(calc(numeros[0], numeros[1], token)));
                } else {
                    if (opMap.containsKey(token) && isPercent(token)) {
                        numeros[0] = Double.parseDouble(newStack.pop());
                        numeros[1] = Double.parseDouble(newStack.pop());
                        newStack.push(Double.toString(numeros[1]));
                        newStack.push(Double.toString(calc(numeros[0], numeros[1], token)));
                    } else {
                        if (opMap.containsKey(token) && oneNumberOperation(token)) {
                            numeros[0] = Double.parseDouble(newStack.pop());
                            newStack.push(Double.toString(calc(numeros[0], token)));
                        }
                    }
                }
            }
        }
        return newStack.pop();
    }

    //
    //
    // The Shunting Yard Algorithm was written using the exact same structure shown in Wikipedia's article "Shunting-yard algorithm"
    // found on https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    //
    //
    public static String shuntingYard(String s) {
        StringBuilder sb = new StringBuilder();
        s = s.trim();
        for (String k : s.split(" ")) {
            if (isNumber(k)) {
                sb.append(k);
                sb.append(" ");
            } else {
                if (opMap.containsKey(k) && isFunction(k)) {
                    operatorStack.push(k);
                } else {
                    if (opMap.containsKey(k)) {
                        while (!operatorStack.isEmpty() && isFunction(operatorStack.peek())
                                || !operatorStack.isEmpty() && returnHigher(operatorStack.peek(), k)
                                || !operatorStack.isEmpty() && returnEqualPrecedence(operatorStack.peek(), k) && getTokenAssociativity(getToken(operatorStack.peek())) == associativity.left) {
                            sb.append(operatorStack.pop());
                            sb.append(" ");
                        }
                        operatorStack.push(k);
                    }
                    if (k.equals("(")) {
                        operatorStack.push(k);
                    }
                    if (k.equals(")")) {
                        while (!operatorStack.peek().equals("(")) {
                            sb.append(operatorStack.pop());
                            sb.append(" ");
                        }
                        operatorStack.pop();
                    }
                }
            }
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().equals("(")) {
                operatorStack.pop();
            }
            sb.append(operatorStack.pop());
            sb.append(" ");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
