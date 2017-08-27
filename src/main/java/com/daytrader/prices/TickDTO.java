package com.daytrader.prices;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import eu.verdelhan.ta4j.Decimal;

public class TickDTO {
	
    private Duration timePeriod;
    /** End time of the tick */
    private ZonedDateTime endTime;
    /** Begin time of the tick */
    private ZonedDateTime beginTime;
    /** Open price of the period */
    private Decimal openPrice = null;
    /** Close price of the period */
    private Decimal closePrice = null;
    /** Max price of the period */
    private Decimal maxPrice = null;
    /** Min price of the period */
    private Decimal minPrice = null;
    /** Volume of the period */
    private Decimal volume = Decimal.ZERO;
    
	public TickDTO(Duration timePeriod, ZonedDateTime endTime, Decimal openPrice, Decimal closePrice, 
		Decimal maxPrice, Decimal minPrice, Decimal volume) {
		this.timePeriod = timePeriod;
		this.endTime = endTime;
		this.beginTime = endTime.minus(timePeriod);
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
		this.volume = volume;
	}
	
    public TickDTO(Duration timePeriod, ZonedDateTime endTime, String openPrice, String closePrice, 
    		String maxPrice, String minPrice, String volume) {
        		this(timePeriod, endTime,
        		Decimal.valueOf(openPrice),
                Decimal.valueOf(closePrice),
                Decimal.valueOf(maxPrice),
                Decimal.valueOf(minPrice),
                Decimal.valueOf(volume));
    }
	
	
	public Duration getTimePeriod() {
		return timePeriod;
	}
	
	public void setTimePeriod(Duration timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public ZonedDateTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	
	public ZonedDateTime getBeginTime() {
		return beginTime;
	}
	
	public void setBeginTime(ZonedDateTime beginTime) {
		this.beginTime = beginTime;
	}
	
	public Decimal getOpenPrice() {
		return openPrice;
	}
	
	public void setOpenPrice(Decimal openPrice) {
		this.openPrice = openPrice;
	}
	
	public Decimal getClosePrice() {
		return closePrice;
	}
	
	public void setClosePrice(Decimal closePrice) {
		this.closePrice = closePrice;
	}
	
	public Decimal getMaxPrice() {
		return maxPrice;
	}
	
	public void setMaxPrice(Decimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public Decimal getMinPrice() {
		return minPrice;
	}
	
	public void setMinPrice(Decimal minPrice) {
		this.minPrice = minPrice;
	}
	
	public Decimal getVolume() {
		return volume;
	}
	
	public void setVolume(Decimal volume) {
		this.volume = volume;
	}
	

    @Override
    public String toString() {
        return String.format("{end time: %1s, close price: %2$f, open price: %3$f, min price: %4$f, max price: %5$f, volume: %6$f}",
                endTime.withZoneSameInstant(ZoneId.systemDefault()), closePrice.toDouble(), 
                openPrice.toDouble(), minPrice.toDouble(), maxPrice.toDouble(), volume.toDouble());
    }
	
}
