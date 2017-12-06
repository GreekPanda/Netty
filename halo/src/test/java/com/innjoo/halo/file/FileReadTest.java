package com.innjoo.halo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.innjoo.halo.utils.DateUtils;

public class FileReadTest {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {
		File f = new File("E:/pack/jiqiren.png");
		InputStream in = new FileInputStream(f);
		byte b[] = new byte[(int) f.length() - 100]; // 创建合适文件大小的数组
		in.skip(100);
		
		in.read(b); // 读取文件中的内容到b[]数组
		//System.out.println(new String(b));
		in.close();
		
		HashMap[] map = new HashMap[5];
		map[0] = new HashMap<String, Integer>();
		map[0].put("kid", 0);
		map[0].put("fkid", 0);
		
		map[1] = new HashMap<String, Integer>();
		map[1].put("kid", 1);
		map[1].put("fkid", 1);
		
		map[2] = new HashMap<String, Integer>();
		map[2].put("kid", 2);
		map[2].put("fkid", 2);
		
		map[3] = new HashMap<String, Integer>();
		map[3].put("kid", 3);
		map[3].put("fkid", 3);
		
		map[4] = new HashMap<String, Integer>();
		map[4].put("kid", 4);
		map[4].put("fkid", 4);
		
		for(int i = 0; i < 5; ++i) {
			//System.out.println("Key = " + map[i].get("kid") + ", " + map[i].get("fkid"));
		}
		
		System.out.println(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		
	}

}
