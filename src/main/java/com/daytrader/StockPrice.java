package com.daytrader;

import java.awt.AlphaComposite;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.daytrader.prices.AlphaVantageStockPrice;


public class StockPrice extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	  String jsonStr = "";
	  response.setContentType("application/json");
	  response.setCharacterEncoding("UTF-8");
	  System.out.println(jsonStr);
	  response.getWriter().write(jsonStr);  
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {
	  	String ticker = request.getParameter("ticker");
	  	String date = request.getParameter("date");
	  	int intraday = Integer.valueOf(request.getParameter("intraday"));
		String s = AlphaVantageStockPrice.getJSONTimeSeriesPrices(ticker, intraday, date);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		System.out.println(s);
	    response.getWriter().write(s);
  }	
  
}