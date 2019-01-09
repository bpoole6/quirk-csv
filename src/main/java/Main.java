import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.poole.csv.annotation.CSVComponent;
import com.poole.csv.annotation.CSVNumberOrder;
import com.poole.csv.annotation.CSVReaderType;
import com.poole.csv.processor.CSVProcessor;
@CSVComponent(type = CSVReaderType.ORDER)
public class Main {
	@CSVNumberOrder(order = 0)
	public String blah;
	public Integer b;
	@CSVNumberOrder(order = 2)
	public Double c;
	//TODO Fix Exeception handling
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, FileNotFoundException, IOException {
		CSVProcessor p=new CSVProcessor();
		System.out.println(p.parse( "f.txt", Main.class));
		Object obj=Main.class.newInstance();
		System.out.println(Main.class.getDeclaredFields()[0].getType()==int.class);
		Main.class.getDeclaredFields()[0].set(obj, "");
		System.out.println(Main.class.getDeclaredMethods()[2].getParameterCount());
		//Main.class.getDeclaredMethods()[1].invoke(obj, 5)
		System.out.println(obj);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return blah+"-"+b+"-"+c;
	}
	@CSVNumberOrder(order = 1)
	public void setB(Integer b) {
		this.b = b;
	}
	
	
}
