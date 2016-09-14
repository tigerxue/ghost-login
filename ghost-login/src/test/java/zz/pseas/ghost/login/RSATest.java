package zz.pseas.ghost.login;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

public class RSATest {
	public static void main(String[] args) throws Exception {
		BigInteger pbk = new BigInteger(
				"9a39c3fefeadf3d194850ef3a1d707dfa7bec0609a60bfcc7fe4ce2c615908b9599c8911e800aff684f804413324dc6d9f982f437e95ad60327d221a00a2575324263477e4f6a15e3b56a315e0434266e092b2dd5a496d109cb15875256c73a2f0237c5332de28388693c643c8764f137e28e8220437f05b7659f58c4df94685",
				16);
		BigInteger exponent = new BigInteger("10001");

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(pbk, exponent);
		
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		System.out.println(publicKey);
		
		String ans = encryptByPublicKey("123456",publicKey);
		
		System.out.println(ans.length());
		
		System.out.println(ans);
		String a = "5ac48b840c5112f3d6da1d0a30880"
		+ "72410bd1ad1f3a517b70e4ac41d143208ae4941e"
		+ "5da0b7f92209ac48b6d7c47dfad43783ecbda"
		+ "5adea357e590dc9d31a6a2f835252c19e80d63"
		+ "6333b8d1d9ea7e59a773467b39161faf822fdf"
		+ "c2e14439ae065cfd30377e532cc5d3ae0fb617"
		+ "cfa3e12e75daeb6624dd5a335d1bb1ca6626";
		
		System.out.println(a.length());
	}

	@SuppressWarnings("unused")
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		
		HttpClient client = null;
	
		CloseableHttpClient client1;
		
		
		
		
		return mi;
	}

	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

}
