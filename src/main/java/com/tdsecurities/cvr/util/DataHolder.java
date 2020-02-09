package com.tdsecurities.cvr.util;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

	public static Integer policyIDx = null;
	public static Integer tradeIDIDx = null;
	public static Integer tradeMatrueDateIDx = null;
	public static Integer mtmCadIDx=null;
	public static Integer tradeStatusIDx= null;
	
	private List<String> warningIDs= new ArrayList<String>();

	public List<String> get() {
		return warningIDs;
	}

	public void set(List<String> warningIDs) {
		this.warningIDs = warningIDs;
	}
	
	public void add(String id){
		warningIDs.add(id);
	}
}