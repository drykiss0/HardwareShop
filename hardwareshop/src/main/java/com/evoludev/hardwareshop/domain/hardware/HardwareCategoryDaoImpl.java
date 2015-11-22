package com.evoludev.hardwareshop.domain.hardware;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("hardwareCategoryDao")
public class HardwareCategoryDaoImpl implements HardwareCategoryDao {

	public static final String SQL_SELECT_ALL_HARDWARE_CATEGORY = "SELECT * FROM HARDWARE_CATEGORY ORDER BY name";
	public static final String SQL_UPDATE_HARDWARE_ITEM_COUNTS = "UPDATE HARDWARE_CATEGORY SET item_count = ? WHERE name = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String, HardwareCategory> getAllCategories() {
		return jdbcTemplate.query(SQL_SELECT_ALL_HARDWARE_CATEGORY, new BeanPropertyRowMapper<HardwareCategory>(HardwareCategory.class))
				.stream().collect(Collectors.toMap(HardwareCategory::getName, Function.identity()));
	}

	@Override
	public void updateItemCounts(Map<String, Integer> categoryCounts) {
		
		List<Entry<String, Integer>> countsList = new ArrayList<>(categoryCounts.entrySet());
		jdbcTemplate.batchUpdate(SQL_UPDATE_HARDWARE_ITEM_COUNTS, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, countsList.get(i).getValue());
				ps.setString(2, countsList.get(i).getKey());				
			}
			
			@Override
			public int getBatchSize() {
				return categoryCounts.size();
			}
		});
	}
}