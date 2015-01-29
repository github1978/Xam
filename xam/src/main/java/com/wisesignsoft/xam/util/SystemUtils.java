package com.wisesignsoft.xam.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SystemUtils {

	public SystemUtils() {
	}

	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentDate(String format){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(c.getTime());
	}

	/**
	 * 获取本地ip
	 * @return
	 * @throws UnknownHostException 
	 */
	public static String getLocalIp() throws UnknownHostException{
		InetAddress address = InetAddress.getLocalHost();
		return address.getHostAddress();
	}
}
