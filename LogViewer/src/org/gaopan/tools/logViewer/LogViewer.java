package org.gaopan.tools.logViewer;

import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LogViewer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JList<String> lstLog = null;
	
	private LogProtocol logProtocol = null;
	private LogProtocolFilter filter = null;
	
	public LogViewer() {
		filter = new LogProtocolFilter();
		
		setLayout(new GridBagLayout());

		// 日志列表上的弹出菜单
		JMenuItem miOpen = new JMenuItem("打开");
		JMenuItem miAddFilter = new JMenuItem("增加屏蔽字");
		JPopupMenu pmLog = new JPopupMenu();
		pmLog.add(miOpen);
		pmLog.add(miAddFilter);
		
		// 过滤列表上的弹出菜单
		JMenuItem miDelFilter = new JMenuItem("删除屏蔽字");
		JPopupMenu pmLogFilter = new JPopupMenu();
		pmLogFilter.add(miDelFilter);
		
		// 左侧的日志列表
		lstLog = new JList<>();
		lstLog.setBorder(new TitledBorder("日志列表"));
		JScrollPane spLog = new JScrollPane(lstLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(spLog, 
				new GBC(0, 0)
				.setFill(GBC.BOTH)
				.setIpad(200, 800)
				.setWeight(0, 100));
		
		// 中间的日志详情
		JTextArea taLogDetail = new JTextArea();
		taLogDetail.setBorder(new TitledBorder("日志详情"));
		taLogDetail.setEditable(false);		
		JScrollPane spLogDetail = new JScrollPane(taLogDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(spLogDetail, 
				new GBC(1, 0)
				.setFill(GBC.BOTH)
				.setIpad(400, 800)
				.setWeight(100, 100));
		
		// 右侧的过滤列表
		JList<String> lstLogFilter = new JList<>(new Vector<>(filter.getFilterKeys()));
		lstLogFilter.setBorder(new TitledBorder("日志屏蔽字列表"));
		JScrollPane spLogFilter = new JScrollPane(lstLogFilter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(spLogFilter, 
				new GBC(2, 0)
				.setFill(GBC.BOTH)
				.setIpad(200, 800)
				.setWeight(0, 100));
		
		// 选择日志时，在右边显示日志详情
		lstLog.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedIndex = lstLog.getSelectedIndex();
				if(selectedIndex >= 0) {
					LogData logDataSelected = logProtocol.getDatasFilter().get(selectedIndex);
					taLogDetail.setText(logDataSelected.context);
					taLogDetail.setCaretPosition(0);
				}
			}
		});
		
		// 右键弹出菜单
		lstLog.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					pmLog.show(e.getComponent(), e.getX(), e.getY());
				} else {
					pmLog.setVisible(false);
				}
			}
		});
		
		// 右键弹出菜单
		lstLogFilter.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					pmLogFilter.show(e.getComponent(), e.getX(), e.getY());
				} else {
					pmLogFilter.setVisible(false);
				}
			}
		});
		
		// 打开文件
		miOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(LogViewer.this, "打开文件", FileDialog.LOAD);
				// 只接受后缀是.log的文件
				fileDialog.setFilenameFilter(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".log");
					}
				});
				
				fileDialog.setVisible(true);
				String fileName = fileDialog.getFile();
				String fileDir = fileDialog.getDirectory();
				if(fileName == null || fileDir == null) {
					return;
				}
				
				File file = new File(fileDir, fileName);
				loadFile(file);
				filter.setLastOpenFile(file.getAbsolutePath());
			}
		});
		
		// 添加屏蔽字
		miAddFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> selectedList = lstLog.getSelectedValuesList();
				if(selectedList.isEmpty()) {
					return;
				}
				
				filter.addFilterKey(selectedList);
				lstLogFilter.setListData(new Vector<>(filter.getFilterKeys()));
				if(logProtocol != null) {
					logProtocol.filter();
					Vector<String> listData = new Vector<>(logProtocol.getDatasFilter().size());
					logProtocol.getDatasFilter().forEach((d) -> listData.add(d.protocolName));
					lstLog.setListData(listData);
				}
			}
		});
		
		// 删除屏蔽字
		miDelFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> selectedList = lstLogFilter.getSelectedValuesList();
				if(selectedList.isEmpty()) {
					return;
				}
				
				filter.delFilterKey(selectedList);
				lstLogFilter.setListData(new Vector<>(filter.getFilterKeys()));
				if(logProtocol != null) {
					logProtocol.filter();
					Vector<String> listData = new Vector<>(logProtocol.getDatasFilter().size());
					logProtocol.getDatasFilter().forEach((d) -> listData.add(d.protocolName));
					lstLog.setListData(listData);
				}
			}
		});
		
		String lastOpenFile = filter.getLastOpenFile();
		if(lastOpenFile != null && !lastOpenFile.isEmpty()) {
			loadFile(new File(lastOpenFile));
		}
	}
	
	private void loadFile(File file) {
		if(!file.exists()) {
			return;
		}
		
		logProtocol = new LogProtocol(file, filter);
		Vector<String> listData = new Vector<>(logProtocol.getDatasFilter().size());
		logProtocol.getDatasFilter().forEach((d) -> listData.add(d.protocolName));
		lstLog.setListData(listData);
	}

	public static void main(String[] args) {
		LogViewer logViewer = new LogViewer();
		logViewer.setTitle("LogViewer");
		logViewer.setSize(1000, 600);
		logViewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		logViewer.pack();
		logViewer.setVisible(true);
	}

}