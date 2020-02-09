package com.tdsecurities.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class PropCache {
   static Logger  logger = Logger.getLogger(PropCache.class.getName());
   private static PropCache instance = null;
   private Map<String,String> hm = new HashMap<String,String>();
   
   public static PropCache getInstance() {
      
      if (instance == null) {
         instance = new PropCache();
      }
      
      return instance;
   }
   
   public void addCache(String key, String value) {
      hm.put(key, value);
   }
   
   public String getCache(String key) {
      String value = (String) hm.get(key);

      return value;
   }

   public Map<String, String> getHm() {
	   return hm;
   }

   public void setHm(Map<String, String> hm) {
	   this.hm = hm;
   }
}
