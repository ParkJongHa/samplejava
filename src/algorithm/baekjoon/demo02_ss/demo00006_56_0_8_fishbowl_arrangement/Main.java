package algorithm.baekjoon.demo02_ss.demo00006_56_0_8_fishbowl_arrangement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
        public FishBowl(int fishCount) {
            this.fishCount = fishCount;
        }

        public FishBowl upFishBowl = null;
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

        // 이제 어항을 쌓는다. 먼저, 가장 왼쪽에 있는 어항을 그 어항의 오른쪽에 있는 어항의 위에 올려 놓아 <그림 3>이 된다.
        List hoveringBowlList = fishBowlList.stream().filter(fishBowl -> fishBowl.upFishBowl!=null).toList();
        if (0==hoveringBowlList.size()) {
            fishBowlList.get(0).row++;
            fishBowlList.stream().filter(fishBowl -> fishBowl.row==0).toList().forEach(fishBowl -> fishBowl.col--);
        }

        print();

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
        int max = fishBowlList.stream().mapToInt(fishBowl-> fishBowl.row).max().getAsInt();

        for (int i=max; i>=0; i--) {
            int finalI = i;
            fishBowlList.stream().filter(fishBowl -> fishBowl.row == finalI).toList().forEach(System.out::print);
            System.out.println();
        }
    }

}
