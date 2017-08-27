package com.daytrader.googlecharts;

import java.util.ArrayList;
import java.util.List;

public class Row {
	
	List<Cell> c;
	
	public Row() {
		c = new ArrayList<Cell>();
	}
	
	public Row(List<Cell> cells) {
		c = cells;
	}
	
	public void setC(List<Cell> cells) {
		c = cells;
	}
	
	public void addCell(Cell cell) {
		c.add(cell);
	}

}
