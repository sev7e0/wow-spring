package com.sev7e0.wow.framework.Utils;

/**
 * Title:  Utils.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-02 22:46
 **/

public class Strings {


	/**
	 * 判断字符串是否为非空
	 *
	 * @param string 输入字符串
	 * @return 为空false 不为空true
	 */
	public static boolean isEmpty(String string) {
		return !noEmpty(string);
	}

	public static boolean noEmpty(String string) {
		return string != null && !string.equals("");
	}

}
