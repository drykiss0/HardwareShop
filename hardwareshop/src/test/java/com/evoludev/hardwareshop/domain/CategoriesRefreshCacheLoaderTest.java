package com.evoludev.hardwareshop.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.context.ApplicationEventPublisher;

import com.evoludev.hardwareshop.TestHelper;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

import net.sf.ehcache.Cache;

public class CategoriesRefreshCacheLoaderTest {

	@InjectMocks
    private CategoriesRefreshCacheLoader cacheLoader;

	@Mock
	private HardwareCategoryDao hardwareCategoryDao;
	
	@Spy
	private CategoriesDeltaCalculator deltaCalculator = new CategoriesDeltaCalculator();
	
	@Mock
	private Cache cache;

	@Mock
	private ApplicationEventPublisher publisher;
	
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    	when(hardwareCategoryDao.getAllCategories()).thenReturn(TestHelper.getCategories(
    			new String[] {"a", "b", "c", "d"}, new Integer[]{1, 10, 300, 1000}));
    	
    	Map<String, HardwareCategory> cachedCategories = TestHelper.getCategories(
    			new String[] {"a", "b", "c", "d"}, new Integer[]{1, 10, 100, 1000});
    	when(cache.getAll(Mockito.anyCollectionOf(String.class))).thenReturn(
    			CategoriesRefreshCacheLoader.fromCategoryMap(cachedCategories));
    }

    @Test
    public void testLoadAllEventPublish() {
    	Map<String, HardwareCategory> expectedDeltaCategories = TestHelper.getCategories(new String[] {"c"}, new Integer[] {300});
    	cacheLoader.loadAll(Arrays.asList(new String[] {"a", "b", "c", "d"}));
    	
    	ArgumentCaptor<CategoriesUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(CategoriesUpdatedEvent.class);
    	Mockito.verify(publisher).publishEvent(eventCaptor.capture());
    	
    	assertEquals(expectedDeltaCategories, eventCaptor.getValue().getUpdatedCategories());
    }	
}
