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
        double answer = 0;
        while (true) {
            // variables
            boolean exit = false;
            boolean again = false;
            boolean beginsWithMinus = false;
            boolean doConvertTest = false;
            String[] inputParsed = null;
            String[] operators = {"-", "+", "*", "/", "^"};
            String inputRaw = "";
            String operation;
            int targetKey = 0;
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

                // replacing ans in inputParsed with the value of ans
                List<String> inputParsedList = new ArrayList<>(Arrays.asList(inputParsed));
                for (int i = 0; i < inputParsedList.size(); i++) {
                    if (inputParsedList.get(i).equalsIgnoreCase("ans")) {
                        inputParsedList.remove(i);
                        inputParsedList.add(i, String.valueOf(answer));
                    }
                    // looking for anything not a number (or operator)
                    try {
                        for (String j: operators) {
                            if (inputParsedList.get(i).equals(j)) {break;}
                            doConvertTest = true;
                        }
                        if (doConvertTest) {
                            double convertTest = Double.parseDouble(inputParsedList.get(i));
                        }
                    } catch (NumberFormatException e) {
                        print("You can only put in (negative) numbers, operators and 'ans'!");
                        break;
                    }
                    doConvertTest = false;
                }
                if (doConvertTest) {continue;}
                inputParsed = inputParsedList.toArray(new String[0]);

                try {
                    if (keys.getFirst() == 0 && !operationMap.get(keys.getFirst()).equals("-")) {
                        print("Error: A operator cannot be at the begin or end!");
                        continue;
                    } else if (keys.getLast() == inputParsed.length - 1) {
                        print("Error: A operator cannot be at the begin or end!");
                        continue;
                    }
                } catch (NoSuchElementException e) {
                    print("Error: You can't enter nothing!");
                    continue;
                }
                // two operators next to each other checker
                for (int key = 0; key < keys.size(); key++) {
                    int key2 = key+1;
                    if (key2 < keys.size() || operationMap.get(keys.getFirst()).equals("-")) {
                        // negative numbers check and will correct two subtractions with a single addition.
                        if ((keys.get(key2) - keys.get(key)) != 2) {
                            if (operationMap.get(keys.get(key)).equals("-") && operationMap.get(keys.get(key2)).equals("-")){
                                // iterate trough operationMap to decrease all position of the operations after
                                // removing one. EX: 7+6--6*9, operationMap = 1 -> "+", 3 -> "-", 4 -> "-", 6 -> "*"
                                // becomes 1 -> "+", 3 -> "+", 5 -> "*" thus becoming 7+6+6*9
                                inputParsedList = new ArrayList<>(Arrays.asList(inputParsed));
                                operationMap.remove(keys.get(key2));
                                operationMap.replace(keys.get(key), "+");
                                targetKey = keys.get(key);
                                inputParsedList.remove(targetKey);
                                inputParsedList.remove(targetKey);
                                inputParsedList.add(targetKey, "+");
                                inputParsed = inputParsedList.toArray(new String[0]);
                                keys.remove(key2);
                            }

                            // Making the negative number, an actual negative number
                            // by making sure the operationMap and keys are properly adjusted
                            // and editing the number in inputParsed to make it negative
                            else if (operationMap.get(keys.get(key2)).equals("-")) {
                                inputParsedList = new ArrayList<>(Arrays.asList(inputParsed));
                                operationMap.remove(keys.get(key2));
                                targetKey = keys.get(key2);
                                inputParsedList.remove(targetKey);
                                inputParsedList.add(targetKey, "-"+inputParsedList.get(targetKey));
                                inputParsedList.remove(targetKey+1);
                                inputParsed = inputParsedList.toArray(new String[0]);
                                keys.remove(key2);
                            } else {
                                print("Error: You can't have two operators next to each other");
                                break;
                            }
                        }
                        // checking if first operator exists and is "-"
                        if (keys.getFirst() == 0 && operationMap.get(keys.getFirst()).equals("-") && key == 0) {
                            inputParsedList = new ArrayList<>(Arrays.asList(inputParsed));
                            keys.removeFirst();
                            inputParsedList.removeFirst();
                            inputParsedList.removeFirst();
                            inputParsedList.addFirst("-" + inputParsed[1]);
                            inputParsed = inputParsedList.toArray(new String[0]);
                            operationMap.remove(0);
                            beginsWithMinus = true;
                        }
                        // correcting keys list and operationMap
                        if (!beginsWithMinus) {targetKey = keys.get(key);}
                        while(true) {
                            int x = 0;
                            try {
                                for (Integer i : keys) {
                                    if (i > targetKey) {
                                        operation = operationMap.get(i);
                                        operationMap.remove(i);
                                        operationMap.put(i - 1, operation);
                                        targetKey = i-1;
                                        keys.add(x, i - 1);
                                        x++;
                                        keys.remove(x);
                                    } else x++;
                                }
                            } catch (RuntimeException e) {
                                continue; // ignore problem because I don't know how to fix it, this is a workaround
                                // band-aid fix but it works lmao
                            }
                            beginsWithMinus = false;
                            break;
                        }

                        // sorting operationMap
                        Map<Integer, String> newOperationMap = new LinkedHashMap<>();
                        for (Integer integer : keys) {
                            newOperationMap.put(integer, operationMap.get(integer));
                        }
                        operationMap = newOperationMap;
                        }
                        exit = true;
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
                answer = result;
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