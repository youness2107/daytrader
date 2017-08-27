package com.daytrader.prices;

import java.time.Duration;
import java.time.ZonedDateTime;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;

/**
 * Creates 2,3,4 ... time series based on 1 minute time series
 * 
 * @author youne
 *
 */
public class TickAggregation {
	
	// based on 1 minute series 
	// produces x minute series
	public static TimeSeries getAggregatedIntradaySeries(TimeSeries minuteSeries, int intraday) {
		if (intraday < 1 || intraday > 60) {
			throw new IllegalArgumentException("intraday value " + intraday + " will not be "
					+ "handled. Values should be in the range of [1,60]");
		}
		if (intraday == 1) {
			return minuteSeries;
		}
		int tickCount = minuteSeries.getTickCount();
		if (tickCount < 60) {
			throw new IllegalArgumentException("1 minute data appears to be incomplete and have only " + tickCount + " ticks");
		}
		
		TimeSeries aggregatedSeries = new TimeSeries();
		int seriesCount = minuteSeries.getTickCount(); 
		for (int i = 0; i < seriesCount; i += intraday) {
			if ((i + intraday - 1) < seriesCount) {
				aggregatedSeries.addTick(aggregateNTicks(minuteSeries, i, intraday));
			}
		}
		return aggregatedSeries;
	}
	
	private static Tick aggregateNTicks(TimeSeries minuteSeries, int startTick, int count) {
		// get open high low close. average the volumes
		// open is open of first close is close of last
		int endIndex = startTick + count - 1;
		Decimal open = minuteSeries.getTick(startTick).getOpenPrice();
		Decimal close = minuteSeries.getTick(endIndex).getClosePrice();
		// high is max of highs and low is min of lows
		Decimal high = minuteSeries.getTick(startTick).getMaxPrice();
		Decimal low = minuteSeries.getTick(startTick).getMinPrice();
		Decimal totalVolume = minuteSeries.getTick(startTick).getVolume();
		for (int i = startTick + 1; i < (startTick + count); i++) {
			Tick currentTick = minuteSeries.getTick(i);
			if (high.isLessThan(currentTick.getMaxPrice())) {
				high = currentTick.getMaxPrice();
			}
			if (low.isGreaterThan(currentTick.getMinPrice())) {
				low = currentTick.getMinPrice();
			}
			totalVolume = totalVolume.plus(currentTick.getVolume());
		}
		Decimal avgVolume = totalVolume.dividedBy(Decimal.valueOf(count));
		ZonedDateTime endTime = minuteSeries.getTick(endIndex).getEndTime();
		Tick aggregatedTick = new Tick(Duration.ofMinutes(count), endTime, open, high, low, close, avgVolume);
		return aggregatedTick;
	}

}
