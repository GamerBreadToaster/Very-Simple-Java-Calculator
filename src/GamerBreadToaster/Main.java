package GamerBreadToaster;

import java.util.Scanner;

public class Main {
    /* easy print method */
    static void print(Object message) {System.out.println(message);}

    public static void main(String[] args) {
        /* EXERCISE 1: Easy Calculator
         *
         * Create a Calculator that runs multiple times until you manually break out of it.
         * You should read the operation first, then read the first and second number.
         *
         * Depending on the operation chosen, you then have to return a different result.
         * You can do this with if statements or a switch statement.
         *
         * Please note that when reading in a String, you want to use scanner.next()
         * And when comparing strings you wanna use:
         * String s = "something";
         * s.equals("something");
         * and not ==
         *
         * After the result has been output, ask the user if they want to continue, if not end the program!
         * Estimated Time: 30-45 minutes
         *
         */

        // scanner
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // variables
            String[] operations = {"*", "/", "+", "-", "^"};
            String operation = "+";
            boolean exit = false;
            String[] inputParsed;
            double result = 0;
            double number1 = 0;
            double number2 = 0;

            // input
            print("Enter your formula as for example '6*9' and '6 * 9'. You can use the '+', '-', '*', '/' and '^' operators:");
            while (!exit) {
                inputParsed = scanner.nextLine().replaceAll(" ", "").split("(?<=[-+*/^])|(?=[-+*/^])");

                // will make sure number 1 and 2 are valid numbers
                try {
                    number1 = Double.parseDouble(inputParsed[0]);
                    number2 = Double.parseDouble(inputParsed[2]);
                } catch (NumberFormatException e) {
                    print("Error: Not a valid number: " + e);
                    continue;
                }

                // device by zero check
                if (number2 == 0 && operation.equals("/")) {
                    print("Error: Can't divide by 0");
                    continue;
                }

                // operations check
                operation = inputParsed[1];
                for (String operator: operations) {
                    if (operator.equals(operation)) {
                        exit = true;
                        break;
                    }
                } if (!exit) {
                    print("Error: unsupported operator: " + operation);
                }
            }

            // result
            result = switch (operation) {
                case "+" -> number1 + number2;
                case "-" -> number1 - number2;
                case "*" -> number1 * number2;
                case "/" -> number1 / number2;
                case "^" -> Math.pow(number1, number2);
                default -> result;
            };

            // exit
            print(number1 + " " + operation + " " + number2 + " = " + result);
            print("Do you want to calculate again? Y or N:");
            if (scanner.nextLine().equalsIgnoreCase("n")) {break;}
        }
    }
}