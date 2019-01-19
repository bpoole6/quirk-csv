package com.poole.demo.wrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.poole.csv.wrappers.Wrapper;

public class LocalDateWrapper implements Wrapper {

	@Override
	public Object apply(String str) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		try {
			return LocalDate.parse(str, df);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}
