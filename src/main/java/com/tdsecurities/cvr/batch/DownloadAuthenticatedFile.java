package com.tdsecurities.cvr.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;


public class DownloadAuthenticatedFile{
	private static final Logger logger = Logger.getLogger(DownloadAuthenticatedFile.class);
	
    public static void main(String... args){

        try{
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response =null;
            HttpEntity entity =null;
            HttpGet httpget = null;

//            HttpGet httpget = new HttpGet("http://tovalrs03.cibg.tdbank.ca:6789");
//
//            HttpResponse response = httpclient.execute(httpget);
//            HttpEntity entity = response.getEntity();
//
//            logger.info("Login form get: " + response.getStatusLine());
//            if (entity != null) {
//                entity.consumeContent();            
//            }
//            logger.info("Initial set of cookies:");
//            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
//            if (cookies.isEmpty()) {
//                logger.info("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
//                    logger.info("- " + cookies.get(i).toString());
//                }
//            }

            HttpPost httpost = new HttpPost("http://tovalrs03.cibg.tdbank.ca:6789/login");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "CVRWSSOIS"));
            nvps.add(new BasicNameValuePair("password", "tdbank10"));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            response = httpclient.execute(httpost);
            entity = response.getEntity();

            logger.info("Login form get: " + response.getStatusLine());
            if (entity != null) {
                entity.consumeContent();
            }

//            logger.info("Post logon cookies:");
//            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
//            String mySessionId = null;
//            if (cookies.isEmpty()) {
//                logger.info("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
//                    if(cookies.get(i).toString().contains("SessionId=")){
//                        int index1 = cookies.get(i).toString().indexOf("SessionId");
//                        String temp = cookies.get(i).toString().substring(index1);
//                        int index2 = temp.toString().indexOf("]");
//                        temp = cookies.get(i).toString().substring(index1+10,index1+index2);
//                        mySessionId = temp;
//                    }
//                    logger.info("- " + cookies.get(i).toString());
//                }
//            } 

            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            httpget = new HttpGet("http://tovalrs03.cibg.tdbank.ca:6789/download-report/DailyOISImpact/20151120/Global/TOR_OIS_MMB_IMPACT_20151120.csv");
 //                                  http://tovalrs03             :6789/download-report/DailyOISImpact/20151120/Global/TOR_OIS_MMB_IMPACT_20151120.csv
 //           nullVS_trade_MMB_20151120.csv
            response = httpclient.execute(httpget);
            entity = response.getEntity();
            logger.info("Content Type Name: "+entity.getContentType().getName());
            logger.info("Content Type: "+entity.getContentType().getValue());
            Header[] headers = response.getAllHeaders();
            for(int i=0;i<headers.length;i++){
                logger.info("Header: "+headers[i].toString());
            }
            logger.info(response.toString());

            logger.info("File get: " + response.getStatusLine());

            InputStream in = entity.getContent();
              File path = new File("/home/mrsevensevenseven");
              path.mkdirs();
              File file = new File(path, "myfile.xls");
              FileOutputStream fos = new FileOutputStream(file);

              byte[] buffer = new byte[1024];
              int len1 = 0;
              while ((len1 = in.read(buffer)) != -1) {
                      fos.write(buffer, 0, len1);
              }

              fos.close();

            httpclient.getConnectionManager().shutdown();
        }catch(Exception e){
            logger.error(e);
        }

    }

}