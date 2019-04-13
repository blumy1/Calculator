import java.util.ArrayList;
import java.util.Stack;

class ExpressionTranslation {
    private static char[] openingParenthesis = new char[]{'(', '{', '['};
    private static char[] closingParenthesis = new char[]{')', '}', ']'};
    private static char[] operators = new char[]{'^', '*', '/', '+', '-'};
    private static char separator = ',';

    static String infixToPostfix(String exp) {
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();
        ArrayList<String> values = splitToArrayList(exp);
        if (values.size() < 2) return null;

        for (String value : values) {
            if (isOperator(value.charAt(0))) {
                if (isOpeningParenthesis(value.charAt(0)) || stack.isEmpty() ||
                        isHigherPriority(stack.peek(), value.charAt(0))) {
                    stack.push(value.charAt(0));
                } else if(isClosingParenthesis(value.charAt(0))) {
                    while (!isOpeningParenthesis(stack.peek())) {
                        appendWithSeparator(result, String.valueOf(stack.pop()));
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty()) {
                        appendWithSeparator(result, String.valueOf(stack.pop()));
                    }
                    stack.push(value.charAt(0));
                }
            } else {
                appendWithSeparator(result, value);
            }

        }

        while(!stack.isEmpty()) {
            appendWithSeparator(result, String.valueOf(stack.pop()));
        }

        if (result.length() > 0){
            result.replace(result.length()-1, result.length(), "");
        }

        return result.toString();
    }

    static Double postfixToDouble(String exp) {
        Stack<String> stack = new Stack<>();
        String[] expressions = exp.split(String.valueOf(separator));
        if (expressions.length < 2) return null;

        for (String expression : expressions) {
            if (isOperator(expression.charAt(0))) {
                String value2 = stack.pop();
                String value1 = stack.pop();
                stack.push(operate(expression, Double.valueOf(value1), Double.valueOf(value2)));
            } else {
                stack.push(expression);
            }
        }

        return Double.valueOf(stack.pop());
    }

    static boolean parenthesesMatched(String exp) {
        Stack<Character> stack = new Stack<>();
        for (int i=0; i<exp.length(); i++) {
            char character = exp.charAt(i);
            if (isOpeningParenthesis(character)) {
                stack.push(character);
            } else if (isClosingParenthesis(character)) {
                if (stack.isEmpty()) return false;

                if (matchesOpeningParenthesis(stack.peek(), character)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    private static String operate(String operator, double value1, double value2) {
        double result = 0;

        switch(operator) {
            case "^":
                result = Math.pow(value1, value2);
                break;
            case "*":
                result = value1 * value2;
                break;
            case "/":
                result = value1 / value2;
                break;
            case "+":
                result = value1 + value2;
                break;
            case "-":
                result = value1 - value2;
                break;
        }

        return String.valueOf(result);
    }

    private static boolean isHigherPriority(char stackChar, char expChar) {
        return getPriority(stackChar) < getPriority(expChar);
    }

    private static int getPriority(char character) {
         if (character == '^') return 3;
         else if (character == '*' || character == '/') return 2;
         else if (character == '+' || character == '-') return 1;
         else return 0;
    }

    private static boolean isOperator(char character) {
        for (char operator : operators) {
            if (character == operator) return true;
        }

        return isOpeningParenthesis(character) || isClosingParenthesis(character);
    }

    private static boolean isOpeningParenthesis(char character) {
        for (char parenthesis : openingParenthesis) {
            if (character == parenthesis) return true;
        }
        return false;
    }

    private static boolean isClosingParenthesis(char character) {
        for (char parenthesis : closingParenthesis) {
            if (character == parenthesis) return true;
        }
        return false;
    }

    private static boolean matchesOpeningParenthesis(char opening, char closing) {
        for (int i=0; i<openingParenthesis.length; i++) {
            if (openingParenthesis[i] == opening) {
                return closingParenthesis[i] == closing;
            }
        }
        return false;
    }

    private static ArrayList<String> splitToArrayList(String exp) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        for (int i=0; i<exp.length(); i++) {
            char character = exp.charAt(i);
            if (isOperator(character)) {
                if (value.length() > 0) {
                    result.add(value.toString());
                    value.delete(0, value.length());
                    i--;
                } else {
                    result.add(String.valueOf(character));
                }
            } else {
                value.append(character);
            }
        }

        if (value.length() > 0)
            result.add(value.toString());

        return result;
    }

    private static void appendWithSeparator(StringBuilder stringBuilder, String value) {
        stringBuilder.append(value);
        stringBuilder.append(separator);
    }
}
