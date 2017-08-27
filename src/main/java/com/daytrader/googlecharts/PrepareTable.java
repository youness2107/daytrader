package com.daytrader.googlecharts;

import com.google.gson.JsonObject;

public class PrepareTable {

	public static String prepareTable(String jsonCols, String jsonRows) {
		
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("cols", jsonCols);
		innerObject.addProperty("rows", jsonRows);
		return innerObject.toString();
	}
	
//	public static String prepareTestTable() {
//		
//	}
//	
	
}
