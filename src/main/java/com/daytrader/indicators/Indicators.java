package com.daytrader.indicators;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.json.simple.JSONArray;

import com.daytrader.prices.AlphaVantageStockPrice;
import com.google.gson.Gson;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.DoubleEMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SmoothedRSIIndicator;

public class Indicators {
	
	public static String getOverlaidIndicators(TimeSeries series) {
		
	    ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
//	    OpenPriceIndicator openPrice = new OpenPriceIndicator(series);	    
	    // Exponential moving averages
	    EMAIndicator ema8 = new EMAIndicator(closePrice, 8);
	    EMAIndicator ema20 = new EMAIndicator(closePrice, 20);
	    DoubleEMAIndicator dema5 = new DoubleEMAIndicator(closePrice, 5);
	    DoubleEMAIndicator dema15 = new DoubleEMAIndicator(closePrice, 15);
	    SmoothedRSIIndicator rsi = new SmoothedRSIIndicator(closePrice, 14);
	    MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
	    EMAIndicator macdavg = new EMAIndicator(macd, 9);	
		
	    JSONArray jsonChart = new JSONArray();
		int tickCount = series.getTickCount();
	    for (int i = 0; i < tickCount; i++) {
	    	Tick tick = series.getTick(i);
	    	// min open close max
	    	JSONArray jsonTick = new JSONArray();
	    	ZonedDateTime date = tick.getBeginTime();
	    	Long dateStr = date.toEpochSecond();
	    	jsonTick.add(dateStr);
	    	jsonTick.add(tick.getMinPrice());
	    	jsonTick.add(tick.getOpenPrice());
	    	jsonTick.add(tick.getClosePrice());
	    	jsonTick.add(tick.getMaxPrice());
	    	jsonTick.add(((Decimal) ema8.getValue(i)).toDouble());
	    	jsonTick.add(((Decimal) ema20.getValue(i)).toDouble());
	    	jsonChart.add(jsonTick);
	    }
		return jsonChart.toString();
	    /**
	     * Creating indicators
	     */
	    // Close price


	}
	
	public static HashMap<String, JSONArray> getJSONIndicators(TimeSeries series) {
		HashMap<String, JSONArray> indicators = new HashMap<String, JSONArray>();
	    /**
	     * Creating indicators
	     */
	    // Close price
	    ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
//	    OpenPriceIndicator openPrice = new OpenPriceIndicator(series);	    
	    // Exponential moving averages
	    EMAIndicator ema8 = new EMAIndicator(closePrice, 8);
	    EMAIndicator ema20 = new EMAIndicator(closePrice, 20);
	    DoubleEMAIndicator dema5 = new DoubleEMAIndicator(closePrice, 5);
	    DoubleEMAIndicator dema15 = new DoubleEMAIndicator(closePrice, 15);
	    SmoothedRSIIndicator rsi = new SmoothedRSIIndicator(closePrice, 14);
	    MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
	    EMAIndicator macdavg = new EMAIndicator(macd, 9);	
	    ;
	    indicators.put("ema8-ema20", getJSonArrayTicks(ema8, ema20));
	    indicators.put("dema5-dema15", getJSonArrayTicks(dema5, dema15));
	    indicators.put("macd-macdavg", getJSonArrayTicks(macd, macdavg));
	    indicators.put("rsi", getJSonArrayTicks(rsi));
	    return indicators;
		
	}

	public static JSONArray getJSonArrayTicks(Indicator indicator) {
		TimeSeries series = indicator.getTimeSeries();
		int tickCount = series.getTickCount();
	    JSONArray jsonArr = new JSONArray();
		
	    for (int i = 0; i < tickCount; i++) {
	    	Tick tick = series.getTick(i);
	    	JSONArray jsonTick = new JSONArray();
	    	ZonedDateTime date = tick.getBeginTime();
//	    	String dateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(date);
	    	Decimal value = (Decimal) indicator.getValue(i);
	    	jsonTick.add(date.toEpochSecond());
	    	jsonTick.add(value.toDouble());
	    	jsonArr.add(jsonTick);
	    }
		
		return jsonArr;
	}
	
	public static JSONArray getJSonArrayTicks(Indicator indicator, Indicator indicator2) {
		TimeSeries series = indicator.getTimeSeries();
		int tickCount = series.getTickCount();
	    JSONArray jsonArr = new JSONArray();
		
	    for (int i = 0; i < tickCount; i++) {
	    	Tick tick = series.getTick(i);
	    	JSONArray jsonTick = new JSONArray();
	    	ZonedDateTime date = tick.getBeginTime();
//	    	String dateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(date);
	    	Decimal value = (Decimal) indicator.getValue(i);
	    	Decimal value2 =(Decimal) indicator2.getValue(i); 
	    	jsonTick.add(date.toEpochSecond());
	    	jsonTick.add(value.toDouble());
	    	jsonTick.add(value2.toDouble());
	    	jsonArr.add(jsonTick);
	    }
		
		return jsonArr;
	}
	
	public static String getJSONIndicators(String ticker, int intraday, String date) {
		TimeSeries ts = AlphaVantageStockPrice.getTimeSeries(ticker, intraday, date);
		HashMap<String, JSONArray> map = getJSONIndicators(ts);
		Gson gson = new Gson();
		String json = gson.toJson(map);  
		return json;
	}
	
}
