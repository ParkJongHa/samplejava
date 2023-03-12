package algorithm.baekjoon.demo02_ss.demo00099_26_7_7_lab3;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
https://www.acmicpc.net/problem/17142
 */
public class Main {

    public enum Virus {
        Active("V"), // 활성 바이러스는 0
        Inactive("*"); // 비활성 바이러스는 *

        public final String code;

        Virus(String _code) {
            code = _code;
        }
    }

    public enum SpaceType {
        Empty(0), // 0은 빈 칸 / 바이러스가 퍼지는 시간
        Wall(1), // 1 은 벽
        Virus(2),
        ;

        public final int inputType;

        SpaceType(int inputType) {
            this.inputType = inputType;
        }
    }

    public static class Space {
        public final int R;
        public final int C;
        public final SpaceType spaceType;
        public int virusSpreadTime = 0; // 초기화 값
        public Virus virus = null;

        public Space spreadSourceSpace = null;

        public void setSpreadSource(Space space) {
            if (0 < virusSpreadTime) {
                spreadSourceSpace = null;
                return; // 이미 바이러스가 퍼져 있다
            }

            if (null == spreadSourceSpace) {
                spreadSourceSpace = space;
            } else if (spreadSourceSpace.virusSpreadTime > space.virusSpreadTime) {
                spreadSourceSpace = space;
            }
        }

        public Space(int R, int C,int inputType) {
            this.R = R;
            this.C = C;

            if (inputType==0) spaceType = SpaceType.Empty;
            else if (inputType==1) spaceType = SpaceType.Wall;
            else/* inputType==2 */ {
                spaceType = SpaceType.Virus;
                virus = Virus.Inactive;
            }
        }

        @Override
        public String toString() {
            if (SpaceType.Empty == spaceType) {
                return virusSpreadTime + "  ";
            } else if (SpaceType.Wall == spaceType) {
                return "-  ";
            } else {/* spaceContent.Virus == spaceContent */
                if (0 < virusSpreadTime) {
                    return "V"+virusSpreadTime + " ";
                }
                return "V0 "; //virus.code + "  ";
            }
        }
    }

    public static Space[][] labSpace;
    public static String line;
    public static String[] splitLine;
    public static int N;// labSize
    public static int M;// virusCount

    public final static List<Space> virusSpaceList = new ArrayList<>();

    public static int minTime = Integer.MAX_VALUE;
    public static List<Space> minTimeActiveVirusList = null;

    public static Space[][] minTimeSpaceArr;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        line = scanner.nextLine();
        splitLine = line.split(" ");

        if (2 != splitLine.length) {
            System.out.println("lab size, virus count err");
            return;
        }

        N = Integer.parseInt(splitLine[0]);
        if (N<4 || 50<N) {
            System.out.println("연구소의 크기 N(4 ≤ N ≤ 50)");
            return;
        }

        M = Integer.parseInt(splitLine[1]);
        if (M<1 || 10<M) {
            System.out.println("바이러스의 개수 M(1 ≤ M ≤ 10)");
            return;
        }

        labSpace = new Space[N][N];
        int tempNo;
        int twoCount = 0;

        for (int i=0; i<N; i++) {
            line = scanner.nextLine();
            splitLine = line.split(" ");

            if (N != splitLine.length) {
                System.out.println("연구소의 크기 " + N);
                return;
            }

            for (int j=0; j<splitLine.length; j++) {
                tempNo = Integer.parseInt(splitLine[j]);
                labSpace[i][j] = new Space(i, j, tempNo);
                if (2 == tempNo) {
                    twoCount++;
                    virusSpaceList.add(labSpace[i][j]);
                }
            }
        }

        if (twoCount<M || 10<twoCount) {
            System.out.println("2의 개수는 M보다 크거나 같고, 10보다 작거나 같은 자연수이다.");
            return;
        }

        // 모든 빈 칸에 바이러스를 퍼뜨리는 최소 시간
        printSpace();

        // 바이러스 위치에서 M개를 활성화 시키는 가지수 추출
        List<List<Space>> spaceListList = getCombinationList();
        AtomicInteger atomicInteger = new AtomicInteger(1);

        spaceListList.forEach(spaceList -> {
            initVirusSpreadTime(); // 바이러스 확산 시간 초기화
            initVirusActive(spaceList); // M개 바이러스 활성화

            System.out.println(
                    M + "개 활성화 "
                    + atomicInteger.getAndIncrement() + "번째 - "
                    + spaceList.stream()
                            .map(space -> "[" + space.R+","+space.C + "]")
                            .collect(Collectors.joining("-"))
            );
            printSpace();

            int spreadTime = getSpreadTime();

            if (spreadTime<minTime && isAllSpreadInEmptySpace(labSpace)) {
                minTime = spreadTime;
                minTimeActiveVirusList = spaceList;
                minTimeSpaceArr = getLabSpaceDeepCopy();
            }

            System.out.println("공간에 바이러스 확산 시간 : " + spreadTime + ", 모든공간에퍼짐 ? " + isAllSpreadInEmptySpace(labSpace));
            printSpace();
            System.out.println("\n\n");
        });



        // 결과 출력
        if (minTimeActiveVirusList == null) {
            // 바이러스를 어떻게 놓아도 모든 빈 칸에 바이러스를 퍼뜨릴 수 없는 경우에는 -1을 출력한다.
            System.out.println("minTime : -1");
        } else {
            System.out.println(minTimeActiveVirusList
                    .stream()
                    .map(space -> "[" + space.R+","+space.C + "]")
                    .collect(Collectors.joining("-"))
            );
            printSpace(minTimeSpaceArr);
            System.out.println("minTime : " + minTime);
        }
    }

    public static void initVirusSpreadTime() {
        // 바이러스 퍼지기전 초기화
        for (Space[] spaceRow : labSpace) {
            for (Space aSpace : spaceRow) {
                aSpace.spreadSourceSpace = null;
                if (aSpace.spaceType==SpaceType.Empty) {
                    aSpace.virusSpreadTime = 0;
                }
            }
        }
    }

    public static int getSpreadTime() {
        Integer[][] lastBeforeSpreadTime = new Integer[N][N];

        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                if (SpaceType.Empty == labSpace[i][j].spaceType) {
                    lastBeforeSpreadTime[i][j] = 0;
                } else {
                    lastBeforeSpreadTime[i][j] = null;
                }
            }
        }

        int spreadTime = 0;

        while(true) {
            // 바이러스 퍼질 공간 설정
            for (Space[] spaceRow : labSpace) {
                for (Space aSpace : spaceRow) {
                    if ((aSpace.spaceType == SpaceType.Virus && aSpace.virus == Virus.Active)
                     || (aSpace.spaceType == SpaceType.Empty && 0 < aSpace.virusSpreadTime)
                    ) {
                        setSpreadTarget(aSpace, getLeftSpace(aSpace) );
                        setSpreadTarget(aSpace, getRightSpace(aSpace) );
                        setSpreadTarget(aSpace, getUpSpace(aSpace) );
                        setSpreadTarget(aSpace, getDownSpace(aSpace) );
                    }
                }
            }

            // 바이러스 퍼지는 시간 설정
            for (Space[] spaceRow : labSpace) {
                for (Space aSpace : spaceRow) {

                    if (SpaceType.Wall!=aSpace.spaceType && null!=aSpace.spreadSourceSpace) {
                        if (aSpace.spaceType == SpaceType.Virus && Virus.Inactive == aSpace.virus) aSpace.virus = Virus.Active;
                        aSpace.virusSpreadTime = aSpace.spreadSourceSpace.virusSpreadTime + 1;
                    }

                    aSpace.spreadSourceSpace = null;
                }
            }

//            System.out.println(spreadTime + "초 지남");
//            printSpace();

            if (isAllSame(labSpace, lastBeforeSpreadTime)) {
                break;
            } else {
                spreadTime++; // 시간이 1초 흘렀다

                // 마지막 virusSpreadTime 저장
                for (int i=0; i<N; i++) {
                    for (int j=0; j<N; j++) {
                        if (SpaceType.Empty == labSpace[i][j].spaceType) {
                            lastBeforeSpreadTime[i][j] = labSpace[i][j].virusSpreadTime;
                        }
                    }
                }
            }
        }

        return spreadTime;
    }

    public static void setSpreadTarget(Space spreadSourceSpace, Space spreadTargetSpace) {
        if (null == spreadSourceSpace
         || null == spreadTargetSpace
         || SpaceType.Wall == spreadTargetSpace.spaceType
        ) return;

        if (0 < spreadTargetSpace.virusSpreadTime) return;
        spreadTargetSpace.setSpreadSource(spreadSourceSpace);
    }

    public static boolean isAllSame(Space[][] sourceSpaceArr, Integer[][] lastBeforeSpreadTime) {
        if (null == sourceSpaceArr || null == lastBeforeSpreadTime) return false;

        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                if (null != lastBeforeSpreadTime[i][j]
                 && lastBeforeSpreadTime[i][j] != sourceSpaceArr[i][j].virusSpreadTime
                ) {
                    return false; // 다르다
                }
            }
        }

        return true;
    }

    public static Space getLeftSpace(Space criteriaSpace) {
        int targetRow = criteriaSpace.R;
        int targetCol = criteriaSpace.C - 1;
        if (targetCol<0 || N<=targetCol) return null;
        return labSpace[targetRow][targetCol];
    }
    public static Space getRightSpace(Space criteriaSpace) {
        int targetRow = criteriaSpace.R;
        int targetCol = criteriaSpace.C + 1;
        if (targetCol<0 || N<=targetCol) return null;
        return labSpace[targetRow][targetCol];
    }
    public static Space getUpSpace(Space criteriaSpace) {
        int targetRow = criteriaSpace.R-1;
        int targetCol = criteriaSpace.C;
        if (targetRow<0 || N<=targetRow) return null;
        return labSpace[targetRow][targetCol];
    }
    public static Space getDownSpace(Space criteriaSpace) {
        int targetRow = criteriaSpace.R+1;
        int targetCol = criteriaSpace.C;
        if (targetRow<0 || N<=targetRow) return null;
        return labSpace[targetRow][targetCol];
    }


    public static void initVirusActive(List<Space> activeVirusSpaceList) {
        for (Space[] spaceRow : labSpace) {
            for(Space aSpace : spaceRow) {
                if (SpaceType.Virus == aSpace.spaceType) {
                    if (activeVirusSpaceList
                            .stream()
                            .anyMatch(aVirusSpace -> aVirusSpace.R==aSpace.R && aVirusSpace.C==aSpace.C)
                    ) {
                        aSpace.virus = Virus.Active;
                    } else {
                        aSpace.virus = Virus.Inactive;
                    }
                }
            }
        }
    }

    private static final List<List<Space>> combinationSpaceListList = new ArrayList<>();
    private static List<Space> combinationSpaceList;
    private static int combinationExtractCount;
    private static Space[] combinationResultArr;
    private static boolean[] combinationVisitArr;
    public static void combination(List<Space> spaceList, int extractCount) {
        combinationSpaceList = spaceList;
        combinationExtractCount = extractCount;
        combinationResultArr = new Space[extractCount];
        combinationVisitArr = new boolean[spaceList.size()];
        combinationCalc(0,0);
    }

    public static void combinationCalc(int idx, int cnt) {
        if (combinationExtractCount == cnt) {
            List<Space> tempSpaceList = new ArrayList<>(Arrays.asList(combinationResultArr));
            combinationSpaceListList.add(tempSpaceList);
        } else {
            for (int i=idx; i<combinationSpaceList.size(); i++) {
                if (combinationVisitArr[i]) continue;

                combinationResultArr[cnt] = combinationSpaceList.get(i);
                combinationVisitArr[i] = true;
                combinationCalc(i, cnt+1);
                combinationVisitArr[i] = false;
            }
        }
    }

    public static List<List<Space>> getCombinationList() {
        combination(virusSpaceList, M);
        return combinationSpaceListList;
    }










    public static void printSpace() {
        printSpace(labSpace);
    }

    public static void printSpace(Space[][] spaceArr) {
        for (Space[] spaces : spaceArr) {
            for (Space space : spaces) {
                System.out.print(space);
            }
            System.out.println();
        }
    }

    public static boolean isAllSpreadInEmptySpace(Space[][] spaceArr) {
        for (Space[] spaces : spaceArr) {
            for (Space space : spaces) {
                if (SpaceType.Empty == space.spaceType && 1 > space.virusSpreadTime) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Space[][] getLabSpaceDeepCopy() {
        Space[][] newSpaceArr = new Space[N][N];

        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                newSpaceArr[i][j] = copy(labSpace[i][j]);
            }
        }

        return newSpaceArr;
    }

    public static Space copy(Space space) {
        Space newSpace = new Space(space.R, space.C, space.spaceType.inputType);
        newSpace.virusSpreadTime = space.virusSpreadTime;
        newSpace.virus = space.virus;
        return newSpace;
    }

}