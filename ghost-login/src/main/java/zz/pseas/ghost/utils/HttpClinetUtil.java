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


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * Http连接工具类
 * 
 * @author GS
 */
public class HttpClinetUtil {
	private static Logger LOG = Logger.getLogger(HttpClinetUtil.class);
	private static final HttpClient hc;
	static {
		hc = new HttpClient();
		hc.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		HttpClientParams httparams = new HttpClientParams();
		hc.setParams(httparams);
		hc.getParams()
		.setParameter(
				HttpMethodParams.USER_AGENT,
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");// 设置信息
	}

	/**
	 * @author GS
	 * @param url
	 * @param para
	 *            get请求中携带的参数
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> para)
			throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s)); // 获取请求参数
		}
		getMethod.setQueryString(data); // 设置请求参数
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOG.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常,UnknownHostException,可能是网络连接有问题"
					+ e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}
	
	/**
	 * @author GS
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOG.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @author GS
	 * @param url
	 * @param para
	 *            Post请求中携带的参数
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> para)
			throws IOException {
		String responseBody = null;
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s));
		}
		postMethod.setRequestBody(data); // 设置请求参数
		try {
			int statusCode = hc.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + postMethod.getStatusLine());
			}
			responseBody = postMethod.getResponseBodyAsString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} finally {
			postMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @author GS
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> para, String cookie)
			throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s)); // 获取请求参数
		}
		getMethod.setQueryString(data); // 设置请求参数
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @author GS
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String cookie) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @author GS
	 * @param url
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Header[] headers) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		for (Header h : headers) {
			getMethod.addRequestHeader(h);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOG.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @author GS
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> para,
			String cookie) throws IOException {
		String responseBody = null;
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s));
		}
		postMethod.setRequestBody(data); // 设置请求参数
		if (!cookie.equals("")) {
			postMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + postMethod.getStatusLine());
			}
			responseBody = postMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOG.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} finally {
			postMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	public static String getCookie() {
		Cookie[] cookies = hc.getState().getCookies();
		String tmpcookies = "";
		for (Cookie c : cookies) {
			tmpcookies += c.toString() + ";";
		}
		return tmpcookies;
	}
	
	public void clearCookies() {
		hc.getState().clearCookies();
	}

	/**
	 * 返回值为HTTP状态码
	 * 
	 * @author GS
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static int getStatusCode(String url, String cookie)
			throws IOException {
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		int statusCode = 0;
		try {
			statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + getMethod.getStatusLine());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOG.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOG.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOG.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOG.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return statusCode;
	}
}