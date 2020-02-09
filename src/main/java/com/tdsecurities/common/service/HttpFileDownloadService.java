package com.tdsecurities.common.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class HttpFileDownloadService extends IFileService {

	private static final Logger logger = Logger.getLogger(HttpFileDownloadService.class);
	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	private boolean login(HttpClient httpClient, URI uri) throws Exception {
		logger.info("Connecting and logging in to HTTP httpClient: " + httpClient + " ...");

		boolean result = false;
		HttpResponse response = null;

		HttpPost httpPost = new HttpPost(uri);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", this.userName));
		nvps.add(new BasicNameValuePair("password", this.password));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		response = httpClient.execute(httpPost);
		logger.info("Login:URL: " + uri);
		logger.info("Login form get: " + response.getStatusLine());
		if (response != null && response.getStatusLine().getStatusCode() == 302) {
			String location = response.getFirstHeader("location").getValue();
			if (location != null && location.contains("Authentication failed")) {
				logger.error("Authentication failed.");
			} else {
				result = true;
				logger.info("Authentication successful.");
			}
		} else {
			logger.error("Authentication failed.");
		}

		return result;
	}

	@Override
	public boolean downloadFile(String remotePath, String filename, String localPath) throws Exception {
		logger.info("Starting download process...");

		boolean result = false;
		HttpResponse response = null;
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();

		URI loginURI = new URIBuilder().setScheme(HTTP).setHost(this.serverHost).setPort(this.port).setPath("/login")
				.build();

		if (login(httpclient, loginURI)) {
			URI uri = new URIBuilder().setScheme(HTTP).setHost(this.serverHost).setPort(this.port)
					.setPath(remotePath + filename).build();
			logger.info("Download URL: " + uri.toString());
			HttpGet httpget = new HttpGet(uri);
			try {
				response = httpclient.execute(httpget);

				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if ("application/octet-stream".equals(entity.getContentType().getValue())) {
						BufferedInputStream bis = new BufferedInputStream(entity.getContent());
						String filePath = localPath + filename;
						logger.info("Downloading to: " + filePath);
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
						try {
							int inByte;
							while ((inByte = bis.read()) != -1)
								bos.write(inByte);
							result = true;
						} catch (Exception e) {
							logger.error("Byte writing exception.", e);
						} finally {
							bis.close();
							bos.close();
						}
					} else {
						logger.error("Content type of HTTP response is not application/octet-stream: " + entity.getContentType().getValue());
						Exception e = new Exception ("application/octet-stream exception");
						throw e;
					}
				} else {
					Exception e = new Exception ("Improper HTTP response != 200 for download");
					throw e;
				}
			} catch (Exception e) {
				logger.error("HTTP client, stream related exception.", e);
				throw e;
			}
		}

		logger.info("Download process complete.");
		return result;
	}

	@Override
	public boolean uploadFile(String path, String fileName, String localPath) throws Exception {
		logger.info("Starting upload process...");
		
		HttpResponse response = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		URI loginURI = new URIBuilder().setScheme(HTTPS).setHost(this.serverHost).setPort(this.port).setPath("/")
				.build();

		if (login(httpClient, loginURI)) {

			logger.info("Server host: " + this.serverHost + ".");
			logger.info("Server port: " + this.port + ".");
			try {
				URI uri = new URIBuilder().setScheme(HTTPS).setHost(this.serverHost).setPort(this.port).setPath(path)
						.build();

				logger.info("Upload URI: " + uri.toString());

				HttpPost httpPost = new HttpPost(uri);
				File XMLfile = new File(localPath + "\\" + fileName);
				FileEntity entity = new FileEntity(XMLfile);
				httpPost.setEntity(entity);

				response = httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				logger.error("HTTP protocol error.");
				logger.debug(e);
			} catch (IOException e) {
				logger.error("IO Exception. Connection aborted?");
				logger.debug(e);
			} catch (Exception e) {
				logger.error("URI exception", e);
			}

		}
		if(null == response){
			logger.info("Upload process failed.");
			return null == response;
		} else {
			logger.info("Upload process complete.");
			return null == response;
		}
	}
}
