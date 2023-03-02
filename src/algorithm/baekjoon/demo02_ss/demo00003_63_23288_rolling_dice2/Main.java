package algorithm.baekjoon.demo02_ss.demo00003_63_23288_rolling_dice2;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
https://www.acmicpc.net/problem/23288
 */
public class Main {

    public enum Direction {
        North,
        East,
        South,
        West;
    }
    public static class Dice {


        int currentRow = 1;
        int currentCol = 1;

        public int getRowOnMap() {
            return currentRow - 1;
        }

        public int getColOnMap() {
            return currentCol - 1;
        }

        public Direction direction = Direction.East; // 가장 처음에 주사위의 이동 방향은 동쪽

        public Direction getOpositeDirection() {
            if (Direction.East == direction) return Direction.West;
            else if (Direction.West == direction) return Direction.East;
            else if (Direction.South == direction) return Direction.North;
            else return Direction.South;
        }

        public void changeDirection(int B_mapNo) {
            int A_bottomNo = getBottomNo();
            if (A_bottomNo == B_mapNo) return; // A = B인 경우 이동 방향에 변화는 없다

            if (A_bottomNo > B_mapNo) { // A > B인 경우 이동 방향을
                // 90도 시계 방향으로 회전시킨다.
                if (Direction.East == direction) direction= Direction.South;
                else if (Direction.South == direction) direction= Direction.West;
                else if (Direction.West == direction) direction= Direction.North;
                else direction= Direction.East;
            } else { // A < B인 경우 이동 방향을
                // 90도 반시계 방향으로 회전시킨다.
                if (Direction.East == direction) direction= Direction.North;
                else if (Direction.South == direction) direction= Direction.East;
                else if (Direction.West == direction) direction= Direction.South;
                else direction= Direction.West;
            }
        }
        int[][] planarFigure = new int[][] {
            {0,2,0},
            {4,1,3},
            {0,5,0},
            {0,6,0}
        };
        public int getNextRow() {
            if (Direction.North == direction) return currentRow-1;
            else if (Direction.South == direction) return currentRow+1;
            else return currentRow;
        }

        public int getNextCol() {
            if (Direction.East == direction) return currentCol+1;
            else if (Direction.West == direction) return currentCol-1;
            else return currentCol;
        }

        public void go(Direction direction, int[][] map) {
            if (! canDiceMoveable(map, this)) {
                direction = getOpositeDirection();
                this.direction = direction;
            }

            if (Direction.East == direction) {
                goEast();
                currentCol++;
            }
            else if (Direction.West == direction) {
                goWest();
                currentCol--;
            }
            else if (Direction.South == direction) {
                goSouth();
                currentRow++;
            }
            else { // north
                goNorth();
                currentRow--;
            }
            changeDirection(getMapNo(map, this));
        }

        public void goEast() {
            int southNo = planarFigure[3][1];
            int eastNo = planarFigure[1][2];
            planarFigure[1][2] = planarFigure[1][1];
            planarFigure[1][1] = planarFigure[1][0];
            planarFigure[3][1] = eastNo;
            planarFigure[1][0] = southNo;
        }

        public void goWest() {
            int southNo = planarFigure[3][1];
            int westNo = planarFigure[1][0];
            planarFigure[1][0] = planarFigure[1][1];
            planarFigure[1][1] = planarFigure[1][2];
            planarFigure[3][1] = westNo;
            planarFigure[1][2] = southNo;
        }

        public void goNorth() {
            int northNo = planarFigure[0][1];
            planarFigure[0][1] = planarFigure[1][1];
            planarFigure[1][1] = planarFigure[2][1];
            planarFigure[2][1] = planarFigure[3][1];
            planarFigure[3][1] = northNo;
        }

        public void goSouth() {
            int southNo = planarFigure[3][1];
            planarFigure[3][1] = planarFigure[2][1];
            planarFigure[2][1] = planarFigure[1][1];
            planarFigure[1][1] = planarFigure[0][1];
            planarFigure[0][1] = southNo;
        }

        public String toString() {
            return "[" + currentRow + "," + currentCol + "] " +direction.name() + "\n"
            + planarFigure[0][0] + " " + planarFigure[0][1] + " " + planarFigure[0][2] + " " + "\n"
            + planarFigure[1][0] + " " + planarFigure[1][1] + " " + planarFigure[1][2] + " " + "\n"
            + planarFigure[2][0] + " " + planarFigure[2][1] + " " + planarFigure[2][2] + " " + "\n"
            + planarFigure[3][0] + " " + planarFigure[3][1] + " " + planarFigure[3][2] + " ";
        }

        public int getBottomNo() {
            return planarFigure[3][1];
        }
    }

    public static void main(String[] args) {
        Dice dice = new Dice();

        System.out.println("demo00003_63_23288_rolling_dice2");

        Scanner scanner = new Scanner(System.in);

        String raw_n_m_k = scanner.nextLine();
        String[] split_n_m_k = raw_n_m_k.split(" ");
        int N = 0, M = 0, K = 0;

        for (int i=0; i<split_n_m_k.length; i++) {
            if (i == 0) { // n
                int n = Integer.parseInt(split_n_m_k[i].trim());
                if (2 > n) {
                    System.out.print("2<=n (n="+n+")");
                    return;
                } else {
                    N = n;
                }
            } else if (i == 1) { // m
                int m = Integer.parseInt(split_n_m_k[i].trim());
                if (20 < m) {
                    System.out.print("m<=20 (m="+m+")");
                    return;
                } else {
                    M = m;
                }
            } else if (i == 2) { // k
                int k = Integer.parseInt(split_n_m_k[i].trim());
                if (k < 1 || 1000 < k) {
                    System.out.println("1<=k<=1000 (k="+k+")");
                    return;
                } else {
                    K = k;
                }
            }
        }

        int[][] NM = new int[N][M];
        String aRow;
        String[] splitRow;
        int tempNo;

        for (int i=0; i<N; i++) {
            aRow = scanner.nextLine();
            splitRow = aRow.split(" ");
            if (M == splitRow.length) {
                for (int j=0; j<splitRow.length; j++) {
                    tempNo = Integer.parseInt(splitRow[j].trim());
                    if (tempNo < 1|| 9 < tempNo) {
                        System.out.println("지도의 각 칸에 쓰여 있는 수는 10 미만의 자연수"); // 자연수 는 0 은 포함 안됨
                    } else {
                        NM[i][j] = tempNo;
                    }
                }
            } else {
                System.out.println("잘못된 M 입력");
                return;
            }
        }

        System.out.println("(Row)N=" + N + ", (Col)M=" + M);
        printMap(NM); // 25 분

        // 문제 3번 더 읽으며 Dice 만들기 50분

        int sum = 0;

        for (int i=0; i<K; i++) {
            dice.go(dice.direction, NM);
            sum += getScore(NM, dice);
        }

        System.out.println("sum : " + sum);
    }

    private static final Dice tempDice = new Dice();

    public static Dice copyToTempDice(Dice dice) {
        tempDice.currentRow = dice.currentRow;
        tempDice.currentCol = dice.currentCol;
        tempDice.direction = dice.direction;
        for (int r=0; r<dice.planarFigure.length; r++) {
            for (int c=0; c<dice.planarFigure[r].length; c++) {
                tempDice.planarFigure[r][c] = dice.planarFigure[r][c];
            }
        }
        return tempDice;
    }

    public static boolean canDiceMoveable(int[][] map, Dice _dice) {
        int nextDiceRow = _dice.getNextRow();
        int nextDiceCol = _dice.getNextCol();

        int mapRowSize = map.length;
        int mapColSize = map[0].length;

        if (nextDiceRow < 1 || mapRowSize < nextDiceRow
         || nextDiceCol < 1 || mapColSize < nextDiceCol
        ) {
            return false;
        } else {
            return true;
        }
    }

    public static class Node {

        final int pR, pC, R, C;

        Node(int parentR, int parentC, int R, int C) {
            this.pR = parentR;
            this.pC = parentC;
            this.R = R;
            this.C = C;
        }

        @Override
        public String toString() {
            return pR+","+pC+"[" + R + "," + C + "]";
        }
    }

    public static boolean isOnMap(int[][] map, int r, int c) {
        final int rowSize = map.length;
        final int colSize = map[0].length;

        return 0<=r && r<rowSize
            && 0<=c && c<colSize;
    }

    /**
     * @return 연속 이동 가능한 칸의 횟수
     */
    public static int getScore(int[][] map, Dice _dice) {
        final int B = getMapNo(map, _dice);
        boolean[][] visitMap = new boolean[map.length][map[0].length];

        List<List<Node>> nodeListList = new ArrayList<>();
        nodeListList.add(List.of(new Node(_dice.getRowOnMap(), _dice.getColOnMap(), _dice.getRowOnMap(), _dice.getColOnMap())));

        List<Node> tempNodeList;
        int listCount = 0;

        while (true) {
            tempNodeList = nodeListList.get(listCount);
            List<Node> newNodeList = new ArrayList<>();

            for (Node parentNode : tempNodeList) {// 해당 노드에관해 동서남북 조건 찾음
                if (
                    isOnMap(map, parentNode.R,parentNode.C+1) // 동쪽이동위치가 맵으로 갈수 있는 영역
                    && !visitMap[parentNode.R][parentNode.C+1] // 방문한적 없고
                    && B == map[parentNode.R][parentNode.C+1] // 해당 위치의 숫자가 같다.
                ) { // 동쪽
                    visitMap[parentNode.R][parentNode.C+1] = true;
                    newNodeList.add(new Node(parentNode.R, parentNode.C, parentNode.R, parentNode.C+1));

                } else if (
                    isOnMap(map, parentNode.R,parentNode.C-1) // 동쪽이동위치가 맵으로 갈수 있는 영역
                    && !visitMap[parentNode.R][parentNode.C-1] // 방문한적 없고
                    && B == map[parentNode.R][parentNode.C-1] // 해당 위치의 숫자가 같다.
                ) { // 서쪽
                    visitMap[parentNode.R][parentNode.C-1] = true;
                    newNodeList.add(new Node(parentNode.R, parentNode.C, parentNode.R, parentNode.C-1));

                } else if (
                    isOnMap(map, parentNode.R+1,parentNode.C) // 동쪽이동위치가 맵으로 갈수 있는 영역
                    && !visitMap[parentNode.R+1][parentNode.C] // 방문한적 없고
                    && B == map[parentNode.R+1][parentNode.C] // 해당 위치의 숫자가 같다.
                ) { // 남쪽
                    visitMap[parentNode.R+1][parentNode.C] = true;
                    newNodeList.add(new Node(parentNode.R, parentNode.C, parentNode.R+1, parentNode.C));

                } else if (
                    isOnMap(map, parentNode.R-1,parentNode.C) // 동쪽이동위치가 맵으로 갈수 있는 영역
                    && !visitMap[parentNode.R-1][parentNode.C] // 방문한적 없고
                    && B == map[parentNode.R-1][parentNode.C] // 해당 위치의 숫자가 같다.
                ) { // 북쪽
                    visitMap[parentNode.R-1][parentNode.C] = true;
                    newNodeList.add(new Node(parentNode.R, parentNode.C, parentNode.R-1, parentNode.C));

                }
            }


            if (1 > newNodeList.size()) {
                break;
            }

            nodeListList.add(newNodeList);
            listCount++;
        }

        AtomicInteger count = new AtomicInteger();
        nodeListList.forEach(nodeList -> {
            System.out.println(nodeList.stream().map(Node::toString).collect(Collectors.joining(",")));
            count.addAndGet(nodeList.size());
        });

        int score = count.get() * B;

        System.out.println("count:" + count.get() + ", score=" + score);
        return score;
    }

    public static int getMapNo(int[][] map, Dice dice) {
        return map[dice.currentRow-1][dice.currentCol-1];
    }

    public static void printMap(int[][] map) {
        System.out.println("map ---------------------------------------------------");
        for (int[] aRow : map) {
           for (int aCol : aRow) {
               System.out.print(aCol+ " ");
           }
           System.out.println();
        }
    }
}
