package com.daytrader.strategy;

import java.time.Duration;
import java.time.ZonedDateTime;


// PYDS 08/31/2017 it should trigger 8:35 am



public class ContinuationBuyTime {

	private int index;
	private ZonedDateTime time;
	private double accumulationVolume;
	double delta;
   	double delta2;
   	double delta3;
   	int tickTen;
	double cpAtI;
	double ema8AtI;
   	double macdAtT;
   	double macdAvgAtT;
   	double rsiAtI;

	public ContinuationBuyTime(int index) {
		this.index = -1;
	}
	
	public ContinuationBuyTime(int index, ZonedDateTime time, double accumulationVolume, 
			double delta, double delta2, double delta3, int tickTen, double cpAtI,
			double ema8AtI, double macdAtT, double macdAvgAtT, double rsiAtI) {
		this.index = index;
		this.time = time.minus(Duration.ofHours(5));
		this.accumulationVolume = accumulationVolume;
		this.delta = delta;
		this.delta2 = delta2;
		this.delta3 = delta3;
		this.tickTen = tickTen;
		this.cpAtI = cpAtI;
		this.ema8AtI = ema8AtI;
		this.macdAtT = macdAtT;
		this.macdAvgAtT = macdAvgAtT;
		this.rsiAtI = rsiAtI;
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
		sb.append("deltas : " + delta + " ");;
		sb.append(delta2 + " ");
		sb.append(delta3 + " ");
		sb.append(System.lineSeparator());
		sb.append("tickTen " + tickTen);
		sb.append(System.lineSeparator());
		sb.append("cpAtI " + cpAtI + " ");
		sb.append("ema8AtI " + ema8AtI + " ");
		sb.append("macdAtT " + macdAtT + " ");
		sb.append("macdAvgAtT " + macdAvgAtT + " ");
		sb.append("rsiAtI " + rsiAtI + " ");
		return sb.toString();
	}
	
}
