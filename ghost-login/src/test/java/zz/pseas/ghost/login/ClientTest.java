package zz.pseas.ghost.login;

import zz.pseas.ghost.client.GhostClient;

public class ClientTest {
	public static void main(String[] args) {
		GhostClient client = new GhostClient();
		String s = client.get("http://www.baidu.com");
		System.out.println(s);
	}

}
