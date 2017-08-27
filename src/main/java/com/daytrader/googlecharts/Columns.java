package com.daytrader.googlecharts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Columns {

	List<Column> cols = null;
	
	public Columns() {
		cols = buildColumns();
	}
	
	public List<Column> buildColumns() {
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
	
	public static void main(String[] args) {
		Columns columns = new Columns();
		Gson gson = new Gson();
		String json = gson.toJson(columns);  
		System.out.println(json);
	}
}
