package com.evoludev.hardwareshop;

import java.util.HashMap;
import java.util.Map;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;

public class TestHelper {

	public static Map<String, HardwareCategory> getCategories(String[] catNames, Integer[] itemCounts) {
		Map<String, HardwareCategory> catMap = new HashMap<>();
		for (int i = 0; i < catNames.length; i++) {
			catMap.put(catNames[i], new HardwareCategory(catNames[i], itemCounts[i]));
		}
		return catMap;
	}
}
