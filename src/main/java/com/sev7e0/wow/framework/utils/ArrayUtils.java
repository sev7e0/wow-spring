package com.sev7e0.wow.framework.utils;

import java.util.Objects;

/**
 * Title:  ArrayUtils.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-16 16:17
 **/

public class ArrayUtils {

	public static boolean isEmpty(String[] strings) {
		if (Objects.isNull(strings)) return true;
		return strings.length <= 0;
	}

}
