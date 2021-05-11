import java.util.ArrayList;
import java.util.Arrays;

public class LinProger {
    private double[] rowZ;
    private double[] displayZ;
    private double[] rowM;
    private double[] displayM;
    private double[][] rowS;

    private String[] rowZRational;
    private String[] rowMRational;
    private String[] rowZRationalDisplay;
    private String[] rowMRationalDisplay;
    private String[][] rowSRational;

    private double[] tempF;
    private double[] tempM;
    private double[] f;
    private double[] m; // big M
    private double[][] a; // A
    private double[] b;
    private double[][] aeq; // Aeq
    private double[] beq;
    private double[] lb;
    private double[] ub;

    private int[] bases;

    private double solF;
    private double solM;

    private double tempSolF;
    private double tempSolM;

    private int numVar;
    private int numCons;
    private final int numSol = 1;

    private boolean modeMax;
    // false: min mode
    // true: max mode

    private int numNegB;

    public static void main(String[] args) {
        new LinProger().run();
    }

    public LinProger() {
        modeMax = false;// min mode initially
    }

    public int getNumVar() {
        return numVar;
    }

    public int getNumCons() {
        return numCons;
    }

    public int getNumSol() {
        return numSol;
    }

    public void setF(double[] f) {
        this.f = f;
        tempF = Arrays.copyOf(f, f.length);
        setTableau();
        checkMode();
    }

    public void setSolF(double solF) {
        this.solF = solF;
        tempSolF = solF;
        setTableau();
        checkMode();
    }

    public void setSolM(double solM) {
        this.solM = solM;
        tempSolM = solM;
        setTableau();
        checkMode();
    }

    public void setA(int index, double[] rowA) {
        this.a[index] = rowA;
        setTableau();
    }

    public void setFM(double[] m) {
        this.m = m;
        tempM = Arrays.copyOf(m, m.length);
        setTableau();
        checkMode();
    }

    public void setB(double[] b) {
        this.b = b;
        setTableau();
    }

    public void setNumNegB(int numNegB) {
        this.numNegB = numNegB;
        //setTableau();
    }

    public void initialize(double[] f, double solF, double[] m, double solM, double[][] a, double[] b) {
        this.f = f;
        this.solF = solF;
        this.solM = solM;
        this.m = m;
        this.a = a;
        this.b = b;

        tempF = Arrays.copyOf(f, f.length);
        tempM = Arrays.copyOf(m, m.length);

        tempSolF = solF;
        tempSolM = solM;

        setTableau();

        // check mode and set up tempF and displayZ
        checkMode();
    }


    private void setZ(double[] rowZ) {
        this.rowZ = rowZ;
    }
    private void setS(double[][] rowS) {
        this.rowS = rowS;
    }
    private void setM(double[] rowM) {
        this.rowM = rowM;
    }

    public void setTableau() {
        rowZ = new double[numVar + numCons + numSol];
        rowM = new double[numVar + numCons + numSol];
        rowS = new double[numCons][numVar + numCons + numSol];

        rowZRational = new String[numVar + numCons + numSol];
        rowMRational = new String[numVar + numCons + numSol];
        rowSRational = new String[numCons][numVar + numCons + numSol];

        bases = new int[numCons];

        // set rowZ by tempF
        for (int i = 0; i < rowZ.length; i++) {
            if (i < numVar) {
                // actual value in rowZ is negative to tempF
                if (f[i]!=0) {
                    rowZ[i] = -tempF[i];
                }else {
                    rowZ[i] = tempF[i];
                }
            }else if (i < numVar + numCons) {
                rowZ[i] = 0;
            }else {
                // initial solution of f = 0
                //System.out.println(solF);
                rowZ[i] = tempSolF;
            }
        }

        // set rowM by tempM
        for (int i = 0; i < rowM.length; i++) {
            if (i < numVar) {
                if (m[i]!=0) {
                    rowM[i] = -tempM[i];
                }else {
                    rowM[i] = tempM[i];
                }
            }else if (i < numVar + numCons) {
                rowM[i] = 0;
            }else {
                // initial solution of rowM = solM
                //rowM[i] = solM;
                rowM[i] = tempSolM;
            }
        }

        for (int i = 0; i < rowS.length; i++) {
            double[] rowA = a[i];
            for (int j = 0; j < rowS[i].length; j++) {
                if (j < numVar) {
                    rowS[i][j] = rowA[j];
                } else if (j < numVar + numCons) {
                    // arrange Im
                    if (j - i == numVar) {
                        rowS[i][j] = 1;
                    }else {
                        rowS[i][j] = 0;
                    }
                } else {
                    // initial solution of rowS[i]
                    rowS[i][j] = b[i];
                }
            }
        }


        for (int i = 0; i < rowZ.length; i++) {
            rowZRational[i] = "" + (int) rowZ[i];
            //System.out.println("NMSL" + rowZRational[i]);
        }

        for (int i = 0; i < rowM.length; i++) {
            rowMRational[i] = "" + (int) rowM[i];
            //System.out.println("NMSL" + rowMRational[i]);

        }

        for (int i = 0; i < rowS.length; i++) {
            for (int j = 0; j < rowS[i].length; j++) {
                rowSRational[i][j] = "" + (int) rowS[i][j];
                //System.out.println("NMSL" + rowSRational[i][j]);
            }
        }

        resetBases();
    }

    private void resetBases() {
        for (int i = 0; i < bases.length; i++) {
            bases[i] = i + numVar;
        }
    }

    // n: number of variables
    // m: number of constraints
    public void setDimension(int numVar, int numCons) {
        this.numVar = numVar;
        this.numCons = numCons;
        rowZ = new double[numVar + numCons + numSol];
        rowM = new double[numVar + numCons + numSol];
        rowS = new double[numCons][numVar + numCons + numSol];
        f = new double[numVar];
        a = new double[numCons][numVar];
        b = new double[numCons];
        m = new double[numVar];
        solF = 0;
        solM = 0;

        checkMode();
    }

    public void showTableau() {
        //showTableau(displayZ, displayM, rowS);
        showTableau(rowZRationalDisplay, rowMRationalDisplay, rowSRational);
        //showTableau(rowZRational, rowMRational, rowSRational);
        //showTableau(rowZ, rowM, rowS);
    }

    private void showTableau(double[] rowZ, double[] rowM, double[][] rowS) {
        System.out.print("Basic ");

        for (int i = 0; i < numVar; i++) {
            //System.out.printf(" %-9s", "x" + (i + 1));
            System.out.printf(" %-9s", findVariable(i));
        }

        for (int i = 0; i < numCons; i++) {
            //System.out.printf(" %-9s", "s" + (i + 1));
            System.out.printf(" %-9s", findVariable(i + numVar));
        }

        System.out.println(" Solution");


        // check z
        System.out.print("z     ");
        for (int i = 0; i < rowZ.length; i++) {
            System.out.printf(" %-+9.4f", rowZ[i]);
        }
        System.out.println();

        System.out.print("M     ");
        for (int i = 0; i < rowM.length; i++) {
            System.out.printf(" %-+9.4f", rowM[i]);
        }
        System.out.println();

        // check A
        for (int j = 0; j < rowS.length; j++) {
            System.out.print(findVariable(bases[j]) + "    ");
            //System.out.print("S[" + (j + 1) + "]  ");
            for (int i = 0; i < rowS[0].length; i++) {
                System.out.printf(" %-+9.4f", rowS[j][i]);
            }
            System.out.println();
        }
        //System.out.println();
    }

    private void showTableau(String[] rowZ, String[] rowM, String[][] rowS) {
        System.out.print("Basic ");

        for (int i = 0; i < numVar; i++) {
            System.out.printf(" %-5s", findVariable(i));
        }

        for (int i = 0; i < numCons; i++) {
            System.out.printf(" %-5s", findVariable(i + numVar));
        }

        System.out.println(" Solution");

        // check z
        System.out.print("z     ");
        for (int i = 0; i < rowZ.length; i++) {
            System.out.printf(" %-5s", rowZ[i]);
        }
        System.out.println();

        System.out.print("M     ");
        for (int i = 0; i < rowM.length; i++) {
            System.out.printf(" %-5s", rowM[i]);
        }
        System.out.println();

        // check A
        for (int j = 0; j < rowS.length; j++) {
            System.out.print(findVariable(bases[j]) + "    ");
            for (int i = 0; i < rowS[0].length; i++) {
                System.out.printf(" %-5s", rowS[j][i]);
            }
            System.out.println();
        }
    }

    private void linProg(double[] rowZ, double[] rowM, double[][] rowS) {
        showTableau();

        // for min
        //boolean finish = false;
        double optimum = 0;

        while(true) {
            boolean allNegative = true;
            double max = -1;
            String maxRational = "" + -1;
            int entering = -1;

            ArrayList<String> str = new ArrayList<>();

            for (int i = 0; i < rowM.length - 1; i++) {
                if (rowM[i] > 0) {
                    allNegative = false;
                    if (max <= rowM[i]) {
                        max = rowM[i];
                        maxRational = rowMRational[i];
                        entering = i;
                    }
                }
            }

            if (allNegative) {
                max = -1;
                maxRational = "" + -1;
                entering = -1;

                for (int i = 0; i < rowZ.length - 1; i++) {
                    if (rowM[i] == 0.0) {
                        if (rowZ[i] > 0) {
                            allNegative = false;
                            if (max <= rowZ[i]) {
                                max = rowZ[i];
                                maxRational = rowZRational[i];
                                entering = i;
                            }
                        }
                    }
                }
            }

            if (allNegative) {
                //finish = true;
                optimum = rowZ[rowZ.length - 1];
                break;
            }

            int leaving = -1;
            double min = 1000000000.0;
            String minRational = "" + 1000000000;
            for (int i = 0; i < rowS.length; i++) {
                if (rowS[i][entering] > 0) {
                    double result = rowS[i][rowS[i].length - 1] / rowS[i][entering];
                    String resultRational = pivotOperation(rowSRational[i][rowSRational[i].length - 1], rowSRational[i][entering]);
                    str.add("result = " + rowSRational[i][rowSRational[i].length - 1] + " ÷ " + rowSRational[i][entering] + " = " + resultRational);
                    if (result < min) {
                        min = result;
                        minRational = resultRational;
                        leaving = i;
                    }
                }else {
                    str.add("result N/A");
                }
            }

            if (leaving == -1) {
                System.out.println("Leaving variable does not exist!");
                break;
            }

            double factor = rowS[leaving][entering];
            String cofactor = rowSRational[leaving][entering];

            for (int i = 0; i < rowS[leaving].length; i++) {
                rowSRational[leaving][i] = pivotOperation(rowSRational[leaving][i], cofactor);
                //System.out.println(rowSRational[leaving][i]);

                double result = rowS[leaving][i] / factor;
                rowS[leaving][i] = result;
            }

            factor = rowM[entering];
            cofactor = rowMRational[entering];

            for (int i = 0; i < rowM.length; i++) {
                rowMRational[i] = reduceOperation(rowMRational[i], cofactor, rowSRational[leaving][i]);

                double result = rowM[i] - factor * rowS[leaving][i];
                if (Math.abs(result) < 0.0001) {
                    result = 0;
                }
                rowM[i] = result;
            }

            factor = rowZ[entering];
            cofactor = rowZRational[entering];

            for (int i = 0; i < rowZ.length; i++) {
                rowZRational[i] = reduceOperation(rowZRational[i], cofactor, rowSRational[leaving][i]);

                double result = rowZ[i] - factor * rowS[leaving][i];
                if (Math.abs(result) < 0.0001) {
                    result = 0;
                }

                rowZ[i] = result;
            }

            for (int i = 0; i < rowS.length; i++) {
                if (i == leaving) {
                    continue;
                }

                factor = rowS[i][entering];
                cofactor = rowSRational[i][entering];
                for (int j = 0; j < rowS[i].length; j++) {
                    rowSRational[i][j] = reduceOperation(rowSRational[i][j], cofactor, rowSRational[leaving][j]);

                    double result = rowS[i][j] - factor * rowS[leaving][j];

                    if (Math.abs(result) < 0.0001) {
                        result = 0;
                    }

                    rowS[i][j] = result;
                }
            }

            String enterVariable = findVariable(entering);
            String leavingVariable = findVariable(bases[leaving]);
            str.add("entering: " + enterVariable + "; leaving: " + leavingVariable);
            swapBases(entering + 1, leaving + 1);

            //str.add("max: " + max + "; Min: " + min);
            str.add("max: " + maxRational + "; Min: " + minRational);


            for (String s : str) {
                System.out.println(s);
            }
            System.out.println();


            // rowZ has been updated, also update displayZ
            if (modeMax) {
                // set displayZ
                for (int i = 0; i < displayZ.length; i++) {
                    if (rowZ[i] != 0) {
                        displayZ[i] = -rowZ[i];
                    }else {
                        displayZ[i] = 0;
                    }
                }
            }else {
                displayZ = Arrays.copyOf(rowZ, rowZ.length);
            }

            // rowM has been updated, also update displayM
            if (modeMax) {
                // set displayM
                for (int i = 0; i < displayM.length; i++) {
                    if (rowM[i] != 0) {
                        displayM[i] = -rowM[i];
                    }else {
                        displayM[i] = 0;
                    }
                }
            }else {
                displayM = Arrays.copyOf(rowM, rowM.length);
            }

            // rowZRatioanl has been updated, also update rowZRationalDisplay
            if (modeMax) {
                // set rowZRationalDisplay
                for (int i = 0; i < rowZRationalDisplay.length; i++) {
                    if (!rowZRational[i].equals("0")) {
                        if (rowZRational[i].charAt(0) != '-') {
                            rowZRationalDisplay[i] = "-" + rowZRational[i];
                        }else {
                            rowZRationalDisplay[i] = rowZRational[i].substring(1);
                        }
                    }else {
                        rowZRationalDisplay[i] = rowZRational[i];
                    }
                }
            }else {
                // set rowZRationalDisplay
                rowZRationalDisplay = new String[rowZRational.length];
                for (int i = 0; i < rowZRationalDisplay.length; i++) {
                    rowZRationalDisplay[i] = rowZRational[i];
                }
            }

            // rowMRational has been updated, also update rowMRationalDisplay
            if (modeMax) {
                // set rowMRationalDisplay
                for (int i = 0; i < rowMRationalDisplay.length; i++) {
                    if (!rowMRational[i].equals("0")) {
                        if (rowMRational[i].charAt(0) != '-') {
                            rowMRationalDisplay[i] = "-" + rowMRational[i];
                        }else {
                            rowMRationalDisplay[i] = rowMRational[i].substring(1);
                        }
                    }else {
                        rowMRationalDisplay[i] = rowMRational[i];
                    }
                }
            }else {
                // set rowMRationalDisplay
                rowMRationalDisplay = new String[rowMRational.length];
                for (int i = 0; i < rowMRationalDisplay.length; i++) {
                    rowMRationalDisplay[i] = rowMRational[i];
                }
            }

            //showTableau(displayZ, displayM, rowS);
            //showTableau(displayZ, displayM, rowS);
            showTableau(rowZRationalDisplay, rowMRationalDisplay, rowSRational);

            //showTableau(rowZ, rowM, rowS);
            //System.out.println();
        }

        //System.out.println("Optimal value " + optimum + " has found.\n");
        System.out.println("Optimal value has found.\n");

        checkMode();
    }

    public void run() {
        checkMode();

        double[] rowZ = Arrays.copyOf(this.rowZ, this.rowZ.length);
        double[] rowM = Arrays.copyOf(this.rowM, this.rowM.length);
        double[][] rowS = new double[this.rowS.length][];


        for (int i = 0; i < this.rowS.length; i++) {
            rowS[i] = Arrays.copyOf(this.rowS[i], this.rowS[i].length);
        }

        linProg(rowZ, rowM, rowS);
        resetBases();
        setTableau();
    }

    public boolean getMode() {
        return modeMax;
    }

    private void checkMode() {
        if (modeMax) {
            // set tempF to -f
            for (int i = 0; i < tempF.length; i++) {
                if (f[i] != 0) {
                    tempF[i] = -f[i];
                }else {
                    tempF[i] = 0;
                }
            }

            // set tempM to -m
            for (int i = 0; i < tempM.length; i++) {
                if (m[i] != 0) {
                    tempM[i] = -m[i];
                }else {
                    tempM[i] = 0;
                }
            }

            if (solF != 0) {
                tempSolF = -solF;
            }else {
                tempSolF = 0;
            }

            if (solM != 0) {
                tempSolM = -solM;
            }else {
                tempSolM = 0;
            }

            setTableau();

            // set displayZ
            for (int i = 0; i < displayZ.length; i++) {
                if (rowZ[i] != 0) {
                    displayZ[i] = -rowZ[i];
                }else {
                    displayZ[i] = 0;
                }
            }

            // set displayM
            for (int i = 0; i < displayM.length; i++) {
                if (rowM[i] != 0) {
                    displayM[i] = -rowM[i];
                }else {
                    displayM[i] = 0;
                }
            }

            // set rowZRationalDisplay
            for (int i = 0; i < rowZRationalDisplay.length; i++) {
                if (!rowZRational[i].equals("0")) {
                    if (rowZRational[i].charAt(0) != '-') {
                        rowZRationalDisplay[i] = "-" + rowZRational[i];
                    }else {
                        rowZRationalDisplay[i] = rowZRational[i].substring(1);
                    }
                }else {
                    rowZRationalDisplay[i] = rowZRational[i];
                }
            }

            // set rowMRationalDisplay
            for (int i = 0; i < rowMRationalDisplay.length; i++) {
                if (!rowMRational[i].equals("0")) {
                    if (rowMRational[i].charAt(0) != '-') {
                        rowMRationalDisplay[i] = "-" + rowMRational[i];
                    }else {
                        rowMRationalDisplay[i] = rowMRational[i].substring(1);
                    }
                }else {
                    rowMRationalDisplay[i] = rowMRational[i];
                }
            }
        }else {
            // sync tempF to f
            tempF = Arrays.copyOf(f, f.length);
            tempM = Arrays.copyOf(m, m.length);
            tempSolF = solF;
            tempSolM = solM;

            setTableau();
            displayZ = Arrays.copyOf(rowZ, rowZ.length);
            displayM = Arrays.copyOf(rowM, rowM.length);

            // set rowZRationalDisplay
            rowZRationalDisplay = new String[rowZRational.length];
            for (int i = 0; i < rowZRationalDisplay.length; i++) {
                rowZRationalDisplay[i] = rowZRational[i];
            }

            // set rowMRationalDisplay
            rowMRationalDisplay = new String[rowMRational.length];
            for (int i = 0; i < rowMRationalDisplay.length; i++) {
                rowMRationalDisplay[i] = rowMRational[i];
            }
        }
    }

    public void modeMax() {
        if (!modeMax) {
            modeMax = true;
            checkMode();
            //System.out.println("Nina run in max mode.");
        }else {
            //System.out.println("Nina already in max mode.");
        }
    }

    public void modeMin() {
        if (modeMax) {
            modeMax = false;
            checkMode();
            //System.out.println("Nina run in min mode.");
        }else {
            //System.out.println("Nina already in min mode.");
        }
    }

    private String findVariable(int id) {
        if (id < 0) {
            return "";
        }else if (id < numVar - numNegB) {
            return "x" + (id + 1);
        }else if (id < numVar) {
            return "μ" + (id + 1 - (numVar - numNegB));
        }else if (id < numVar + numNegB) {
            return "t" + (id + 1 - numVar);
        }else if (id < numVar + numCons) {
            return "s" + (id + 1 - (numVar + numNegB));
        }else {
            return "";
        }
    }

    private void swapBases(int entering, int leaving) {
        int idEnter = entering - 1;
        int positionLeave = leaving - 1;

        bases[positionLeave] = idEnter;
    }

    //最大公约数(Greatest Common Divisor)
    public int gcd(int p,int q){
        if(q == 0)    return p;
        return gcd(q, p % q);
    }

    public boolean isFraction(String number) {
        String[] numbers = number.trim().split("/");

        if (numbers.length == 2) {
            return true;
        }
        return false;
    }

    public int[] splitFraction(String number) {
        int[] parts = new int[2];

        if (isFraction(number)) {
            String[] numbers = number.trim().split("/");
            parts[0] = Integer.parseInt(numbers[0]);
            parts[1] = Integer.parseInt(numbers[1]);
            return parts;

        }else {
//            System.out.println(Integer.parseInt("0.01"));
//            System.out.println(number);

            // can not parse decimal
            parts[0] = Integer.parseInt(number);
            parts[1] = 1;

            return parts;
        }
    }

    public String pivotOperation(String a, String b) {
        //System.out.println("Pivot " + a + " / " + b);
        int[] fractionsA = splitFraction(a);
        int[] fractionsB = splitFraction(b);

        int numerator = fractionsA[0] * fractionsB[1];
        int denominator = fractionsA[1] * fractionsB[0];

        int common = gcd(numerator, denominator);

        if (denominator / common == 1) {
            return "" + numerator / common;
        }else {
            return signMove((numerator / common), (denominator / common));
        }
    }

    public String reduceOperation(String a, String b, String c) {
        //System.out.println("Reduce " + a + " - " + b + " * " + c);
        int[] fractionsA = splitFraction(a);
        int[] fractionsB = splitFraction(b);
        int[] fractionsC = splitFraction(c);

//        System.out.println("a = " + fractionsA[0] + "/" + fractionsA[1]);
//        System.out.println("b = " + fractionsB[0] + "/" + fractionsB[1]);
//        System.out.println("c = " + fractionsC[0] + "/" + fractionsC[1]);

        int numerator = fractionsA[0] * fractionsB[1] * fractionsC[1] - fractionsA[1] * fractionsB[0] * fractionsC[0];
        int denominator = fractionsA[1] * fractionsB[1] * fractionsC[1];

        int common = gcd(numerator, denominator);

        if (denominator / common == 1) {
            return "" + numerator / common;
        }else {
            return signMove((numerator / common), (denominator / common));
        }
    }

    public String signMove(int numerator, int denominator) {
        if (numerator < 0 && denominator < 0) {
            return (-numerator) + "/" + (-denominator);
        }else if (denominator < 0) {
            return "-" + numerator + "/" + (-denominator);
        }else {
            return numerator + "/" + denominator;
        }
    }
}
