package com.tdsecurities.cvr.batch.thread;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.tdsecurities.cvr.batch.item.OISReport;

public class ProductItemWriter extends JdbcDaoSupport implements
		ItemWriter<OISReport> {

	public void write(List<? extends OISReport> items) throws Exception {
		for (OISReport product : items) {
			getJdbcTemplate().update(
					"update product set processed=? where id=?", true,
					product.getId());
			// Writing then the product content
		}
	}

}