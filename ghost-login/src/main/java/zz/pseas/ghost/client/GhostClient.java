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
package zz.pseas.ghost.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import zz.pseas.ghost.utils.DownloadUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class GhostClient {
	private String charset = "GBK";

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	private CloseableHttpClient client = ClientFactory.getNewClient();
	private HttpClientContext context = HttpClientContext.create();

	public GhostClient() {
		super();
	}

	public GhostClient(String charset) {
		super();
		this.charset = charset;
		context =  HttpClientContext.create();
	}

	public GhostClient(String charset, CookieStore cookies) {
		this(charset);
		context.setCookieStore(cookies);
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

	public HttpClientContext getContext() {
		return context;
	}

	public void setContext(HttpClientContext context) {
		this.context = context;
	}

	public String execute(HttpUriRequest request, String charset) {
		DownloadUtil.setUserAgent(request);
		String ans = null;
		try {
			CloseableHttpResponse resp = client.execute(request, context);
			ans = EntityUtils.toString(resp.getEntity(), charset);
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			ans = null;
		} finally {
			if (request instanceof HttpGet) {
				((HttpGet) request).releaseConnection();
			} else if (request instanceof HttpPost) {
				((HttpPost) request).releaseConnection();
			} else {

			}
		}
		return ans;
	}

	public String execute(HttpUriRequest request) {
		return execute(request, charset);
	}

	public String post(String url, Map<String, String> params) {
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> arr = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			arr.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(arr, "utf-8");
			post.setEntity(entity);
			String ans = execute(post);
			return ans;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} finally {
			post.releaseConnection();
		}
	}

	public CommonsPage postForPage(String url, Map<String, String> params) {
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> arr = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			arr.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		CommonsPage commonsPage = new CommonsPage();
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(arr, "utf-8");
			post.setEntity(entity);

			CloseableHttpResponse resp = client.execute(post, context);
			Header[] headers = resp.getAllHeaders();
			for (Header h : headers) {
				String k = h.getName();
				String v = h.getValue();
				commonsPage.addHeader(k, v);
			}

			byte[] bytes = EntityUtils.toByteArray(resp.getEntity());
			commonsPage.setContents(bytes);
		} catch (Exception e) {
			commonsPage.setErrMsg(e.getMessage());
		} finally {
			post.releaseConnection();
		}
		return commonsPage;
	}

	public String get(String url) {
		HttpGet get = new HttpGet(url);
		return execute(get);
	}

	public void showCookies() {
		List<Cookie> cookies = context.getCookieStore().getCookies();
		for (Cookie c : cookies) {
			System.out.println(c.toString());
		}
	}

	public byte[] getBytes(String url) {

		HttpGet get = new HttpGet(url);
		DownloadUtil.setUserAgent(get);
		CloseableHttpResponse resp;
		try {
			resp = client.execute(get, context);
			byte[] bs = EntityUtils.toByteArray(resp.getEntity());
			EntityUtils.consume(resp.getEntity());
			get.releaseConnection();
			return bs;
		} catch (IOException e) {
			return null;
		}

	}

	public String getCookieValue(String cookieName) {
		List<Cookie> cs = context.getCookieStore().getCookies();
		for (Cookie c : cs) {
			if (c.getName().equals(cookieName)) {
				return c.getValue();
			}
		}
		return null;
	}

	public void addCookie(String k, String v) {
		BasicClientCookie cookie = new BasicClientCookie(k, v);
		cookie.setDomain(".taobao.com");
		CookieStore cookieStore = context.getCookieStore();
		if (cookieStore == null) {
			context.setCookieStore(new BasicCookieStore());
		}
		context.getCookieStore().addCookie((Cookie) cookie);
	}

	public static void main(String[] args) throws Exception {
		GhostClient client = new GhostClient("utf-8");
		/*
		 * client.get("http://www.baidu.com"); client.addCookie("k1", "v1");
		 * client.showCookies();
		 */
		client.importFirefoxCookie("g:/cookies.txt");
		client.showCookies();

	}

	public void addFirefoxCookie(String v) {

	}

	public void importFirefoxCookie(String path) throws Exception {
		FileReader reader = new FileReader(new File(path));
		BufferedReader r2 = new BufferedReader(reader);
		while (true) {
			String s = r2.readLine();
			if (s == null)
				break;
			int n = s.indexOf(";");
			String s1 = s.substring(0, n);
			String k = s1.substring(0, s1.indexOf("="));
			String v = s.substring(s.indexOf("=") + 1, s1.length());

			addCookie(k, v);
		}
		r2.close();
	}

}
