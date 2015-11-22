package com.evoludev.hardwareshop.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.evoludev.hardwareshop.TestHelper;
import com.evoludev.hardwareshop.domain.CategoriesUpdatedEvent;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

public class HardwareShopControllerTest {

	@InjectMocks
    private HardwareShopController shopController;

	@Mock
	private SimpMessagingTemplate template;	
	
	@Mock
	private HardwareCategoryDao hardwareCategoryDao;

	@Captor
	private ArgumentCaptor<Collection<HardwareCategory>> hardwareCollectionCaptor; 
	
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    	when(hardwareCategoryDao.getAllCategories()).thenReturn(TestHelper.getCategories(
    			new String[] {"a", "b", "c", "d"}, new Integer[]{1, 10, 300, 1000}));
    }

	@Test
	public void testCategoriesOnSubscribe() {
		assertEquals(hardwareCategoryDao.getAllCategories().values(), shopController.categoriesOnSubscribe());
	}
	
	@Test
	public void testCategoriesUpdated() {
		CategoriesUpdatedEvent updatedEvent = new CategoriesUpdatedEvent(
				TestHelper.getCategories(new String[]{"a"}, new Integer[] {99}));
		
		shopController.handleCategoriesUpdatedEvent(updatedEvent);
    	
    	Mockito.verify(template).convertAndSend(Matchers.anyString(), hardwareCollectionCaptor.capture());
    	assertEquals(updatedEvent.getUpdatedCategories().values(), hardwareCollectionCaptor.getValue());
	}
}
