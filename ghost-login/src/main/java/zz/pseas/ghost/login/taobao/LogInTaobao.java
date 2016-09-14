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
package zz.pseas.ghost.login.taobao;

import java.util.HashMap;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.jsoup.Jsoup;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import zz.pseas.ghost.browser.BrowserFactory;
import zz.pseas.ghost.client.GhostClient;
import zz.pseas.ghost.utils.DownloadUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class LogInTaobao {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		
		String tbuserNmae="TBname";
		String tbpassWord="TBpasssword";
		
		String url = "https://login.taobao.com/member/login.jhtml";
		WebDriver ie = BrowserFactory.getIE();
		ie.get(url);

		Thread.sleep(5000L);

		Set<Cookie> cookies = ie.manage().getCookies();
		CookieStore store = DownloadUtil.convertToCookieStore(cookies);

		GhostClient client = new GhostClient("utf-8", store);

		String html = ie.getPageSource();
		String pbk = Jsoup.parse(html).select("input#J_PBK").attr("value");
		String pwd1 = RsaUtil.enCode(pbk, "10001", tbuserNmae);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TPL_username", tbuserNmae);
		map.put("TPL_password", tbpassWord);
		map.put("fc", "default");
		map.put("from", "tb");
		map.put("gvfdcname", "10");
		map.put("keyLogin", "false");
		map.put("loginASR", "1");
		map.put("loginASRSuc", "1");
		map.put("loginType", "3");
		map.put("loginsite", "0");
		map.put("naviVer", "firefox|47");

		String ncoToken = Jsoup.parse(html).select("input#J_NcoToken").attr("value");
		map.put("ncoToken", ncoToken);

		map.put("newMini", "false");
		map.put("newMini2", "false");
		map.put("newlogin", "0");
		map.put("osVer", "windows|6.1");
		map.put("oslanguage", "zh-cn");
		map.put("qrLogin", "true");
		map.put("slideCodeShow", "false");
		map.put("sr", "1920*1080");

		String ua = Jsoup.parse(html).select("input#UA_InputId").attr("value");
		//map.put("ua", ua);

		String umToken = Jsoup.parse(html).select("input[name=um_token]")
		.attr("value");
		map.put("um_token", umToken); // TODO get um_token

		String ans = client.post("https://login.taobao.com/member/login.jhtml", map);
		System.out.println(ans);
	}
}
