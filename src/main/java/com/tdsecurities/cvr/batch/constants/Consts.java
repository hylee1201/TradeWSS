package com.tdsecurities.cvr.batch.constants;

/**
 * @author wangp4
 *
 */
public class Consts {

	public static final String DATE_FORMAT_1					= "MM-dd-yyyy";
	public static final String DATE_FORMAT_2					= "yyyy-MM-dd";
	public static final String DATE_FORMAT_3					= "yyyyMMdd";
	public static final String DATE_FORMAT_4					= "MMMMM dd, yyyy";
	public static final String DATE_FORMAT_5					= "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_6					= "MM/dd/yy";
	public static final String DATE_FORMAT_7					= "YYYYMMDD"; // used for Regular Expression replacement
	public static final String DATE_FORMAT_8					= "MMMMM yyyy";
	public static final String DATE_FORMAT_9					= "yyyy_MM_dd_HH_mm_ss";

	public static final String POLICYHEADER="\"Policy\"";
	public static final String TRADEIDHEADER="\"Description\"";
	public static final String TRADEMATRUEDATEHEADER="\"MaturityDate\"";
	public static final String MTMCADHEADER="\"UQL_OC_MMB_MS\"";
	public static final String TRADESTATUSHEADER="\"TradeStatus\"";
	
	//The following 4 values should match "job id" in each job configuration xml.
	public static final String JOB_REPORT_ARCHIVER = "reportArchiver";
	public static final String JOB_SUMMIT_FX = "summitFX";
	public static final String JOB_FXEOD = "fxeod";
	public static final String JOB_TRADE_MMB = "tradeMMB";
	public static final String JOB_TRADE_WSS = "wsstradeWSS";
	public static final String JOB_DIAMOND = "diamond";
	public static final String JOB_LAUNCHER = "jobLauncher";
	public static final String COMMONS_CONFIG_FACTORY_BEAN = "commonsConfigurationFactoryBean";
}
