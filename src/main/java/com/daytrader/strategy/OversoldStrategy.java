package com.daytrader.strategy;


import java.time.ZonedDateTime;
import java.util.List;

import com.daytrader.prices.AlphaVantageStockPrice;
import com.daytrader.prices.TickAggregation;
import com.daytrader.prices.TickDTO;

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.DoubleEMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SmoothedRSIIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;

public class OversoldStrategy {
	
	
	public static TimeSeries getTimeSeries(String ticker, int intraday, String date) {
		String jsonStr = AlphaVantageStockPrice.getStock1minData(ticker);
		List<TickDTO> tickDTOs = AlphaVantageStockPrice.parseIntradayJSONData(jsonStr, date); 
		TimeSeries series = new TimeSeries();
		for (TickDTO tickDTO : tickDTOs) {
			Tick tick = new Tick(tickDTO.getTimePeriod(), tickDTO.getEndTime(), 
					tickDTO.getOpenPrice(), tickDTO.getMaxPrice(), tickDTO.getMinPrice(), 
					tickDTO.getClosePrice(), tickDTO.getVolume());
			series.addTick(tick);
		}
		if (intraday == 1) {
			return series;
		}
		series = TickAggregation.getAggregatedIntradaySeries(series, intraday);
		return series;
	}
	
	public static BuyTime detectStrategy(String ticker, String date) {
		TimeSeries ts1min = getTimeSeries(ticker, 1, date);
		TimeSeries ts3min = TickAggregation.getAggregatedIntradaySeries(ts1min, 3);
		TimeSeries ts5min = TickAggregation.getAggregatedIntradaySeries(ts1min, 5);
		return getBuyTime(ts3min, ts5min);
	}


	// return index of tick of when to buy with respect to the 5min series
	public static BuyTime getBuyTime(TimeSeries ts3min, TimeSeries ts5min) {
		
	    ClosePriceIndicator closePrice3min = new ClosePriceIndicator(ts3min);
	    ClosePriceIndicator closePrice5min = new ClosePriceIndicator(ts5min);
	    
//	    OpenPriceIndicator openPrice = new OpenPriceIndicator(series);	    
	    // Exponential moving averages
		// 5DEMA and 15DEMA on 5 minutes
	    DoubleEMAIndicator dema5 = new DoubleEMAIndicator(closePrice5min, 5);
	    DoubleEMAIndicator dema15 = new DoubleEMAIndicator(closePrice5min, 15);
	    SmoothedRSIIndicator rsi = new SmoothedRSIIndicator(closePrice5min, 14);
		// MACD and MACDAVG on 3 minutes
	    MACDIndicator macd = new MACDIndicator(closePrice3min, 12, 26);
	    EMAIndicator macdavg = new EMAIndicator(macd, 9);	
	    
	    CrossedUpIndicatorRule crossUp = new CrossedUpIndicatorRule(dema5, dema15);
	    
	    double accumulationVolume = 0;
	    int tickCount5min = ts5min.getTickCount();
	    for (int i = 0; i < tickCount5min; i++) {
	    	accumulationVolume += ts5min.getTick(i).getVolume().toDouble();
	    	
	    	if (i >= 3 && i <= 18 && accumulationVolume >= 1000000.0) {
	    		if (crossUp.isSatisfied(i)) {
	    			int t = convert5minTickTo3min(i);
	    			int t1 = t - 1;
	    			int t2 = t - 2;
	    			int t3 = t - 3;
//	    			int t4 = t - 4;
	    			
	    			double d = macd.getValue(t).toDouble() - macdavg.getValue(t).toDouble();
	    			double d1 = macd.getValue(t1).toDouble() - macdavg.getValue(t1).toDouble();
	    			double d2 = macd.getValue(t2).toDouble() - macdavg.getValue(t2).toDouble();
	    			double d3 = macd.getValue(t3).toDouble() - macdavg.getValue(t3).toDouble();
//	    			double d4 = macd.getValue(t4).toDouble() - macdavg.getValue(t4).toDouble();
	    			
	    			boolean sat1 = (Math.signum(d) == Math.signum(d1));
	    			boolean sat2 = (Math.signum(d2) == Math.signum(d3));
	    			boolean sat3 = (Math.signum(d) == Math.signum(d3));
//	    			boolean sat3 = (Math.signum(d) == Math.signum(d4));
	    			
//	    			if (sat1 && sat2 && sat3)
	    			if (sat1 && sat2 && sat3) {
	    				if (rsi.getValue(i).toDouble() <= 55.0) {
		    				return new BuyTime(i, ts5min.getTick(i).getBeginTime(), accumulationVolume, t, t1, t2, t3, d, d1, d2, d3);
	    				}
	    			}
	    		}
	    	}
	    }
		
		return new BuyTime(-1);
	}
	
	public static int convert5minTickTo3min(int i) {
		return 5*(i - i%3)/3 + 2*(i%3);
	}
	
	
	
}
