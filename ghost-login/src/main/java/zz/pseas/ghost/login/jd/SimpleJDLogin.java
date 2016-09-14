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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import zz.pseas.ghost.utils.BrowserUtil;
import zz.pseas.ghost.utils.ImgUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class SimpleJDLogin {
	public static void main(String[] args) throws IOException {
		
		String jduserNmae="youname";
		String jdpassWord="youpasssword";
		
		JDCookie cookieParamSupplier = new JDCookie();
		cookieParamSupplier.init();
		
		WebDriver browser = cookieParamSupplier.getBrowser();
		
		browser.findElement(By.id("loginname")).clear();
		browser.findElement(By.id("loginname"))
		.sendKeys(jduserNmae);
		
		browser.findElement(By.id("nloginpwd")).clear();
		browser.findElement(By.id("nloginpwd"))
		.sendKeys(jdpassWord);
		
		
		WebElement authCode = browser.findElement(By.id("authcode"));
		if(authCode.isDisplayed()){
			byte[] bytes = BrowserUtil.captureScreenShotById(browser, "JD_Verification1");
			
			BufferedImage im = ImgUtil.bytesToBImage(bytes);
			ImageIO.write(im, "jpg", new File("d:/jd.jpg"));
			
			browser.findElement(By.id("authcode")).clear();
			browser.findElement(By.id("authcode"))
			.sendKeys("YYEU");
		}
		
		browser.findElement(By.id("loginsubmit"))
		.click();
	}

}
