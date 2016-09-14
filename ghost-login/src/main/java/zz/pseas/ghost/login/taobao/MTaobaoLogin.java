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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import zz.pseas.ghost.client.GhostClient;
import zz.pseas.ghost.client.CommonsPage;
import zz.pseas.ghost.utils.FileUtil;
import zz.pseas.ghost.utils.MD5Util;
import zz.pseas.ghost.utils.StringUtil;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class MTaobaoLogin {
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		String tbuserNmae="TBname";
		String tbpassWord="TBpasssword";
		
		GhostClient iPhone = new GhostClient("utf-8");
		String ans = iPhone.get("https://login.m.taobao.com/login.htm");

		Document doc = Jsoup.parse(ans);
		String url = doc.select("form#loginForm").first().attr("action");

		String _tb_token = doc.select("input[name=_tb_token_]").first().attr("value");

		String sid = doc.select("input[name=sid]").first().attr("value");
		System.out.println(_tb_token);
		System.out.println(sid);
		System.out.println(url);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TPL_password", tbpassWord);
		map.put("TPL_username", tbuserNmae);
		map.put("_tb_token_", _tb_token);
		map.put("action", "LoginAction");
		map.put("event_submit_do_login", "1");
		map.put("loginFrom", "WAP_TAOBAO");
		map.put("sid", sid);

		String location = null;
		while (true) {
			CommonsPage commonsPage = iPhone.postForPage(url, map);
			location = commonsPage.getHeader("Location");
			String postAns = new String(commonsPage.getContents(), "utf-8");
			if (StringUtil.isNotEmpty(location) && StringUtil.isEmpty(postAns)) {
				break;
			}

			String s = Jsoup.parse(postAns).select("img.checkcode-img").first().attr("src");
			String imgUrl = "https:" + s;

			byte[] bytes = iPhone.getBytes(imgUrl);
			FileUtil.writeFile(bytes, "g:/tbCaptcha.jpg");

			String wepCheckId = Jsoup.parse(postAns).select("input[name=wapCheckId]").val();
			String captcha = null;
			map.put("TPL_checkcode", captcha);
			map.put("wapCheckId", wepCheckId);
		}
		
		iPhone.get(location);

		String tk = iPhone.getCookieValue("_m_h5_tk");
		if (StringUtil.isNotEmpty(tk)) {
			tk = tk.split("_")[0];
		} else {
			tk = "undefined";
		}


		String url2 = genUrl(tk);
		String ans1 = iPhone.get(url2);
		System.out.println(url2);
		System.out.println(ans1);
		
		
		tk = iPhone.getCookieValue("_m_h5_tk").split("_")[0];
		if (StringUtil.isEmpty(tk)) {
			tk = "undefined";
		}
		System.out.println(tk);
		url2 = genUrl(tk);
		iPhone.showCookies();
		RequestConfig requestConfig = RequestConfig.custom()
		.setProxy(new HttpHost("127.0.0.1",8888))
		.build();
		
		HttpUriRequest get = RequestBuilder.get()
		.setConfig(requestConfig)
		//.addHeader("User-Agent","Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16")
		.addHeader("Host","api.m.taobao.com")
		.setUri(url2)
		.build();
		
		ans1 = iPhone.execute(get);
		
		System.out.println(ans1);
		
	}

	public static String replaceStr(String s) {
		String ans = s.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\\\", "%5C").replaceAll(":", "%3A").replaceAll("#", "%23").replaceAll(",", "%2C")
				.replaceFirst("%3A", ":");
		return ans;
	}

	public static String genUrl(String tk) {
		String t = String.valueOf(System.currentTimeMillis());
		String appKey = "12574478";
		String data = "{\"spm\":\"a2141.7756461.2.6\",\"page\":1,\"tabCode\":\"all\",\"appVersion\":\"1.0\",\"appName\":\"tborder\"}";
		String s = tk + "&" + t + "&" + appKey + "&" + data;
		String sign = MD5Util.string2MD5(s);

		StringBuffer sb = new StringBuffer("http://api.m.taobao.com/h5/mtop.order.queryboughtlist/3.0/?");
		sb.append("AntiCreep=").append("true");
		sb.append("&AntiFlood=").append("true");
		sb.append("&LoginRequest=").append("mtop.order.queryBoughtList");
		sb.append("&api=").append("mtop.order.queryBoughtList");
		sb.append("&appKey=").append("12574478");
		sb.append("&callback=").append("mtopjsonp1");
		sb.append("&data=").append(data);
		sb.append("&dataType=").append("jsonp");
		sb.append("&ecode=").append("1");
		sb.append("&sign=").append(sign);
		sb.append("&t=").append(t);
		sb.append("&ttid=").append("##h5");
		sb.append("&type=").append("jsonp");
		sb.append("&v=").append("3.0");

		String url = sb.toString();

		return replaceStr(url);
	}

}
