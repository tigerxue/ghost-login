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

package zz.pseas.ghost.utils;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.SystemUtils;


public class LoginUtils {

	private static final long COUNT = 10000000;
	private static final int THREADS = 2;

	//
	protected int sleeps;

	public int getSleeps() {
		return sleeps;
	}

	public void setSleeps(int sleeps) {
		this.sleeps = sleeps;
	}

	// 停顿固定的时间=0.3秒//
	public static void minSleep() {
		try {
			Thread.sleep(3 * 100);
		} catch (InterruptedException e) {
			// throw e;
		}
	}

	// 设置线程停顿时间,1秒-millis之间的任意时间停顿 //
	public static void sleep(long millis) {
		//Random rand = new Random(System.currentTimeMillis());
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		// 平均模拟停顿时间30秒//
		int minInternalTime = 1 * 1000;
		int maxInternalTime = (int) millis;
		int randnum = 0;

		if (maxInternalTime < minInternalTime) {
			maxInternalTime = 3 * minInternalTime + 1;
		}
		randnum = tlr.nextInt(maxInternalTime) % (maxInternalTime - minInternalTime + 1) + minInternalTime;
		//
		if (randnum <= 0)
			return;
		try {
			Thread.sleep(randnum);
			//Thread.currentThread().sleep(randnum);
		} catch (InterruptedException e) {
		}
	}

	// 以标准格式返回此刻时间//
	public static String getCurrentTime() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return ts.toString();
	}

	//
	public static String getCurrentTime(String format){
		
		  Calendar calendar = Calendar.getInstance();
		  //format=yyyyMMdd-hhmmss//
		  SimpleDateFormat formats = new SimpleDateFormat(format);
		  String time = formats.format(calendar.getTime());
		  //System.out.println(time);
		  return time;
	}
	////////////
	// runner for all tests
	
	/**  
	 * getCaptchaDir:. <br/>  
	 * 新建Captcha目录//
	 */
	public static void getCaptchaDir(){
		
		File tmpath=SystemUtils.getUserDir();
		String path=tmpath.toString()+"\\captcha";
		File workdir=new File(path);
		if(!workdir.exists()){
			workdir.mkdirs();
		}
	}
	
	/*
	 * 任何情况下都不要在多个线程间共享一个java.util.Random实例，而该把它放入ThreadLocal之中。
	 * Java7在所有情形下都更推荐使用java.util.concurrent.ThreadLocalRandom——它向下兼容已有的代码且运营成本更低。
	 */
	public static void main(String[] args) {
		
	}
}
