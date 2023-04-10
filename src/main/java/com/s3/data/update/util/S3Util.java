package com.s3.data.update.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3Util {
	private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	public static AmazonS3 s3Client = null ;
	
	/**
	 * Store Data To s3
	 * 
	 * 
	 * @param dataTobeUpdated
	 * 
	 * @return file
	 */

	public static String storeDataToS3(String dataTobeUpdated) throws Exception {
		String bucketName  =  System.getenv(UtilConstants.BUCKET_NAME);
		String region = System.getenv(UtilConstants.REGION);
		s3Client = AmazonS3ClientBuilder.defaultClient();
		if(!s3Client.doesBucketExist(bucketName)) {
			LOGGER.info("Bucket does not exists");
			s3Client.createBucket(new CreateBucketRequest(bucketName, region));
		}
		String fileName = generateFileName();
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, writeDataToFile(fileName,dataTobeUpdated)));
		return "Success";
	}

	/**
	 * Write Data To File
	 * 
	 * @param fileName
	 * 
	 * @param dataTobeUpdated
	 * 
	 * @return file
	 */

	private static File writeDataToFile(String fileName, String dataTobeUpdated) throws IOException {
		LOGGER.info("Writing data into file : {}", fileName);
		String fileType = UtilConstants.FILE_TYPE;
		Writer writer = null;
		File file = null;
		try {
			file = File.createTempFile(fileName, fileType);
			file.deleteOnExit();
			writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write(dataTobeUpdated);
		} catch (IOException e) {
			throw e;
		} finally {
			writer.close();
		}
		
		return file;
	}

	/**
	 * Generate the file name
	 * @return fileName
	 */

	private static String generateFileName() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd/hh");
		String path = dateFormat.format(now);
		long epochTime = System.currentTimeMillis();
		String fileName = path + UtilConstants.SLASH + epochTime;
		return fileName;

	}

}
