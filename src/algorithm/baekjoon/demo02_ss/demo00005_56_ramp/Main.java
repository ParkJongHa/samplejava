package algorithm.baekjoon.demo02_ss.demo00005_56_ramp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
https://www.acmicpc.net/problem/14890
경사로
 */
public class Main {


    public enum Direction {
        Horizontal,
        Vertical;
    }

    public static class Height {
        final int R;
        final int C;
        final int H;
        final Direction D;
        public Height(int row, int col, int height, Direction direction) {
            this.R = row;
            this.C = col;
            this.H = height;
            this.D = direction;
        }

        String ramp = " ";

        @Override
        public String toString() {
            return H + "(" + R + "," + C + ")" + D.name().charAt(0) + ramp;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String rawNL = scanner.nextLine();
        String[] splitRawNL = rawNL.split(" ");

        int N = Integer.parseInt(splitRawNL[0]);

        if (N < 2 || 100 < N) {
            System.out.println("N (2 ≤ N ≤ 100)");
            return;
        }

        int L = Integer.parseInt(splitRawNL[1]);

        if (L < 1 || N < L) {
            System.out.println("L (1 ≤ L ≤ N)");
            return;
        }

        int[][] map = new int[N][N];

        String rawLine;
        String[] splitRawLine;
        int tempNo;
        for (int i=0; i<N; i++) {
            rawLine = scanner.nextLine();
            splitRawLine = rawLine.split(" ");

            if (N != splitRawLine.length) {
                System.out.println("크기가 N×N인 지도 여야 한다.");
                return;
            }

            for (int j=0; j<N; j++) {
                tempNo = Integer.parseInt(splitRawLine[j]);
                if (tempNo < 1 || 10 < tempNo) {
                    System.out.println("각 칸의 높이는 10보다 작거나 같은 자연수이다.");
                    return;
                } else {
                    map[i][j] = tempNo;
                }
            }
        }

        printMap(map);

        // 지도가 주어졌을 때, 지나갈 수 있는 길의 개수를 구하는 프로그램을 작성하시오.

        List<List<Height>> wayableList = new ArrayList<>();

        for (int i=0; i<N; i++) {
            List<Height> rowWayList = new ArrayList<>();
            List<Height> colWayList = new ArrayList<>();
            for (int j=0; j<N; j++) {
                rowWayList.add(new Height(i,j,map[i][j], Direction.Horizontal));
                colWayList.add(new Height(j,i,map[j][i], Direction.Vertical));
            }
            wayableList.add(rowWayList);
            wayableList.add(colWayList);
        }

        List<List<Height>> pathList = new ArrayList<>() {{
            addAll(wayableList.stream().filter(heightList -> heightList.get(0).D == Direction.Horizontal).toList());
            addAll(wayableList.stream().filter(heightList -> heightList.get(0).D == Direction.Vertical).toList());
        }};

        // 길 개수
        pathList.forEach(heightList -> {
            System.out.println(heightList.stream().map(Height::toString).collect(Collectors.joining(",")));
        });

/*
길을 지나갈 수 있으려면 길에 속한 모든 칸의 높이가 모두 같아야 한다.
또는, 경사로를 놓아서 지나갈 수 있는 길을 만들 수 있다.
    경사로는 높이가 항상 1이며, 길이는 L (1≤L≤N) 이다.
    또, 개수는 매우 많아 부족할 일이 없다. 경사로는 낮은 칸과 높은 칸을 연결하며,
    아래와 같은 조건을 만족해야한다.

경사로는 낮은 칸에 놓으며, L개의 연속된 칸에 경사로의 바닥이 모두 접해야 한다.
낮은 칸과 높은 칸의 높이 차이는 1이어야 한다.
경사로를 놓을 낮은 칸의 높이는 모두 같아야 하고, L개의 칸이 연속되어 있어야 한다.
아래와 같은 경우에는 경사로를 놓을 수 없다.

경사로를 놓은 곳에 또 경사로를 놓는 경우
낮은 칸과 높은 칸의 높이 차이가 1이 아닌 경우
낮은 지점의 칸의 높이가 모두 같지 않거나, L개가 연속되지 않은 경우
경사로를 놓다가 범위를 벗어나는 경우
 */

        int wayCount = 0;

        for (List<Height> heightList : pathList) {
            if (isAllSameHeight(heightList, null) && !hasRamp(heightList)) {
                wayCount++; // 모두 같은 높이 && // 경사로 없음
                System.out.println("길 - " + heightList.stream().map(Height::toString).collect(Collectors.joining(" - ")));
            } else if (setRampAndGetResult(heightList, L)) {
                wayCount++;// 경사로를 놓을수 있음
                System.out.println("길 - " + heightList.stream().map(Height::toString).collect(Collectors.joining(" - ")));
            }
        }

        System.out.println("wayCount : " + wayCount);
    }

    public static boolean isAllSameHeight(List<Height> heightList, Integer targetHeight) {
        Map<Integer, List<Height>> heightMap
                = heightList
                .stream()
                .collect(Collectors.groupingByConcurrent(o -> o.H));

        if (targetHeight == null) {
            return 1 == heightMap.keySet().size(); // 모두 같은 높이
        }

        return heightList.size() == heightList.stream()
                .filter(height -> height.H == targetHeight.intValue())
                .count();
    }

    public static boolean hasRamp(List<Height> heightList) {
        int rampCount = (int) heightList
                .stream()
                .filter(height -> !height.ramp.trim().equals(""))// 경사로 있음
                .count();

        return 0 < rampCount;
    }

    // 길이 되려면 경사로를 설치 해야만한다 높이가 다른 리스트가 들어온다
    public static boolean setRampAndGetResult(List<Height> heightList, final int L) {
        if ("1(3,0)H ,1(3,1)H ,1(3,2)H ,2(3,3)H ,2(3,4)H ,2(3,5)H ".equals(heightList.stream().map(Height::toString).collect(Collectors.joining(",")))) {
            System.out.println(heightList);
        }

        final List<Height> rampCandidateList = new ArrayList<>(); // 경사로가 설치되어야 할 리스트
        final int size = heightList.size();
        int nextNo;

        for (int i=0; i<size; i++) {
            nextNo = i + 1;
            if (nextNo < size) {
                if (1 < Math.abs(heightList.get(i).H - heightList.get(nextNo).H)) {
                    return false; // 낮은 칸과 높은 칸의 높이 차이는 1이어야 한다.
                }
            }
        }


        Height tempRampCandidate;

        // 2 2 2 3 2 3
        for (int i=0; i<size; i++) {
            final Height height = heightList.get(i);

            tempRampCandidate = rampCandidateList
                    .stream()
                    .filter(ramp -> ramp.R==height.R && ramp.C==height.C)
                    .findFirst()
                    .orElse(null);

            if (!height.ramp.trim().equals("") || tempRampCandidate!=null) {
                // 이미 경사로 설치 되있거나 // 설치할지 판단할 후보가 있음
            } else {
                if ( (i+1)<size
                 && (i+1+L)<size
                 && (height.H-1)==heightList.get(i+1).H
                 && isAllSameHeight(heightList.subList(i+1, i+1+L), null)
                ) {
                    heightList.subList(i+1, i+1+L).forEach(height1 -> rampCandidateList.add(height1));
                } else if (
                 (i+L)<size
                 && (height.H+1)==heightList.get(i+L).H
                 && isAllSameHeight(heightList.subList(i, i+L), null)
                ) {
                    heightList.subList(i, i + L).forEach(height1 -> rampCandidateList.add(height1));
                } else if ((i+1)<size && height.H==heightList.get(i+1).H) {
                    // 같으므로 그냥 넘어간다.
                } else if ((i+1)==size) { // 마지막
                    // 그냥 넘어간다.
                } else {
//                    System.out.println(
//                            (i-1 < 0 ? "null" : heightList.get(i-1) ) + ", "
//                            + heightList.get(i) + ", "
//                            + (i+1 < size ? heightList.get(i+1) : "null" )
//                    );
                    return false;
                }
            }
        }

        for (Height aRamp : rampCandidateList) {
            aRamp.ramp = "R";
        }

        return true; // true 라면 경사로를 설치할 수 있다.
    }

    public static boolean[][] copyRampMap(boolean[][] rampMap) {
        boolean[][] newRampMap = new boolean[rampMap.length][rampMap.length];

        for (int i=0; i< rampMap.length; i++) {
            for (int j= 0; j< rampMap.length; j++) {
                newRampMap[i][j] = rampMap[i][j];
            }
        }

        return newRampMap;
    }

    public static void printMap(int[][] map) {
        for (int[] row : map) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }

    public static void printMap(boolean[][] map) {
        for (boolean[] row : map) {
            for (boolean col : row) {
                System.out.print(col ? "T" : "F");
            }
            System.out.println();
        }
    }

}
