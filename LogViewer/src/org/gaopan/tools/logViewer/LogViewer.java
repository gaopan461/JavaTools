package org.gaopan.tools.logViewer;

import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LogViewer extends JFrame {
	
	public LogViewer() {
		setLayout(new GridBagLayout());
		
		// 增加菜单栏
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu mFile = new JMenu("文件");
		menuBar.add(mFile);
		JMenuItem miOpen = new JMenuItem("打开");
		mFile.add(miOpen);
		
		// 左侧的日志列表
		JList<String> lstLog = new JList<>(new String[]{"gao", "pan"});
		lstLog.setBorder(new TitledBorder("日志列表"));
		this.add(lstLog, 
				new GBC(0, 0)
				.setFill(GBC.BOTH)
				.setIpad(200, 800)
				.setWeight(0, 100));
		
		// 右侧的日志详情
		JTextArea taLogDetail = new JTextArea();
		taLogDetail.setBorder(new TitledBorder("日志详情"));
		taLogDetail.setEditable(false);
		this.add(taLogDetail, 
				new GBC(1, 0)
				.setFill(GBC.BOTH)
				.setIpad(400, 800)
				.setWeight(100, 100));
		
		// 选择日志时，在右边显示日志详情
		lstLog.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				taLogDetail.setText(lstLog.getSelectedValuesList().toString());
			}
		});
		
		miOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(LogViewer.this, "打开文件", FileDialog.LOAD);
				fileDialog.setFilenameFilter(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".html");
					}
				});
				
				fileDialog.setVisible(true);
				String fileName = fileDialog.getFile();
				String fileDir = fileDialog.getDirectory();
				
				taLogDetail.setText("Open file:" + fileName + ", dir:" + fileDir);
			}
		});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		LogViewer logViewer = new LogViewer();
		logViewer.setTitle("LogViewer");
		logViewer.setSize(800, 600);
		logViewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		logViewer.pack();
		logViewer.setVisible(true);
	}

}