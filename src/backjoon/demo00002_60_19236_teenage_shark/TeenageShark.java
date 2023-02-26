package backjoon.demo00002_60_19236_teenage_shark;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * https://www.acmicpc.net/problem/19236
 * 정답률 75% 임 .. 여러 경우의 수 중
 */
public class TeenageShark {

    public static List<List<MarineLife>> eatenFishListList = new ArrayList<>();
    public enum Type {
        Fish,
        Shark
    }

    public enum Direction {
        NN(1, "↑"),
        NW(2, "↖"),
        WW(3, "←"),
        SW(4, "↙"),
        SS(5, "↓"),
        SE(6, "↘"),
        EE(7, "→"),
        NE(8, "↗");

        final int no;
        final String symbol;

        Direction(int no, String symbol) {
            this.no = no;
            this.symbol = symbol;
        }
    }

    public static class MarineLife {
        final Type type;
        int no;

        public int getNo() {
            return no;
        }

        int dir;

        int row;
        int col;


        MarineLife(Type type){
            this.type = type;
        }

        public void changeDirection() {
            if ( 8 < (dir + 1) ) {
                dir = 1;
            } else {
                dir = dir + 1;
            }
        }

        public int nextRowPoint() {
            if (getDirectionArrow().equals(Direction.NN.symbol) // "↑"
             || getDirectionArrow().equals(Direction.NW.symbol) // "↖"
             || getDirectionArrow().equals(Direction.NE.symbol))// "↗"
            {
                return row-1;
            } else if (
               getDirectionArrow().equals(Direction.WW.symbol) // "←"
            || getDirectionArrow().equals(Direction.EE.symbol) // "→"
            ) {
                return row;
            } else {
                return row+1;
            }
        }

        public int nextColPoint() {
             if (getDirectionArrow().equals(Direction.NW.symbol) // "↖"
              || getDirectionArrow().equals(Direction.WW.symbol) // "←"
              || getDirectionArrow().equals(Direction.SW.symbol) // "↙"
             ) {
                return col-1;
            } else if (getDirectionArrow().equals(Direction.SS.symbol) // "↓"
                    || getDirectionArrow().equals(Direction.NN.symbol) // "↑"
             ) {
                return col;
            } else {
                return col+1;
            }
        }

        public boolean hasNextPoint() {
            int row = nextRowPoint();
            if (row<0 || 3<row) return false;
            int col = nextColPoint();
            return col >= 0 && 3 >= col;
        }

        public String getDirectionArrow() {
                 if (dir == 1) return Direction.NN.symbol;//."↑";
            else if (dir == 2) return Direction.NW.symbol;//."↖";
            else if (dir == 3) return Direction.WW.symbol;//."←";
            else if (dir == 4) return Direction.SW.symbol;//."↙";
            else if (dir == 5) return Direction.SS.symbol;//."↓";
            else if (dir == 6) return Direction.SE.symbol;//."↘";
            else if (dir == 7) return Direction.EE.symbol;//."→";
                          else return Direction.NE.symbol;//."↗";
        }

        @Override
        public String toString() {
            return type.name().charAt(0)
                + String.valueOf(no>=10 ? no : no+" ")
                + getDirectionArrow()
                + getPointString(row, col)
                + " ";
        }

        public String toPathString() {
            return getPointString(row, col);
        }
    }

    public static String getPointString(int row, int col) {
        return "[" + row/*row*/ + "," + col/*col*/ + "]";
    }

    public static void main(String[] args) {
        // input start
        Scanner scanner = new Scanner(System.in);
        String rawLine;
        String[] splittedNoArr;
        int fishNo;
        int fishDir;

        MarineLife[][] originMarineSpaceArr = new MarineLife[4][4];

        for (int row=0; row<4; row++) {
            rawLine = scanner.nextLine();
            splittedNoArr = rawLine.split(" ");

            if (splittedNoArr.length != 8) {
                return; // 총 8개의 숫자가 있어야 한다.
            }

            int lineNoCount = 0;
            MarineLife fish;

            for (String aNo : splittedNoArr) {
                if (null == originMarineSpaceArr[row][lineNoCount/2]) {
                    originMarineSpaceArr[row][lineNoCount/2] = new MarineLife(Type.Fish);
                }

                fish = originMarineSpaceArr[row][lineNoCount/2];

                if (lineNoCount % 2 == 0) {
                    fishNo = Integer.parseInt(aNo.trim());
                    if (fishNo<1 || 16<fishNo) {
                        return; // 1 <= fish no <= 16
                    } else {
                        fish.no = fishNo;
                    }
                } else { // fish direction
                    fishDir = Integer.parseInt(aNo.trim());
                    if (fishDir<1 || 8<fishDir) {
                        return; // 1부터 순서대로 1↑, 2↖, 3←, 4↙, 5↓, 6↘, 7→, 8↗   // 1 <= direction <= 8 상하좌우, 대각선
                    } else {
                        fish.dir = fishDir;
                        fish.row = row;
                        fish.col = lineNoCount/2;
                    }
                }

                lineNoCount++;
            }
        }

//        System.out.println("--------------------------------------------------------");
//        printFish(originMarineSpaceArr);
//        System.out.println("--------------------------------------------------------");
        // input end 40 분 소요 //

//        MarineLife aShark = new MarineLife(Type.Shark);
//        eatFish(marineSpaceArr, aShark, 0, 0);

        // fish move start
        // 이동 가능한 칸 = 빈칸, 다른 물고기가 있는칸
        // 이동 불가능 칸 = 상어칸, 공간의 경계를 넘는칸
        // 이동 할 수 있는 칸이 없으면 이동 안함

        MarineLife aFish;
        MarineLife tempShark;
        MarineLife[][] marineSpaceArr;

        while (true) {
            marineSpaceArr = copy(originMarineSpaceArr);
            MarineLife aShark = new MarineLife(Type.Shark);
            List<MarineLife> eatenFishList = new ArrayList<>();
            eatFish(marineSpaceArr, aShark, 0,0, eatenFishList);
            marineSpaceArr[0][0] = aShark;

            System.out.println("================================================================");

            while (true) {
                // fish move end
                System.out.println("\n물고기 이동 시작");
                for (int i=1; i<=16; i++) {
//                    System.out.println(i + "번 물고기 이동 시작");
                    aFish = findNthFish(marineSpaceArr, i);

                    if (aFish != null) {
                        fishMove(marineSpaceArr, aFish);
                    }

//                    printFish(marineSpaceArr);
                }

                printFish(marineSpaceArr);
                System.out.println("물고기 이동 완료 (1 ~ 16번)");
                // fish move end

                // shark move start
                tempShark = findShark(marineSpaceArr); // 상어이동 // 상어 현재 위치 획득
                List<int[]> sharkMoveablePointList = getSharkMovablePosition(marineSpaceArr, tempShark); // 이동가능한 상어 위치
                System.out.println("상어 이동 시작 " + tempShark + " 이동가능위치 " + sharkMoveablePointList.stream().map(point->getPointString(point[0], point[1])).collect(Collectors.joining(",")));

                int[] nextSharkPoint = nextSharkPoint(eatenFishList, sharkMoveablePointList); // 기존의 다른 패스 획득을 위함

                if (null == nextSharkPoint) {
                    System.out.println("해양생물공간에서 다음 상어 위치 없음 " + eatenFishList.stream().mapToInt(MarineLife::getNo).sum());
                    break;
                } else {
                    eatFish(marineSpaceArr, tempShark, nextSharkPoint[0], nextSharkPoint[1], eatenFishList);
                }

                printFish(marineSpaceArr);
                System.out.println("상어 이동 완료 " + tempShark + " (먹은 물고기:" + eatenFishList.stream().map(MarineLife::toString).collect(Collectors.joining(" & ")) + ")");
                // shark move end
            }

            String newEatenFishListPath = getEatenFishPath(eatenFishList);

            if (1 > eatenFishListList.size()) {
                eatenFishListList.add(eatenFishList);
            } else {
                String oldEatenFishListPath;
                boolean hasSamePath = false;

                for (List<MarineLife> oldEatenFishList : eatenFishListList) {
                    oldEatenFishListPath = getEatenFishPath(oldEatenFishList);

                    if (oldEatenFishListPath.equals(newEatenFishListPath)) {
                        hasSamePath = true;
                    }
                }

                if (hasSamePath) {
                    break;
                } else {
                    eatenFishListList.add(eatenFishList);
                }
            }
        }

        AtomicInteger maxSum = new AtomicInteger(Integer.MIN_VALUE);
        eatenFishListList.forEach(eatenFishList -> {
            int sum = eatenFishList.stream().mapToInt(MarineLife::getNo).sum();
            System.out.println(sum + " " +  getEatenFishPath(eatenFishList));

            if (maxSum.get() < sum) {
                maxSum.set(sum);
            }
        });

        System.out.println("상어가 먹을 수 있는 물고기 번호의 합의 최댓값 : " + maxSum);
    }

    public static void fishMove(/*Not null*/ MarineLife[][] marineSpaceArr, /*Not null*/ MarineLife aFish) {
        if (aFish.no == 8 && aFish.row==3 && aFish.col==2) {
            System.out.println();
        }

        int aFishTempNextRow;
        int aFishTempNextCol;
        MarineLife aNextPositionMarineLife;

        for (int j=1; j<=8; j++) { // 모든 방향에 대해 한번씩 이동 시도를 해본다.
            aFishTempNextRow = aFish.nextRowPoint();
            aFishTempNextCol = aFish.nextColPoint();

            if (aFishTempNextRow < 0 || 3 < aFishTempNextRow || aFishTempNextCol < 0 || 3 < aFishTempNextCol) {
                // 공간의 경계 바깥
                aFish.changeDirection(); // 방향 전환

            } else {
                // 공간의 경계 안쪽 // 이동 가능한 칸
                aNextPositionMarineLife = marineSpaceArr[aFishTempNextRow][aFishTempNextCol];

                if (null == aNextPositionMarineLife) {
                    // 다음 갈 위치에 물고기가 없음
                    marineSpaceArr[aFishTempNextRow][aFishTempNextCol] = aFish;
                    marineSpaceArr[aFish.row][aFish.col] = null; // 현재 위치를 비운다
                    aFish.row = aFishTempNextRow; // 다음 위치로 이동
                    aFish.col = aFishTempNextCol;
                    return;
                } else if (Type.Shark == aNextPositionMarineLife.type) {
                    // 다음 갈 위치에 상어가 있음
                    aFish.changeDirection(); // 방향 전환

                } else { // (Type.Fish == aNextPositionMarineLife.type)
                    // 다음 갈 위치에 물고기가 있음
                    marineSpaceArr[aFish.row][aFish.col] = aNextPositionMarineLife;
                    aNextPositionMarineLife.row = aFish.row;
                    aNextPositionMarineLife.col = aFish.col;

                    marineSpaceArr[aFishTempNextRow][aFishTempNextCol] = aFish;
                    aFish.row = aFishTempNextRow;
                    aFish.col = aFishTempNextCol;
                    return;
                }
            }
        }

        printFish(marineSpaceArr);
    }

    public static int[] nextSharkPoint(List<MarineLife> eatenFishList, List<int[]> sharkMoveablePointList) {
        if (1 > eatenFishListList.size()) {
            return 0 == sharkMoveablePointList.size()
                ? null
                : sharkMoveablePointList.get(0);
        }

        String oldEatenFishPath;
        String newEatenFishPath;

        for (List<MarineLife> oldEatenFishList : eatenFishListList) {
            oldEatenFishPath = getEatenFishPath(oldEatenFishList);

            for (int[] sharkMoveablePoint : sharkMoveablePointList) {
                newEatenFishPath = getEatenFishPath(eatenFishList, sharkMoveablePoint);

                System.out.println("다음 상어 위치 / oldEatenFishPath:" + oldEatenFishPath + " / newEatenFishPath:" + newEatenFishPath);

                if (oldEatenFishList.size() == (eatenFishList.size()+1)) {
                    if (!oldEatenFishPath.equals(newEatenFishPath)) {
                        return sharkMoveablePoint;
                    }
                } else {
                    return sharkMoveablePoint;
                }

            }

        }

        return null;
    }

    public static String getEatenFishPath(List<MarineLife> eatenFishList) {
        if (null == eatenFishList || 1 > eatenFishList.size()) return "";
        return eatenFishList.stream().map(MarineLife::toPathString).collect(Collectors.joining("-"));
    }

    public static String getEatenFishPath(List<MarineLife> eatenFishList, int[] point) {
        List<String> pointStringList = eatenFishList.stream().map(MarineLife::toPathString).collect(Collectors.toList());
        pointStringList.add( getPointString(point[0], point[1]) );
        return String.join("-", pointStringList);
    }

    public static MarineLife findNthFish(MarineLife[][] marineSpaceArr, int nTh) {
        for (MarineLife[] aRowSpace : marineSpaceArr) {
            for (MarineLife aMarineLife : aRowSpace) {

                if (null != aMarineLife
                && aMarineLife.no == nTh
                && aMarineLife.type == Type.Fish) {
                    return aMarineLife;
                }

            }
        }
        return null;
    }

    public static MarineLife findShark(MarineLife[][] marineSpaceArr) {
        for (MarineLife[] aRowSpace : marineSpaceArr) {
            for (MarineLife aMarineLife : aRowSpace) {
                if (null!= aMarineLife && aMarineLife.type == Type.Shark) {
                    return aMarineLife;
                }
            }
        }
        return null;
    }

    public static void eatFish(MarineLife[][] marineSpaceArr, MarineLife aShark, int row, int col, List<MarineLife> eatenFishList) {
        int oldSharkRow = aShark.row;
        int oldSharkCol = aShark.col;

        MarineLife aFish = marineSpaceArr[row][col];
        aShark.dir = aFish.dir;
        aShark.row = aFish.row;
        aShark.col = aFish.col;

        marineSpaceArr[aFish.row][aFish.col] = aShark;
        marineSpaceArr[oldSharkRow][oldSharkCol] = null;
        eatenFishList.add(aFish);
    }

    public static void printFish(MarineLife[][] marineSpaceArr) {
        for (MarineLife[] aRowSpace : marineSpaceArr) {
            for (MarineLife aMarineLife : aRowSpace) {
                if (null == aMarineLife) {
                    System.out.print("null      ");
                } else {
                    System.out.print(aMarineLife);
                }
            }
            System.out.println();
        }
    }

    public static List<int[]> getSharkMovablePosition(MarineLife[][] marineSpaceArr, MarineLife aShark) {
        // 이동가능한 칸 구하기 / 상어는 방향에 있는 칸으로 이동할 수 있는데, 한 번에 여러 개의 칸을 이동할 수 있다
        // 물고기가 없는 칸으로는 이동할 수 없다.

        List<int[]> moveablePositionList = new ArrayList<>();
        MarineLife tempShark = new MarineLife(Type.Shark);
        tempShark.dir = aShark.dir;

        int[] nextPoint;

        do {
            nextPoint = null;

            if (0 == moveablePositionList.size()) {
                if (aShark.hasNextPoint()) {
                    nextPoint = new int[]{aShark.nextRowPoint(), aShark.nextColPoint()};
                }
            } else {
                int[] lastPoint = moveablePositionList.get(moveablePositionList.size() - 1);
                tempShark.row = lastPoint[0];
                tempShark.col = lastPoint[1];

                if (tempShark.hasNextPoint()) {
                    nextPoint = new int[]{tempShark.nextRowPoint(), tempShark.nextColPoint()};
                }
            }

            if (null != nextPoint) {
                moveablePositionList.add(nextPoint);
            }

        } while (null != nextPoint);

//        System.out.println(aShark);
//        System.out.println("이동가능 경로");
//        System.out.println(moveablePositionList.stream().map(point -> getPointString(point[0], point[1])).collect(Collectors.joining("-")));

        List<int[]> sharkMoveablePointList = new ArrayList<>();

        moveablePositionList.forEach(position -> {
            MarineLife tempFish = marineSpaceArr[position[0]][position[1]];

            if (null!=tempFish && Type.Fish==tempFish.type) {
                sharkMoveablePointList.add(position);
            }
        });

//        System.out.println("상어 이동가능 경로");
//        System.out.println(sharkMoveablePointList.stream().map(point -> getPointString(point[0], point[1])).collect(Collectors.joining("-")));

        return sharkMoveablePointList;
    }

    public static MarineLife[][] copy(MarineLife[][] originMarineSpace) {
        MarineLife[][] newMarineSpace = new MarineLife[originMarineSpace.length][originMarineSpace.length];

        for (int i=0; i<originMarineSpace.length; i++) {
            for (int j=0; j<originMarineSpace[i].length; j++) {
                MarineLife aMarineLife = originMarineSpace[i][j];

                if (null == aMarineLife) {
                    newMarineSpace[i][j] = null;
                } else {
                    newMarineSpace[i][j] = new MarineLife(aMarineLife.type);
                    newMarineSpace[i][j].no = aMarineLife.no;
                    newMarineSpace[i][j].dir = aMarineLife.dir;
                    newMarineSpace[i][j].row = aMarineLife.row;
                    newMarineSpace[i][j].col = aMarineLife.col;
                }
            }
        }

        return newMarineSpace;
    }

}
