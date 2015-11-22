package com.evoludev.hardwareshop.domain.hardware;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.evoludev.hardwareshop.EntryPoint;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {EntryPoint.class})
public class HardwareCategoryDaoTest {

	@Autowired
	private HardwareCategoryDao hardwareCategoryDao;
	
    @Test
    public void testUpdateItemCounts() {
    	
    	Map<String, Integer> newItemCounts = new HashMap<>();
    	newItemCounts.put("bricks", 11);
    	newItemCounts.put("hammers", 12);  	
    	newItemCounts.put("drillers", 13);    	
    	hardwareCategoryDao.updateItemCounts(newItemCounts);
    	
    	Map<String, HardwareCategory> categories = hardwareCategoryDao.getAllCategories();
    	for (String category: newItemCounts.keySet()) {
        	Assert.assertEquals(newItemCounts.get(category), categories.get(category).getItemCount());    		
    	}
    }
    
    @Test
    public void testGetAllCategories() {
    	Map<String, HardwareCategory> allCategories = hardwareCategoryDao.getAllCategories();
    	assertTrue(allCategories.size() > 0);
    }

}
