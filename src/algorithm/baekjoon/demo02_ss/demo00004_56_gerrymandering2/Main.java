package algorithm.baekjoon.demo02_ss.demo00004_56_gerrymandering2;

import java.util.Arrays;
import java.util.Scanner;

/*
https://www.acmicpc.net/problem/17779
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = Integer.parseInt(scanner.nextLine());

        if (N<4 || 20<N) {
            System.out.println("첫째 줄에 재현시의 크기 N / 5 ≤ N ≤ 20");
            return;
        }

        int[][] A = new int[N][N];
        int[][] map = new int[N][N];

        String[] splitLine;
        int population;

        for (int i=0; i<N; i++) {
            splitLine = scanner.nextLine().split(" ");

            if (N != splitLine.length) {
                System.out.println("재현시는 크기가 N×N");
                return;
            } else {
                for (int j=0; j<splitLine.length; j++) {
                    population = Integer.parseInt(splitLine[j]);
                    if (population < 1 || 100 < population) {
                        System.out.println("1 ≤ A[r][c] ≤ 100");
                        return;
                    } else {
                        A[i][j] = population;
                    }
                }
            }
        }

        printMap(A);

/*
구역은 (r, c)
구역을 다섯 개의 선거구로 나눠야 하고,
각 구역은 다섯 선거구 중 하나에 포함되어야 한다.
선거구는 구역을 적어도 하나 포함해야 하고, 한 선거구에 포함되어 있는 구역은 모두 연결되어 있어야 한다.
구역 A에서 인접한 구역을 통해서 구역 B로 갈 수 있을 때, 두 구역은 연결되어 있다고 한다.
중간에 통하는 인접한 구역은 0개 이상이어야 하고, 모두 같은 선거구에 포함된 구역이어야 한다.
 */


/*
선거구를 나누는 방법은 다음과 같다.
기준점 (x, y)와 경계의 길이 d1, d2를 정한다.
    d1, d2 ≥ 1, 1 ≤ x < x+d1+d2 ≤ N
    1 ≤ y-d1 < y < y+d2 ≤ N

다음 칸은 경계선이다.
(x, y), (x+1, y-1), ..., (x+d1, y-d1)
(x, y), (x+1, y+1), ..., (x+d2, y+d2)
(x+d1, y-d1), (x+d1+1, y-d1+1), ... (x+d1+d2, y-d1+d2)
(x+d2, y+d2), (x+d2+1, y+d2-1), ..., (x+d2+d1, y+d2-d1)
경계선과 경계선의 안에 포함되어있는 곳은 5번 선거구이다.
5번 선거구에 포함되지 않은 구역 (r, c)의 선거구 번호는 다음 기준을 따른다.
1번 선거구: 1 ≤ r < x+d1, 1 ≤ c ≤ y
2번 선거구: 1 ≤ r ≤ x+d2, y < c ≤ N
3번 선거구: x+d1 ≤ r ≤ N, 1 ≤ c < y-d1+d2
4번 선거구: x+d2 < r ≤ N, y-d1+d2 ≤ c ≤ N

 */

//        int x = 2;
//        int y = 4;
//        int d1 = 2;
//        int d2 = 2;


        // d1, d2 ≥ 1
        // 1 ≤ x < x+d1+d2 ≤ N      (5 ≤ N ≤ 20)
        // 1 ≤ y-d1 < y < y+d2 ≤ N  (5 ≤ N ≤ 20)
        // 1+d1 ≤ y < y+d1 < y+d1+d2 ≤ N + d1

        int diffMin = Integer.MAX_VALUE;
        int[][] borderedMap;// = setBorder(map.length, x, y, d1, d2);
        int[] populationArr;// = getPopulationArray(A, borderedMap);
        int diff;// = getPopulationDiff(populationArr);

        for (int d1=1; d1<=20; d1++) {
            for (int d2=1; d2<=20; d2++) {
                for (int x=1; x<(x+d1+d2) && (x+d1+d2<=N); x++ ) {
                    for (int y=(1+d1); y < (y+d1) && (y+d1) < (y+d1+d2) && (y+d1+d2)<=(N+d1); y++) {
                        borderedMap = setBorder(map.length, x, y, d1, d2);
                        populationArr = getPopulationArray(A, borderedMap);
                        diff = getPopulationDiff(populationArr);

                        System.out.println("x=" + x + ", y=" + y + ", d1=" + d1 + ", d2=" + d2 + ", diff=" + diff);
                        diffMin = Math.min(diffMin, diff);
                    }
                }
            }
        }

        System.out.println("diffMin=" + diffMin);

    }

    // 다섯 개의 선거구로 나눈
    public static int[][] setBorder(int N, int x, int y, int d1, int d2) {
        int[][] map = new int[N][N];

        final int xForMap = x-1;
        final int yForMap = y-1;

        // (x, y), (x+1, y-1), ..., (x+d1, y-d1)
        for (int i=0; i<=d1; i++) {
            map[xForMap+i][yForMap-i] = 5;
        }

        // (x, y), (x+1, y+1), ..., (x+d2, y+d2)
        for (int i=0; i<=d2; i++) {
            map[xForMap+i][yForMap+i] = 5;
        }

        // (x+d1,   y-d1),
        // (x+d1+1, y-d1+1), ...
        // (x+d1+d2, y-d1+d2)
        for (int i=0; i<=d2; i++) {
            map[xForMap+d1+i][yForMap-d1+i] = 5;
        }

        // (x+d2, y+d2),
        // (x+d2+1, y+d2-1), ...,
        // (x+d2+d1, y+d2-d1)
        for (int i=0; i<=d1; i++) {
            map[xForMap+d2+i][yForMap+d2-i] = 5;
        }

        // 경계선과 경계선의 안에 포함되어있는 곳은 5번 선거구이다.
        // 5번 선거구 맵핑
        for (int i=0; i<map.length; i++) {
            int startCol = -1;
            int endCol = -1;

            for (int j=0; j<map[0].length; j++) {
                if (startCol == -1 && map[i][j] == 5) {
                    startCol = j;
                }
                if (map[i][j] == 5) {
                    endCol = j;
                }
            }

            if (startCol!=-1 && endCol!=-1) {
                for (int k=startCol; k<endCol; k++) {
                    map[i][k] = 5;
                }
            }
        }

        // 1번 선거구: 1 ≤ r < x+d1, 1 ≤ c ≤ y
        for (int r=0; r<(xForMap+d1); r++) {
            for (int c=0; c<=yForMap; c++) {
                if (5!=map[r][c]) map[r][c] = 1;
            }
        }
//        printMap(map);

        // 2번 선거구: 1 ≤ r ≤ x+d2, y < c ≤ N
        for (int r=0; r<=(xForMap+d2); r++) {
            for (int c=(y); c<map[0].length; c++) {
                if (5!=map[r][c]) map[r][c] = 2;
            }
        }

        // 3번 선거구: x+d1 ≤ r ≤ N, 1 ≤ c < y-d1+d2
        for (int r=(xForMap+d1); r<map[0].length; r++) {
            for (int c=0; c<(yForMap-d1+d2); c++) {
                if (5!=map[r][c]) map[r][c] = 3;
            }
        }

        // 4번 선거구: x+d2 < r ≤ N, y-d1+d2 ≤ c ≤ N
        for (int r=(xForMap+d2+1); r<map[0].length; r++) {
            for (int c=(yForMap-d1+d2); c<map[0].length; c++) {
                if (5!=map[r][c]) map[r][c] = 4;
            }
        }

        return map;
    }

    public static int[] getPopulationArray(int[][] populationMap, int[][] borderedMap) {
        int[] populationArr = new int[5]; // 5개의 선거구

        for (int r=0; r<borderedMap.length; r++) {
            for (int c=0; c<borderedMap[0].length; c++) {
                     if (1==borderedMap[r][c]) populationArr[0] += populationMap[r][c]; // 0==1번 선거구 합
                else if (2==borderedMap[r][c]) populationArr[1] += populationMap[r][c]; // 1==2번선거구 합
                else if (3==borderedMap[r][c]) populationArr[2] += populationMap[r][c];
                else if (4==borderedMap[r][c]) populationArr[3] += populationMap[r][c];
                else if (5==borderedMap[r][c]) populationArr[4] += populationMap[r][c];
            }
        }

        return populationArr;
    }

    public static int getPopulationDiff(int[] populationArr) {
        return Arrays.stream(populationArr).max().getAsInt() - Arrays.stream(populationArr).min().getAsInt();
    }

    public static void printMap(int[][] A) {
        for (int[] row : A) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
    }



}
