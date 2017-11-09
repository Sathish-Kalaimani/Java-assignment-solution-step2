package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	
	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		
		queryParameter.setFile(getFile(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setLogicalOperators(LogicalOperators(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setAggregateFunctions(aggr(queryString));
		queryParameter.setRestriction(Rest(queryString));
		
		queryParameter.getLogicalOperators();
				queryParameter.getAggregateFunctions();
		queryParameter.getRestrictions();
		
		
		return queryParameter;
		
	}
	
		public String getFile(String queryString) {
		
			String file = queryString.toLowerCase();
			if (Pattern.matches("(.*where.*)|(.*group by.*)|(.*order by.*)", file)) {
				file = file.split("from")[1].split("where|group|order")[0].trim();
				return file;
			} else {
				file = file.split("from")[1].trim();
				return file;
			}
			}
	
		/*
		 * extract the order by fields from the query string. Please note that we will
		 * need to extract the field(s) after "order by" clause in the query, if at all
		 * the order by clause exists. For eg: select city,winner,team1,team2 from
		 * data/ipl.csv order by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one order by fields.
		 */
		
		public List<String> getOrderByFields(String queryString) {
			
			String o = queryString.toLowerCase().trim();
			if (Pattern.matches("(.*order by.*)", o)) {
				o = o.split("order by")[1].trim();
				List<String> order = new ArrayList<String>(Arrays.asList(o.split(",")));
				return order;
			} else {
				return null;
			}
			}
		
		
		public String getConditionsPartQuery(String queryString) {
			
			String partquery = "";
			if(queryString.contains("where") && queryString.contains("group")) {
				partquery = queryString.substring(queryString.indexOf("where"), queryString.indexOf("group")).replace("where", "").trim();
			}else if(queryString.contains("where") && queryString.contains("order")){
				partquery = queryString.substring(queryString.indexOf("where"),queryString.indexOf("order")).replace("where", "").trim();
			}else if(queryString.contains("where")){
				partquery = queryString.substring(queryString.indexOf("where")).replace("where", "").trim();
			}else {
				return null;
			}
			
			return partquery;
		}
		
		
		
		/*
		 * extract the group by fields from the query string. Please note that we will
		 * need to extract the field(s) after "group by" clause in the query, if at all
		 * the group by clause exists. For eg: select city,max(win_by_runs) from
		 * data/ipl.csv group by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one group by fields.
		 */
		
		public List<String> getGroupByFields(String queryString) {
			
			String groupby = queryString.toLowerCase().trim();
			List<String> group = new ArrayList<String>();
			if (Pattern.matches("(.*group by.*)(.*order by.*)", groupby)) {
				groupby = groupby.split("group by")[1].trim().split("order by")[0].trim();
			} else if (Pattern.matches("(.*group by.*)", groupby)) {
				groupby = groupby.split("group by")[1].trim();
			} else {
				return null;
			}
			group = Arrays.asList(groupby.split(","));
			return group;
		}
		
		/*
		 * extract the selected fields from the query string. Please note that we will
		 * need to extract the field(s) after "select" clause followed by a space from
		 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
		 * query mentioned above, we need to extract "city" and "win_by_runs". Please
		 * note that we might have a field containing name "from_date" or "from_hrs".
		 * Hence, consider this while parsing.
		 */
		
		public List<String> getFields(String queryString) {
			
			String field = queryString.toLowerCase().trim();
			List<String> fields = new ArrayList<String>();
			field = field.toLowerCase();
			field = field.split("select")[1].split("from")[0].trim();
			/*if (field.contains("*")) {
				return ;
			} else {*/
				String[] f = field.split(",");
				for(String s: f) {
					fields.add(s.trim());
				}
				return fields;
		}
		
		/*
		 * extract the conditions from the query string(if exists). for each condition,
		 * we need to capture the following: 
		 * 1. Name of field 
		 * 2. condition 
		 * 3. value
		 * 
		 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
		 * where season >= 2008 or toss_decision != bat
		 * 
		 * here, for the first condition, "season>=2008" we need to capture: 
		 * 1. Name of field: season 
		 * 2. condition: >= 
		 * 3. value: 2008
		 * 
		 * the query might contain multiple conditions separated by OR/AND operators.
		 * Please consider this while parsing the conditions.
		 * 
		 */
		
		public String Conditions(String queryString) {

			String conditions = null;
			//queryString = queryString.toLowerCase();
			if (Pattern.matches("(.*where.*)(.*group.*)(.*order.*)", queryString)) {
				conditions = queryString.split("where")[1].trim().split("group by|order by")[0].trim();
			} else if (Pattern.matches("(.*where.*)", queryString)) {
				conditions = queryString.split("where")[1].trim();
			} else {
				return null;
			}
			
			return conditions;
		}
		
		/*
		 * extract the logical operators(AND/OR) from the query, if at all it is
		 * present. For eg: select city,winner,team1,team2,player_of_match from
		 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
		 * bangalore
		 * 
		 * the query mentioned above in the example should return a List of Strings
		 * containing [or,and]
		 */
		
		public List<String> LogicalOperators(String queryString) {
			
			String operators = Conditions(queryString);
			List<String> operator = new ArrayList<String>();
			if (operators != null) {
				String[] ope = operators.split("\\s+");
				for (String s : ope) {
					if (Pattern.matches("(and)|(or)", s)) {
						operator.add(s);
					}
				}
				return operator;
			} else {
				return null;
			}
		}
		
		/*
		 * extract the aggregate functions from the query. The presence of the aggregate
		 * functions can determined if we have either "min" or "max" or "sum" or "count"
		 * or "avg" followed by opening braces"(" after "select" clause in the query
		 * string. in case it is present, then we will have to extract the same. For
		 * each aggregate functions, we need to know the following: 
		 * 1. type of aggregate function(min/max/count/sum/avg) 
		 * 2. field on which the aggregate function is being applied
		 * 
		 * Please note that more than one aggregate function can be present in a query
		 * 
		 * 
		 */
		
		public String aggregate(String queryString) {
			
			String fields = "";
			if(queryString.isEmpty()) {
				 return null;
			}else {
				fields = queryString.substring(queryString.indexOf("select"), queryString.indexOf("from")).replace("select", "").trim();
			}		
			return fields;
		}
		
		public List<AggregateFunction> aggr(String queryString){
			List<AggregateFunction> aggre = new ArrayList<AggregateFunction>();
			
			String check = aggregate(queryString);
			if(check!=null){
				String[] c = check.split(",");
					for(int i=0;i<c.length;i++) {
						AggregateFunction a = new AggregateFunction();
						a.setField(c[i]);
						a.setFunction(c[i]);
						aggre.add(a);
					}
			}else {
				return null;
			}
			return aggre;
		}
		
		public List<Restriction> Rest(String queryString){
			List<Restriction> restrict = new ArrayList<Restriction>();
			
			String check = Conditions(queryString);
			if(check!=null) {
				String[] c= check.split(",");
				for(int i=0;i<c.length;i++){
					Restriction res = new Restriction();
					res.setPropertyName(c[i]);
					res.setPropertyValue(c[i]);
					res.setCondition(c[i]);
					restrict.add(res);
				}
			}else {
				return null;
			}
			return restrict;
		}
		
}
