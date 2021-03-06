package com.tdsecurities.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * This class is needed to send e-mail attachments
 * 
 * @author BRINZF2
 * 
 */
public class ByteArrayDataSource implements DataSource {

	private byte[] data;
	private String mimeType;

	/**
	 * Constructor
	 * 
	 * @param data
	 * @param mimeType
	 */
	public ByteArrayDataSource(byte[] data, String mimeType) {
		this.data = data;
		this.mimeType = mimeType;
	}

	/**
	 * This method returns the MIME type of the data in the form of a string. 
	 * 
	 * @return mimeType
	 */
	public String getContentType() {
		return mimeType;
	}

	/**
	 * Return the 'name' of this object where the name of the object is dependant on the nature of 
	 * the underlying objects. 
	 * 
	 * @return String the name of the object
	 */
	public String getName() {
		return "Anonymous ByteArrayDataSource";
	}

	/**
	 * This method returns an InputStream representing the the data and throws the 
	 * appropriate exception if it can not do so.
	 * 
	 * @return an InputStream
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(data);
	}

	/**
	 * This method returns an OutputStream where the data can be written and throws the appropriate 
	 * exception if it can not do so.
	 * 
	 * @return an OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("Output not supported");
	}

}
