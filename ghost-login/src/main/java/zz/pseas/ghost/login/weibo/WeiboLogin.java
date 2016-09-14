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
package zz.pseas.ghost.login.weibo;

import java.util.Date;
import java.util.Set;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import zz.pseas.ghost.browser.BrowserFactory;
import zz.pseas.ghost.client.GhostClient;
import zz.pseas.ghost.utils.StringUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class WeiboLogin {
	public static void main(String[] args) {
		WebDriver driver = BrowserFactory.getIE();

		driver.get("http://weibo.com/");

		Set<Cookie> cookies = driver.manage().getCookies();
		BasicCookieStore cookieStore = new BasicCookieStore();
		for (Cookie c : cookies) {
			BasicClientCookie c1 = new BasicClientCookie(c.getName(), c.getValue());
			c1.setDomain(c.getDomain() == null ? "weibo" : c.getDomain());
			c1.setPath(c.getPath());
			Date d = c.getExpiry();
			if (d != null) {
				c1.setExpiryDate(d);
			}
			cookieStore.addCookie(c1);
		}
		driver.quit();

		GhostClient client = new GhostClient("utf-8", cookieStore);
		String url = "http://weibo.com/p/10080813dc27e2acb1441006674c8aa2ef07d4/followlist?page=1#Pl_Core_F4RightUserList__38";
		//HttpGet get = new HttpGet(url);
		String s = client.get(url);
		System.out.println(s);
		String next = StringUtil.regex(s, "(?<=replace\\(\").*?(?=\")");
		String html = client.get(next);
		System.out.println(html);
	}
}
