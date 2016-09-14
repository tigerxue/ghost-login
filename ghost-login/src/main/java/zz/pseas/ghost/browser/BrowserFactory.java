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
package zz.pseas.ghost.browser;

import java.io.IOException;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class BrowserFactory {
	
	public static void main(String[] args) {
		WebDriver fireFox = BrowserFactory.getChrome();
		fireFox.close();
	}
	
	public static WebDriver getIE() {
		try {
			DriverInitter.init();
		} catch (IOException e) {
			return null;
		}
		WebDriver browser = new InternetExplorerDriver();
		return browser;
	}

	public static WebDriver getPhantomJS() {
		try {
			DriverInitter.init();
		} catch (IOException e) {
			return null;
		}
		WebDriver browser = new PhantomJSDriver();
		return browser;
	}

	public static WebDriver getFireFox() {
		try {
			DriverInitter.init();
		} catch (IOException e) {
			return null;
		}
		WebDriver browser = new FirefoxDriver();
		return browser;
	}
	
	public static WebDriver getChrome() {
		try {
			DriverInitter.init();
		} catch (IOException e) {
			return null;
		}
		WebDriver browser = new ChromeDriver();
		return browser;
	}

	

	public static CookieStore getCookieStore(WebDriver driver) {
		CookieStore store = new BasicCookieStore();
		Set<Cookie> cs = driver.manage().getCookies();
		System.out.println(cs.size());
		for (Cookie c : cs) {
			BasicClientCookie c1 = new BasicClientCookie(c.getName(), c.getValue());
			if (c.getDomain() == null) {
				System.out.println(c.getName() + "->" + c.getValue());
			} else {
				System.out.println(c.getDomain());
			}

			c1.setDomain(c.getDomain() == null ? "my.alipay.com" : c.getDomain());
			c1.setPath(c.getPath());
			c1.setExpiryDate(c.getExpiry());
			store.addCookie(c1);
		}
		System.out.println(store.getCookies().size());
		return store;
	}

}
