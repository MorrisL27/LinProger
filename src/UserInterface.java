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
    static double[] f;
    static double[][] a;
    static double[] m;
    static double[] b;
    static double solF;
    static double solM;
    static boolean modeMax;

    // parameters for dual problem
    static int numVarDual;
    static int numConsDual;
    static double[] fDual;
    static double[][] aDual;
    static double[] mDual;
    static double[] bDual;
    static double solFDual;
    static double solMDual;
    static boolean modeMaxDual;

    // parameters for LinProger
    static int tempNumVar;
    static int tempNumCons;
    static double[] tempF;
    static double[][] tempA;
    static double[] tempM;
    static double[] tempB;
    static double tempSolF;
    static double tempSolM;

    static int tempNumNegB;

    static boolean initialized;

    static String lineMiu;
    static String lineT;

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
            //int numVar;
            //int numCons;

            switch (option) {
                case "help":
                    if (s.length == 1) {
                        System.out.printf("%-15s%s", "PROB", "Display the current problem.\n");
                        System.out.printf("%-15s%s", "DEF", "Define the dimension of the problem.\n");
                        System.out.printf("%-15s%s", "VAR", "Modify the value of that variable.\n");
                        System.out.printf("%-15s%s", "MODE", "Change the mode of problem.\n");
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

                            case "mode":
                                System.out.println("Change the mode of problem.\n");
                                System.out.println("MODE [mode]");
                                System.out.printf("%-15s%s", "mode", "max or min.\n");
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
                                System.out.println("STAT\n");
                                // add more documentation here
                                // TODO: add stat for each variable

                                System.out.println();
                                break;

                            case "init":
                                System.out.println("Initialize the LinProger with parameters of problem.\n");
                                System.out.println("INIT\n");
                                System.out.println("INIT DUAL\n");

                                System.out.println();
                                break;

                            case "prob":
                                System.out.println("Display the current problem.\n");
                                System.out.println("PROB\n");
                                System.out.println("PROB DUAL\n");

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
                    if (s.length >= 2) {
                        String row = s[1].toLowerCase();
                        double[] entries;

                        if (s.length == 2) {
                            switch (row) {
                                case "a":

                                    // update the whole matrix A
                                    //numCons = Nina.getNumCons();

                                    for (int i = 0; i < numCons; i++) {
                                        entries = fetchRow("" + (i + 1));

                                        if (entries == null) {
                                            System.out.println("Update failed.\n");
                                            break;
                                        }

                                        //numVar = Nina.getNumVar();

                                        if (entries.length == numVar) {
                                            setA(i, entries);
                                            //Nina.setA(i, entries);
                                        } else {
                                            System.out.println("Illegal number of arguments.\n");
                                            System.out.println(row + " should contain " + numVar + " entries.\n");
                                            break;
                                        }

                                        setDual();
                                        initialized = false;
                                        System.out.println("Update " + row + " successfully.\n");
                                    }

                                    break;


                                case "f":
                                    entries = fetchRow(row);

                                    if (entries == null) {
                                        System.out.println("Update failed.\n");
                                        break;
                                    }

                                    //numVar = Nina.getNumVar();

                                    if (entries.length == (numVar + 1)) {
                                        setF(Arrays.copyOf(entries, entries.length - 1));
                                        solF = entries[entries.length - 1];
                                    } else {
                                        System.out.println("Illegal number of arguments.\n");
                                        System.out.println(row + " should contain " + numVar + " entries and 1 solution.\n");
                                        break;
                                    }

                                    setDual();
                                    initialized = false;
                                    System.out.println("Update " + row + " successfully.\n");
                                    break;

                                case "b":
                                    entries = fetchRow(row);

                                    if (entries == null) {
                                        System.out.println("Update failed.\n");
                                        break;
                                    }

                                    //numCons = Nina.getNumCons();

                                    if (entries.length == numCons) {
                                        setB(entries);
                                    } else {
                                        System.out.println("Illegal number of arguments.\n");
                                        System.out.println(row + " should contain " + numCons + " entries.\n");
                                        break;
                                    }

                                    setDual();
                                    initialized = false;
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
                                    System.out.println("Variable not exists.\n");
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

                                //numVar = Nina.getNumVar();

                                if (entries.length == numVar) {
                                    setA(index, entries);
                                } else {
                                    System.out.println("Illegal number of arguments.\n");
                                    System.out.println(row + " should contain " + numVar + " entries.\n");
                                    break;
                                }

                                setDual();
                                initialized = false;
                                System.out.println("Update " + row + " successfully.\n");
                            }else {
                                System.out.println("Variable not exists.\n");
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
                        if (initialized) {
                            System.out.println("Current status: ");
                            if (Nina.getMode()) {
                                System.out.println("mode: Max");
                            } else {
                                System.out.println("mode: Min");
                            }

                            Nina.showTableau();
                            System.out.println();
                        }else {
                            System.out.println("Nina not initialized by new problem!\n");
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "prob":
                    if (s.length == 1) {
                        System.out.println("Original problem:");
                        showProblem(false);
                        System.out.println();
                    } else if (s.length == 2) {
                        if (s[1].equals("dual")) {
                            System.out.println("Dual problem:");

                            setDual();
                            showProblem(true);
                            System.out.println();
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "init":
                    if (s.length == 1) {
                        transform(false);

                        // TODO: add dual

                        if (modeMax) {
                            Nina.modeMax();
                        } else {
                            Nina.modeMin();
                        }

                        initialized = true;
                        System.out.println("Nina initialized.\n");
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "run":
                    if (s.length == 1) {
                        if (initialized) {
                            showBigM();
                            Nina.run();
                        }else {
                            System.out.println("Nina not initialized by new problem!\n");
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "mode":
                    if (s.length == 2) {
                        String mode = s[1].toLowerCase();
                        switch(mode) {
                            case "max":
                                if (!modeMax) {
                                    modeMax = true;
                                    setDual();

                                    initialized = false;
                                    System.out.println("Problem changed to max mode.\n");
                                }else {
                                    System.out.println("Problem already in max mode!\n");
                                }
                                break;

                            case "min":
                                if (modeMax) {
                                    modeMax = false;
                                    setDual();

                                    initialized = false;
                                    System.out.println("Problem changed to min mode.\n");
                                }else {
                                    System.out.println("Problem already in min mode!\n");
                                }
                                break;

                            default:
                                System.out.println("Invalid mode.\n");
                                break;
                        }
                    } else {
                        System.out.println("Illegal number of arguments.\n");
                    }
                    break;

                case "def":
                    if (s.length == 3) {
                        int readVar = 0;
                        int readCons = 0;

                        try {
                            readVar = Integer.parseInt(s[1]);
                        } catch (Exception e){
                            System.out.println("Number of variables is not a number.\n");
                            break;
                        }

                        try {
                            readCons = Integer.parseInt(s[2]);
                        } catch (Exception e){
                            System.out.println("Number of constraints is not a number.\n");
                            break;
                        }

                        numVar = readVar;
                        numCons = readCons;

                        setDimension(numVar, numCons);
                        setDimensionDual();

                        initialized = false;
                        System.out.println("Status updated.\n");
                    }else {
                        System.out.println("Illegal number of arguments.\n");
                    }

                    break;

                case "exit":
                    System.out.println("See you!");

                    // cmd can not recognize Unicode
                    //System.out.println("See you! \u2708\n");
                    break;

                default:
                    System.out.println("Invalid command.\n");
            }
        }

        System.out.println("LinProger End.\n");
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
        initialized = false;

        modeMax = false;
        solF = 0;
        numCons = 3;
        //numSol = 1;
        numVar = 3;
        f = new double[] {2, -3, 6};
        a = new double[numCons][];
        a[0] = new double[] {-3, 4, 6};
        a[1] = new double[] {-2, -1, -2};
        a[2] = new double[] {1, 3, -2};
        b = new double[] {-2, -11, 5};

        // for original problem
        transform(numVar, numCons, f, a, b, solF, modeMax);

        // for dual problem
        //transform(numVarDual, numConsDual, fDual, aDual, bDual, solFDual, modeMaxDual);

        initialized = true;
    }

    public static void transform(boolean dual) {
        if (dual) {
            transform(numVarDual, numConsDual, fDual, aDual, bDual, solFDual, modeMaxDual);
        }else {
            transform(numVar, numCons, f, a, b, solF, modeMax);
        }
    }

    public static void transform(int numVar, int numCons, double[] f, double[][] a, double[] b, double solF, boolean modeMax) {
        // describe a problem here.
        tempSolF = solF;
        tempNumCons = numCons;
        //numSol = 1;
        tempNumVar = numVar;
        tempF = Arrays.copyOf(f, f.length);
        tempA = Arrays.copyOf(a, a.length);
        tempB = Arrays.copyOf(b, b.length);

        initialize(tempF, tempA, tempB);

        Nina.setDimension(tempNumVar, tempNumCons);
        Nina.setNumNegB(tempNumNegB);

        Nina.initialize(tempF, tempSolF, tempM, tempSolM, tempA, tempB);

        if (modeMax) {
            Nina.modeMax();
        }
    }

    public static void showProblem(boolean dual) {
        if (dual) {
            showProblem(fDual, aDual, bDual, solFDual, modeMaxDual);
        }else {
            showProblem(f, a, b, solF, modeMax);
        }
    }

    public static void showProblem(double[] f, double[][] a, double[] b, double solF, boolean modeMax) {
        if (modeMax) {
            System.out.print("max ");
        }else {
            System.out.print("min ");
        }

        String lineF = "z = ";
        for (int i = 0; i < f.length; i++) {
            if (f[i] < 0) {
                lineF += "- " + (-f[i]) + "x" + (i + 1) + " ";
            }else {
                lineF += "+ " + f[i] + "x" + (i+1) + " ";
            }
        }
        if (solF < 0) {
            lineF += "- " + (-solF);
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
    }



    // conversion inside temp
    public static void initialize(double[] f, double[][] a, double[] b) {
        // rearrange here
        double[][] oldA;
        double[] oldB;

        oldA = Arrays.copyOf(a, a.length);
        a = new double[numCons][numVar];
        oldB = Arrays.copyOf(b, b.length);
        b = new double[numCons];

        int indexRe = 0;
        // show line i applied Big-M
        lineMiu = "";
        lineT = "";
        for (int i = 0; i < b.length; i++) {
            if (oldB[i] < 0) {
                for (int j = 0; j < a[i].length; j++) {
                    a[indexRe][j] = oldA[i][j];
                    b[indexRe] = oldB[i];
                }

                int index = (indexRe+1);
                // show line i applied Big-M
                lineMiu += "μ" + index + " = ";
                lineT += "t" + index + " = ";
                for (int j = 0; j < a[i].length; j++) {
                    double ai = -oldA[i][j];
                    if (ai < 0) {
                        lineMiu += "- " + (-ai) + "x" + (j+1) + " ";
                    }else {
                        lineMiu += "+ " + ai + "x" + (j+1) + " ";
                    }

                    ai = oldA[i][j];
                    if (ai < 0) {
                        lineT += "- " + (-ai) + "x" + (j+1) + " ";
                    }else {
                        lineT += "+ " + ai + "x" + (j+1) + " ";
                    }
                }
                double bi = oldB[i];
                if (bi < 0) {
                    lineMiu += "- " + (-bi) + "\n";
                }else {
                    lineMiu += "+ " + bi + "\n";
                }

                bi = -bi;

                lineT += "+ μ" + index + " ";

                if (bi < 0) {
                    lineT += "- " + (-bi) + "\n";
                }else {
                    lineT += "+ " + bi + "\n";
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
                if (modeMax) {
                    tempSolM += b[i];
                }else {
                    tempSolM -= b[i];
                }
            }
        }

        tempNumVar = numVar;
        tempNumVar += numNegB;

        tempM = new double[tempNumVar];
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                for (int j = 0; j < tempM.length; j++) {
                    if (j < tempNumVar-numNegB) {
                        if (modeMax) {
                            tempM[j] -= a[i][j];
                        }else {
                            tempM[j] += a[i][j];
                        }
                    }else {
                        if (modeMax) {
                            tempM[j] = -1;
                        }else {
                            tempM[j] = 1;
                        }
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
        tempNumNegB = numNegB;
    }

    public static void setA(int index, double[] rowA) {
        a[index] = rowA;
    }

    public static void setF(double[] rowF) {
        f = Arrays.copyOf(rowF, rowF.length);
    }

    public static void setB(double[] rowB) {
        b = Arrays.copyOf(rowB, rowB.length);
    }

    public static void setDimension(int numVar, int numCons) {
        f = new double[numVar];
        a = new double[numCons][numVar];
        b = new double[numCons];
        m = new double[numVar];
        solF = 0;
        solM = 0;
    }

    public static void showBigM() {
        if (!lineMiu.equals("")) {
            System.out.println("Big-M Technique applied.");
        }
        System.out.println(lineMiu);
        System.out.println(lineT);
    }

    public static void setDual() {
        if (solF == 0) {
            setDimensionDual();

            if (modeMax) {
                modeMaxDual = false;

                // set fDual
                for (int i = 0; i < fDual.length; i++) {
                    fDual[i] = b[i];
                }

                // set bDual
                for (int i = 0; i < bDual.length; i++) {
                    if (f[i] != 0) {
                        bDual[i] = -f[i];
                    }else {
                        bDual[i] = 0;
                    }

                }
            }else {
                modeMaxDual = true;

                // set fDual
                for (int i = 0; i < fDual.length; i++) {
                    if (b[i] != 0) {
                        fDual[i] = -b[i];
                    }else {
                        fDual[i] = 0;
                    }
                }

                // set bDual
                for (int i = 0; i < bDual.length; i++) {
                    bDual[i] = f[i];
                }
            }

            // set aDual
            aDual = negativeMatrix(transposeMatrix(a));

            //initialize();
        }else {
            System.out.println("Content non-zero solF!");
        }

    }

    public static double[][] transposeMatrix(double[][] a) {
        double[][] newA = new double[a[0].length][a.length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                newA[j][i] = a[i][j];
            }
        }
        return newA;
    }

    public static double[][] negativeMatrix(double[][] a) {
        double[][] newA = Arrays.copyOf(a, a.length);

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != 0) {
                    newA[i][j] = -a[i][j];
                }else {
                    newA[i][j] = 0;
                }
            }
        }
        return newA;
    }

    public static void setDimensionDual() {
        numVarDual = numCons;
        numConsDual = numVar;

        fDual = new double[numVarDual];
        aDual = new double[numConsDual][numVarDual];
        bDual = new double[numConsDual];
        mDual = new double[numVarDual];
        solFDual = 0;
        solMDual = 0;
    }
}
