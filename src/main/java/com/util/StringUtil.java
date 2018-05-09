package com.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * @description 字符串工具类
 * @author 黄连忠
 */
public final class StringUtil {
	/**
	 * 当str不为null并且不为""时返回true
	 */
	public static boolean isNotNull(String str){
		return (null != str && !"".equalsIgnoreCase(str));
	}
	
	/**
	 * 当str为null或者为""时返回true
	 */
	public static boolean isNull(String str){
		return (null == str || "".equalsIgnoreCase(str));
	}
	/**
	 * 判断是不是中文
	 * @param strName
	 * @return
	 */
	public static  boolean isChinese(String strName) {
		boolean flag = true;
	    char[] ch = strName.toCharArray();
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (!isChinese(c)) {
	        	flag =false;
	        	break;
	        }
	    }
	    return flag;
	}
	 
	private static boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        return true;
	    }
	    return false;
	}
	public static boolean isNumber(String str){
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	}
	public static String getRandomString(int length) {
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
}
