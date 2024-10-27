package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This class is used for encrypting data using XOR Cipher.
 *
 * @author GabrielV
 * @author Avalon
 * @ii-approved 0.3.1
 * @since 18.09.2024
 */
public class Cryptographer
{
	private static byte[] processData(byte[] data, byte[] password)
	{
		int counter = 0;
		byte[] encrypted = new byte[data.length];

		for(int i = 0; i < data.length; i++)
		{
			if(counter >= password.length) counter = 0;
			encrypted[i] = (byte)(data[i]^password[counter]);
			counter++;
		}

		return encrypted;
	}

	/**
	 * Encrypt <code>data</code> using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be encrypted
	 * @param password Password for encryption
	 * @return Encrypted data
	 */
	public static byte[] encrypt(byte[] data, byte[] password)
	{
		return Base64.getEncoder().encode(processData(data, password));
	}

	/**
	 * Encrypt <code>data</code> using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be encrypted
	 * @param password Password for encryption
	 * @return Encrypted data
	 */
	public static byte[] encrypt(String data, String password)
	{
		return encrypt(data.getBytes(StandardCharsets.UTF_8), password.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Decrypt <code>data</code> using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be decrypted
	 * @param password Password for decryption (must be the same as for encryption)
	 * @return Decrypted data
	 */
	public static byte[] decrypt(byte[] data, byte[] password)
	{
		try
		{
			byte[] decoded = Base64.getDecoder().decode(data);
			return processData(decoded, password);
		} catch(IllegalArgumentException e)
		{
			//Log error and return original data packet if decryption fails
			IILogger.warn("Failed to decrypt data: "+e);
			return data; // Return original data without modifying
		}
	}

	/**
	 * Decrypt <code>data</code> using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be decrypted
	 * @param password Password for decryption (must be the same as for encryption)
	 * @return Decrypted data
	 */
	public static byte[] decrypt(String data, String password)
	{
		return decrypt(data.getBytes(StandardCharsets.UTF_8), password.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Decrypt <code>data</code> to string using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be decrypted
	 * @param password Password for decryption (must be the same as for encryption)
	 * @return Decrypted data
	 */
	public static String decryptToString(byte[] data, byte[] password)
	{
		try
		{
			return new String(decrypt(data, password), StandardCharsets.UTF_8);
		} catch(Exception e)
		{
			IILogger.warn("Failed to decrypt data to string: "+e);
			return new String(data, StandardCharsets.UTF_8); // Return original data
		}
	}

	/**
	 * Decrypt <code>data</code> to string using xor cipher with <code>password</code>
	 *
	 * @param data     Data to be decrypted
	 * @param password Password for decryption (must be the same as for encryption)
	 * @return Decrypted data
	 */
	public static String decryptToString(String data, String password)
	{
		return decryptToString(data.getBytes(StandardCharsets.UTF_8), password.getBytes(StandardCharsets.UTF_8));
	}
}