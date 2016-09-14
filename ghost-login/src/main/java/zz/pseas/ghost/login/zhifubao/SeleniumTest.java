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
package zz.pseas.ghost.login.zhifubao;

import org.apache.http.client.CookieStore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import zz.pseas.ghost.browser.BrowserFactory;
import zz.pseas.ghost.client.GhostClient;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class SeleniumTest {
	public static void main(String[] args) throws Exception {
		
		String zfbuserNmae="ZFBname";
		String zfbpassWord="ZFBpasssword";
		
		WebDriver driver = BrowserFactory.getPhantomJS();
		
		driver.get("https://auth.alipay.com/login/index.htm");
		
		driver.findElement(By.id("J-input-user")).clear();
		driver.findElement(By.id("J-input-user")).sendKeys(zfbuserNmae);
		driver.findElement(By.id("password_rsainput")).clear();
		driver.findElement(By.id("password_rsainput")).sendKeys(zfbpassWord);
		driver.findElement(By.id("J-login-btn"))
		.click();
		
		CookieStore store = BrowserFactory.getCookieStore(driver);
		
		GhostClient client = new GhostClient("utf-8", store);
		String url = "https://my.alipay.com/portal/i.htm?referer=https%3A%2F%2Fauth.alipay.com%2Flogin%2Findex.htm";
		String ans = client.get(url);
		System.out.println(ans);
	}
}
