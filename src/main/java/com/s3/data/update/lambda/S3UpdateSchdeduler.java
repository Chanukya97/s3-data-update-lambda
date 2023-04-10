package com.s3.data.update.lambda;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.s3.data.update.util.HTTPUtil;
import com.s3.data.update.util.S3Util;
import com.s3.data.update.util.UtilConstants;

public class S3UpdateSchdeduler {

	private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	/**
	 * handleRequest
	 *
	 * 
	 * @return uploadStatus
	 */
	public String handleRequest() {
		LOGGER.info("Started the Scheduler");
		String uploadStatus = "Failed ";
		try {
			String dataUrl = System.getenv(UtilConstants.DATAURL);
			String dataTobeUpdated = HTTPUtil.makeAPICall(dataUrl);
			uploadStatus = S3Util.storeDataToS3(dataTobeUpdated);
		} catch(Exception e) {
			uploadStatus += e.getMessage();
		}
		return uploadStatus;
		
	}
	
}
