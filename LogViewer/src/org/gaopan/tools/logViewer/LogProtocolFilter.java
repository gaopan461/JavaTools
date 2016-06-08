package org.gaopan.tools.logViewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class LogProtocolFilter {
	private static final String FILTER_FILE_NAME = "ProtocolFilter.txt";
	
	/** 过滤字列表 */
	private List<String> filterKeys = new ArrayList<>();
	
	/** 上次打开的文件 */
	private String lastOpenFile = "";
	
	public LogProtocolFilter() {
		String path = LogProtocolFilter.class.getClassLoader().getResource("").getPath();
		File file = new File(path, FILTER_FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String firstLine = br.readLine();
			if(firstLine == null) {
				return;
			}
			
			if(firstLine.startsWith("LastOpenFile=")) {
				lastOpenFile = firstLine.split("=")[1];
			} else {
				filterKeys.add(firstLine);
			}
			
			String line = "";
			while((line = br.readLine()) != null) {
				filterKeys.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 该内容是否要被过滤掉
	 * @param context
	 * @return
	 */
	public boolean isFilter(String context) {
		for(String key : filterKeys) {
			if(context.contains(key)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 增加屏蔽字
	 * @param key
	 */
	public void addFilterKey(List<String> keys) {
		for(String k : keys) {
			if(!filterKeys.contains(k)) {
				filterKeys.add(k);
			}
		}
		
		saveToFile();
	}
	
	/**
	 * 增加屏蔽字
	 * @param key
	 */
	public void delFilterKey(List<String> keys) {
		filterKeys.removeAll(keys);
		
		saveToFile();
	}
	
	private void saveToFile() {
		String path = LogProtocolFilter.class.getClassLoader().getResource("").getPath();
		File file = new File(path, FILTER_FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			if(lastOpenFile != null && !lastOpenFile.isEmpty()) {
				bw.write("LastOpenFile=" + lastOpenFile);
				bw.newLine();
			}
			
			for(String k : filterKeys) {
				bw.write(k);
				bw.newLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getFilterKeys() {
		return filterKeys;
	}

	public String getLastOpenFile() {
		return lastOpenFile;
	}

	public void setLastOpenFile(String lastOpenFile) {
		this.lastOpenFile = lastOpenFile;
		
		saveToFile();
	}
	
}
