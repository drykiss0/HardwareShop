package com.evoludev.hardwareshop.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.evoludev.hardwareshop.domain.CategoriesUpdatedEvent;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;
import com.evoludev.hardwareshop.domain.hardware.HardwareCategoryDao;

@Controller
public class HardwareShopController {

	@Autowired
	private SimpMessagingTemplate template;	
	
	@Autowired
	private HardwareCategoryDao categoryDao;
	
    @SubscribeMapping("/topic")
    @SendTo("/topic")
    public Collection<HardwareCategory> categoriesOnSubscribe() {
        return categoryDao.getAllCategories().values();
    }

	@EventListener
	public void handleCategoriesUpdatedEvent(CategoriesUpdatedEvent ev) {
		this.template.convertAndSend("/topic", ev.getUpdatedCategories().values());
	}
}
