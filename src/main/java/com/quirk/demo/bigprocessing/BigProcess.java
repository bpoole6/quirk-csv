package com.quirk.demo.bigprocessing;

import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * This is to show just how fast processing can occur for reading then writing the csv. Granted all this will be in memory.
 * Reading and writing to disk would incur additional time
 */
public class BigProcess {
    public static void main(String[] args) {
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (int i = 0; i < 1_000_000; i++) {
            sj.add(produceRow());
        }
        String csv = "name,age,money" + System.lineSeparator() + sj.toString();

        CSVProcessor processor = new CSVProcessor(Pojo.class);
        List<Pojo> list = new ArrayList<>();
        try {
            System.out.println(((int)(csv.getBytes().length/1024/1024))+" Mb to be processed");
            long startRead = System.currentTimeMillis();
            list.addAll(processor.parse(new StringReader(csv)));
//			list.forEach(System.out::println);
            System.out.println(String.format("Read Time elapse milli: %.3f seconds", (System.currentTimeMillis() - startRead) / 1000.0));

            System.out.println();

            long startWrite = System.currentTimeMillis();
            StringWriter sw = new StringWriter();
            processor.write(list, sw);
            System.out.println(String.format("Write Time elapse milli: %.3f seconds", (System.currentTimeMillis() - startWrite) / 1000.0));
//			System.out.println(sw.toString());
        } catch (IOException e) {
        }


    }

    static String produceRow() {
        List<String> lastNames = Arrays.asList("Smith", "Jones", "Johnson", "Lee", "Brown", "Williams", "Rodriguez", "Garcia", "Lopez", "Brooks", "Poole", "Manchester", "Dinkins", "Chang", "Lin");
        List<String> firstNames = Arrays.asList("John", "David", "Michael", "Chris", "Mike", "Mark", "Paul", "Daniel", "James", "Maria", "Dillian", "Bryan", "Austin", "Ben", "Tim", "Rick", "Taylor", "Matt", "Chris");
        Random r = new Random();
        String name = String.format("%s %s", firstNames.get(r.nextInt(firstNames.size())), lastNames.get(r.nextInt(lastNames.size())));
        int age = r.nextInt(100) + 1;
        double money = r.nextDouble() * 1000000 + 1;

        return String.format("%s,%s,%.2f", name, age, money);
    }
}