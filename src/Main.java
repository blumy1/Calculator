import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true) {
            String exp = in.nextLine();
            if (exp.equals("exit")) return;

            if (!ExpressionTranslation.parenthesesMatched(exp)) {
                System.out.println("Parentheses don't match.");
                continue;
            }

            String result = ExpressionTranslation.infixToPostfix(exp);
            if (result == null) {
                System.out.println("Wrong expression.");
                continue;
            }

            Double res = ExpressionTranslation.postfixToDouble(result);
            if (res == null) {
                System.out.println("Wrong expression.");
                continue;
            }

            System.out.println(res);
        }
    }
}
