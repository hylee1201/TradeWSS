package com.tdsecurities.cvr.batch.item;

public class OISReport {
	
	private String id;
	private Double mtmCad;
	private Double mtmUsd;
	private String policy;
	private String tradeStatus;
	private String date;
	private String maturityDate;
	private String mtmCadStr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getMtmCad() {
		return mtmCad;
	}
	public void setMtmCad(Double mtmCad) {
		this.mtmCad = mtmCad;
	}
	public Double getMtmUsd() {
		return mtmUsd;
	}
	public void setMtmUsd(Double mtmUsd) {
		this.mtmUsd = mtmUsd;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDealNumber() {
		return id+"*";
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
	public String getMtmCadStr() {
		return mtmCadStr;
	}
	public void setMtmCadStr(String mtmCadStr) {
		this.mtmCadStr = mtmCadStr;
	}
	
}
