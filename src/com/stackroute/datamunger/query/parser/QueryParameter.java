package com.stackroute.datamunger.query.parser;

import java.util.List;
/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {
	
	private String file;
	private List<String>Logical;
	private List<String>fields;
	private List<String>groupby;
	private List<String>orderby;
	private List<AggregateFunction> aggregate;
	private List<Restriction> Restriction;
	
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String fname) {
		file = fname;
	}
	
	public List<Restriction> getRestrictions() {
		return Restriction;
	}
	
	public void setRestriction(List<Restriction> rest) {
		Restriction = rest;
	}
	
	public List<String> getLogicalOperators() {
		return Logical;
	}
	
	public void setLogicalOperators(List<String> logic) {
		Logical = logic;
	}
	
	public List<String> getFields() {
		return fields;
	}
	
	public void setFields(List<String> field) {
		fields = field;
	}
	
	public List<AggregateFunction> getAggregateFunctions() {
		return aggregate;
	}
	
	public void setAggregateFunctions(List<AggregateFunction> agg) {
		aggregate = agg;
	}
	
	public List<String> getGroupByFields() {
		return groupby;
	}
	
	public void setGroupByFields(List<String> group) {
		groupby = group;
	}
	
	public List<String> getOrderByFields() {
		return orderby;
	}
	
	public void setOrderByFields(List<String> order) {
		orderby = order;
	}
	
}
