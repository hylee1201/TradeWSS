package com.tdsecurities.cvr.model;

import java.util.Date;

/**
 * @author wangp4
 *
 */
public class DataTrasferVO {

	private int requestId;;
	private String groupType;
	private Date valuationDate;
	private String statusStr;
	private String description;
	private int deliveryType=0;
	private String transferDescription="DATA UPLOAD";
	
	public DataTrasferVO() {}
	
	public DataTrasferVO (int requestId, String groupType, Date valuationDate, boolean status,boolean warning,String message) {
		this.requestId=requestId;
		this.groupType = groupType;
		this.valuationDate = valuationDate;
		
		if(status){
			if(!warning){
				statusStr="SUCCESSFUL";
			}else{
				statusStr="SUCCESSFULWITHERRORS";
				if(message!=null) {
					if(message.length()<200){
						this.description=message;
					}else{
						this.description=message.substring(0,199);
					}
					
				}
			}
		}else{
			statusStr="ERROR";
			if(message!=null) {
				if(message.length()<200){
					this.description=message;
				}else{
					this.description=message.substring(0,199);
				}
				
			}
		}
		
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Date getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public String getStatus() {
		return statusStr;
	}

	public void setStatus(String status) {
		this.statusStr = status;
	}

	public int getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(int deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getTransferDescription() {
		return transferDescription;
	}

	public void setTransferDescription(String transferDescription) {
		this.transferDescription = transferDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

}
