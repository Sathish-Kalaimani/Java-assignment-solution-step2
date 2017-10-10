package com.stackroute.datamunger.query.parser;

/*
 * This class is used for storing name of field, condition and value for 
 * each conditions
 * */
public class Restriction {
	
	private String colname;
	private String value;
	private String condition;
	
	public String getPropertyName() {
		return colname;
	}
	
	public void setPropertyName(String n) {
		colname = n;
	}
	
	public String getPropertyValue() {
		return value;
	}
	
	public void setPropertyValue(String val) {
		value = val;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String con) {
		condition = con;
	}

}
