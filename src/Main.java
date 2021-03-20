import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static LinProger Nina;

    public static void main(String[] args) {
        //new LinProger().linProg();
        userInterface();
    }

    public static void userInterface() {
        Nina = new LinProger();
        System.out.println("Welcome to LinProger for simplex method.");

        Scanner in = new Scanner(System.in);

        String command = "";
        String option = "";

        while (!option.equals("exit")) {
            System.out.print("Nina\\>");
            command = in.nextLine();
            String[] s = handler(command);
            option = s[0].toLowerCase();

            switch (option) {
                case "help":
                    if (s.length == 1) {
                        System.out.printf("%-15s%s", "VAR", "Modify the value of that variable.\n");
                        System.out.printf("%-15s%s", "STAT", "Display the status of variables and matrices.\n");
                        System.out.printf("%-15s%s", "RUN", "Calculate and display the tableau step by step.\n");
                        System.out.printf("%-15s%s", "EXIT", "Exit the LinProger.\n");
                        // add more documentation here

                        System.out.println();
                    } else {
                        switch (s[1]) {
                            case "var":
                                System.out.println("Modify the value of that variable.\n");
                                System.out.println("VAR [variable]\n");
                                System.out.println("VAR [variable] ; [row of A]\n");
                                System.out.printf("%-15s%s", "variable", "the variable you want to modify.\n");
                                System.out.printf("%-15s%s", "row of A", "the row of matrix A you want to modify.\n");
                                // add more documentation here

                                System.out.println();
                                break;

                            case "stat":
                                System.out.println( "Display the status of variables and matrices.\n");
                                System.out.println("STAT\n"
                                    + "STAT [variable]\n");
                                System.out.println("variable\t\n");
                                System.out.printf("%-15s%s", "variable", "the variable you want to show the status.\n");

                                System.out.println();
                                break;

                            case "run":
                                System.out.println("Calculate and display the tableau step by step.\n");
                                System.out.println("RUN\n");
                                break;

                            case "exit":
                                System.out.println("Exit the LinProger.\n");
                                System.out.println("EXIT\n");
                                break;

                            default:
                                System.out.println("Invalid command\n");
                        }
                    }
                    break;

                case "var":
                    if (s.length == 2) {
                        String row = s[1].toLowerCase();

                        double[] entries;
                        int numVar;
                        int numCons;

                        if (row.equals("a")) {

                            // update the whole matrix A
                            numCons = Nina.getNumCons();

                            for (int i = 0; i < numCons; i++) {
                                entries = fetchRow("" + (i + 1));

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numVar = Nina.getNumVar();

                                if (entries.length == numVar) {
                                    Nina.setA(i, entries);
                                }else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
                            }


                            /*

                            System.out.print("Update row number: ");
                            command = in.nextLine();
                            s = handler(command);

                            if (s.length == 1) {
                                // update a[i - 1] with real index i

                                int index;
                                try {
                                    index = Integer.parseInt(s[0]) - 1;
                                } catch (Exception e){
                                    System.out.println("Row number is not a number.\n");
                                    break;
                                }

                                entries = fetchRow(row);

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numVar = Nina.getNumVar();

                                if (entries.length == numVar) {
                                    Nina.setA(index, entries);
                                }else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
                            }else {
                                System.out.println("Illegal number of arguments.\n");
                            }*/

                            break;
                        }

                        switch(row) {
                            case "f":
                                entries = fetchRow(row);

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numVar = Nina.getNumVar();

                                if (entries.length == numVar) {
                                    Nina.setF(entries);
                                }else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
                                break;

                            case "fm":
                                break;

                            case "b":
                                entries = fetchRow(row);

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numCons = Nina.getNumCons();

                                if (entries.length == numCons) {
                                    Nina.setB(entries);
                                }else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numCons + " entries.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
                                break;

                            case "aeq":
                                break;
                            case "beq":
                                break;

                            case "lb":
                                break;
                            case "ub":
                                break;
                            default:
                                System.out.println("Variable not exists.");
                        }

                        //Nina.showTableau();
                    }else if (s.length == 4) {

                    }else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "stat":
                    if (s.length == 1) {
                        System.out.println("Current status: ");
                        Nina.showTableau();
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "run":
                    Nina.run();
                    break;

                case "def":
                    if (s.length == 3) {
                        int numVar;
                        int numCons;

                        try {
                            numVar = Integer.parseInt(s[1]);
                        } catch (Exception e){
                            System.out.println("Number of variables is not a number.\n");
                            break;
                        }

                        try {
                            numCons = Integer.parseInt(s[2]);
                        } catch (Exception e){
                            System.out.println("Number of constraints is not a number.\n");
                            break;
                        }

                        Nina.setDimension(numVar, numCons);

                        System.out.println("Status updated.\n");
                    }else {
                        System.out.println("Illegal number of arguments.\n");
                    }

                    break;

                case "exit":
                    System.out.println("See you! \u2708\n");
                    break;

                default:
                    System.out.println("Invalid command.\n");
            }
        }

        System.out.println("LinProger End.");
        in.close();
    }

    public static String[] handler(String command) {
        String[] arr = command.trim().split("\\s+");
        return arr;
    }

    public static double[] fetchRow(String row) {
        Scanner in = new Scanner(System.in);

        System.out.print("Update " + row + " by: ");
        String command = in.nextLine();
        String[] s = handler(command);
        double[] entries = new double[s.length];

        for (int i = 0; i < s.length; i++) {
            double entry;
            try {
                entry = Double.parseDouble(s[i]);
            } catch (Exception e){
                System.out.println("Row[" + (i + 1) + "] is not a number.\n");
                entries = null;
                break;
            }
            entries[i] = entry;
        }

        return entries;
    }
}
