/** 
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zz.pseas.ghost.utils.LoginUtils;

/**
 * @date 2016年6月24日 下午12:35:32
 * @version
 * @since JDK 1.8
 */
public class SinaWeiboLogin {

	/**
	 * @param args
	 * @since JDK 1.8
	 */

	public static final Logger LOG = LoggerFactory.getLogger(SinaWeiboLogin.class);

	public static String getSinaCookie(String username, String password) throws Exception {

		StringBuilder sb = new StringBuilder();
		HtmlUnitDriver driver = new HtmlUnitDriver(true);
		// driver.setSocksProxy("127.0.0.1", 1080);
		// driver.setProxy("127.0.0.1", 1080);
		// HtmlOption htmlOption=new Ht
		// WebDriver driver = new FirefoxDriver();
		driver.setJavascriptEnabled(true);
		// user agent switcher//
		String loginAddress = "http://login.weibo.cn/login/";
		driver.get(loginAddress);
		WebElement ele = driver.findElementByCssSelector("img");
		String src = ele.getAttribute("src");
		String cookie = concatCookie(driver);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom()  
		        .setSocketTimeout(1000)  
		        .setConnectTimeout(1000)
		        .setCookieSpec(cookie)
		        .build();  
		
		HttpGet httpget = new HttpGet(src);
		httpget.setConfig(requestConfig);
		CloseableHttpResponse response = httpclient.execute(httpget);

		HttpEntity entity;
		String result=null;
		try {

			if(response.getStatusLine().getStatusCode()!= HttpURLConnection.HTTP_OK){
				// try again//
			}
			entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					// do something useful
					InputStream inputStream = entity.getContent();
					BufferedImage img = ImageIO.read(inputStream);
					// deal with the weibo captcha //
					String picName = LoginUtils.getCurrentTime("yyyyMMdd-hhmmss");
					// 新建captcha目录//
					LoginUtils.getCaptchaDir();
					picName = "./captcha/captcha-" + picName + ".png";
					ImageIO.write(img, "png", new File(picName));
					String userInput = new CaptchaFrame(img).getUserInput();
					WebElement mobile = driver.findElementByCssSelector("input[name=mobile]");
					mobile.sendKeys(username);
					WebElement pass = driver.findElementByCssSelector("input[name^=password]");
					pass.sendKeys(password);
					WebElement code = driver.findElementByCssSelector("input[name=code]");
					code.sendKeys(userInput);
					WebElement rem = driver.findElementByCssSelector("input[name=remember]");
					rem.click();
					WebElement submit = driver.findElementByCssSelector("input[name=submit]");
					// 错误捕获//
					submit.click();
					result = concatCookie(driver);
					driver.close();
				} finally {
					instream.close();
				}
			}
		} finally {
			response.close();
		}

		if (result.contains("gsid_CTandWM")) {
			return result;
		} else {
			// throw new Exception("weibo login failed");
			return null;
		}
	}

	public static String retryGetSinaCookie(String username, String password) {
		String cookie = "";
		//
		try {
			do {
				cookie = "";
				cookie = getSinaCookie(username, password);
				// 没有得到正确的cookie则间隔10秒钟继续响应服务器//
				if (!StringUtils.isEmpty(cookie)) {
					break;
				} else {
					LOG.error("response server failed,will sleep 10 seconds,then try again...");
					LoginUtils.sleep(10 * 1000);
				}
			} while (true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookie;

	}

	public static String concatCookie(HtmlUnitDriver driver) {
		Set<Cookie> cookieSet = driver.manage().getCookies();
		StringBuilder sb = new StringBuilder();
		for (Cookie cookie : cookieSet) {
			sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
		}
		String result = sb.toString();
		return result;
	}

	public static class CaptchaFrame {

		JFrame frame;
		JPanel panel;
		JTextField input;
		int inputWidth = 100;
		BufferedImage img;
		String userInput = null;

		public CaptchaFrame(BufferedImage img) {
			this.img = img;
		}

		public String getUserInput() {
			frame = new JFrame("Captcha Input");
			final int imgWidth = img.getWidth();
			final int imgHeight = img.getHeight();
			int width = imgWidth * 2 + inputWidth * 2;
			int height = imgHeight * 4;
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int startx = (dim.width - width) / 2;
			int starty = (dim.height - height) / 2;
			frame.setBounds(startx, starty, width, height);
			frame.setTitle("新浪微博验证码输入框");
			// String iconPath = "./icon/weibo-4.png";
			String iconPath = "./icon/weibo-1.jpg";
			ImageIcon testImg = new ImageIcon(new String(iconPath));
			frame.setIconImage(testImg.getImage());
			// 得到一个Toolkit对象
			// Toolkit tool = frame.getToolkit();
			// 由tool获取图像
			// Image myimage = tool.getImage(iconPath);
			// frame.setIconImage(myimage);
			Container container = frame.getContentPane();
			container.setLayout(new BorderLayout());
			panel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, imgWidth * 2, imgHeight * 2, null);
				}
			};
			panel.setLayout(null);
			container.add(panel);
			input = new JTextField(6);
			input.setBounds(imgWidth * 2, 0, inputWidth, imgHeight * 2);
			panel.add(input);
			JButton btn = new JButton("确定");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					userInput = input.getText().trim();
					synchronized (CaptchaFrame.this) {
						CaptchaFrame.this.notify();
					}
				}
			});
			input.addKeyListener(new KeyAdapter() {
				// 输入内容若为回车键则结束，捕获该事件//
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						userInput = input.getText().trim();
						synchronized (CaptchaFrame.this) {
							CaptchaFrame.this.notify();
						}
					}
				}
			});

			// ============================
			btn.setBounds(imgWidth * 2 + inputWidth, 0, inputWidth, imgHeight * 2);
			panel.add(btn);
			frame.setVisible(true);
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			frame.dispose();
			return userInput;
		}
	}
}
