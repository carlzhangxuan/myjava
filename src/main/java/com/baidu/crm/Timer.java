package com.baidu.crm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timer {
	
	public int daysFromNow(Calendar toDate, String fromDate){
		long deltaDayInM;
		int deltaDay;
		Date date;
		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = Calendar.getInstance();
		try {
			date = dateFormat.parse(fromDate);
			startTime.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		deltaDayInM=toDate.getTimeInMillis()-startTime.getTimeInMillis();
		deltaDay = new Long(deltaDayInM/(1000*60*60*24)).intValue();
		return deltaDay;}
		
	
	public int daysBetween(String toDate, String fromDate){
		long deltaDayInM;
		int deltaDay;
		Date startDate, endDate;
		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		try {
			startDate = dateFormat.parse(fromDate);
			endDate = dateFormat.parse(toDate);
			startTime.setTime(startDate);
			endTime.setTime(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		deltaDayInM=endTime.getTimeInMillis()-startTime.getTimeInMillis();
		deltaDay = new Long(deltaDayInM/(1000*60*60*24)).intValue();
		return deltaDay;}

}
