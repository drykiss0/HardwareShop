package com.evoludev.hardwareshop.domain;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.evoludev.hardwareshop.TestHelper;
import com.evoludev.hardwareshop.domain.CategoriesDeltaCalculator;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;

public class CategoriesDeltaCalculatorTest {

    private CategoriesDeltaCalculator deltaCalculator;

    @Before
    public void init() {
    	deltaCalculator = new CategoriesDeltaCalculator();
    }

    @Test
    public void testUpdatedDataDelta() {
    	Map<String, HardwareCategory> oldData = TestHelper.getCategories(
    			new String[] {"a", "b", "c", "d"}, new Integer[]{1, 10, 100, 1000});

    	Map<String, HardwareCategory> newData = TestHelper.getCategories(
    			new String[] {"a", "b", "c", "d"}, new Integer[]{1, 20, 100, 2000});
    	
    	Map<String, HardwareCategory> delta = deltaCalculator.getDelta(oldData, newData);
    	assertTrue(delta.size() == 2);
    	assertTrue(delta.get("b").getItemCount().equals(20));
    	assertTrue(delta.get("d").getItemCount().equals(2000));    	
    }	
}
