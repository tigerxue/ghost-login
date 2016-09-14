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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import zz.pseas.ghost.browser.BrowserFactory;
import zz.pseas.ghost.browser.DriverInitter;
import zz.pseas.ghost.utils.StringUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
@SuppressWarnings("unused")
public class JDCookie {
	public static int maxCheckTimes = 10;
	public static int waitInterval = 100; // milliseconds
	public static String[] requiredCookies = { "3AB9D23F7A4B3C9B" };

	private WebDriver browser = null;
	private boolean cookieDone = false;
	private boolean paramDone = false;

	public void init() {
		boolean done = false;
		try {
			DriverInitter.init();
			browser = BrowserFactory.getIE();
			//browser = new InternetExplorerDriver();
			browser.manage().deleteAllCookies();
			browser.get("https://passport.jd.com/new/login.aspx");

			int count = 0;
			while (true && count < maxCheckTimes) {
				System.out.println("check for the " + (++count) + "th time...");
				done = isBrowserDone();
				if (done) {
					break;
				}
				Thread.sleep(waitInterval);
			}

			if (!done) {
				System.out.println("browser collection failed...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public boolean isBrowserDone() {
		String html = browser.getPageSource();
		if (!paramDone) {
			Document doc = Jsoup.parse(html);
			String eid = doc.select("input[name=eid]").first().attr("value");
			String fp = doc.select("input[name=fp]").first().attr("value");
			if (StringUtil.isNotEmpty(eid) && StringUtil.isNotEmpty(fp)) {
				paramDone = true;
				cookieDone = cookiesReady();
				return cookieDone;
			}
		} else {
			cookieDone = cookiesReady();
			return cookieDone;
		}
		return false;
	}

	public boolean cookiesReady() {
		Set<Cookie> cookies = browser.manage().getCookies();
		Set<String> ks = new HashSet<String>();
		for (Cookie c : cookies) {
			String k = c.getName();
			ks.add(k);
		}

		for (String k : requiredCookies) {
			if (!ks.contains(k)) {
				return false;
			}
		}

		return true;

	}

	public CookieStore supplyCookies() {
		if (!(paramDone && cookieDone)) {
			return null;
		}

		BasicCookieStore cookieStore = new BasicCookieStore();
		Set<Cookie> cookies = browser.manage().getCookies();
		for (Cookie c : cookies) {
			BasicClientCookie c1 = new BasicClientCookie(c.getName(), c.getValue());
			/*
			c1.setDomain(c.getDomain());
			c1.setPath(c.getPath());
			*/
			c1.setExpiryDate(c.getExpiry());
			cookieStore.addCookie(c1);
		}
		return cookieStore;
	}

	public Map<String, String> supplyParams() {
		if (!(paramDone && cookieDone)) {
			return null;
		}

		HashMap<String, String> map = new HashMap<String, String>();

		String html = browser.getPageSource();
		Document doc = Jsoup.parse(html);
		Elements inputs = doc.select("form[id=formlogin] input");
		for (Element input : inputs) {
			String k = input.attr("name");
			String v = input.attr("value");
			map.put(k, v);
		}

		return map;
	}

	public void close() {
		if (browser != null) {
			browser.close();
			browser.quit();
		}
	}
	
	

	public WebDriver getBrowser() {
		return browser;
	}

	public static void main(String[] args) {
		JDCookie cookieParamSupplier = new JDCookie();
		cookieParamSupplier.init();
		CookieStore cookieStore = cookieParamSupplier.supplyCookies();
		Map<String, String> map = cookieParamSupplier.supplyParams();
		System.out.println("done" + map + cookieStore);
		cookieParamSupplier.close();
	}
}
