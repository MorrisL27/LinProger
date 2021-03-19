public class LinProger {
    double[] rowZ;
    double[] fM;
    double[][] rowS;
    double[] b;
    double[][] Aeq; // use lower case
    double[][] beq;
    double[] lb;
    double[] ub;

    double[][] tableau;

    int numVar;
    int numCons;

    public static void main(String[] args) {
        new LinProger().linProg();
    }

    public int getNumVar() {
        return numVar;
    }

    public int getNumCons() {
        return numCons;
    }

    public void setZ(double[] rowZ) {
        this.rowZ = rowZ;
    }

    public void setS(double[][] rowS) {
        this.rowS = rowS;
    }

    public void set(double[] rowA, int index) {
        //this.rowS[index] = rowA;
    }

    public void setF(double[] rowZ) {
        //this.rowZ = rowZ;
    }

    public void setA(double[] rowA, int index) {
        //this.rowS[index] = rowA;
    }

    public void setFM(double[] fM) {
        this.fM = fM;
    }

    public void setB(double[] b) {
        this.b = b;
    }

    public void setTableau() {
        tableau = new double[numCons][numVar + numCons + 1];
    }

    public LinProger() {
        numVar = 1;
        numCons = 1;
        setDimension(numVar, numCons);

        rowZ = new double[] {2, 2, 0, 0, 0, 0, 0, 0, 0};
        fM = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        rowS = new double[4][9];
        rowS[0] = new double[] {-1, -1, 0, 0, 1, 0, 0, 0, 1};
        rowS[1] = new double[] {0, 1, -1, 1, 0, 1, 0, 0, 2};
        rowS[2] = new double[] {5, 0, 2, -2, 0, 0, 1, 0, 0};
        rowS[3] = new double[] {-1, 0, 1, 1, 0, 0, 0, 1, 0};
    }

    // n: number of variables
    // m: number of constraints
    public void setDimension(int n, int m) {
        numVar = n;
        numCons = m;
        rowZ = new double[numVar + numCons + 1];
        fM = new double[numVar + numCons + 1];
        rowS = new double[numCons][numVar + numCons + 1];
    }

    void showTableau() {
        // check z
        System.out.print("f:    ");
        for (int i = 0; i < rowZ.length; i++) {
            System.out.printf(" %+.2f", rowZ[i]);
        }
        System.out.println();

        System.out.print("fM:   ");
        for (int i = 0; i < fM.length; i++) {
            System.out.printf(" %+.2f", fM[i]);
        }
        System.out.println();

        // check A
        for (int j = 0; j < rowS.length; j++) {
            System.out.print("S[" + j + "]: ");
            for (int i = 0; i < rowS[0].length; i++) {
                System.out.printf(" %+.2f", rowS[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    void linProg() {
        showTableau();

        // for min
        //boolean finish = false;
        while(true) {
            boolean allNegative = true;
            double max = -1;
            int entering = -1;

            for (int i = 0; i < fM.length - 1; i++) {
                if (fM[i] > 0) {
                    allNegative = false;
                    if (max <= fM[i]) {
                        max = fM[i];
                        entering = i;
                    }
                }
            }

            if (allNegative) {
                max = -1;
                entering = -1;


                for (int i = 0; i < rowZ.length - 1; i++) {
                    if (fM[i] == 0.0) {
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
                break;
            }

            int leaving = -1;
            double min = 100000000000000000.0;
            for (int i = 0; i < rowS.length; i++) {
                if (rowS[i][entering] > 0) {
                    double result = rowS[i][rowS[i].length - 1] / rowS[i][entering];
                    System.out.println("result = " + rowS[i][rowS[i].length - 1] + "/" + rowS[i][entering] + " = " + result);
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

            factor = fM[entering];
            for (int i = 0; i < fM.length; i++) {
                fM[i] = fM[i] - factor * rowS[leaving][i];
                if (Math.abs(fM[i]) < 0.0001) {
                    fM[i] = 0;
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

            System.out.println("entering: " + entering + "; leaving: " + leaving);
            System.out.println("max: " + max + "; Min: " + min);
            showTableau();
        }
    }
}
