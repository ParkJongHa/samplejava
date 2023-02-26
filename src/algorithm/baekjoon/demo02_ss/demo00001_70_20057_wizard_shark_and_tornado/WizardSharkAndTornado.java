package algorithm.baekjoon.demo02_ss.demo00001_70_20057_wizard_shark_and_tornado;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * https://www.acmicpc.net/problem/20057
 */
public class WizardSharkAndTornado {

    public static class Input {
        final int N;
        final int[][] A;
        public Input(int N, int[][] A) {
            this.N = N;
            this.A = A;
        }
    }

    public static int outedSandAmount = 0;

    /**
     * 인풋 잡는데 30 분 소요
     * @param args
     */
    public static void main(String[] args) {
        Input input = getInput();

        if (null == input) return;

        ArrayList<Point> pointList = getTornadoPointPathList(input.N);

        pointList.forEach(System.out::println);

        int[][] tempMatrix = new int[input.N][input.N];
        int[][] remainSandMatrix;

        copyArray(input.A, tempMatrix);

        showMatrix(tempMatrix);

        Point beforePoint;
        Point nowPoint;
        Double[][] direction;

        for (int i=1; i<pointList.size(); i++) {
            beforePoint = pointList.get(i-1);
            nowPoint = pointList.get(i);

            System.out.println(i + " nowP:" +nowPoint + ", beforeP:" + beforePoint + " " + getDirectionName( beforePoint, nowPoint ));
            direction =  getDirection( beforePoint, nowPoint );
            showMatrix(direction);

            remainSandMatrix = calcRemainSandMatrix( tempMatrix[nowPoint.x][nowPoint.y], direction );
            tempMatrix[nowPoint.x][nowPoint.y] = 0;

            System.out.println("tempMatrix");
            showMatrix(tempMatrix);

            System.out.println("remainSandMatrix");
            showMatrix(remainSandMatrix);

            int[][] movedMatrix = calcMoveRemainSandMatrix(input.N, nowPoint, remainSandMatrix);

            System.out.println("movedMatrix");
            showMatrix(movedMatrix);

            plusArray(tempMatrix, movedMatrix, tempMatrix);
            System.out.println("plusMatrix");
            showMatrix(tempMatrix);
            System.out.println("============================================================================================================");
        }

        System.out.println("outedSandAmount:" + outedSandAmount);
    }

    static void copyArray(int[][] source, int[][] target) {
        for (int i=0; i<source.length; i++) {
            for (int j=0; j<source.length; j++) {
                target[i][j] = source[i][j];
            }
        }
    }

    static void plusArray(int[][] source1, int[][] source2, int[][] calcingArr) {
        for (int i=0; i<source1.length; i++) {
            for (int j=0; j<source1.length; j++) {
                calcingArr[i][j] = source1[i][j] + source2[i][j];
            }
        }
    }

    static int[][] calcMoveRemainSandMatrix(
            int matrixSize,
            Point remainSandMatrixPoint,
            int[][] remainSandMatrix
    ) {
        int newX;
        int newY;
        final int remainSandCenter = remainSandMatrix.length/2;

        int[][] baseMatrix = new int[matrixSize][matrixSize];

        for (int i=0; i<remainSandMatrix.length; i++) {
            for (int j=0; j<remainSandMatrix.length; j++) {
                newX = i + remainSandMatrixPoint.x - remainSandCenter;//
                newY = j + remainSandMatrixPoint.y - remainSandCenter;//

                if (newX<0 || matrixSize<=newX) {
                    outedSandAmount = outedSandAmount + remainSandMatrix[i][j];
                } else if (newY<0 || matrixSize<=newY) {
                    outedSandAmount = outedSandAmount + remainSandMatrix[i][j];
                } else {
                    baseMatrix[newX][newY] = remainSandMatrix[i][j]; // 1+new Random().nextInt(8); //
                }
            }
        }

        System.out.println("outedSandAmount : " + outedSandAmount);
        return baseMatrix;
    }

    static final Double[][] W = new Double[][] {
            {null, null, 0.02, null, null},
            {null, 0.1, 0.07, 0.01, null},
            {0.05, 0.75, null, null, null},
            {null, 0.1, 0.07, 0.01, null},
            {null, null, 0.02, null, null},
    };
    static final Double[][] E = new Double[][] {
            {null, null, 0.02, null, null},
            {null, 0.01, 0.07, 0.1, null},
            {null, null, null, 0.75, 0.05},
            {null, 0.01, 0.07, 0.1, null},
            {null, null, 0.02, null, null},
    };
    static final Double[][] N = new Double[][] {
            {null, null, 0.05, null, null},
            {null, 0.1,  0.75, 0.1,  null},
            {0.02, 0.07, null, 0.07, 0.02},
            {null, 0.01, null, 0.01, null},
            {null, null, null, null, null},
    };
    static final Double[][] S = new Double[][] {
            {null, null, null, null, null},
            {null, 0.01, null, 0.01, null},
            {0.02, 0.07, null, 0.07, 0.02},
            {null,  0.1, 0.75,  0.1, null},
            {null, null, 0.05, null, null},
    };

    public static Input getInput() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        int n = Integer.parseInt(line);

        if (n < 3 || 499 < n) {
            System.out.println("3 <= n <= 499");
            return null;
        }

        if (n % 2 == 0) {
            System.out.println("n must be odd number");
            return null;
        }

        int A[][] = new int[n][n];

        String inputLine;
        String[] splittedInputLine;

        for (int i=0; i<n; i++) {
            inputLine = scanner.nextLine();
            splittedInputLine = inputLine.split(" ");

            for (int j=0; j<splittedInputLine.length; j++) {
                int Arc = Integer.parseInt(splittedInputLine[j].trim());
                if (Arc < 0 || 1000 < Arc) {
                    System.out.println("0 <= A[r][c] <= 1000");
                    return null;
                }
                A[i][j] = Arc;
            }
        }

        if (0 != A[(n/2)][(n/2)]) {
            System.out.println("가운데 칸의 모래양은 0 유효성 체크");
            return null;
        }

        System.out.println("==========================================");
        showMatrix(A);

        return new Input(n, A);
    }

    public static int[][] calcRemainSandMatrix(int sandAmount, Double[][] splitRatioMatrix) {
        int[][] remainSandMatrix = new int[splitRatioMatrix.length][splitRatioMatrix.length];
        Point remainSandPoint = null;
        int remainSandSum = 0;
        int sand;

        for (int i=0; i<splitRatioMatrix.length; i++) {
            for (int j=0; j<splitRatioMatrix[i].length; j++) {
                if (null == splitRatioMatrix[i][j]) {
                    sand = 0;
                } else if (0.75 == splitRatioMatrix[i][j]) {
                    sand = 0;
                    remainSandPoint = new Point(i, j);
                } else {
                    sand = (int)(sandAmount * splitRatioMatrix[i][j]);
                }
                remainSandSum = remainSandSum + sand;
                remainSandMatrix[i][j] = sand;
            }
        }

        remainSandMatrix[remainSandPoint.x][remainSandPoint.y] = sandAmount - remainSandSum;
        return remainSandMatrix;
    }

    /**
     * 이동 횟수의 규칙 찾기 (2시간 소요)
     * @param n nxn 메트릭스의 가운데에서 출발 하여 반시계 방향으로 이동시
     * @return 포지션
     */
    public static ArrayList<Point> getTornadoPointPathList(int n) {
        int moveCount = n * n - 1;

        ArrayList<Point> tornadoPathPointList = new ArrayList<>();

        int posX = n/2;
        int posY = n/2;
        tornadoPathPointList.add(new Point(posX, posY));

        int accumMoveCount = 0;
        boolean isXDirection = false;
        boolean isXPlus = true;
        boolean isYPlus = false;
        int range = 1;
        int rangeForEndCount = 0;

        while (true) {
            for (int i=0; i<range; i++) {
                if (moveCount == accumMoveCount) {
                    return tornadoPathPointList;
                }

                if (isXDirection) {
                    if (isXPlus) posX = posX + 1;
                    else posX = posX - 1;
                } else {
                    if (isYPlus) posY = posY + 1;
                    else posY = posY - 1;
                }

                tornadoPathPointList.add(new Point(posX, posY));
                accumMoveCount++;
            }
            rangeForEndCount++;

            if (rangeForEndCount%2==1) {
                isXDirection = true;
                isYPlus = !isYPlus;
            } else {
                isXDirection = false;
                isXPlus = !isXPlus;
                range = range + 1;
            }
        }
    }

    public static Double[][] getDirection(Point beforePoint, Point afterPoint) {
        if (beforePoint.x == afterPoint.x && beforePoint.y > afterPoint.y) return W;
        else if (beforePoint.x == afterPoint.x && beforePoint.y < afterPoint.y) return E;
        else if (beforePoint.x > afterPoint.x && beforePoint.y == afterPoint.y) return N;
        else return S;
    }

    public static String getDirectionName(Point beforePoint, Point afterPoint) {
        if (beforePoint.x == afterPoint.x && beforePoint.y > afterPoint.y) return "W";
        else if (beforePoint.x == afterPoint.x && beforePoint.y < afterPoint.y) return "E";
        else if (beforePoint.x > afterPoint.x && beforePoint.y == afterPoint.y) return "N";
        else return "S";
    }










    public static void showMatrix(int A[][]) {
        for (int[] ints : A) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

    public static void showMatrix(Double A[][]) {
        for (Double[] ints : A) {
            for (Double anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

}