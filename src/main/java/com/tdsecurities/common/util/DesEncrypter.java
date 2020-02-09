/**
 * Copyright (c) 2010 TD Bank Financial Group. All Rights Reserved.
 **/
package com.tdsecurities.common.util;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * The Class DesEncrypter.
 */
public class DesEncrypter {
	
	/** The Constant log. */
	private static final Logger logger = Logger.getLogger(DesEncrypter.class);
	
	/** The encrypter. */
	private static DesEncrypter encrypter;
	
	/** The ecipher. */
	private Cipher ecipher;
	
	/** The dcipher. */
	private Cipher dcipher;

	/** The 8-byte Salt. */
	private byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, 
			(byte) 0x56, (byte) 0x35, (byte) 0xE3,
			(byte) 0x03};

	/**  Iteration count. */
	private int iterationCount = 19;
	
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			logger.error("Error! Please enter a password to encrypt.");
		}
		String pwd = args[0];
		DesEncrypter enc = new DesEncrypter(pwd);
		String encryptedPwd = enc.encrypt(pwd);
		logger.info(encryptedPwd);
	}

	/**
	 * Instantiates a new des encrypter.
	 * 
	 * @param p_passPhrase the pass phrase
	 */
	private DesEncrypter(String p_passPhrase) {
		try {
			// Create the key
			KeySpec l_keySpec = new PBEKeySpec(p_passPhrase.toCharArray());
			SecretKey l_key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(l_keySpec);
			ecipher = Cipher.getInstance(l_key.getAlgorithm());
			dcipher = Cipher.getInstance(l_key.getAlgorithm());

			// Prepare the parameter to the ciphers
			AlgorithmParameterSpec l_paramSpec = new PBEParameterSpec(salt, iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, l_key, l_paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, l_key, l_paramSpec);
		} catch (InvalidAlgorithmParameterException l_ex) {
			logger.error("InvalidAlgorithmParameterException occured while instantiating encrypter");
		} catch (InvalidKeySpecException l_ex) {
			logger.error("InvalidKeySpecException occured while instantiating encrypter");
		} catch (NoSuchPaddingException l_ex) {
			logger.error("NoSuchPaddingException occured while instantiating encrypter");
		} catch (NoSuchAlgorithmException l_ex) {
			logger.error("NoSuchAlgorithmException occured while instantiating encrypter");
		} catch (InvalidKeyException l_ex) {
			logger.error("InvalidKeyException occured while instantiating encrypter");
		}
	}

	/**
	 * Gets the single instance of DesEncrypter.
	 * 
	 * @return single instance of DesEncrypter
	 */
	public static DesEncrypter getInstance() {
		if (null == encrypter) {
			encrypter = new DesEncrypter("passphrase");
		}
		
		return encrypter;
	}
	
	/**
	 * Encrypt.
	 * 
	 * @param p_str the str
	 * 
	 * @return the string
	 */
	public String encrypt(String p_str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] l_utf8 = p_str.getBytes("UTF8");

			// Encrypt
			byte[] l_enc = ecipher.doFinal(l_utf8);

			// Encode bytes to base64 to get a string
			return encode(l_enc);
		} catch (BadPaddingException l_ex) {
			logger.error("BadPaddingException occured while instantiating encrypter");
		} catch (IllegalBlockSizeException l_ex) {
			logger.error("IllegalBlockSizeException occured while instantiating encrypter");
		} catch (UnsupportedEncodingException l_ex) {
			logger.error("UnsupportedEncodingException occured while instantiating encrypter");
		}
		return null;
	}

	/**
	 * Decrypt.
	 * 
	 * @param p_str the str
	 * 
	 * @return the string
	 */
	public String decrypt(String p_str) {
		try {
			// Decode base64 to get bytes
			byte[] l_dec = decode(p_str);

			// Decrypt
			byte[] l_utf8 = dcipher.doFinal(l_dec);

			// Decode using utf-8
			return new String(l_utf8, "UTF8");
		} catch (BadPaddingException l_ex) {
			logger.error("BadPaddingException occured while decrypting");
		} catch (IllegalBlockSizeException l_ex) {
			logger.error("IllegalBlockSizeException occured while decrypting");
		} catch (UnsupportedEncodingException l_ex) {
			logger.error("UnsupportedEncodingException occured while decrypting");
		}
		return null;
	}
	
	/**
	 * Encode.
	 * 
	 * @param p_str the str
	 * 
	 * @return the string
	 */
	public String encode(byte[] p_bytes) {
		try {
			byte[] l_bytes = Base64.encodeBase64(p_bytes);
			return new String(l_bytes, "UTF-8");
		} catch (UnsupportedEncodingException l_ex) {
			logger.error("UnsupportedEncodingException occured while encoding");
		}
		return null;
	}
	
	/**
	 * Decode.
	 * 
	 * @param p_str the str
	 * 
	 * @return the string
	 */
	public byte[] decode(String p_str) {
		try {
			byte[] l_bytes = Base64.decodeBase64(p_str.getBytes("UTF8"));
			return l_bytes;
		} catch (UnsupportedEncodingException l_ex) {
			logger.error("UnsupportedEncodingException occured while decoding");
		}
		return null;
	}
}
