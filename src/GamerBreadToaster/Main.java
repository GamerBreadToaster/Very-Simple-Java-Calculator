package GamerBreadToaster;

import java.util.*;

public class Main {
    // easy print method
    static void print(Object message) {System.out.println(message);}

    // operations finder
    public static Map<Integer, String> operationFinder(String[] input) {
        // variables
        String[] operations = {"*", "/", "+", "-", "^"};
        Map<Integer, String> operationMap = new HashMap<>();

        // finding the operation in the input
        for (int x = 0; x < input.length; x++) {for (String y: operations) {
            if (input[x].equals(y)) operationMap.put(x, y);
        }}

        return operationMap;
    }

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

        // scanner and variables
        Scanner scanner = new Scanner(System.in);
        String[] operations = {"*", "/", "+", "-", "^"};
        double awnser = 0;
        do {
            // variables
            String operation = "";
            boolean exit = false;
            String[] inputParsed;
            String input = "";
            List<Integer> keys = new ArrayList<>();
            double result = 0;
            double[] number;
            Map<Integer, String> operationMap;

            // input
            print("Enter your formula as for example '6*9' and '6 * 9'.\nYou can use the '+', '-', '*', '/' and '^' operators\nYou can use 'ans' to reuse last answer:");
            while (!exit) {
                input = scanner.nextLine();
                inputParsed = input.replaceAll(" ", "").split("(?<=[-+*/^])|(?=[-+*/^])");

                // operations parsing
                operationMap = operationFinder(inputParsed);
                for (Map.Entry<Integer, String> entry: operationMap.entrySet()) {
                    keys.add(entry.getKey());
                }
                // two operators next to each other checker
                for (int key = 0; key < keys.size();) {
                    int key2 = key+1;
                    if (key2 < keys.size()) {
                        if ((keys.get(key2) - keys.get(key)) != 2) {
                            print("Error: You can't have two operators next to each other");
                            continue;
                        }
                    }
                }
            }

            // result
            /*
            result = switch (operation) {
                case "+" -> number1 + number2;
                case "-" -> number1 - number2;
                case "*" -> number1 * number2;
                case "/" -> number1 / number2;
                case "^" -> Math.pow(number1, number2);
                default -> result;
            };
            awnser = result;

            // exit
            print(input + " = " + result); */
            print("Do you want to calculate again? Y or N:");
        } while (!scanner.nextLine().equalsIgnoreCase("n"));
    }
}