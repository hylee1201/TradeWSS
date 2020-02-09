package com.tdsecurities.cvr.batch.wss;

import org.apache.log4j.Logger;
import org.springframework.batch.item.file.LineCallbackHandler;

import com.tdsecurities.cvr.util.DataHolder;

import static com.tdsecurities.cvr.batch.constants.Consts.*;

public class WSSHeaderHandler implements LineCallbackHandler {

	private static final Logger logger = Logger.getLogger(WSSHeaderHandler.class);
	
	@Override
	public void handleLine(String line) {
		String[] headers=line.split(",");
		for(int i=0;i<headers.length;i++){
			if(headers[i].contains(POLICYHEADER)){
				DataHolder.policyIDx=i;
			}
			if(headers[i].contains(TRADEIDHEADER)){
				DataHolder.tradeIDIDx=i;
			}
			if(headers[i].contains(TRADEMATRUEDATEHEADER)){
				DataHolder.tradeMatrueDateIDx=i;
			}
			if(headers[i].contains(MTMCADHEADER)){
				DataHolder.mtmCadIDx=i;
			}
			if(headers[i].contains(TRADESTATUSHEADER)){
				DataHolder.tradeStatusIDx=i;
			}
		}
		
		if(DataHolder.policyIDx!=null){
			logger.info("Found policy column index:"+DataHolder.policyIDx);
		}else{
			logger.error("Can't find policy column in feed, Please verify.");
			throw new RuntimeException("Can't find policy column in feed, Please verify.");  
		}
		if(DataHolder.tradeIDIDx!=null){
			logger.info("Found Description column index:"+DataHolder.tradeIDIDx);
		}else{
			logger.error("Can't find Description column in feed, Please verify.");
			throw new RuntimeException("Can't find Description column in feed, Please verify.");  
		}
		if(DataHolder.tradeMatrueDateIDx!=null){
			logger.info("Found MaturityDate column index:"+DataHolder.tradeMatrueDateIDx);
		}else{
			logger.error("Can't find MaturityDate column in feed, Please verify.");
			throw new RuntimeException("Can't find MaturityDate column in feed, Please verify.");  
		}
		if(DataHolder.mtmCadIDx!=null){
			logger.info("Found UQL_OC_MMB_MS column index :"+DataHolder.mtmCadIDx);
		}else{
			logger.error("Can't find UQL_OC_MMB_MS column in feed, Please verify.");
			throw new RuntimeException("Can't find UQL_OC_MMB_MS column in feed, Please verify.");  
		}
		if(DataHolder.tradeStatusIDx!=null){
			logger.info("Found TradeStatus column index:"+DataHolder.tradeStatusIDx);
		}else{
			logger.error("Can't find TradeStatus column in feed, Please verify.");
			throw new RuntimeException("Can't find TradeStatus column in feed, Please verify.");  
		}
		
	}

}
