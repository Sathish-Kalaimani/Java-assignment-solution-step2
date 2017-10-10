package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	
	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		
		queryParameter.setFile(FileName(queryString));
		queryParameter.setFields(Fields(queryString));
		queryParameter.setLogicalOperators(LogicalOperators(queryString));
		queryParameter.setGroupByFields(GroupByFields(queryString));
		queryParameter.setOrderByFields(OrderByFields(queryString));
		queryParameter.setAggregateFunctions(aggr(queryString));
		queryParameter.setRestriction(Rest(queryString));
		
		queryParameter.getFile();
		queryParameter.getFields();
		queryParameter.getLogicalOperators();
		queryParameter.getGroupByFields();
		queryParameter.getOrderByFields();
		queryParameter.getAggregateFunctions();
		queryParameter.getRestrictions();
		
		
		return queryParameter;
		
	}
	
		public String FileName(String queryString) {
		
				String filename=null;
				if(queryString.isEmpty()) {
					return null;
				}else {
					if(queryString.contains("where")) {
						filename = queryString.substring(queryString.indexOf("from "), queryString.indexOf(" where")).replace("from", "").trim();
					}else if(queryString.contains("order")){
						filename = queryString.substring(queryString.indexOf("from "),queryString.indexOf("order")).replace("from", "").trim();
					}else {
					
						filename = queryString.substring(queryString.indexOf("from ")).replace("from", "").trim();
					}
				}
		
				return filename.toLowerCase();
			}
	
		/*
		 * extract the order by fields from the query string. Please note that we will
		 * need to extract the field(s) after "order by" clause in the query, if at all
		 * the order by clause exists. For eg: select city,winner,team1,team2 from
		 * data/ipl.csv order by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one order by fields.
		 */
		
		public List<String> OrderByFields(String queryString) {
			
			String[] oby = null;
			List<String>orderby = new ArrayList<String>();
			if(queryString.isEmpty()) {
				return null;
				
			}else if(queryString.contains("order")) {
				String field = queryString.substring(queryString.indexOf("order by ")).replace("order by ", "");
				oby = field.split(" ");
			}else {
				return null;
			}
			orderby = Arrays.asList(oby);
			return orderby;
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
		
		public List<String> GroupByFields(String queryString) {
			
			String[] gby = null;
			String field ="";
			if(queryString.isEmpty()) {
				return null;
				
			}else if(queryString.contains("group by") && queryString.contains("order by") ){
				field = queryString.substring(queryString.indexOf("group by "),queryString.indexOf("order")).replace("group by ", "").trim();
				gby = field.split(" ");
			}else if(queryString.contains("group")){
				field = queryString.substring(queryString.indexOf("group by ")).replace("group by ", "");
				gby = field.split(" ");
			}else {
				return null;
			}
			List<String>groupby = Arrays.asList(gby);
			return groupby;
		}
		
		/*
		 * extract the selected fields from the query string. Please note that we will
		 * need to extract the field(s) after "select" clause followed by a space from
		 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
		 * query mentioned above, we need to extract "city" and "win_by_runs". Please
		 * note that we might have a field containing name "from_date" or "from_hrs".
		 * Hence, consider this while parsing.
		 */
		
		public List<String> Fields(String queryString) {
			
			String[] f = null;
			if(queryString.isEmpty()) {
				 return null;
			}else {
				String field = queryString.substring(queryString.indexOf("select"), queryString.indexOf("from")).replace("select", "").trim();
				f = field.split(",");
				for(int i=0;i<f.length;i++) {
					f[i] = f[i].trim();
				}
			}	
			List<String>fields = Arrays.asList(f);
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

			String con = getConditionsPartQuery(queryString);
			
			if(con==null) {
				con= null;
			}else if(con.contains("and") && con.contains("or")) {
				con = con.replace(" and ", ",").trim().replace(" or ", ",").trim();
			}else if(con.contains("and")) {
				con = con.replace(" and ", ",").trim();
			}else if(con.contains("or")) {
				con = con.replace(" or ", ",").trim();
			}else {
				return con;
			}
			return con;
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
			
			String Cons = getConditionsPartQuery(queryString);
			ArrayList<String> list = null;
			if(Cons!=null) {
				Cons = Cons.trim();
				String[] c = Cons.split(" ");
				list = new ArrayList<String>();
				for (int i=0;i<c.length;i++) {
					if(c[i].equals("and") || c[i].equals("or")){
						list.add(c[i]);
					}
				}
			}else {
				return null;
			}		
			return list;

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
