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
package zz.pseas.ghost.utils;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import zz.pseas.ghost.browser.BrowserFactory;
/**   
* @date 2016年9月13日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class BrowserUtil {
	public static byte[] captureScreenShotById(WebDriver driver, String id) throws IOException {
		byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		WebElement ele = driver.findElement(By.id(id));
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);

		BufferedImage image = ImgUtil.bytesToBImage(bytes);

		ImageIO.write(image, "jpg", new File("d:/big.jpg"));
		in.close();
		System.out.println(image.getType());
	

		Point p = ele.getLocation();
		Dimension xy = ele.getSize();

		BufferedImage subImage = image.getSubimage(p.getX(), p.getY(), xy.getWidth(), xy.getHeight());
		ImageIO.write(subImage, "jpg", new File("d:/sc.jpg"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	
		ImageIO.write(subImage, "jpg", out);
		out.flush();
		byte[] res = out.toByteArray();
		out.close();
		return res;
	}

	public static void main(String[] args) throws IOException {
		WebDriver ie = BrowserFactory.getIE();
		ie.get("https://passport.jd.com/new/login.aspx");
		byte[] bytes = captureScreenShotById(ie, "loginsubmit");
		BufferedImage sc1 = ImgUtil.bytesToBImage(bytes);
		ImageIO.write(sc1, "jpg", new File("d:/sc1.jpg"));
	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

}
