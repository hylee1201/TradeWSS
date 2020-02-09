package com.tdsecurities.cvr.batch.wss;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.tdsecurities.cvr.batch.constants.Consts;
import com.tdsecurities.cvr.batch.item.OISReport;
import com.tdsecurities.cvr.util.BatchUtils;
import com.tdsecurities.cvr.util.DataHolder;

/**
 * filtering unneeded data
 * 
 * @author wangp4
 *
 */
public class WSSFilterItemProcessor implements
		ItemProcessor<OISReport, OISReport> {
	private static final Logger logger = Logger.getLogger(WSSFilterItemProcessor.class);
	private List<String> policies;
	private double rate;
	private Date date;
	private DataHolder warningDataHolder;

	@Override
	public OISReport process(OISReport item) throws Exception {
		if (!needsToBeFiltered(item)) {
			Double mtmCad = new Double(item.getMtmCadStr());
			item.setMtmCad(mtmCad);
			item.setMtmUsd(Math.round(item.getMtmCad()*100/rate)/100.0);
			item.setDate(BatchUtils.formatDateToStr(date, Consts.DATE_FORMAT_2));
			return item;
		} else {
			return null;
		}
	}

	private boolean needsToBeFiltered(OISReport item) throws Exception {

		boolean filter = true;
		if ("VERIFIED".equalsIgnoreCase(item.getTradeStatus())) {
			if (policies != null && policies.size() > 0
					&& policies.contains(item.getPolicy())) {
				if ("null".equalsIgnoreCase(item.getMaturityDate())
						|| StringUtils.isEmpty(item.getMaturityDate())) {
					raiseWarning(item.getId());

				} else if (!date.before(BatchUtils.parseDateStr(item
						.getMaturityDate()))) {
					logger.debug("Trade Number "
							+ item.getId()
							+ " skipped since Maturity Date is not after evaluation date.");
				} else if (StringUtils.isEmpty(item.getMtmCadStr())||"null".equalsIgnoreCase(item.getMtmCadStr())) {
					raiseWarning(item.getId());

				} else {
					filter = false;
				}
			}
		}
		return filter;
	}

	public void setPolicies(List<String> policies) {
		this.policies = policies;
	}

	public void setCadRate(float cadRate) {
		this.rate = cadRate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void raiseWarning(String id) {
		warningDataHolder.add(id);
	}

	public void setWarningDataHolder(DataHolder warningDataHolder) {
		this.warningDataHolder = warningDataHolder;
	}

}