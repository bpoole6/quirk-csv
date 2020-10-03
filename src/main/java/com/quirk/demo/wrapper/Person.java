package com.quirk.demo.wrapper;

public class Person {
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Person{id=" + id +"}";
	}
}
