package com.quirk.demo.wrapper;

import com.quirk.csv.wrappers.read.ReadWrapper;

public class PersonReadWrapper implements ReadWrapper<Person> {

	@Override
	public Person apply(String str) {
		Person person = new Person();
		person.setId(Long.valueOf(str));
		return person;
	}

}
