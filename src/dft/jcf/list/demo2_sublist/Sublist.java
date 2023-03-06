package dft.jcf.list.demo2_sublist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sublist {

    public static void main(String[] args) {
        List<String> charList
            = Arrays.asList("0", "1", "2", "3", "4", "5", "6");

        charList.subList(0,1).forEach(System.out::println); // 0

        System.out.println("-----------------------------");

        charList.subList(0,2).forEach(System.out::println); // 0,1

        System.out.println("-----------------------------");

        charList.subList(1,1).forEach(System.out::println); //

        System.out.println("-----------------------------");

        charList.subList(1,3).forEach(System.out::println); // 1,2

        System.out.println("-----------------------------");

        charList.subList(5,7).forEach(System.out::println); // 5,6

        System.out.println("-----------------------------");

        charList.subList(5,8).forEach(System.out::println); // IndexOutOfBoundsException
    }

}
