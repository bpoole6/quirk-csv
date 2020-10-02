package com.quirk.demo.wrapper;

import com.quirk.csv.annotation.*;

import java.time.LocalDate;

@CSVReadComponent(type = CSVType.ORDER)
@CSVWriteComponent(type = CSVType.NAMED, namedIsOrdered = true)
public class Pojo {
    @CSVReadBinding(order = 0)
    @CSVWriteBinding(order = 0, header = "name")
    private String name;

    @CSVReadBinding(order = 1, wrapper = LocalDateReadWrapper.class)
    @CSVWriteBinding(order = 0, header = "dob") // Without specifying a wrapper the toString will be called
    private LocalDate dob;

    @CSVReadBinding(order = 2)
    private Person person;

    @Override
    public String toString() {

        return "Name: " + name + System.lineSeparator() + "\tDate Of Birth: " + dob + System.lineSeparator() + "\tId: "
                + person.getId();
    }

    @CSVWriteBinding(order = 0, header = "id", wrapper = PersonWriteWrapper.class)
    public Person getPerson() {
        return person;
    }
}
