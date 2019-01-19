package com.poole.demo.wrapper;

import com.poole.csv.wrappers.Wrapper;

public class PersonWrapper implements Wrapper {

	@Override
	public Object apply(String str) {
		Person person = new Person();
		person.setId(Long.valueOf(str));
		return person;
	}

}
