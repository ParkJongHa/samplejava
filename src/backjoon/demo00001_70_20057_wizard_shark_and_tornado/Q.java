package backjoon.demo00001_70_20057_wizard_shark_and_tornado;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * https://www.acmicpc.net/problem/20057
 * WizardSharkAndTornado
 */
public class Q {

    private Q q;

    public int outedSandAmount = 0;

    int N;
    int[][] A = null;

    ArrayList<Point> pointList = new ArrayList<>();

    int[][] remainSandMatrix;

    /**
     * 인풋 잡는데 30 분 소요
     * @param args
     */
    public static void main(String[] args) {
        Q q = new Q();
        q.setInput();
        if (null == q.A) return;

        q.setTornadoPointPathList();

        q.pointList.forEach(System.out::println);

        int[][] tempMatrix = new int[q.N][q.N];
        int[][] remainSandMatrix;

        q.copyArray(q.A, tempMatrix);

        Point beforePoint;
        Point nowPoint;
        Double[][] direction;

        for (int i=1; i<q.pointList.size(); i++) {
            beforePoint = q.pointList.get(i-1);
            nowPoint = q.pointList.get(i);

            System.out.println("============================================================================================================");
            System.out.println(i + " nowP:" +nowPoint + ", beforeP:" + beforePoint + "\ndirection");
            direction = q.getDirection( beforePoint, nowPoint ); // east, west, north, south
            showMatrix(direction);
            System.out.println("============================================================================================================");

            remainSandMatrix = q.calcRemainSandMatrix( tempMatrix[nowPoint.x][nowPoint.y], direction );
            tempMatrix[nowPoint.x][nowPoint.y] = 0;

            System.out.println("tempMatrix");
            showMatrix(tempMatrix);

            System.out.println("remainSandMatrix");
            showMatrix(remainSandMatrix);

            int[][] movedMatrix = q.calcMoveRemainSandMatrix(q.N, nowPoint, remainSandMatrix);

            System.out.println("movedMatrix");
            showMatrix(movedMatrix);

            q.plusArray(tempMatrix, movedMatrix, tempMatrix);
            System.out.println("plusMatrix");
            showMatrix(tempMatrix);
            System.out.println("============================================================================================================");
        }

        System.out.println("outedSandAmount:" + q.outedSandAmount);
    }

    void copyArray(int[][] source, int[][] target) {
        for (int i=0; i<source.length; i++) {
            for (int j=0; j<source.length; j++) {
                target[i][j] = source[i][j];
            }
        }
    }

    void plusArray(int[][] source1, int[][] source2, int[][] calcingArr) {
        for (int i=0; i<source1.length; i++) {
            for (int j=0; j<source1.length; j++) {
                calcingArr[i][j] = source1[i][j] + source2[i][j];
            }
        }
    }

    int[][] calcMoveRemainSandMatrix(
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
                    outedSandAmount += remainSandMatrix[i][j];
                } else if (newY<0 || matrixSize<=newY) {
                    outedSandAmount += remainSandMatrix[i][j];
                } else {
                    baseMatrix[newX][newY] = remainSandMatrix[i][j]; // 1+new Random().nextInt(8); //
                }
            }
        }

        System.out.println("outedSandAmount : " + outedSandAmount);
        return baseMatrix;
    }

    final Double[][] West = new Double[][] {
            {null, null, 0.02, null, null},
            {null, 0.1, 0.07, 0.01, null},
            {0.05, 0.75, null, null, null},
            {null, 0.1, 0.07, 0.01, null},
            {null, null, 0.02, null, null},
    };
    final Double[][] East = new Double[][] {
            {null, null, 0.02, null, null},
            {null, 0.01, 0.07, 0.1, null},
            {null, null, null, 0.75, 0.05},
            {null, 0.01, 0.07, 0.1, null},
            {null, null, 0.02, null, null},
    };
    final Double[][] North = new Double[][] {
            {null, null, 0.05, null, null},
            {null, 0.1,  0.75, 0.1,  null},
            {0.02, 0.07, null, 0.07, 0.02},
            {null, 0.01, null, 0.01, null},
            {null, null, null, null, null},
    };
    final Double[][] South = new Double[][] {
            {null, null, null, null, null},
            {null, 0.01, null, 0.01, null},
            {0.02, 0.07, null, 0.07, 0.02},
            {null,  0.1, 0.75,  0.1, null},
            {null, null, 0.05, null, null},
    };

    public void setInput() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        N = Integer.parseInt(line);

        if (N < 3 || 499 < N) {
            System.out.println("3 <= n <= 499");
            return;
        }

        if (N % 2 == 0) {
            System.out.println("n must be odd number");
            return;
        }

        A= new int[N][N];

        String inputLine;
        String[] splittedInputLine;

        for (int i=0; i<N; i++) {
            inputLine = scanner.nextLine();
            splittedInputLine = inputLine.split(" ");

            for (int j=0; j<splittedInputLine.length; j++) {
                int Arc = Integer.parseInt(splittedInputLine[j].trim());
                if (Arc < 0 || 1000 < Arc) {
                    System.out.println("0 <= A[r][c] <= 1000");
                    return;
                }
                A[i][j] = Arc;
            }
        }

        if (0 != A[(N/2)][(N/2)]) {
            System.out.println("가운데 칸의 모래양은 0 유효성 체크");
            return;
        }

        System.out.println("input matrix (N=" + N + ")");
        showMatrix(A);
        System.out.println("==========================================");
    }

    public int[][] calcRemainSandMatrix(int sandAmount, Double[][] splitRatioMatrix) {
        remainSandMatrix = new int[splitRatioMatrix.length][splitRatioMatrix.length];
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
     */
    public void setTornadoPointPathList() {
        int moveCount = N * N - 1;

        int posX = N/2;
        int posY = N/2;
        pointList.add(new Point(posX, posY));

        int accumMoveCount = 0;
        boolean isXDirection = false;
        boolean isXPlus = true;
        boolean isYPlus = false;
        int range = 1;
        int rangeForEndCount = 0;

        while (true) {
            for (int i=0; i<range; i++) {
                if (moveCount == accumMoveCount) {
                    return;
                }

                if (isXDirection) {
                    if (isXPlus) posX = posX + 1;
                    else posX = posX - 1;
                } else {
                    if (isYPlus) posY = posY + 1;
                    else posY = posY - 1;
                }

                pointList.add(new Point(posX, posY));
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

    public Double[][] getDirection(Point beforePoint, Point afterPoint) {
        if (beforePoint.x == afterPoint.x && beforePoint.y > afterPoint.y) return West;
        else if (beforePoint.x == afterPoint.x && beforePoint.y < afterPoint.y) return East;
        else if (beforePoint.x > afterPoint.x && beforePoint.y == afterPoint.y) return North;
        else return South;
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