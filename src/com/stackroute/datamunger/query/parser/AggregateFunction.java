package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * */
public class AggregateFunction {
	
	private String field;
	private String function;
	
	public String getField() {
		return field;
	}
	
	public void setField(String fields) {
		field = fields;
	}
	
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String func) {
		function = func;
	}
}
