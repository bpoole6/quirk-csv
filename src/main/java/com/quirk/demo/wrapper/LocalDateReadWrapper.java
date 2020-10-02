package com.quirk.demo.wrapper;

import com.quirk.csv.wrappers.read.ReadWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateReadWrapper implements ReadWrapper<LocalDate> {

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
