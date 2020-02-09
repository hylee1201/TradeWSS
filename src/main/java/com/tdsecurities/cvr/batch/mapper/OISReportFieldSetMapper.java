package com.tdsecurities.cvr.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.tdsecurities.cvr.batch.item.OISReport;
import com.tdsecurities.cvr.util.DataHolder;

public class OISReportFieldSetMapper implements FieldSetMapper<OISReport> {

	@Override
	public OISReport mapFieldSet(FieldSet fieldSet) throws BindException {
		OISReport report = new OISReport();
		report.setId(fieldSet.readString(DataHolder.tradeIDIDx));
		report.setPolicy(fieldSet.readString(DataHolder.policyIDx));
		report.setTradeStatus(fieldSet.readString(DataHolder.tradeStatusIDx));
		report.setMtmCadStr(fieldSet.readString(DataHolder.mtmCadIDx));
		report.setMaturityDate(fieldSet.readString(DataHolder.tradeMatrueDateIDx));
		return report;
	}
}