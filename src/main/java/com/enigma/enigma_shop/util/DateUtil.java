package com.enigma.enigma_shop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static Date parseDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date tempDate = new Date();
		try {
			tempDate = sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return tempDate;
	}
}
