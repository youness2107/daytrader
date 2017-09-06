package com.daytrader.strategy;

import java.time.Duration;
import java.time.ZonedDateTime;

public class BuyTime {
	
	private int index;
	private ZonedDateTime time;
	private double accumulationVolume;
	private int t;
	private int t1;
	private int t2;
	private int t3;
//	private int t4;
	private double d;
	private double d1;
	private double d2;
	private double d3;
//	private double d4;
	
	public BuyTime(int index) {
		this.index = -1;
	}
	
	public BuyTime(int index, ZonedDateTime time, double accumulationVolume, 
			int t, int t1, int t2, int t3,
			double d, double d1, double d2, double d3) {
		this.index = index;
		this.time = time.minus(Duration.ofHours(5));
		this.accumulationVolume = accumulationVolume;
		this.t = t;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
//		this.t4 = t4;
		this.d = d;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
//		this.d4 = d4;
	}
	
	public String toString() {
		if (index == -1) {
			return "No Match";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("index: " + index);
		sb.append(System.lineSeparator());
		sb.append("time: " + time.toString());
		sb.append(System.lineSeparator());
		sb.append("accumulationVolume " + accumulationVolume);
		sb.append(System.lineSeparator());
		sb.append("3min ticks : " + t + " ");;
		sb.append(t1 + " ");
		sb.append(t2 + " ");
		sb.append(t3 + " ");
//		sb.append(t4 + " ");
		sb.append(System.lineSeparator());
		sb.append("3min delta macd and macd average ");
		sb.append(d + " ");
		sb.append(d1 + " ");
		sb.append(d2 + " ");
		sb.append(d3 + " ");
//		sb.append(d4 + " ");
		return sb.toString();
	}
	
}