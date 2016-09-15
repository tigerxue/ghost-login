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
 * 
 */
package zz.pseas.ghost.login.weibo;

/**   
* @date 2016年9月15日 下午7:30:57 
* @version   
* @since JDK 1.8  
*/
public class WeiboLoginTest {

	/**  
	 * @param args  
	 * @since JDK 1.8  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			String cookie=SinaWeiboLogin.getSinaCookie("test", "test");
			System.out.println(cookie);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
