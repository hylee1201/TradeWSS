package com.tdsecurities.cvr.batch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.tdsecurities.cvr.model.BatchJobVO;

public class TradeWssBatchService extends BatchService {
	private static final int MAX_SIZE =2000;
	private static final String SQL_GET_RATE_BY_VALUATION_DATE = "select top 1 cad_value from  fx_rates where ccy = 'USD' and valuation_date<= ? order by valuation_date desc";
	private static final String SQL_GET_VALUATION_DATE = "select valuation_date from data_transfer_status where update_date=(select max(update_date) from data_transfer_status where group_type = 'FXFWD' and status = 'SUCCESSFUL')";
	private static final String SQL_GET_WARNING_LIST = "select deal_number from fxfwd_deals where report_date = :date and deal_number in (:ids) ";
	private static final String UPDATE_WSS_OIS_RECORD = "update fxfwd_deals set deal_number=:dealNumber, mtm_cad=:mtmCad, mtm_usd=:mtmUsd where deal_number = :id and report_date= :date";
	private NamedParameterJdbcTemplate template=new NamedParameterJdbcTemplate(this.getJdbcTemplate());
	private static final String SET_VALUATION_DATE = "update env_lookup set key_value = null where key_name =:valuationDate";
	private static String valuationDateKey;
	//	private static final String SQL_MOVE_HISTORY_DEAL = 	"insert into cpg_deals_history  "+
//			"(load_date, hold_deal, requested_item_id, deal_id, valuation_date, counterparty_short_name, description, deal_type, "+
//			"source_system_id, source_system_name, issuer, trade_date, maturity_date, notional, currency, notional2, currency2, "+
//			"rate_spread, pay_frequency, day_count, clean_bond_pv, pv, pv_currency, pv2, pv_currency2, pv3, pv_currency3, "+
//			"aswp, basel, leg_type, side, tenor, leverage_factor, cost_to_unwind, cost_to_initiate, comment, accrued_interest, portfolio, number_of_legs, pv_usd, usd_to_cad_rate, error_message, update_date, update_by) "+
//			"SELECT load_date, hold_deal, requested_item_id, deal_id, valuation_date, counterparty_short_name, description, deal_type, "+
//			"source_system_id, source_system_name, issuer, trade_date, maturity_date, notional, currency, notional2, currency2, "+
//			"rate_spread, pay_frequency, day_count, clean_bond_pv, pv, pv_currency, pv2, pv_currency2, pv3, pv_currency3, "+
//			"aswp, basel, leg_type, side, tenor, leverage_factor, cost_to_unwind, cost_to_initiate, comment, accrued_interest, portfolio, number_of_legs, pv_usd, usd_to_cad_rate, error_message, update_date, update_by FROM cpg_deals WHERE valuation_date=? AND deal_id like '%*'";

	public TradeWssBatchService(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Date getValuationDateFromDB() {
		logger.info("Getting valuation day from last succesful data transfer.");
		Date date=null;
		Object obj=this.getJdbcTemplate().queryForObject(SQL_GET_VALUATION_DATE, new Object[]{}, Date.class);
		if(obj!=null){
			date = (Date)obj;
		}
		return date;

	}
	
	public void clearValuationDate(){
		logger.info("Clearing value for key " + valuationDateKey + " from DB");
		SqlParameterSource namedParameters = new MapSqlParameterSource("valuationDate", valuationDateKey); 
		template.update(SET_VALUATION_DATE,namedParameters);
	}

	@Override
	public int[] processData(List<? extends Object> items) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());
        int[] updateCounts = template.batchUpdate(
		UPDATE_WSS_OIS_RECORD, batch);
        return updateCounts;
		
	}

	@Override
	public void postProcessData(Date date) {
//		skipped since not history table
//		try{
//			this.getJdbcTemplate().update(SQL_MOVE_HISTORY_DEAL, new Object[]{date}, new int[]{java.sql.Types.DATE});
//		} catch (Exception e) {
//			logger.error(e);
//		}
	}

	public Double getRateByValuationDateFromDB(Date date) {

		Object obj=this.getJdbcTemplate().queryForObject(SQL_GET_RATE_BY_VALUATION_DATE, new Object[]{date}, Double.class);
		Double rate=null;
		if(obj!=null){
			rate = (Double)obj;
		}
		return rate;

	}

	@Override
	public List<String> exsistInDB(Date date,List<String> keys) {
		NamedParameterJdbcTemplate  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource());
		List<List<String>> fList = split(keys, MAX_SIZE);
		List<String> list= new ArrayList<String>();
		for(List<String> subList:fList){
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", subList);
			parameters.addValue("date", date);
			list.addAll(namedParameterJdbcTemplate.queryForList(SQL_GET_WARNING_LIST, parameters, String.class));
		}
		return list;
	}

	public static <T> List<List<T>> split(List<T> list, final int length) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int size = list.size();
	    for (int i = 0; i < size; i += length) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(size, i + length)))
	        );
	    }
	    return parts;
	}

	public static String getValuationDateKey() {
		return valuationDateKey;
	}

	public static void setValuationDateKey(String valuationDateKey) {
		TradeWssBatchService.valuationDateKey = valuationDateKey;
	}

}
