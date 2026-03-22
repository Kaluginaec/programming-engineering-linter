package ru.yarsu;

import java.util.*;

public class Calculator {
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "^", "(", ")");

    public static ArrayList<String> infixtoPostfix(ArrayList<String> infix) throws IllegalArgumentException {
        ArrayList<String> postfix = new ArrayList<>();
        Deque<String> stackOfOperators = new ArrayDeque<>();
        for (int i = 0; i < infix.size(); i++) {
            String el = infix.get(i);
            if (OPERATORS.contains(el)) {
                if (stackOfOperators.isEmpty() || el.equals("("))
                    stackOfOperators.push(el);
                else if (el.equals(")")) {
                    while (!stackOfOperators.isEmpty() && !(stackOfOperators.peek()).equals("("))
                        postfix.add(stackOfOperators.pop());
                    if (stackOfOperators.isEmpty())
                        throw new IllegalArgumentException("Unbalanced brackets: missing '('");
                    stackOfOperators.pop();
                } else {
                    if (el.equals("+") || el.equals("-"))
                        while (!stackOfOperators.isEmpty() && !stackOfOperators.peek().equals("("))
                            postfix.add(stackOfOperators.pop());
                    else if (el.equals("*") || el.equals("/"))
                        while (!stackOfOperators.isEmpty() && List.of("*", "/", "^").contains(stackOfOperators.peek()))
                            postfix.add(stackOfOperators.pop());
                    stackOfOperators.push(el);
                }
            } else
                postfix.add(el);
        }
        while (!stackOfOperators.isEmpty()) {
            if (stackOfOperators.peek().equals("("))
                throw new IllegalArgumentException("Unbalanced brackets: missing ')'");
            postfix.add(stackOfOperators.pop());
        }
        return postfix;
    }

    public static double resultByPostfix(ArrayList<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        for (int i = 0; i < postfix.size(); i++) {
            if (OPERATORS.contains(postfix.get(i))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression");
                }
                Double el2 = stack.pop();
                Double el1 = stack.pop();
                String operator = postfix.get(i);
                if (operator.equals("+"))
                    stack.push(el1 + el2);
                else if (operator.equals("-"))
                    stack.push(el1 - el2);
                else if (operator.equals("*"))
                    stack.push(el1 * el2);
                else if (operator.equals("/")) {
                    if (el2 == 0)
                        throw new IllegalArgumentException("Not divide by 0");
                    stack.push(el1 / el2);
                } else
                    stack.push(Math.pow(el1, el2));
            } else
                stack.push(Double.parseDouble(postfix.get(i)));
        }
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression");
        }
        return stack.pop();
    }
}