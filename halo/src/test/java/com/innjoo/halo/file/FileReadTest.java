package com.innjoo.halo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReadTest {
	public static void main(String[] args) throws IOException {
		File f = new File("E:/pack/jiqiren.png");
		InputStream in = new FileInputStream(f);
		byte b[] = new byte[(int) f.length() - 100]; // 创建合适文件大小的数组
		in.skip(100);
		
		in.read(b); // 读取文件中的内容到b[]数组
		System.out.println(new String(b));
		in.close();
	}

}
