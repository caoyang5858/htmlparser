package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Tool {
	public static String readFileContent(String fileName) throws IOException {//��ȡ�ļ���String
		File file = new File(fileName);    
		BufferedReader bf = new BufferedReader(new FileReader(file));    
		String content = "";  
		StringBuilder sb = new StringBuilder();  
		while(content != null){  
			content = bf.readLine();    
			if(content == null){    break;   }    
			sb.append(content.trim());  
		}  
		bf.close();  
		return sb.toString(); 
	}
}

