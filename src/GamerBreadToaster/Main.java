package GamerBreadToaster;

import java.util.*;

public class Main {
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
        do {
            // variables
            boolean exit = false;
            String[] inputParsed = null;
            String inputRaw = "";
            List<Integer> keys = new ArrayList<>();
            double result = 0;
            Map<Integer, String> operationMap = Map.of();

            // input
            print("Enter your formula as for example '6*9' and '6 * 9'.\nYou can use the '+', '-', '*', '/' and '^' operators\nYou can use 'ans' to re-use last answer:");
            while (!exit) {
                inputRaw = scanner.nextLine();
                inputParsed = inputRaw.replaceAll(" ", "").split("(?<=[-+*/^])|(?=[-+*/^])");

                // operations parsing
                operationMap = operationFinder(inputParsed);
                for (Map.Entry<Integer, String> entry: operationMap.entrySet()) {
                    keys.add(entry.getKey());
                }
                // two operators next to each other checker
                for (int key = 0; key < keys.size(); key++) {
                    int key2 = key+1;
                    if (key2 < keys.size()) {
                        if ((keys.get(key2) - keys.get(key)) != 2) {
                            print("Error: You can't have two operators next to each other");
                            break;}}
                }
                exit = true;
            }
            List<String> inputList = new ArrayList<>(Arrays.asList(inputParsed));

            // result
            // calculation rules: power -> division or multiplication  -> sum / subtraction
            while (inputList.size() != 1) {
                Map.Entry<Integer, String> entry2 = null;
                for (Map.Entry<Integer, String> entry : operationMap.entrySet()) {
                    entry2 = entry;
                    double number1 = Double.parseDouble(inputList.get(entry.getKey() - 1));
                    double number2 = Double.parseDouble(inputList.get(entry.getKey() + 1));
                    if (entry.getValue().equals("^")) {
                        result = pow(number1, number2);
                        break;
                    } else if (entry.getValue().equals("*") | entry.getValue().equals("/")) {
                        // dubbel if statement to make sure the multi doesn't come first even if division is first.
                        if (entry.getValue().equals("*")) {
                            result = multi(number1, number2);
                        } else {
                            result = div(number1, number2);
                        }
                        break;
                    } else if (entry.getValue().equals("+") | entry.getValue().equals("-")) {
                        // dubbel if statement to make sure the + doesn't come first even if - is first.
                        if (entry.getValue().equals("+")) {
                            result = sum(number1, number2);
                        } else {result = sub(number1, number2);}
                        break;
                    }
                }
                // removing from old numbers and operator from list and replacing it with the result
                assert entry2 != null;
                inputList.remove(entry2.getKey() + 1);
                inputList.remove(entry2.getKey().intValue());
                inputList.remove(entry2.getKey() - 1);
                inputList.add(entry2.getKey() - 1, String.valueOf(result));
                operationMap = operationFinder(inputList.toArray());
            }
            // exit
            print(inputRaw + " = " + result);
            print("Do you want to calculate again? Y or N:");
        } while (!scanner.nextLine().equalsIgnoreCase("n"));
    }

    // easy print method
    static void print(Object message) {System.out.println(message);}

    // operations finder
    public static Map<Integer, String> operationFinder(Object[] input) {
        // variables
        String[] operations = {"*", "/", "+", "-", "^"};
        Map<Integer, String> operationMap = new HashMap<>();

        // finding the operation in the input
        for (int x = 0; x < input.length; x++) {for (String y: operations) {
            if (input[x].equals(y)) operationMap.put(x, y);
        }}

        return operationMap;
    }

    public static double sum(double x, double y) {
        return x + y;
    }

    public static double sub(double x, double y) {
        return x - y;
    }

    public static double multi(double x, double y) {
        return x * y;
    }

    public static double div(double x, double y) {
        return x / y;
    }

    public static double pow(double x, double y) {
        return Math.pow(x, y);
    }
}