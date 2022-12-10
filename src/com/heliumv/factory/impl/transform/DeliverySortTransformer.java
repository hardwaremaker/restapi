package com.heliumv.factory.impl.transform;

import com.heliumv.api.delivery.DeliveryPositionSort;
import com.heliumv.factory.impl.TransformParamValue;
import com.heliumv.tools.StringHelper;

public class DeliverySortTransformer extends TransformParamValue {
	public static DeliveryPositionSort transform(String value) {
		return StringHelper.isEmpty(value) 
				? DeliveryPositionSort.NOTINITIALIZED 
				: DeliveryPositionSort.fromString(value);
	}
}
