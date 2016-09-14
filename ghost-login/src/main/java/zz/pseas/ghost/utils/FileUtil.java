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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
/**   
* @date 2016年9月14日 下午9:26:00 
* @version   
* @since JDK 1.8  
*/
public class FileUtil {
	public static void writeFile(byte[] bytes, String path) {
		int n = path.lastIndexOf("/");
		if (n > 0) {
			String dir = path.substring(0, n);
			File f = new File(dir);
			f.mkdir();
		}

		try {
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(bytes);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public static String readFile(String file, String charset) throws Exception {
		FileInputStream in = new FileInputStream(file);
		InputStreamReader r1 = new InputStreamReader(in, charset);
		BufferedReader r2 = new BufferedReader(r1);
		StringBuffer sb = new StringBuffer();
		while (true) {
			String s = r2.readLine();
			if (s == null)
				break;
			sb.append(s);
		}
		r2.close();
		return sb.toString();
	}
}
