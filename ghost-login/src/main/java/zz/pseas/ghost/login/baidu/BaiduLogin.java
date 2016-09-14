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
package zz.pseas.ghost.login.baidu;

import static zz.pseas.ghost.utils.CodeUtils.getcode;
import static zz.pseas.ghost.utils.CodeUtils.getcodeWithAnd;
import static zz.pseas.ghost.utils.CodeUtils.getcodeWithSingleQua;
import static zz.pseas.ghost.utils.CodeUtils.getcodeWithoutQua;
import static zz.pseas.ghost.utils.HttpClinetUtil.get;
import static zz.pseas.ghost.utils.HttpClinetUtil.getCookie;
import static zz.pseas.ghost.utils.HttpClinetUtil.post;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class BaiduLogin {

	public static String getBaiduCookie(String username,String password) throws IOException{
		get("http://passport.baidu.com/phoenix/account/jsapi");
		String BAIDUID = getcode(getCookie(), "BAIDUID");
		String tokenContent = get(
				"https://passport.baidu.com/v2/api/?getapi&class=login&tpl=mn&tangram=false",
				"BAIDUID=" + BAIDUID + ";HOSUPPORT=1");
		String token = getcodeWithSingleQua(tokenContent, "login_token");
		get("https://passport.baidu.com/v2/api/?loginhistory&token=" + token
				+ "&tpl=mn&apiver=v3&tt=" + System.currentTimeMillis()
				+ "&callback=bd__cbs__vehc6w");
		String UBI = getcodeWithoutQua(getCookie(), "UBI");
		BAIDUID = getcode(getCookie(), "BAIDUID");
		Map<String, String> para1 = new HashMap<String, String>();
		para1.put("apiver", "v3");
		para1.put("callback", "parent.bd__pcbs__k2eobr");
		para1.put("charset", "utf-8");
		para1.put("codestring", "");
		para1.put("isPhone", "");
		para1.put("loginmerge", "true");
		para1.put("logintype", "dialogLogin");
		para1.put("logLoginType", "pc_loginDialog");
		para1.put("mem_pass", "on");
		para1.put("password", password);
		para1.put("ppui_logintime", "26604");
		para1.put("quick_user", "0");
		para1.put("safeflg", "0");
		para1.put("splogin", "rate");
		para1.put("staticpage",
				"http://www.baidu.com/cache/user/html/v3Jump.html");
		para1.put("token", token);
		para1.put("tpl", "mn");
		para1.put("tt", Long.toString(System.currentTimeMillis()));
		para1.put("u", "http://www.baidu.com/");
		para1.put("username", username);
		para1.put("verifycode", "");
		String VCodeContent = post("https://passport.baidu.com/v2/api/?login",
				para1, getCookie());
		String codeString = getcodeWithAnd(VCodeContent, "codeString");
		String code = JOptionPane.showInputDialog(null, new ImageIcon(new URL(
				"https://passport.baidu.com/cgi-bin/genimage?" + codeString)));
		Map<String, String> para2 = new HashMap<String, String>();
		para2.put("apiver", "v3");
		para2.put("callback", "parent.bd__pcbs__k2eobr");
		para2.put("charset", "utf-8");
		para2.put("codestring", codeString);
		para2.put("isPhone", "");
		para2.put("loginmerge", "true");
		para2.put("logintype", "dialogLogin");
		para2.put("logLoginType", "pc_loginDialog");
		para2.put("mem_pass", "on");
		para2.put("password", password);
		para2.put("ppui_logintime", "26604");
		para2.put("quick_user", "0");
		para2.put("safeflg", "0");
		para2.put("splogin", "rate");
		para2.put("staticpage",
				"http://www.baidu.com/cache/user/html/v3Jump.html");
		para2.put("token", token);
		para2.put("tpl", "mn");
		para2.put("tt", Long.toString(System.currentTimeMillis()));
		para2.put("u", "http://www.baidu.com/");
		para2.put("username", username);
		para2.put("verifycode", code);
		String loginContent = post("https://passport.baidu.com/v2/api/?login",
				para2, "BAIDUID=" + BAIDUID + ";UBI=" + UBI + ";HOSUPPORT=1");
		String param = getcodeWithAnd(loginContent, "hao123Param");
		get("http://user.hao123.com/static/crossdomain.php?bdu="
				+ param + "&t=" + System.currentTimeMillis());
		return getCookie();
	}
}
