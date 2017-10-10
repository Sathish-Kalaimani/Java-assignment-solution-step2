package com.stackroute.datamunger;

import java.util.Scanner;

import com.stackroute.datamunger.query.parser.QueryParser;

public class DataMunger {

	public static void main(String[] args) {

		//read the query from the user
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the query: ");
		String queryString = scan.nextLine();
		scan.close();
				
		//create an object of QueryParser class
		QueryParser parser = new QueryParser();
		
		/*
		 * call parseQuery() method of the class by passing the query string which will
		 * return object of QueryParameter
		 */
		parser.parseQuery(queryString);
		

	}

	

}
