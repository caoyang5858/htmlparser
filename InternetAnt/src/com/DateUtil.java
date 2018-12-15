package com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	   private static String FORMAT = "yy-MM-dd HH:mm:ss";
	   private static SimpleDateFormat sdf;
	   
	   public static void setFormat(String format){
		   FORMAT=format;
	   }
	   public static String dateToString(Date date){
		  
			   sdf = new SimpleDateFormat(FORMAT);
			   return sdf.format(date);
		   
	   }
	   
	   public static Date strToDate(String strDate){
		   
		   sdf = new SimpleDateFormat(FORMAT);
		   try {
			    return sdf.parse(strDate);
		    } catch (ParseException e) {
			    return null;
		   }
	   }
	   
}
