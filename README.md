# CSV-Parser
Allows you to parse your csv using java annotations quickly and efficiently!

To see some quick [demos](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo)
## DEMOS

### [ORDER](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo/ordersimple)

### [NAMED](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo/namedsimple)

### [INHERITANCE](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo/inheritance)

### [WRAPPER](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo/wrapper)

### [LARGE DATA PROCESSING](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/quirk/demo/bigprocessing)

## Simple example

POJO:
```
import com.quirk.csv.annotation.*;

@CSVReadComponent(type = CSVType.ORDER)
@CSVWriteComponent(type = CSVType.ORDER)
public class Pojo {
	@CSVWriteBinding(order = 3)
	private String name;

	@CSVReadBinding(order = 1)
	@CSVWriteBinding(order = 2)
	private Integer age;

	@CSVReadBinding(order = 2)
	@CSVWriteBinding(order = 1)
	private Double money;

	@CSVReadBinding(order = 0)
	public void setA(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return "Name: " + name + System.lineSeparator() + "\tAge: " + age + System.lineSeparator() + "\tMoney: "
				+ money;
	}

}
```

The Main:
```
import com.quirk.csv.processor.CSVProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,34,20000.32" + System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor<Pojo> processor = new CSVProcessor<>(Pojo.class);

		try {
			//Reading
			List<Pojo> list = processor.parse(new StringReader(csv));
			list.forEach(System.out::println);

			System.out.println();

			//Writing
			StringWriter sw = new StringWriter();
			processor.write(list,sw);

		} catch (IOException e) {
		}

	}
}

```
The above example is as simple as that. There are some more advance examples like if you have non-primitive types.



 

### Extending commons-csv
This project is built on top of org.apache.commons:commons-csv and the parser allows you to pass in as an arguement [CSVFormat](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html). This allows you to specify things such as if your csv file has a header, the delimiter exists as a possible value, and/or etc.

## Author
[Austin Poole](http://software-poole.com/) Java Developer

