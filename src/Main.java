import java.io.FileNotFoundException;
import java.util.Arrays;
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
        defaultInitialize();

        System.out.println("Welcome to LinProger for simplex method.");

        Scanner in = new Scanner(System.in);

        String command = "";
        String option = "";

        while (!option.equals("exit")) {
            System.out.print("Nina\\>");
            command = in.nextLine();
            String[] s = handler(command);
            option = s[0].toLowerCase();
            int numVar;
            int numCons;

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
                                System.out.println("VAR [variable]");
                                System.out.println("VAR A [row of A]\n");
                                System.out.printf("%-15s%s", "variable", "the variable you want to modify.\n");
                                System.out.printf("%-15s%s", "row of A", "the row of matrix A you want to modify.\n");
                                // add more documentation here

                                System.out.println();
                                break;

                            case "stat":
                                System.out.println("Display the status of variables and matrices.\n");
                                //System.out.println("STAT\n" + "STAT [variable]\n");
                                //System.out.println("variable\t\n");
                                System.out.println("STAT\n");
                                //System.out.printf("%-15s%s", "variable", "the variable you want to show the status.\n");

                                System.out.println();
                                break;

                            case "run":
                                System.out.println("Calculate and display the tableau step by step.\n");
                                System.out.println("RUN MIN");
                                System.out.println("RUN MAX\n");
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
                    String row = s[1].toLowerCase();
                    double[] entries;

                    if (s.length == 2) {
                        switch (row) {
                            case "a":
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
                                    } else {
                                        System.out.println("Illegal number of arguments.\n");
                                        System.out.println(row + " should contain " + numVar + " entries.\n");
                                        break;
                                    }
                                    System.out.println("Update " + row + " successfully.\n");
                                }

                                break;


                            case "f":
                                entries = fetchRow(row);

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numVar = Nina.getNumVar();

                                if (entries.length == (numVar + 1)) {
                                    Nina.setF(Arrays.copyOf(entries, entries.length - 1));
                                    Nina.setSolF(entries[entries.length - 1]);
                                } else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries and 1 solution.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
                                break;

                            case "fm":
                                entries = fetchRow(row);

                                if (entries == null) {
                                    System.out.println("Update failed.\n");
                                    break;
                                }

                                numVar = Nina.getNumVar();

                                if (entries.length == (numVar + 1)) {
                                    Nina.setFM(Arrays.copyOf(entries, entries.length - 1));
                                    Nina.setSolM(entries[entries.length - 1]);
                                } else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries and 1 solution.\n");
                                    break;
                                }
                                System.out.println("Update " + row + " successfully.\n");
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
                                } else {
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
                    } else if (s.length == 3) {
                        if (s[1].equals("a")) {
                            // update a[i - 1] with real index i

                            int index;
                            try {
                                index = Integer.parseInt(s[2]) - 1;
                            } catch (Exception e) {
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
                            } else {
                                System.out.println("Illegal number of arguments.\n");
                                System.out.println(row + " should contain " + numVar + " entries.\n");
                                break;
                            }
                            System.out.println("Update " + row + " successfully.\n");
                        }else {
                            System.out.println("Variable not exists.");
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;
                case "stat":
                    if (s.length == 1) {
                        System.out.println("Current status: ");
                        if (Nina.getMode()) {
                            System.out.println("mode: Max");
                        }else {
                            System.out.println("mode: Min");
                        }

                        Nina.showTableau();
                        System.out.println();
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "run":
                    if (s.length == 2) {
                        String mode = s[1].toLowerCase();
                        switch(mode) {
                            case "max":
                                Nina.modeMax();
                                Nina.run();
                                break;
                            case "min":
                                Nina.modeMin();
                                Nina.run();
                                break;
                            default:
                                System.out.println("Invalid mode.");
                                break;
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "def":
                    if (s.length == 3) {
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

    public static void defaultInitialize() {
        int numVar = 3;
        int numCons = 3;
        //numSol = 1;
        Nina.setDimension(numVar, numCons);

        double[] f = new double[] {4, 1, 0};
        double[][] a = new double[numCons][];

        a[0] = new double[] {3, 1, 0};
        a[1] = new double[] {4, 3, -1};
        a[2] = new double[] {1, 2, 0};

        double[] m = new double[] {-7, -4, 1};
        double[] b = new double[] {3, 6, 4};

        Nina.initialize(f, 0, m, 9, a, b);
    }
}
