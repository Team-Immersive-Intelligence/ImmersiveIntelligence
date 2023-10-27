package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author GabrielV
 * @version 1.0.0
 * This class is used for encrypting data using XOR Cipher.
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
		byte[] decoded = Base64.getDecoder().decode(data);
		return processData(decoded, password);
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
		return new String(decrypt(data, password));
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