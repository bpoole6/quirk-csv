# Quirk CSV
A thread-safe library that allows you to parse and write csv files using java annotations quickly and efficiently!

## Table of Contents
1. [Maven Dependency](https://github.com/bpoole6/quirk-csv#maven-dependency)
2. [Demos](https://github.com/bpoole6/quirk-csv#demos)
3. [Quick Start](https://github.com/bpoole6/quirk-csv#quick-start)
4. [Reading(Parsing) CSV](https://github.com/bpoole6/quirk-csv#reading-csv)
5. [Writing(Generating) CSV](https://github.com/bpoole6/quirk-csv#writing-csv)
6. [Wrappers for handling Non-primitive types](https://github.com/bpoole6/quirk-csv#wrappers-handling-non-primitive-types)
7. [Inheritance](https://github.com/bpoole6/quirk-csv#inheritance)
8. [Extending Apache commons-csv](https://github.com/bpoole6/quirk-csv#extending-commons-csv)

## Maven Dependency
````
<dependency>
  <groupId>io.github.bpoole6</groupId>
  <artifactId>quirk-csv</artifactId>
  <version>1.0.0</version>
</dependency>
````

## DEMOS

### [ORDER](https://github.com/bpoole6/quirk-csv/tree/master/src/main/java/com/quirk/demo/ordersimple) | [NAMED](https://github.com/bpoole6/quirk-csv/tree/master/src/main/java/com/quirk/demo/namedsimple) | [INHERITANCE](https://github.com/bpoole6/quirk-csv/tree/master/src/main/java/com/quirk/demo/inheritance) | [WRAPPER](https://github.com/bpoole6/quirk-csv/tree/master/src/main/java/com/quirk/demo/wrapper) | [LARGE DATA PROCESSING](https://github.com/bpoole6/quirk-csv/tree/master/src/main/java/com/quirk/demo/bigprocessing)

## Quick Start

#### POJO
````
@CSVReadComponent(type = CSVType.NAMED)
@CSVWriteComponent(type = CSVType.ORDER)
public class Pojo {
	@CSVWriteBinding(order = 0)
	private String name;

	@CSVWriteBinding(order = 1)
	@CSVReadBinding(header = "age")
	private Integer age;

	@CSVWriteBinding(order = 2)
	@CSVReadBinding(header = "money")
	private Double money;

	@CSVReadBinding(header = "name")
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
````

#### Main

````
import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;


public class SimpleMain {
    public static void main(String[] args) {
        String csv = "name,age,money" + System.lineSeparator() + "Michael Williams,34,39332.15";

        CSVProcessor processor = new CSVProcessor(Pojo.class);
        List<Pojo> list = new ArrayList<>();
        try {
            list.addAll(processor.parse(new StringReader(csv)));
			list.forEach(System.out::println);

            System.out.println();

            StringWriter sw = new StringWriter();
            processor.write(list, sw);
			System.out.println(sw.toString());
        } catch (IOException e) {
        }


    }
}

````
## Reading CSV

There are two annotations you'll use when parsing a csv [@CSVReadComponent](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVReadComponent.java) and [@CSVReadBinding](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVReadBinding.java).
A CSV is either header based or order based. 


If you are parsing a header based then you would set the [CSVType](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVType.java)
 to NAMED and [@CSVReadBinding](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVReadBinding.java) will need the header field set to the header it'll get its value from.
#### Example
````
import com.quirk.csv.annotation.*;

@CSVReadComponent(type = CSVType.NAMED)
public class Pojo {
	private String name;

	@CSVReadBinding(header = "age")
	private Integer age;

	@CSVReadBinding(header = "money")
	private Double money;

	@CSVReadBinding(header = "name")
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

    public static void main(String[] args) {
		final String csv = "Marvin Nowell,34,20000.32" + System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class);

		try {
			//Reading
			List<Pojo> list = processor.parse(new StringReader(csv));
			list.forEach(System.out::println);

		} catch (IOException e) {
		}

	}
}
````

## Writing CSV
There are two annotations you'll use when writing a csv [@CSVWriteComponent](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVWriteComponent.java) and [CSVWriteBinding](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/annotation/CSVWriteBinding.java)
#### Example
Write an ORDER based csv file. Meaning the CSV will not have headers when generated.
````
import com.quirk.csv.annotation.*;
import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@CSVWriteComponent(type = CSVType.ORDER)
public class Pojo {

	public Pojo(String name, Integer age, Double money) {
		this.name = name;
		this.age = age;
		this.money = money;
	}

	private String name;

	@CSVWriteBinding(order = 2)
	private Integer age;

	@CSVWriteBinding(order = 1)
	private Double money;


	@CSVWriteBinding(order = 3)
	public String getName() {
		return name;
	}
	
	public static void main(String[] args) {
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class);
		try {

			List<Pojo> list = Arrays.asList(new Pojo("Dillian Lamour",22,340.70));
			//Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);
			System.out.println(sw.toString());
		} catch (IOException e) {
		}

	}
}
````

## Wrappers Handling Non-primitive types
To handle parsing and reading non-primitive types, use [ReadWrapper](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/wrappers/read/ReadWrapper.java) when parsering a csv and [WriteWrapper](https://github.com/bpoole6/quirk-csv/blob/master/src/main/java/com/quirk/csv/wrappers/write/WriteWrapper.java) when writing.

#### Example
````
import com.quirk.csv.annotation.*;
import com.quirk.csv.processor.CSVProcessor;
import com.quirk.csv.wrappers.read.ReadWrapper;
import com.quirk.csv.wrappers.write.WriteWrapper;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CSVReadComponent(type = CSVType.ORDER)
@CSVWriteComponent(type = CSVType.NAMED, namedIsOrdered = true)
public class Pojo {

    @CSVReadBinding(order = 1, wrapper = LocalDateReadWrapper.class) //Specifying
    private LocalDate dob;

    @CSVWriteBinding(order = 0, header = "id")
    public LocalDate getDob() {
        return dob;
    }

    @Override
    public String toString() {
        return "Date Of Birth: " + dob + System.lineSeparator();
    }

    public static class LocalDateReadWrapper implements ReadWrapper<LocalDate> {

        @Override
        public LocalDate apply(String str) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
            try {
                return LocalDate.parse(str, df);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static class LocalDateWriteWrapper implements WriteWrapper<LocalDate> {
        @Override
        public String apply(LocalDate ld) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
            return df.format(ld);
        }
    }

    public static void main(String[] args) {
        final String csv = "Marvin Nowell,19820511,234" + System.lineSeparator() + "Dillian Lamour,19960111,21";
        Map<Class, WriteWrapper> m = new HashMap<>();
        m.put(LocalDate.class, new LocalDateWriteWrapper());
        CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class, new HashMap<>(),m);
        try {

            List<Pojo> list = processor.parse(new StringReader(csv), CSVFormat.DEFAULT);
            list.forEach(System.out::println);

            System.out.println();

            StringWriter sw = new StringWriter();
            processor.write(list,sw);

        } catch (IOException e) {
        }

    }
}
````

## Inheritance
This is used when trying to utilize a parent class' annotations.

#### Example
````
import com.quirk.csv.annotation.*;
import com.quirk.csv.processor.CSVProcessor;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@CSVReadComponent(type = CSVType.ORDER)
@CSVWriteComponent(type = CSVType.NAMED)
public class PojoParent {
	@CSVWriteBinding(header = "name")
	private String name;

	@CSVReadBinding(order = 0)
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name;
	}

	// Make sure inheritSuper is TRUE
	@CSVReadComponent(type = CSVType.ORDER, inheritSuper = true)
	@CSVWriteComponent(type = CSVType.NAMED, inheritSuper = true)
	public static class PojoChild extends PojoParent {
		@CSVReadBinding(order = 1)
		@CSVWriteBinding(header = "nationality")
		private String nationality;

		@Override
		public String toString() {

			return super.toString() + System.lineSeparator() + "\tNationality: " + nationality;
		}
	}

	public static void main(String[] args) throws NoSuchMethodException, IOException {
		final String csv = "Marvin Nowell|USA" + System.lineSeparator() + "Dillian Lamour|France";
		CSVProcessor<PojoChild> processor = new CSVProcessor(PojoChild.class);
		try {
			//Reading
			CSVFormat format = CSVFormat.DEFAULT.withDelimiter('|');
			List<PojoChild> list = processor.parse(new StringReader(csv),format);
			list.forEach(System.out::println);

			System.out.println();

			//Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);
			System.out.println(sw.toString());
		} catch (IOException e) {
		}


	}
}
````

## CSVFormat

## Extending commons-csv
This project is built on top of org.apache.commons:commons-csv and the parser allows you to pass in as an argument [CSVFormat](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html). This allows you to specify things such as if your csv file has a header, the delimiter exists as a possible value, and/or etc.
For example if you had a csv that was pipe "|" delimited you'd modified the CSVFormat to include it.
````
CSVFormat format = CSVFormat.DEFAULT.withDelimiter('|');
List<PojoChild> list = processor.parse(new StringReader(csv),format);
````

## Author
[Austin Poole](http://software-poole.com/) Java Developer