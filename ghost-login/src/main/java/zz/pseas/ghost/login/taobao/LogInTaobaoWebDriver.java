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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import zz.pseas.ghost.browser.BrowserFactory;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class LogInTaobaoWebDriver {
	public static void main(String[] args) throws InterruptedException {
		WebDriver ie = BrowserFactory.getIE();
		ie.get("https://login.taobao.com/member/login.jhtml");
		
		Thread.sleep(5000);
		
		String tbuserNmae="TBname";
		String tbpassWord="TBpasssword";
		
		WebElement btn = ie.findElement(By.id("J_Quick2Static"));
		if(btn!=null && btn.isDisplayed()){
			btn.click();
		}
		
		ie.findElement(By.id("TPL_username_1")).clear();
		ie.findElement(By.id("TPL_username_1")).sendKeys(tbuserNmae);
		
		ie.findElement(By.id("TPL_password_1")).clear();
		ie.findElement(By.id("TPL_password_1")).sendKeys(tbpassWord);
		
		ie.findElement(By.id("J_SubmitStatic")).click();
		
		Thread.sleep(10000L);
		
		String html = ie.getPageSource();
		
		System.out.println(html);
		String url = ie.getCurrentUrl();
		System.out.println(url);
		/*WebElement phoneCheck = ie.findElement(By.id("J_GetCode"));
		if(phoneCheck!=null && phoneCheck.isDisplayed()){
			WebElement btn2 = ie.findElement(By.id("J_GetCode"));
			btn2.click();
		}*/
		
		//Thread.sleep(5000);
		
		
		
	}
}
