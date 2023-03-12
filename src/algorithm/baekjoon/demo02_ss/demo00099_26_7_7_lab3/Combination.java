package algorithm.baekjoon.demo02_ss.demo00099_26_7_7_lab3;

import java.util.ArrayList;
import java.util.List;

public class Combination<T> {
    private final List<T> originalItemList;
    private final int extractCount;
    private final List<List<T>> resultListList = new ArrayList<>();
    private final boolean[] visitArr;
    private final Object[] resultArr;

    public Combination(
        List<T> originalItemList,
        int extractCount
    ) {
        this.originalItemList = originalItemList;
        this.extractCount = extractCount;
        resultArr = new Object[extractCount];
        visitArr = new boolean[originalItemList.size()];
        getCombinationResult(0,0);
    }

    private void getCombinationResult(int idx, int cnt) {
        if (cnt == extractCount) {
            List<T> tempList = new ArrayList<T>();
            for (Object o : resultArr) {
                tempList.add((T)o);
            }
            resultListList.add(tempList);
        } else {
            for (int i=idx; i<originalItemList.size(); i++) {
                if (visitArr[i]) continue;

                resultArr[cnt] = originalItemList.get(i);
                visitArr[i] = true;
                getCombinationResult(i, cnt+1);
                visitArr[i] = false;
            }
        }
    }

    public List<List<T>> get() {
        return resultListList;
    }

}
