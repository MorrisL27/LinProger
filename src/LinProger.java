import java.util.ArrayList;
import java.util.Arrays;

public class LinProger {
    private double[] rowZ;
    private double[] displayZ;
    private double[] rowM;
    private double[] displayM;
    private double[][] rowS;

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

    public static void main(String[] args) {
        new LinProger().run();
    }

    public LinProger() {
        // default initialization
//        numVar = 4;
//        numCons = 4;
        //numSol = 1;
//        setDimension(numVar, numCons);
//        setDefaultValue();

        modeMax = false;// min mode initially

        //setTableau();
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

        resetBases();
    }

    private void resetBases() {
        for (int i = 0; i < bases.length; i++) {
            bases[i] = i + numVar;
        }
    }

    /*
    rowZ = new double[] {2, 2, 0, 0, 0, 0, 0, 0, 0};
    rowM = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
    rowS = new double[4][9];
    rowS[0] = new double[] {-1, -1, 0, 0, 1, 0, 0, 0, 1};
    rowS[1] = new double[] {0, 1, -1, 1, 0, 1, 0, 0, 2};
    rowS[2] = new double[] {5, 0, 2, -2, 0, 0, 1, 0, 0};
    rowS[3] = new double[] {-1, 0, 1, 1, 0, 0, 0, 1, 0};
     */

    /*
    private void setDefaultValue() {
        f = new double[] {-2, -2, 0, 0};
        a[0] = new double[] {-1, -1, 0, 0};
        a[1] = new double[] {0, 1, -1, 1};
        a[2] = new double[] {5, 0, 2, -2};
        a[3] = new double[] {-1, 0, 1, 1};
        m = new double[] {0, 0, 0, 0};
        b = new double[] {1, 2, 0, 0};

        //double[] m; // big M


        //rowZ = new double[] {2, 2, 0, 0, 0, 0, 0, 0, 0};
        //rowM = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        //rowS = new double[4][9];
        //rowS[0] = new double[] {-1, -1, 0, 0, 1, 0, 0, 0, 1};
        //owS[1] = new double[] {0, 1, -1, 1, 0, 1, 0, 0, 2};
        //rowS[2] = new double[] {5, 0, 2, -2, 0, 0, 1, 0, 0};
        //rowS[3] = new double[] {-1, 0, 1, 1, 0, 0, 0, 1, 0};
    }
*/

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
        showTableau(displayZ, displayM, rowS);
        //showTableau(rowZ, rowM, rowS);
    }

    private void showTableau(double[] rowZ, double[] rowM, double[][] rowS) {
        System.out.print("Basic ");

        for (int i = 0; i < numVar; i++) {
            System.out.printf(" %-5s", "x" + (i + 1));
        }

        for (int i = 0; i < numCons; i++) {
            System.out.printf(" %-5s", "s" + (i + 1));
        }

        System.out.println(" Solution");


        // check z
        System.out.print("z     ");
        for (int i = 0; i < rowZ.length; i++) {
            System.out.printf(" %+.2f", rowZ[i]);
        }
        System.out.println();

        System.out.print("M     ");
        for (int i = 0; i < rowM.length; i++) {
            System.out.printf(" %+.2f", rowM[i]);
        }
        System.out.println();

        // check A
        for (int j = 0; j < rowS.length; j++) {
            System.out.print(findVariable(bases[j]) + "    ");
            //System.out.print("S[" + (j + 1) + "]  ");
            for (int i = 0; i < rowS[0].length; i++) {
                System.out.printf(" %+.2f", rowS[j][i]);
            }
            System.out.println();
        }
        //System.out.println();
    }

    private void linProg(double[] rowZ, double[] rowM, double[][] rowS) {
        showTableau();

        // for min
        //boolean finish = false;
        double optimum = 0;

        while(true) {
            boolean allNegative = true;
            double max = -1;
            int entering = -1;

            ArrayList<String> str = new ArrayList<>();

            for (int i = 0; i < rowM.length - 1; i++) {
                if (rowM[i] > 0) {
                    allNegative = false;
                    if (max <= rowM[i]) {
                        max = rowM[i];
                        entering = i;
                    }
                }
            }

            if (allNegative) {
                max = -1;
                entering = -1;

                for (int i = 0; i < rowZ.length - 1; i++) {
                    if (rowM[i] == 0.0) {
                        if (rowZ[i] > 0) {
                            allNegative = false;
                            if (max <= rowZ[i]) {
                                max = rowZ[i];
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
            double min = 100000000000000000.0;
            for (int i = 0; i < rowS.length; i++) {
                if (rowS[i][entering] > 0) {
                    double result = rowS[i][rowS[i].length - 1] / rowS[i][entering];
                    str.add("result = " + rowS[i][rowS[i].length - 1] + "/" + rowS[i][entering] + " = " + result);
                    if (result < min) {
                        min = result;
                        leaving = i;
                    }
                }
            }

            if (leaving == -1) {
                System.out.println("Leaving variable does not exist!");
                break;
            }

            double factor = rowS[leaving][entering];

            for (int i = 0; i < rowS[leaving].length; i++) {
                rowS[leaving][i] = rowS[leaving][i] / factor;
            }

            factor = rowM[entering];
            for (int i = 0; i < rowM.length; i++) {
                rowM[i] = rowM[i] - factor * rowS[leaving][i];
                if (Math.abs(rowM[i]) < 0.0001) {
                    rowM[i] = 0;
                }
            }

            factor = rowZ[entering];
            for (int i = 0; i < rowZ.length; i++) {
                rowZ[i] = rowZ[i] - factor * rowS[leaving][i];
            }

            for (int i = 0; i < rowS.length; i++) {
                if (i == leaving) {
                    continue;
                }

                factor = rowS[i][entering];
                for (int j = 0; j < rowS[i].length; j++) {
                    rowS[i][j] = rowS[i][j] - factor * rowS[leaving][j];
                }
            }

            // real index
            //String enterVariable = findVariable(entering) + "\u0332";
            //String leavingVariable = findVariable(bases[leaving]) + "\u0332";


            String enterVariable = findVariable(entering);
            String leavingVariable = findVariable(bases[leaving]);
            str.add("entering: " + enterVariable + "; leaving: " + leavingVariable);
            swapBases(entering + 1, leaving + 1);

            str.add("max: " + max + "; Min: " + min);


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

            showTableau(displayZ, displayM, rowS);

            //showTableau(rowZ, rowM, rowS);
            //System.out.println();
        }

        //System.out.println("Optimal value " + optimum + " has found.\n");
        System.out.println("Optimal value has found.\n");

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
        }else {
            // sync tempF to f
            tempF = Arrays.copyOf(f, f.length);
            tempM = Arrays.copyOf(m, m.length);
            tempSolF = solF;
            tempSolM = solM;

            setTableau();
            displayZ = Arrays.copyOf(rowZ, rowZ.length);
            displayM = Arrays.copyOf(rowM, rowM.length);
        }
    }

    public void modeMax() {
        if (!modeMax) {
            modeMax = true;
            checkMode();
            System.out.println("Nina run in max mode.");
        }else {
            System.out.println("Nina already in max mode.");
        }
    }

    public void modeMin() {
        if (modeMax) {
            modeMax = false;
            checkMode();
            System.out.println("Nina run in min mode.");
        }else {
            System.out.println("Nina already in min mode.");
        }
    }

    private String findVariable(int id) {
        if (id < 0) {
            return "";
        }else if (id < numVar) {
            return "x" + (id + 1);
        }else if (id < numVar + numCons) {
            return "s" + (id + 1 - numVar);
        }else {
            return "";
        }
    }

    private void swapBases(int entering, int leaving) {
        int idEnter = entering - 1;
        int positionLeave = leaving - 1;

        bases[positionLeave] = idEnter;
    }
}
