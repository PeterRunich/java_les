package com.company;

import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        HubList<String> youList = new HubList<String>();
        youList.add("ccc");
        youList.add("b");
        youList.add("aa");
        System.out.println(youList); //[ccc, b, aa]
        System.out.println(youList.find("b")); //1
        System.out.println(youList.find("s")); //-1
        youList.sort(new HubStringComparator());
        System.out.println(youList); //[b, aa, ccc]
        System.out.println(youList.removeAt(1)); //aa
        System.out.println(youList); //[b, ccc]
    }
}

class HubStringComparator implements Comparator {
    @Override
    public int compare(Object str1, Object str2) {
        return Integer.compare(((String) str1).length(), ((String) str2).length());
    }
}

class HubList<E> {
    private int capacity = 0;
    private int size = 0;
    E[] array;

    public HubList() {
        capacity = 1;
        array = (E[]) new Object[capacity];
    }

    public void add(E element) {
        if (size + 1 == capacity) increaseCapacity();

        array[size++] = element;
    }

    private void increaseCapacity() {
        capacity = capacity * 2;
        E[] newArray = (E[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
            array[i] = null;
        }

        array = newArray;
    }

    public E removeAt(int index) {
        E object = array[index];
        array[index] = null;

        System.arraycopy(array, index + 1, array, index, size - index);

        size--;
        return object;
    }

    public void sort(Comparator<? super E> c) {
        for(int i = 0 ; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (0 < c.compare(array[i], array[j])) {
                    E oldEl = array[i];

                    array[i] = array[j];
                    array[j] = oldEl;
                }
            }
        }
    }

    public int find(E element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) return i;
        }

        return -1;
    }

    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(array, 0, size));
    }
}