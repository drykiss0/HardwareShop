package com.evoludev.hardwareshop.domain.hardware;

import java.util.Map;

public interface HardwareCategoryDao{
	
	Map<String, HardwareCategory> getAllCategories();
	void updateItemCounts(Map<String, Integer> categoryCounts);
}