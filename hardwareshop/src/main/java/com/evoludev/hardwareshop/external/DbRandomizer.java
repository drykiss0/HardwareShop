package com.evoludev.hardwareshop.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

@Component
public class DbRandomizer {
	
	public static final Integer MAX_ITEM_COUNT = 999;
	
	@Autowired
	private HardwareCategoryDao hardwareCategoryDao;
	
	private List<String> categoryNames;
	
	@PostConstruct
	public void init() {
		categoryNames = new ArrayList<String>(hardwareCategoryDao.getAllCategories().keySet());
	}

	@Scheduled(initialDelay = 500, fixedRate=1000)
	private void randomizeItemCounts() {
		List<String> shuffledCategories = new ArrayList<>(categoryNames);
		Collections.shuffle(shuffledCategories);
		Map<String, Integer> categoriesToUpdate = new HashMap<>();
		int updateCategoriesUpToIndex = new Random().nextInt(shuffledCategories.size());
		for (int i = 0; i <= updateCategoriesUpToIndex; i++ ) {
			categoriesToUpdate.put(shuffledCategories.get(i), new Random().nextInt(MAX_ITEM_COUNT + 1));
		}
		hardwareCategoryDao.updateItemCounts(categoriesToUpdate);
	}
}
