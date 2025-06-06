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
        while (true) {
            // variables
            boolean exit = false;
            boolean again = false;
            String[] inputParsed = null;
            String inputRaw = "";
            double result = 0;
            Map<Integer, String> operationMap = Map.of();

            // input
            print("Enter your formula as for example '6*9' and '6 * 9'.\nYou can use the '+', '-', '*', '/' and '^' operators\nYou can use 'ans' to re-use last answer:");

            while (!exit) {
                inputRaw = scanner.nextLine();
                inputParsed = inputRaw.replaceAll(" ", "").split("(?<=[-+*/^])|(?=[-+*/^])");
                List<Integer> keys = new ArrayList<>();

                // operations parsing
                operationMap = operationFinder(inputParsed);
                for (Map.Entry<Integer, String> entry: operationMap.entrySet()) {
                    keys.add(entry.getKey());
                }
                if (keys.getFirst() == 0 || keys.getLast() == inputParsed.length-1) {
                    print("Error: A operator cannot be at the begin or end!");
                    continue;
                }
                // two operators next to each other checker
                for (int key = 0; key < keys.size(); key++) {
                    int key2 = key+1;
                    if (key2 < keys.size()) {
                        if ((keys.get(key2) - keys.get(key)) != 2) {
                            print("Error: You can't have two operators next to each other");
                            break;
                        }
                    } else {exit = true;}
                }
            }
            List<String> inputList = new ArrayList<>(Arrays.asList(inputParsed));

            // result
            // calculation rules: power -> division or multiplication  -> sum / subtraction
            boolean divisionBy0 = false;
            while (inputList.size() != 1) {
                for (Map.Entry<Integer, String> entry : operationMap.entrySet()) {
                    // to the power of
                    if (entry.getValue().equals("^")) {
                        double number1 = Double.parseDouble(inputList.get(entry.getKey() - 1));
                        double number2 = Double.parseDouble(inputList.get(entry.getKey() + 1));
                        result = pow(number1, number2);
                        operationMap = parseList(entry, inputList, result);
                        again = true;
                        break;
                    }
                }
                if (again) {again = false; continue;} // these are to make sure the parsing doesn't affect the order of the Map, otherwise 6+6*9*2 would come out as 120 because of wrong order.
                for (Map.Entry<Integer, String> entry : operationMap.entrySet()) {
                    // Multiply x by y or division of x by y
                    if (entry.getValue().equals("*") || entry.getValue().equals("/")) {
                        double number1 = Double.parseDouble(inputList.get(entry.getKey() - 1));
                        double number2 = Double.parseDouble(inputList.get(entry.getKey() + 1));
                        // dubbel if statement to make sure the multi doesn't come first even if division is first.
                        if (entry.getValue().equals("*")) {
                            result = multi(number1, number2);
                        } else {
                            if (number2 == 0) {divisionBy0 = true; break;}
                            result = div(number1, number2);
                        }
                        operationMap = parseList(entry, inputList, result);
                        again = true;
                        break;
                    }
                }
                if (divisionBy0) {print("Error: can't divide by 0"); result = 0; break;}
                if (again) {again = false; continue;}
                for (Map.Entry<Integer, String> entry : operationMap.entrySet()) {
                    // x + y or x - y
                    if (entry.getValue().equals("+") || entry.getValue().equals("-")) {
                        double number1 = Double.parseDouble(inputList.get(entry.getKey() - 1));
                        double number2 = Double.parseDouble(inputList.get(entry.getKey() + 1));
                        // dubbel if statement to make sure the + doesn't come first even if - is first.
                        if (entry.getValue().equals("+")) {
                            result = sum(number1, number2);
                        } else {
                            result = sub(number1, number2);
                        }
                        operationMap = parseList(entry, inputList, result);
                    }
                }
            }
            // exit
            if (!divisionBy0) {
                print(inputRaw + " = " + result);
                print("Do you want to calculate again? Y or N:");
                if (scanner.nextLine().equalsIgnoreCase("n")) {break;}
            }
        }
    }

    // easy print method
    static void print(Object message) {System.out.println(message);}

    // operations finder
    public static Map<Integer, String> operationFinder(Object[] input) {
        // variables
        String[] operations = {"*", "/", "+", "-", "^"};
        Map<Integer, String> operationMap = new LinkedHashMap<>();

        // finding the operation in the input
        for (int x = 0; x < input.length; x++) {for (String y: operations) {
            if (input[x].equals(y)) operationMap.put(x, y);
        }}

        return operationMap;
    }

    // this makes sure that operationMap is properly parsed so
    public static Map<Integer, String> parseList(Map.Entry<Integer, String> entry, List<String> inputList, Double result) {
        assert entry != null;
        inputList.remove(entry.getKey() + 1);
        inputList.remove(entry.getKey().intValue());
        inputList.remove(entry.getKey() - 1);
        inputList.add(entry.getKey() - 1, String.valueOf(result));
        return operationFinder(inputList.toArray());
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