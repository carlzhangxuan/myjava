package com.baidu.crm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class GrammerTest {

	public static void main(String args[]) throws ParseException{
		Calendar now = Calendar.getInstance();
	    Date date;
	    int S = 0;
	    String detime = "2014-01-01 00:09:05.0".split("\\.")[0];
	   /* Calendar createTime = Calendar.getInstance();
	    DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    date = dateFormat.parse(detime);
	    createTime.setTime(date);
	    long deltaDayInM = now.getTimeInMillis() - createTime.getTimeInMillis();
		int deltaDay = new Long(deltaDayInM/(1000*60*60*24)).intValue();
	    System.out.println(now);
	    System.out.println(createTime);
	    System.out.println(deltaDay);*/
	    String A = "is";
	    String B = "is";
	    if (A.equals("is") ){
	    S += 1;}
	    
	    List<Integer> tmp=new ArrayList<Integer>();
	    tmp.add(0);
	    tmp.add(9);
	    tmp.add(1);
	    Collections.sort(tmp);
	    System.out.println(tmp);
	    tmp.remove(0);
	    System.out.println(tmp);

		List<Integer> teset = new ArrayList<Integer>();

		
		
		if(!CollectionUtils.isEmpty(teset)){
	    System.out.println(teset); }
		else{
			teset.add(0);
			teset.set(0, teset.get(0)+1);
			System.out.println(teset);
		}
		
		/*List<String> timeLabels = new ArrayList<String>(); //day, month labels
		timeLabels.set(0,"d7");timeLabels.set(1,"d14");
		timeLabels.set(2,"m1");timeLabels.set(3,"m2");
		timeLabels.set(4,"m3");timeLabels.set(5,"m6");
		timeLabels.set(6,"m9");timeLabels.set(7,"m12");
		List<Integer> fnD = new ArrayList<Integer>(); // 7,14,30,60,90,180,270,365天内商机次数 次数
		for(int i=0;i<timeLabels.size();i++){fnD.add(0);}
		
		System.out.println(fnD);*/
		
		/*String[] oriLabels = new String[8];
		oriLabels[0] = "d7";
		oriLabels[1] = "d14";
		oriLabels[2] = "m1";
		oriLabels[3] = "m2";
		oriLabels[4] = "m3";
		oriLabels[5] = "m6";
		oriLabels[6] = "m9";
		oriLabels[7] = "m12";
		
		List<String> timeLabels = new ArrayList<String>(); //day, month labels
		for (int i=0;i<oriLabels.length;i++){timeLabels.add(oriLabels[i]);}
		List<Integer> fnD = new ArrayList<Integer>(); // 7,14,30,60,90,180,270,365天内商机次数 次数
		for(int i=0;i<timeLabels.size();i++){fnD.add(0);} 
		fnD.set(0, fnD.get(0)+1);
		System.out.println(fnD);*/
		System.out.println(now.getTime());

		}


}
