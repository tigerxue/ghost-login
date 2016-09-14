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

import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.Cookie;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class DownloadUtil {
	public static void setUserAgent(HttpUriRequest request) {
		request.addHeader("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
	
		//request.addHeader("Host","gd.189.cn");
		//request.addHeader("Content-Type","text/plain");
		//request.addHeader("Referer","http://gd.189.cn/common/login.jsp?UATicket=-1&loginOldUri=null");
		//request.addHeader("host","my.alipay.com");
		//request.addHeader("User-Agent","Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16");
		//request.addHeader("Host","api.m.taobao.com");
	}

	public static CookieStore convertToCookieStore(Set<Cookie> cookies) {
		BasicCookieStore store = new BasicCookieStore();
		for (Cookie c : cookies) {
			BasicClientCookie c1 = new BasicClientCookie(c.getName(), c.getValue());
			c1.setDomain(c.getDomain());
			c1.setPath(c.getPath());
			c1.setExpiryDate(c.getExpiry());
			store.addCookie(c1);
		}
		return store;
	}
}
