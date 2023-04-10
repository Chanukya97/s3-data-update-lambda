package com.s3.data.update.util;

import java.lang.invoke.MethodHandles;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HTTPUtil {
	private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	private static CloseableHttpClient httpClient;
	
	/**
	 * make API Call
	 * 
	 * 
	 * @param dataUrl
	 * 
	 * @return responseEntity
	 */
	public static String makeAPICall(String dataUrl) throws Exception {
		
		try {
			httpClient = getCloseableHttpClient(1000000);
			HttpGet request = new HttpGet(dataUrl);
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity httpEntity = response.getEntity();
			LOGGER.info("Status Code = {}, Status Description = {}",response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
			String responseEntity = EntityUtils.toString(httpEntity);
			return responseEntity;
		} catch (Exception e) {
			LOGGER.error("Exception occured during http call {}",e.getMessage());
			throw e;
		}
		
	}
	
	/**
	 * get CloseableHttpClient
	 * 
	 * 
	 * @param timeout
	 * 
	 * @return httpClient
	 */
	private static CloseableHttpClient getCloseableHttpClient(int timeout) throws Exception{
		LOGGER.info("TimeOut : {}", timeout);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).
											setConnectionRequestTimeout(timeout).setCookieSpec(CookieSpecs.STANDARD).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(config).disableContentCompression().build();
		
		return httpClient;
	}
}
