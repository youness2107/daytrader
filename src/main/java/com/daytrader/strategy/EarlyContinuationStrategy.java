package com.daytrader.strategy;

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

// Deploy this so that it could be tested
// Upload this to Github and send link of this class to Mitch
// Add possibility of multiple tickers
// Possibility to input 3 percentages
// Threshold are 0 by default unless set to something

public class EarlyContinuationStrategy {
	
	
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
	
	public static ContinuationBuyTime detectStrategy(String ticker, String date, double perc1, double perc2, double perc3) {
		TimeSeries ts1min = getTimeSeries(ticker, 1, date);
		TimeSeries ts10min = TickAggregation.getAggregatedIntradaySeries(ts1min, 10);
		return getBuyTime(ts1min, ts10min, perc1, perc2, perc3);
	}


	// return index of tick of when to buy with respect to the 5min series
	// add input box for percentage
	public static ContinuationBuyTime getBuyTime(TimeSeries ts1min, TimeSeries ts10min, 
			double deltaPerc, double deltaPerc2, double deltaPerc3) {
		
	    ClosePriceIndicator closePrice1min = new ClosePriceIndicator(ts1min);
	    ClosePriceIndicator closePrice10min = new ClosePriceIndicator(ts10min);
	    
	    // 1 min series indicators
	    EMAIndicator ema8 = new EMAIndicator(closePrice1min, 8);
	    EMAIndicator ema20 = new EMAIndicator(closePrice1min, 20);
	    SmoothedRSIIndicator rsi = new SmoothedRSIIndicator(closePrice1min, 14);
	    
	    // 10 min series indicators
	    EMAIndicator ema8_10min = new EMAIndicator(closePrice10min, 8);
	    EMAIndicator ema13 = new EMAIndicator(closePrice10min, 13);
	    MACDIndicator macd = new MACDIndicator(closePrice10min, 12, 26);
	    EMAIndicator macdavg = new EMAIndicator(macd, 9);	
	    	
	    
//	    CrossedUpIndicatorRule crossUp = new CrossedUpIndicatorRule(dema5, dema15);
	    
	    double accumulationVolume = 0;
	    int tickCount1min = ts1min.getTickCount();
	    for (int i = 0; i < tickCount1min; i++) {
	    	accumulationVolume += ts1min.getTick(i).getVolume().toDouble();
	    	
	    	if (i >= 4 && i <= 15 && accumulationVolume >= 500000.0) {
	    		// 8ema is greater than 20 ema if and only if delta is positive
	    		double delta = ema8.getValue(i).toDouble() - ema20.getValue(i).toDouble();
	    		double threshold = deltaPerc * ema8.getValue(i).toDouble() / 100;
    			if (delta > threshold) {
    		   		int tickTen = convert1minTickTo10min(i);
    		  		double threshold2 = deltaPerc2 * ema8_10min.getValue(tickTen).toDouble()/100;
    		   		double delta2 = ema8_10min.getValue(tickTen).toDouble() - ema13.getValue(tickTen).toDouble();
    		   		if (delta2 > threshold2) {
    		   			// make sure that macd is greater than macd average
    		   			double macdAtT = macd.getValue(tickTen).toDouble();
    		   			double macdAvgAtT = macdavg.getValue(tickTen).toDouble();
    		   			if (macdAtT > macdAvgAtT && rsi.getValue(i).toDouble() > 35.0) {
    		   				// i is current tick on 1 minute
    		   				// tickTen is current tick on 10 minutes
    		   				double cpAtI = closePrice1min.getValue(i).toDouble();
    		   				double ema8AtI = ema8.getValue(i).toDouble();
    		   				double threshold3 = deltaPerc3 * ema8.getValue(i).toDouble()/100;
    		   				double delta3 = cpAtI - ema8AtI ;
    		   				if (delta3 <= threshold3) {
    		   					// Display Time and Price only
    		   					// Change this
    		   					return new ContinuationBuyTime(i, ts1min.getTick(i).getBeginTime(), accumulationVolume, delta, 
    		   							delta2, delta3, tickTen, cpAtI, ema8AtI, macdAtT, macdAvgAtT, rsi.getValue(i).toDouble());
    		   							
    		   				}
    		   			}
    		   		}
    			}
	    	}
	    }
		
		return new ContinuationBuyTime(-1);
	}
	
	public static int convert1minTickTo10min(int i) {
		return i/10;
	}
	
	
	
}
