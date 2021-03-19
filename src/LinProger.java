public class LinProger {

//    double[] f = {-2, 3, -6, 0, 0, 0, 0, 0, 0};
//    double[] fM = {5, -3, -4, -1, -1, 0, 0, 0, 13};
    //double[][] A = new double[3][9];


//    double[] f = {-4, -1, 0, 0, 0, 0, 0};
//    double[] fM = {7, 4, -1, 0, 0, 0, 9};
    //double[][] A = new double[3][7];

    //A3.3
//    double[] f = {3, 4, 0, 0, 0, 0};
//    double[] fM = {0, 0, 0, 0, 0, 0};
//    double[][] A = new double[3][6];

    //Tutorial 8.1
//    double[] f = {3, 2, 0, 0, 0, 0};
//    double[] fM = {0, 0, 0, 0, 0, 0};
//    double[][] A = new double[3][6];


    double[] f;
    double[] fM;
    double[][] A; // use lower case
    double[] b;
    double[][] Aeq; // use lower case
    double[][] beq;
    double[] lb;
    double[] ub;

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

    public void setF(double[] f) {
        this.f = f;
    }

    public void setA(double[] rowA, int index) {
        this.A[index] = rowA;
    }

    public void setFM(double[] fM) {
        this.fM = fM;
    }

    public void setB(double[] b) {
        this.b = b;
    }


    public LinProger() {
        numVar = 1;
        numCons = 1;
        setDimension(numVar, numCons);

        /*
        //A3.1
        f = new double[] {2, 2, 0, 0, 0, 0, 0, 0, 0};
        fM = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        A = new double[4][9];

        A[0] = new double[] {-1, -1, 0, 0, 1, 0, 0, 0, 1};
        A[1] = new double[] {0, 1, -1, 1, 0, 1, 0, 0, 2};
        A[2] = new double[] {5, 0, 2, -2, 0, 0, 1, 0, 0};
        A[3] = new double[] {-1, 0, 1, 1, 0, 0, 0, 1, 0};
        */
    }

    // n: number of variables
    // m: number of constraints
    public void setDimension(int n, int m) {
        numVar = n;
        numCons = m;
        f = new double[numVar + numCons + 1];
        fM = new double[numVar + numCons + 1];
        A = new double[numCons][numVar + numCons + 1];
    }

    void showTableau() {
        // check z
        System.out.print("f:    ");
        for (int i = 0; i < f.length; i++) {
            System.out.printf(" %+.2f",f[i]);
        }
        System.out.println();

        System.out.print("fM:   ");
        for (int i = 0; i < fM.length; i++) {
            System.out.printf(" %+.2f", fM[i]);
        }
        System.out.println();

        // check A
        for (int j = 0; j < A.length; j++) {
            System.out.print("A[" + j + "]: ");
            for (int i = 0; i < A[0].length; i++) {
                System.out.printf(" %+.2f", A[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

//        A[0] = new double[] {3, -4, -6, -1, 0, 1, 0, 0, 2};
//        A[1] = new double[] {2, 1, 2, 0 , -1, 0, 1, 0, 11};
//        A[2] = new double[] {1, 3, -2, 0, 0, 0, 0, 1, 5};


//
//        A[0] = new double[] {3, 1, 0, 1, 0, 0, 3};
//        A[1] = new double[] {4, 3, -1, 0 , 1, 0, 6};
//        A[2] = new double[] {1, 2, 0, 0, 0, 1, 4};



    //A3.3
//        A[0] = new double[] {1, 2, 1, 0, 0, 10};
//        A[1] = new double[] {1, 1, 0, 1, 0, 8};
//        A[2] = new double[] {3, 5, 0, 0, 1, 26};

    // Tutorial 8.1
//        A[0] = new double[] {1, 1, 1, 0, 0, 3};
//        A[1] = new double[] {2, 1, 0, 1, 0, 4};
//        A[2] = new double[] {4, 3, 0, 0, 1, 10};

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


                for (int i = 0; i < f.length - 1; i++) {
                    if (fM[i] == 0.0) {
                        if (f[i] > 0) {
                            allNegative = false;
                            if (max <= f[i]) {
                                max = f[i];
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
            for (int i = 0; i < A.length; i++) {
                if (A[i][entering] > 0) {
                    double result = A[i][A[i].length - 1] / A[i][entering];
                    System.out.println("result = " + A[i][A[i].length - 1] + "/" + A[i][entering] + " = " + result);
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


            double factor = A[leaving][entering];

            for (int i = 0; i < A[leaving].length; i++) {
                A[leaving][i] = A[leaving][i] / factor;
            }

            factor = fM[entering];
            for (int i = 0; i < fM.length; i++) {
                fM[i] = fM[i] - factor * A[leaving][i];
                if (Math.abs(fM[i]) < 0.0001) {
                    fM[i] = 0;
                }
            }

            factor = f[entering];
            for (int i = 0; i < f.length; i++) {
                f[i] = f[i] - factor * A[leaving][i];
            }



            for (int i = 0; i < A.length; i++) {
                if (i == leaving) {
                    continue;
                }

                factor = A[i][entering];
                for (int j = 0; j < A[i].length; j++) {
                    A[i][j] = A[i][j] - factor * A[leaving][j];
                }
            }

            System.out.println("entering: " + entering + "; leaving: " + leaving);
            System.out.println("max: " + max + "; Min: " + min);
            showTableau();
        }
    }
}
