package com.evoludev.hardwareshop.domain.hardware;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


public class HardwareCategoryTest {

	@Test
	public void testHashCodeEquals() {
		Set<HardwareCategory> s1 = new HashSet<>();
		s1.add(new HardwareCategory("a", 10));
		Set<HardwareCategory> s2 = new HashSet<>();
		s2.add(new HardwareCategory("a", 10));
		
		Assert.assertEquals(s1, s2);
		Assert.assertTrue(s1.hashCode() == s2.hashCode());
	}
}
