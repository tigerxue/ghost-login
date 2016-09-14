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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class CodeUtils {

	public static String getcode(String string,String name){
		String code = null;
		Pattern p = Pattern.compile(name+"=\"([\\s\\S]*?)\"");
		Matcher m = p.matcher(string);
		if(m.find()){
			code = m.group(1);
		}
		return code;
	}
	
	public static String getcodeWithoutQua(String string,String name){
		String code = null;
		Pattern p = Pattern.compile(name+"=([\\s\\S]*?);");
		Matcher m = p.matcher(string);
		if(m.find()){
			code = m.group(1);
		}
		return code;
	}
	
	public static String getcodeWithAnd(String string,String name){
		String code = null;
		Pattern p = Pattern.compile(name+"=([\\s\\S]*?)&");
		Matcher m = p.matcher(string);
		if(m.find()){
			code = m.group(1);
		}
		return code;
	}
	
	public static String getcodeWithSingleQua(String string,String name){
		String code = null;
		Pattern p = Pattern.compile(name+"='([\\s\\S]*?)'");
		Matcher m = p.matcher(string);
		if(m.find()){
			code = m.group(1);
		}
		return code;
	}

}
