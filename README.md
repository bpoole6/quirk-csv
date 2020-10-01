# CSV-Parser
Allows you to parse your csv using java annotations!

To see some quick [demos](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/poole/demo)
## DEMOS

### [ORDER](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/poole/demo/ordersimple)

### [NAMED](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/poole/demo/namedsimple)

### [INHERITANCE](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/poole/demo/inheritance)

### [WRAPPER](https://github.com/bpoole6/CSV-Parser/tree/master/src/main/java/com/poole/demo/readWrapper)
By default only the primitives(wrappers included) and enums will be handled. You can see this in the [DefaultWrapper](https://github.com/bpoole6/CSV-Parser/blob/master/src/main/java/com/poole/csv/wrappers/defaults/DefaultWrappers.java) class.

## Simple example

POJO:
```
@CSVReadComponent(type = CSVReaderType.ORDER)
public class Pojo {
	private String name;

	@CSVReadBinding(order = 1)
	private Integer age;

	@CSVReadBinding(order = 2)
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
public class SimpleMain {
	public static void main(String[] args) {
		final String csv = "Marvin Nowell,34,20000.32" + System.lineSeparator() + "Dillian Lamour,22,2499";
		CSVProcessor processor = new CSVProcessor();
		List<Pojo> list = new ArrayList<>();
		try {
			list.addAll(processor.parse(new StringReader(csv), Pojo.class));
		} catch (IOException e) {
		}
		list.forEach(System.out::println);
	}
}
```

### Extending commons-csv
This project is built on top of org.apache.commons:commons-csv and the parser allows you to pass in as an arguement [CSVFormat](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html). This allows you to specify things such as if your csv file has a header, the delimiter exists as a possible value, and/or etc.

## Author
[Austin Poole](http://software-poole.com/) Java Developer

