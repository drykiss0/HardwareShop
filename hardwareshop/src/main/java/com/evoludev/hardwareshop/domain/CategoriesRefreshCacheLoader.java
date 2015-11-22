package com.evoludev.hardwareshop.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.loader.CacheLoader;

@Component
public class CategoriesRefreshCacheLoader implements CacheLoader {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private HardwareCategoryDao hardwareCategoryDao;
	
	@Autowired
	private CategoriesDeltaCalculator deltaCalculator;
	
	@Autowired
	private Cache cache;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map loadAll(Collection keys) {
		// Calculate delta using deltaCalculator and publish an event with updated data
		
		Map<String, HardwareCategory> allCategoriesFromDao = hardwareCategoryDao.getAllCategories();
		Map<String, HardwareCategory> delta = deltaCalculator.getDelta(fromElementMap(cache.getAll(keys)), allCategoriesFromDao);
		log.info("DELTA=" + delta.values());		
		
		publisher.publishEvent(new CategoriesUpdatedEvent(delta));
		return allCategoriesFromDao;
	}

	@Override
	public Object load(Object key) throws CacheException {
		return null;
	}

	@Override
	public Object load(Object key, Object argument) {
		return load(key);				
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map loadAll(Collection keys, Object argument) {
		return loadAll(keys);
	}

	@Override
	public String getName() {
		return CategoriesRefreshCacheLoader.class.getSimpleName();
	}

	@Override
	public CacheLoader clone(Ehcache cache) throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public void init() {
		log.info(CategoriesRefreshCacheLoader.class.getSimpleName() + " is initializing");
		cache.putAll(fromCategoryMap(hardwareCategoryDao.getAllCategories()).values());
	}

	@Override
	public void dispose() throws CacheException {
		log.info(CategoriesRefreshCacheLoader.class.getSimpleName() + " is disposing");		
	}

	@Override
	public Status getStatus() {
		return Status.STATUS_ALIVE;
	}

	protected static Map<Object, Element> fromCategoryMap(Map<String, HardwareCategory> catMap) {
		return catMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, e -> new Element(e.getKey(), e.getValue())));
	}

	protected static Map<String, HardwareCategory> fromElementMap(Map<Object, Element> cacheElements) {
		
		Map<String, HardwareCategory> categoriesMap = new HashMap<>();
		for (Map.Entry<Object, Element> entry: cacheElements.entrySet()) {
			Object value = entry.getValue().getObjectValue();
			if (value instanceof HardwareCategory) {
				HardwareCategory cat = (HardwareCategory) value;
				categoriesMap.put(cat.getName(), cat);
			}
		}
		return categoriesMap;
	}
}
