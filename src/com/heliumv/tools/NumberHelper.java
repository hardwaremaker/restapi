package com.heliumv.tools;

import java.math.BigDecimal;

public class NumberHelper {

	public static boolean isNullOrZero(BigDecimal number) {
		return number == null || BigDecimal.ZERO.compareTo(number) == 0;
	}

}
