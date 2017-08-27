package com.daytrader.prices;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Parses JSON Data from Alpha Advantage
 * @author youne
 *
 */
public class AplhaVantageStockPrice {
	

	//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=15min&outputsize=full&apikey=demo
	
	private static final String API_URL_BASE = "https://www.alphavantage.co/query?";
	private static final String API_KEY = "8IAP5B610W1O18H0";
	private static final String FUNCTION = "TIME_SERIES_INTRADAY";
	private static final String INTERVAL = "1min";
	private static final String OUTPUT_SIZE = "full";
	
	private static String discardTickTimeStamp = "09:30:00";
	/**
	 * Transforms the Json String returned by alphavantage
	 * to a list of TickDTOs
	 * @param jsonString
	 * @param date
	 * @return
	 */
	public static List<TickDTO> parseIntradayJSONData(String jsonString, String date) {
		// TODO use Gson instead (use only one library)
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		JSONObject jsonObject = (JSONObject) obj;
		JSONObject jsonticks = (JSONObject) jsonObject.get("Time Series (1min)");
		Object[] arr = jsonticks.keySet().toArray();
		List<TickDTO> ticks = new ArrayList<TickDTO>();
		Arrays.sort(arr);
		for (Object key : arr) {
			String dateTimeStr = (String) key;
			if (date != null && (!dateTimeStr.contains(date) || dateTimeStr.contains(discardTickTimeStamp))) {
				if (dateTimeStr.contains(discardTickTimeStamp)) {
					System.out.println("Discarding tick at " + dateTimeStr);
				}
				continue;
			}
			ZonedDateTime endTime = toZonedDateTime(dateTimeStr);
			JSONObject tick = (JSONObject) jsonticks.get(key);
			String open = (String) tick.get("1. open");
			String high = (String) tick.get("2. high");
			String low = (String) tick.get("3. low");
			String close = (String) tick.get("4. close");
			String volume = (String) tick.get("5. volume");
			TickDTO tickDTO = new TickDTO(Duration.ofMinutes(1), endTime, open, close, high, low, volume);
			ticks.add(tickDTO);
		}
		return ticks;
	}

	
    private static ZonedDateTime toZonedDateTime(String dateStr) {
    	// example 2017-08-10 09:35:00
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;
    	ZonedDateTime endTime = null;
    	try {
    		date = df.parse(dateStr);
         	Long epochTime = date.toInstant().toEpochMilli();
            endTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochTime), ZoneId.systemDefault());
    	}
    	catch (Exception e) {
    		
    	}
    	return endTime;
    }
	
	public static String buildAPICall(String base, String apiKey, String function, 
			String symbol, String interval, String outputSize) {
		String apiCallURL = base + "function=" + FUNCTION + "&" + "symbol=" + symbol + "&" 
		+ "interval=" + interval + "&" + "outputsize="+ outputSize + "&" +
		"apikey=" + apiKey;
		return apiCallURL;
	}
	
	public static String getStock1minData(String ticker) {
		String apiCallURL = buildAPICall(API_URL_BASE, API_KEY, FUNCTION, ticker, INTERVAL, OUTPUT_SIZE);
		System.out.println("URL: " + apiCallURL);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		      .url(apiCallURL)
		      .build();
		Response response = null;
		String data = null;
		try {
			response = client.newCall(request).execute();
			data = response.body().string();
		}
		catch (Exception e) {
			return null;
		}
		return data;
	}
		
	/**
	 *  Gets an intraday time series from AlphaVantage
	 *  if intraday == 1 return the 1 min data straight from Alpha Vantage (filtered by date)
	 *  if not it will aggregate the 1 min data to a group of intraday numbers of ticks
	 *  
	 * @param ticker
	 * @param intraday
	 * @param date
	 * @return
	 */
	public static TimeSeries getTimeSeries(String ticker, int intraday, String date) {
		String jsonStr = getStock1minData(ticker);
		List<TickDTO> tickDTOs = AplhaVantageStockPrice.parseIntradayJSONData(jsonStr, date); 
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
	
	/**
	 * Transform TimeSeries to JSON Time Series String for Javascript client (Google Charts)
	 * @param series
	 * @return
	 */
	private static String getJSONTimeSeries(TimeSeries series) {
	    JSONArray jsonChart = new JSONArray();
		int tickCount = series.getTickCount();
	    for (int i = 0; i < tickCount; i++) {
	    	Tick tick = series.getTick(i);
	    	// min open close max
	    	JSONArray jsonTick = new JSONArray();
	    	ZonedDateTime date = tick.getEndTime();
	    	String dateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(date);
	    	jsonTick.add(dateStr);
	    	jsonTick.add(tick.getMinPrice());
	    	jsonTick.add(tick.getOpenPrice());
	    	jsonTick.add(tick.getClosePrice());
	    	jsonTick.add(tick.getMaxPrice());
	    	jsonChart.add(jsonTick);
	    }
		return jsonChart.toString();
	}

	
	public static String getJSONTimeSeriesPrices(String ticker, int intraday, String date) {
		TimeSeries ts = getTimeSeries(ticker, intraday, date);
		String s = getJSONTimeSeries(ts);
		return s;
	}
    
	public static void main(String[] args) throws ParseException, IOException {
		//yyyy-MM-dd
		List<TickDTO> ticks = parseIntradayJSONData(getStock1minData("MSFT"), "2017-08-23");
		for (TickDTO tick : ticks) {
			System.out.println(tick);
		}
	}
}
