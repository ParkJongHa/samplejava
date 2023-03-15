package algorithm.baekjoon.demo02_ss.demo00006_56_0_8_fishbowl_arrangement;

import java.util.Scanner;

/*
https://www.acmicpc.net/problem/23291
 */
public class Main {

    public static class FishBowl {
        int fishCount = 0;
    }

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
            int initFishCount = scanner.nextInt();
            System.out.println("initFishCount " + initFishCount);
        }

    }


}
