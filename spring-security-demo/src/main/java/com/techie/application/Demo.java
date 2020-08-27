package com.techie.application;

import java.util.Date;

public class Demo {

	public static void main(String[] args) {
		String s1 = "Hello";
		String s2 = "Hello";

		System.out.println(s1.equals(s2));

		Order o1 = new Order();

		o1.setDescription("This product is good");
		o1.setOrderDate(new Date());
		o1.setOrderId(12);
		o1.setOrderName("Flipkart");
		o1.setOrderNumber("s1221e3");

		Order o2 = new Order();

		o1.setDescription("This product is good");
		o1.setOrderDate(new Date());
		o1.setOrderId(12);
		o1.setOrderName("Flipkart");
		o1.setOrderNumber("s1221e3");

		System.out.println("Order Equality Check :: " + o1.equals(o2));

	}
}
