package com.daytrader.googlecharts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class DataTable {

	private List<Column> cols;
	private List<Row> rows;
	
	public DataTable(List<Column> cols, List<Row> rows) {
		this.cols = cols;
		this.rows = rows;
	}
	
	public static DataTable testTable() {
		List<Column> columns = buildColumns();
		List<Row> rows = new ArrayList<Row>();
		Row row1 = new Row();
		Cell c11 = new Cell("t1");
		Cell c12 = new Cell("10");
		Cell c13 = new Cell("30");
		Cell c14 = new Cell("50");
		Cell c15 = new Cell("70");
		row1.addCell(c11);
		row1.addCell(c12);
		row1.addCell(c13);
		row1.addCell(c14);
		row1.addCell(c15);
		
		Row row2 = new Row();
		Cell c21 = new Cell("t2");
		Cell c22 = new Cell("20");
		Cell c23 = new Cell("40");
		Cell c24 = new Cell("60");
		Cell c25 = new Cell("80");
		row2.addCell(c21);
		row2.addCell(c22);
		row2.addCell(c23);
		row2.addCell(c24);
		row2.addCell(c25);
		rows.add(row1);
		rows.add(row2);
		
		return new DataTable(columns, rows);
	}
	
	public static List<Column> buildColumns() {
		List<Column> columns = new ArrayList<Column>();
		Column c1 = new Column();
		c1.setId("time");
		c1.setLabel("time");
		c1.setType("string");
		Column c2 = new Column();
		c2.setId("min");
		c2.setLabel("min");
		c2.setType("number");
		Column c3 = new Column();
		c3.setId("open");
		c3.setLabel("open");
		c3.setType("number");
		Column c4 = new Column();
		c4.setId("close");
		c4.setLabel("close");
		c4.setType("number");
		Column c5 = new Column();
		c5.setId("high");
		c5.setLabel("high");
		c5.setType("number");
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		columns.add(c4);
		columns.add(c5);
		return columns;
	}
	
	public static String getJsonTest2() {
		DataTable dt = testTable();
		Gson gson = new Gson();
		String json = gson.toJson(dt);  
		return json;
	}
	
	public static String getJsonTest() {
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>(); 
		int[] v1 = {1, 10, 30, 50, 70};
		int[] v2 = {2, 30, 60, 90, 120};
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		for (int i : v1) {
			l1.add(i);
		}
		for (int i : v2) {
			l2.add(i);
		}
		map.put("Monday", l1);
		map.put("Tuesday", l2);
		Gson gson = new Gson();
		String json = gson.toJson(map);  
		return json;
	}
	
	public static void main(String[] args) {
		DataTable dt = testTable();
		Gson gson = new Gson();
		String json = gson.toJson(dt);  
		System.out.println(json);
		
	}
	
	
}
