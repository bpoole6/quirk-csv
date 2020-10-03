package com.quirk.demo.threadsafe;

import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * This is to show just how fast processing can occur for reading then writing the csv. Granted all this will be in memory.
 * Reading and writing to disk would incur additional time
 */
public class SimpleMain {
    public static void main(String[] args) throws InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        CSVProcessor processor = new CSVProcessor(Pojo.class);
        String header = "name,age,money";
        IntStream.range(0,4).forEach(i -> {
            StringJoiner sj = new StringJoiner(System.lineSeparator());
            sj.add(header);
            IntStream.range(0,500_000).boxed().map(j->produceRow()).forEach(sj::add);
            sj.add(System.lineSeparator());
            callables.add(new Caller(processor,sj.toString()));
        });
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.invokeAll(callables);
        es.shutdown();
    }

    public static class Caller implements Callable<String> {
        final CSVProcessor<Pojo> processor ;
        final String csv;
        public Caller(CSVProcessor<Pojo> processor, String csv) {
            this.processor = processor;
            this.csv = csv;
        }

        @Override
        public String call() {
            List<Pojo> list = new ArrayList<>();
            try {
                System.out.println(String.format("Thread %s has %s Bytes to be processed",Thread.currentThread().getName(),(int)(csv.getBytes().length)));
                long startRead = System.currentTimeMillis();
                list.addAll(processor.parse(new StringReader(csv)));
//			list.forEach(System.out::println);
                System.out.println(String.format("Thread %s. Read Time elapse milli: %.3f seconds",Thread.currentThread().getName(), (System.currentTimeMillis() - startRead) / 1000.0));

                System.out.println();

                long startWrite = System.currentTimeMillis();
                StringWriter sw = new StringWriter();
                processor.write(list, sw);
                System.out.println(String.format("Thread %s. Write Time elapse milli: %.3f seconds", Thread.currentThread().getName(), (System.currentTimeMillis() - startWrite) / 1000.0));
                if(sw.toString().equals(csv)){
                    throw new RuntimeException(String.format("Thread %s failed",Thread.currentThread().getName()));
                }
            } catch (IOException e) {
            }
            return null;
        }
    }
    static String produceRow() {
        List<String> lastNames = Arrays.asList("Smith", "Jones", "Johnson", "Lee",
                "Brown", "Williams", "Rodriguez", "Garcia", "Lopez", "Brooks", "Poole", "Manchester", "Dinkins", "Chang", "Lin");

        List<String> firstNames = Arrays.asList("John", "David", "Michael", "Chris",
                "Mike", "Mark", "Paul", "Daniel", "James", "Maria", "Dillian", "Bryan",
                "Austin", "Ben", "Tim", "Rick", "Taylor", "Matt", "Chris");

        Random r = new Random();
        String name = String.format("%s %s", firstNames.get(r.nextInt(firstNames.size())), lastNames.get(r.nextInt(lastNames.size())));
        int age = r.nextInt(100) + 1;
        double money = r.nextDouble() * 1000000 + 1;

        return String.format("%s,%s,%.2f", name, age, money);
    }
}
