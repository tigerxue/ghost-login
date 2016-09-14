/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package zz.pseas.ghost.login.jd;

import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import net.sf.json.JSONObject;
import zz.pseas.ghost.client.GhostClient;

/**
 * @date 2016年9月14日 下午9:26:00
 * @version
 * @since JDK 1.8
 */
public class JDLogIn {
	public static void main(String[] args) {
		
		String jduserNmae="JDname";
		String jdpassWord="JDpasssword";
		
		
		JDCookie cookieParamSupplier = new JDCookie();
		cookieParamSupplier.init();
		CookieStore cookies = cookieParamSupplier.supplyCookies();
		for (Cookie c : cookies.getCookies()) {
			System.out.println(c.getName() + " -> " + c.getValue());
		}
		Map<String, String> map = cookieParamSupplier.supplyParams();

		// 关闭浏览器
		cookieParamSupplier.close();

		map.put("loginname", jduserNmae);
		map.put("loginpwd", jdpassWord);
		map.put("nloginpwd", jdpassWord);

		GhostClient client = new GhostClient("GBK", cookies);

		String url = "https://passport.jd.com/uc/loginService?";
		url = url + "&uuid=" + map.get("uuid") + "&&r=" + Math.random() + "version=2015";

		String ans = client.post(url, map);

		JSONObject jso = JSONObject.fromObject(ans.substring(1, ans.length() - 1));
		System.out.println(jso.get("username"));

		System.out.println("==================");
		System.out.println(url);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
	}

}
