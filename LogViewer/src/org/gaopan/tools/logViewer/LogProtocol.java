package org.gaopan.tools.logViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogProtocol {
//	private static final String LOG_PATTERN = "(\d\d\d\d-\d\d-\d\d \d\d:\d\d:\d\d,\d\d\d) \(\w+.java:\d+\) \[\w+\]\[\w+\] ((SC|CS)\w+)";
	private static final String LOG_PATTERN = "(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) \\[\\w+\\]\\[\\w+\\]";
	private static final String LOG_PROTOCOL_PATTERN = "(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) \\[\\w+\\]\\[\\w+\\] ((SC|CS)\\w+)";
	
	private Pattern logPattern = Pattern.compile(LOG_PATTERN);
	private Pattern logProtocolPattern = Pattern.compile(LOG_PROTOCOL_PATTERN);
	
	private List<LogData> datas = new ArrayList<>();
	private String currentLine = "";
	
	private LogProtocolFilter filter = null;
	private List<LogData> datasFilter = new ArrayList<>();
	
	public LogProtocol(File file, LogProtocolFilter filter) {
		this.filter = filter;
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			LogData nextLog = null;
			while((nextLog = getNextLog(br)) != null) {
				if(!nextLog.isProtocol) {
					continue;
				}
				
				datas.add(nextLog);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		filter();
	}
	
	/**
	 * 该行是否是日志
	 * @param line
	 * @return
	 */
	private boolean isLog(String line) {
		if(line == null) {
			return false;
		}
		
		return logPattern.matcher(line).find();
	}
	
	/**
	 * 获取协议名
	 * @param matcher
	 * @return
	 */
	private String getProtocolName(Matcher matcher) {
		return matcher.group(2);
	}
	
	/**
	 * 获取时间戳
	 * @param matcher
	 * @return
	 */
	private long getTimestamp(Matcher matcher) {
		String time = matcher.group(1);
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	private LogData getNextLog(BufferedReader br) throws IOException {
		// 找到日志第一行
		if(!isLog(currentLine)) {
			while((currentLine = br.readLine()) != null) {
				if(isLog(currentLine)) {
					break;
				}
			}
		}
		
		// 没找到
		if(!isLog(currentLine)) {
			return null;
		}
		
		LogData logData = new LogData();
		
		Matcher matcher = logProtocolPattern.matcher(currentLine);
		// 如果是协议
		if(matcher.find()) {
			logData.timestamp = getTimestamp(matcher);
			logData.isProtocol = true;
			logData.protocolName = getProtocolName(matcher);
		} else {
			logData.isProtocol = false;
		}
		
		StringBuilder sb = new StringBuilder(currentLine);
		while((currentLine = br.readLine()) != null) {
			if(isLog(currentLine)) {
				break;
			}
			
			sb.append('\n').append(currentLine);
		}
		
		logData.context = sb.toString();
		return logData;
	}
	
	public void filter() {
		datasFilter = datas.stream().filter((data) -> !filter.isFilter(data.protocolName)).collect(Collectors.toList());
	}

	public List<LogData> getDatasFilter() {
		return datasFilter;
	}
}
