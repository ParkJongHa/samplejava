package algorithm.baekjoon.demo02_ss.demo00005_56_2_4_ramp;
import java.util.*;

public class Main2 {

    static int n, l;
    static int[][] board;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        n = scan.nextInt();
        l = scan.nextInt();

        board = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                board[i][j] = scan.nextInt();
            }
        }

        int count = 0;
        for(int i = 0; i < n; i++) {
            if(calRow(i)) count++; //i번째 열 확인
            if(calCol(i)) count++; //i번째 행 확인
        }
        System.out.println(count);
    }

    public static boolean calRow(int row) {
        boolean[] isIncline = new boolean[n]; //경사면 설치 여부를 확인하는 배열

        for(int i = 0; i < n - 1; i++) {
            int diff = board[row][i] - board[row][i + 1];

            if(diff > 1 || diff < -1) return false; //높이차 1 초과하므로 false
            else if(diff == -1) { // 다음 계단이 한 계단 높다
                for(int j = 0; j < l; j++) { // 올라가는 경사로를 설치할 수 있는지 확인한다.
                    if(i - j < 0 || isIncline[i - j]) return false;
                    if(board[row][i] != board[row][i - j]) return false;
                    isIncline[i - j]  = true; //경사면 설치
                }
            }
            else if(diff == 1) { //다음 계단이 한 계단 낮다
                for(int j = 1; j <= l; j++) { //내려가는 경사로를 설치할 수 있는지 확인한다.
                    if(i + j >= n || isIncline[i + j]) return false;
                    if(board[row][i] - 1 != board[row][i + j]) return false;
                    isIncline[i + j] = true; //경사면 설치
                }
            }
        }
        return true;
    }

    public static boolean calCol(int col) {
        boolean[] isIncline = new boolean[n]; //경사면 설치 여부를 확인하는 배열

        for(int i = 0; i < n - 1; i++) {
            int diff = board[i][col] - board[i + 1][col];

            if(diff > 1 || diff < -1) return false; //높이차 1 초과하므로 false
            else if(diff == -1) { // 다음 계단이 한 계단 높다
                for(int j = 0; j < l; j++) { // 올라가는 경사로를 설치할 수 있는지 확인한다.
                    if(i - j < 0 || isIncline[i - j]) return false;
                    if(board[i][col] != board[i - j][col]) return false;
                    isIncline[i - j]  = true; //경사면 설치
                }
            }
            else if(diff == 1) { //다음 계단이 한 계단 낮다
                for(int j = 1; j <= l; j++) { //내려가는 경사로를 설치할 수 있는지 확인한다.
                    if(i + j >= n || isIncline[i + j]) return false;
                    if(board[i][col] - 1 != board[i + j][col]) return false;
                    isIncline[i + j] = true; //경사면 설치
                }
            }
        }
        return true;
    }
}
