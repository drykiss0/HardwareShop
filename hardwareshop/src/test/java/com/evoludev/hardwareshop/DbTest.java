package com.evoludev.hardwareshop;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.evoludev.hardwareshop.domain.hardware.HardwareCategory;

public class DbTest {

	private EmbeddedDatabase db;
	
	@Before
    public void setUp() {
    	db = new EmbeddedDatabaseBuilder()
    		.setType(EmbeddedDatabaseType.HSQL)
    		.addScript("db/sql/schema.sql")
    		.addScript("db/sql/data.sql")
    		.build();
    }

    @Test
    public void testFindAllHardwareCategories() {
    	JdbcTemplate template = new JdbcTemplate(db);
    	List<HardwareCategory> list = template.query("select * from HARDWARE_CATEGORY ORDER BY name", 
    			new BeanPropertyRowMapper<HardwareCategory>(HardwareCategory.class, true));

    	Assert.assertTrue(list.size() >= 5);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }	
}
