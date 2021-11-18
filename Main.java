package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        HashMap<String, Integer> markRecord = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("Борисов Клим Михайлович");
        nameList.add("Волощенко  Егор Михайлович");
        nameList.add("Рунич Пётр Станиславович");

        while (nameList.size() != 0) {
            Random randomizer = new Random();
            String random = nameList.get(randomizer.nextInt(nameList.size()));

            System.out.println("Введите print или оценку для " + random);
            String input = sc.next();

            if (input.equals("print")) {
                System.out.println(markRecord);
                System.out.println("Введите оценку для " + random);
                input = sc.next();
            }

            int mark = Integer.parseInt(input);

            markRecord.put(random, mark);

            nameList.remove(random);
        }

        System.out.println(markRecord);
    }
}