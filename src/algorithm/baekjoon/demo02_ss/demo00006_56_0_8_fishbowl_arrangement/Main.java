package algorithm.baekjoon.demo02_ss.demo00006_56_0_8_fishbowl_arrangement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
https://www.acmicpc.net/problem/23291
 */
public class Main {

    public static class FishBowl {
        int fishCount;
        int row = 0;
        int col = 0;

        public FishBowl setCol(int _col) {
            this.col = _col;
            return this;
        }

        public FishBowl setRow(int _row) {
            this.row = _row;
            return this;
        }
        public FishBowl(int fishCount) {
            this.fishCount = fishCount;
        }

        @Override
        public String toString() {
            return "[" + row + "," + col + "]" + fishCount + " ";
        }
    }

    public static final List<FishBowl> OriginalFishBowlList = new ArrayList<>();
    public static List<FishBowl> fishBowlList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("hi ");

        Scanner scanner = new Scanner(System.in);
        String rawLine;
        String[] splitLine;

        rawLine = scanner.nextLine();
        splitLine = rawLine.split(" ");

        int N = Integer.parseInt(splitLine[0].trim());
        int K = Integer.parseInt(splitLine[1].trim());

        for (int i=0; i<N; i++) {
            OriginalFishBowlList.add(new FishBowl(scanner.nextInt()).setCol(i));
        }

        fishBowlList = copy(OriginalFishBowlList);
        print();

        // 물고기의 수가 가장 적은 어항에 물고기를 한 마리 넣는다.
        final int minFishCount = fishBowlList.stream().mapToInt(value -> value.fishCount).min().getAsInt();
        fishBowlList.forEach(fishBowl -> {if (fishBowl.fishCount == minFishCount) fishBowl.fishCount++;});
        print();

        /*
        // 이제 어항을 쌓는다. 먼저, 가장 왼쪽에 있는 어항을 그 어항의 오른쪽에 있는 어항의 위에 올려 놓아 <그림 3>이 된다.
        List<FishBowl> hoveringBowlList = fishBowlList.stream().filter(fishBowl -> fishBowl.upFishBowl!=null).toList();
        if (0==hoveringBowlList.size()) {
            fishBowlList.get(0).row++;
            fishBowlList.stream().filter(fishBowl -> fishBowl.row==0).toList().forEach(fishBowl -> fishBowl.col--);
        }

        if (isRotatable(fishBowlList)) { // 공중부양 시키고 회전 가능하면
            // 이제, 2개 이상 쌓여있는 어항을 모두 공중 부양시킨 다음, 전체를 시계방향으로 90도 회전시킨다. 이후 공중 부양시킨 어항을 바닥에 있는 어항의 위에 올려놓는다.
            Map<Integer, List<FishBowl>> fishBowlListByColMap = fishBowlList.stream().collect(Collectors.groupingByConcurrent(fishBowl -> fishBowl.col));

            int hoveringTargetWidth = 0; // 공중부양될 어항의 길이
            int tempNo;

            for(Integer col : fishBowlListByColMap.keySet().stream().toList()) {
                if (2<=fishBowlListByColMap.get(col).size()) {
                    hoveringTargetWidth++;
                }
            }

            print();

            System.out.println("hoveringTargetWidth: " + hoveringTargetWidth);

            for (Integer col : fishBowlListByColMap.keySet().stream().toList()) {
                if (2<=fishBowlListByColMap.get(col).size()) { // // 이제, 2개 이상 쌓여있는
                    for (FishBowl fishBowl : fishBowlListByColMap.get(col)) {
                        fishBowl.col = hoveringTargetWidth - fishBowl.col; //전체를 시계방향으로 90도 회전시킨다.

                        tempNo = fishBowl.row;
                        fishBowl.row = fishBowl.col;
                        fishBowl.col = tempNo;
                    }
                }
            }

            for (FishBowl aFishBowl : fishBowlList.stream().filter(fishBowl -> fishBowl.row==0).toList()) {
                aFishBowl.col = aFishBowl.col - hoveringTargetWidth;
            }
        }

        print();
        //*/

        hoveringTask();

        // 물고기 수 조절

        flattening();
    }

    public static void hoveringTask() {
        while (true) {
            // 이제 어항을 쌓는다. 먼저, 가장 왼쪽에 있는 어항을 그 어항의 오른쪽에 있는 어항의 위에 올려 놓아 <그림 3>이 된다.
            if (0==fishBowlList.stream().filter(fishBowl -> fishBowl.row>0).toList().size()) {
                fishBowlList.get(0).row++;
                fishBowlList.stream().filter(fishBowl -> fishBowl.row==0).toList().forEach(fishBowl -> fishBowl.col--);
            } else if ( isRotatable(fishBowlList) ) { // 공중부양 시키고 회전 가능하면
                // 이제, 2개 이상 쌓여있는 어항을 모두 공중 부양시킨 다음, 전체를 시계방향으로 90도 회전시킨다. 이후 공중 부양시킨 어항을 바닥에 있는 어항의 위에 올려놓는다.
                Map<Integer, List<FishBowl>> fishBowlListByColMap = fishBowlList.stream().collect(Collectors.groupingByConcurrent(fishBowl -> fishBowl.col));

                int hoveringTargetWidth = 0; // 공중부양될 어항의 길이
                int tempNo;

                for(Integer col : fishBowlListByColMap.keySet().stream().toList()) {
                    if (2<=fishBowlListByColMap.get(col).size()) {
                        hoveringTargetWidth++;
                    }
                }

                print();

                System.out.println("hoveringTargetWidth: " + hoveringTargetWidth);

                for (Integer col : fishBowlListByColMap.keySet().stream().toList()) {
                    if (2<=fishBowlListByColMap.get(col).size()) { // // 이제, 2개 이상 쌓여있는
                        for (FishBowl fishBowl : fishBowlListByColMap.get(col)) {
                            fishBowl.col = hoveringTargetWidth - fishBowl.col; //전체를 시계방향으로 90도 회전시킨다.

                            tempNo = fishBowl.row;
                            fishBowl.row = fishBowl.col;
                            fishBowl.col = tempNo;
                        }
                    }
                }

                for (FishBowl aFishBowl : fishBowlList.stream().filter(fishBowl -> fishBowl.row==0).toList()) {
                    aFishBowl.col = aFishBowl.col - hoveringTargetWidth;
                }
            } else {
                break;
            }
            print();
        }

        print();
    }

    public static void flattening() {
        Map<Integer, List<FishBowl>> fishBowlListByColMap = fishBowlList.stream().collect(Collectors.groupingByConcurrent(fishBowl -> fishBowl.col));

        int maxCol = Integer.MIN_VALUE;
        for (Integer integer : fishBowlListByColMap.keySet().stream().toList()) {
            maxCol = Math.max(integer, maxCol);
        }

        List<FishBowl> newFishBowlList = new ArrayList<>();
        int newCol = 0;

        for (int i=0; i<=maxCol; i++) {
            List<FishBowl> fishBowlList = fishBowlListByColMap.get(i);

            int maxRow = Integer.MIN_VALUE;
            for (Integer integer : fishBowlListByColMap.keySet().stream().toList()) {
                maxRow = Math.max(integer, maxRow);
            }

            for (int j=0; j<=maxRow; j++) {
                int finalJ = j;
                FishBowl newFishBowl = fishBowlList.stream().filter(fishBowl -> fishBowl.row == finalJ).findFirst().orElse(null);

                if (null != newFishBowl) {
                    newFishBowlList.add( newFishBowl.setRow(0).setCol(newCol) );
                    newCol++;
                }
            }
        }

        fishBowlList = newFishBowlList;
        print();
    }


    public static boolean isRotatable(List<FishBowl>  fishBowlList) {
        Map<Integer, List<FishBowl>> fishBowlListByColMap = fishBowlList.stream().collect(Collectors.groupingByConcurrent(fishBowl -> fishBowl.col));
        int height = Integer.MIN_VALUE;
        int width = 0;

        List<Integer> colList = fishBowlListByColMap.keySet().stream().toList();
        for (Integer col : colList) {
            if (2 <= fishBowlListByColMap.get(col).size()) {
                height = Math.max(fishBowlListByColMap.get(col).size(), height);
            } else {
                width = width+1;
            }
        }

        boolean isRotatable = height <= width; // 땅이 더 넓어야 공중 부양된 어항을 회전 시킬수 있다.

        System.out.println("isRotatable(" + (isRotatable)+ ") width:" + width + ", height:" + height);

        return isRotatable; // 회전 가능
    }

    public static List<FishBowl> copy(List<FishBowl> _fishBowlList) {
        List<FishBowl> fishBowlList = new ArrayList<>();
        _fishBowlList.forEach(aFishBowl -> fishBowlList.add(copy(aFishBowl)));
        return fishBowlList;
    }

    public static FishBowl copy(FishBowl _fishBowl) {
        FishBowl fishBowl = new FishBowl(_fishBowl.fishCount);
        fishBowl.row = _fishBowl.row;
        fishBowl.col = _fishBowl.col;
        return fishBowl;
    }

    public static void print() {
        int maxRow = fishBowlList.stream().mapToInt(fishBowl-> fishBowl.row).max().getAsInt();
        List<FishBowl> rowBowlList;

        for (int i=maxRow; i>=0; i--) {
            int finalI = i;
            rowBowlList = fishBowlList.stream().filter(fishBowl -> fishBowl.row == finalI).toList();
            AtomicInteger maxCol = new AtomicInteger(Integer.MIN_VALUE);
            rowBowlList.stream().forEach(fishBowl -> maxCol.set(Math.max(fishBowl.col, maxCol.get())));
            for (int j=0; j<=maxCol.get(); j++) {
                int finalJ = j;
                FishBowl aFishBowl = rowBowlList.stream().filter(fishBowl -> fishBowl.col== finalJ).findFirst().orElse(null);
                System.out.print((null==aFishBowl ? "" : aFishBowl));
            }

            System.out.println();
        }

        System.out.println("\n\n");
    }

}
