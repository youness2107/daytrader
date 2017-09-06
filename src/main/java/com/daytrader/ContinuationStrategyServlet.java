package com.daytrader;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.daytrader.strategy.EarlyContinuationStrategy;

public class ContinuationStrategyServlet extends HttpServlet {

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
		  Double perc1 = Double.valueOf(request.getParameter("perc1"));
		  Double perc2 = Double.valueOf(request.getParameter("perc2"));
		  Double perc3 = Double.valueOf(request.getParameter("perc3"));
		  String s = EarlyContinuationStrategy.detectStrategy(ticker, date, perc1, perc2, perc3).toString();
		  response.setContentType("text");
		  response.setCharacterEncoding("UTF-8");
		  System.out.println(s);
		  response.getWriter().write(s);
	  }	
	
}
