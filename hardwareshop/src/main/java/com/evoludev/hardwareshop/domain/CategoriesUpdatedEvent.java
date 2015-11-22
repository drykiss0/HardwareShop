package com.evoludev.hardwareshop.domain;

import java.util.Map;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;

public class CategoriesUpdatedEvent {
	
	private final Map<String, HardwareCategory> updatedCategories;

	public CategoriesUpdatedEvent(Map<String, HardwareCategory> updatedCategories) {
		this.updatedCategories = updatedCategories;
	}

	public Map<String, HardwareCategory> getUpdatedCategories() {
		return updatedCategories;
	}

}
