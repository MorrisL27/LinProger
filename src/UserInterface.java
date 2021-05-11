import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class UserInterface {
    static LinProger Nina;

    // parameters for original problem
    static int numVar;
    static int numCons;
//    static double[] f = new double[numVar];
//    static double[][] a = new double[numCons][numVar];
//    static double[] m = new double[numVar];
//    static double[] b = new double[numCons];
    static double[] f;
    static double[][] a;
    static double[] m;
    static double[] b;
    static double solF;
    static double solM;
    static boolean modeMax;

    // parameters for LinProger
    static int tempNumVar;
    static int tempNumCons;
    static double[] tempF;
    static double[][] tempA;
    static double[] tempM;
    static double[] tempB;
    static double tempSolF;
    static double tempSolM;

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
                        System.out.printf("%-15s%s", "PROB", "Display the current problem.\n");
                        System.out.printf("%-15s%s", "DEF", "Define the dimension of the problem.\n");
                        System.out.printf("%-15s%s", "VAR", "Modify the value of that variable.\n");
                        System.out.printf("%-15s%s", "INIT", "Initialize the LinProger with parameters of problem.\n");
                        System.out.printf("%-15s%s", "STAT", "Display the status of variables and matrices.\n");
                        System.out.printf("%-15s%s", "RUN", "Calculate and display the tableau step by step.\n");
                        System.out.printf("%-15s%s", "EXIT", "Exit the LinProger.\n");
                        // add more documentation here

                        System.out.println();
                    } else {
                        switch (s[1]) {
                            case "def":
                                System.out.println("Define the dimension of the problem.\n");
                                System.out.println("DEF [numVar] [numCons]");
                                System.out.printf("%-15s%s", "numVar", "the number of variables.\n");
                                System.out.printf("%-15s%s", "numCons", "the number of constraints.\n");
                                // add more documentation here

                                System.out.println();
                                break;

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

                            case "init":
                                System.out.println("Initialize the LinProger with parameters of problem.\n");
                                System.out.println("INIT\n");

                                System.out.println();
                                break;

                            case "prob":
                                System.out.println("Display the current problem.\n");
                                System.out.println("PROB\n");

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
                    if (s.length >= 2) {
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

                                    // buffer f in Main
/*
                                    if (entries.length == (numVar + 1)) {
                                        Nina.setF(Arrays.copyOf(entries, entries.length - 1));
                                        Nina.setSolF(entries[entries.length - 1]);
                                    } else {
                                        System.out.println("Illegal number of arguments.\n");
                                        System.out.println(row + " should contain " + numVar + " entries and 1 solution.\n");
                                        break;
                                    }

 */


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

                case "prob":
                    if (s.length == 1) {
                        /*
                        System.out.println("Current status: ");
                        if (Nina.getMode()) {
                            System.out.println("mode: Max");
                        }else {
                            System.out.println("mode: Min");
                        }*/

                        showProblem();
                        System.out.println();
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "init":
                    if (s.length == 1) {
                        transform();

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
                                modeMax = true;
                                Nina.modeMax();
                                Nina.run();
                                break;
                            case "min":
                                modeMax = false;
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
//        numVar = 3;
//        numCons = 3;
//        //numSol = 1;
//
//
//        f = new double[] {4, 1, 0};
//        a = new double[numCons][];
//
//        a[0] = new double[] {3, 1, 0};
//        a[1] = new double[] {4, 3, -1};
//        a[2] = new double[] {1, 2, 0};
//
//        m = new double[] {-7, -4, 1};
//        b = new double[] {3, 6, 4};
//
//        solF = 10;
//        solM = 9;

        modeMax = false;
        solF = 10;
        numCons = 4;
        //numSol = 1;
        numVar = 2;
        f = new double[] {4, 1};
        a = new double[numCons][];
        a[0] = new double[] {3, 1};
        a[1] = new double[] {-3, -1};
        a[2] = new double[] {-4, -3};
        a[3] = new double[] {1, 2};
        b = new double[] {3, -3, -6, 4};

        transform();
    }

    public static void transform() {
        // describe a problem here.
        tempSolF = solF;
        tempNumCons = numCons;
        //numSol = 1;
        //tempNumVar = numVar;
        tempF = Arrays.copyOf(f, f.length);
        tempA = Arrays.copyOf(a, a.length);
        tempB = Arrays.copyOf(b, b.length);

        initialize(tempF, tempA, tempB);

        Nina.setDimension(tempNumVar, tempNumCons);
        Nina.initialize(tempF, tempSolF, tempM, tempSolM, tempA, tempB);
    }

    public static void showProblem() {
        if (modeMax) {
            System.out.print("max ");
        }else {
            System.out.print("min ");
        }

        String lineF = "z = ";
        for (int i = 0; i < f.length; i++) {
            if (f[i] < 0) {
                lineF += "- " + (-f[i]) + "x" + (i+1) + " ";
            }else {
                lineF += "+ " + f[i] + "x" + (i+1) + " ";
            }
        }
        if (solF < 0) {
            lineF += solF;
        }else {
            lineF += "+ " + solF;
        }

        System.out.println(lineF);

        System.out.println("s.t.");

        String lineA = "";

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0) {
                    lineA += "- " + (-a[i][j]) + "x" + (j+1) + " ";
                }else {
                    lineA += "+ " + a[i][j] + "x" + (j+1) + " ";
                }
            }

            lineA += "<= " + b[i];

            System.out.println(lineA);
            lineA = "";
        }
        //System.out.println();
    }

    public static void initialize(double[] f, double[][] a, double[] b) {
        // rearrange here
        double[][] oldA;
        double[] oldB;

        oldA = Arrays.copyOf(a, a.length);
        a = new double[numCons][numVar];
        oldB = Arrays.copyOf(b, b.length);
        b = new double[numCons];

        int indexRe = 0;
        for (int i = 0; i < b.length; i++) {
            if (oldB[i] < 0) {
                for (int j = 0; j < a[i].length; j++) {
                    a[indexRe][j] = oldA[i][j];
                    b[indexRe] = oldB[i];
                }
                indexRe++;
            }
        }

        for (int i = 0; i < b.length; i++) {
            if (oldB[i] >= 0) {
                for (int j = 0; j < a[i].length; j++) {
                    a[indexRe][j] = oldA[i][j];
                    b[indexRe] = oldB[i];
                }
                indexRe++;
            }
        }


        tempSolM = 0;
        int numNegB = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                numNegB++;
                tempSolM -= b[i];
            }
        }
        //solM = 9;
        //System.out.println("TempSolM: " + tempSolM);

        //numVar = 4; // numVar += numNegB
        tempNumVar = numVar;
        tempNumVar += numNegB;
        //numCons = 4;
        //numSol = 1;
        //solF = 10;

        tempM = new double[tempNumVar];
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                for (int j = 0; j < tempM.length; j++) {
                    if (j < tempNumVar-numNegB) {
                        tempM[j] += a[i][j];
                    }else {
                        tempM[j] = 1;
                    }
                }
            }
        }

        double[] oldF = Arrays.copyOf(f, f.length);

        f = new double[tempNumVar];

        for (int i = 0; i < f.length; i++) {
            if (i < tempNumVar-numNegB) {
                f[i] = oldF[i];
            }
        }

        oldA = Arrays.copyOf(a, a.length);
        a = new double[numCons][tempNumVar];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (j < tempNumVar-numNegB) {
                    if (b[i] < 0) {
                        a[i][j] = -oldA[i][j];
                    }else {
                        a[i][j] = oldA[i][j];
                    }
                }else {
                    if (b[i] < 0) {
                        if (j - i == (tempNumVar-numNegB)) {
                            a[i][j] = -1;
                        }else {
                            a[i][j] = 0;
                        }
                    }else {
                        a[i][j] = 0;
                    }
                }
            }
        }

        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                b[i] = -b[i];
            }
        }

        tempA = Arrays.copyOf(a, a.length);
        tempB = Arrays.copyOf(b, b.length);
        tempF = Arrays.copyOf(f, f.length);

//        System.out.print("innerF: ");
//        for (double num : f) {
//            System.out.print(num + " ");
//        }
    }
}
