package com.evoludev.hardwareshop.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;

@Component
public class CategoriesDeltaCalculator {

	public Map<String, HardwareCategory> getDelta(Map<String, HardwareCategory> oldData, Map<String, HardwareCategory> newData) {
		
		Map<String, HardwareCategory> categoriesDelta = new HashMap<>();
		for (Map.Entry<String, HardwareCategory> entry: newData.entrySet()) {
			if (!(oldData.containsKey(entry.getKey()) && oldData.get(entry.getKey()).getItemCount().equals(entry.getValue().getItemCount()))) {
				categoriesDelta.put(entry.getKey(), entry.getValue());
			}
		}
		return categoriesDelta;
	}
}
