package org.gaopan.tools.logViewer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author gaopan
 *
 * 该类用来简化对GridBagConstraints的操作
 */
@SuppressWarnings("serial")
public class GBC extends GridBagConstraints {
	/**
	 * 初始化左上角的位置，单位是格子
	 * @param gridx
	 * @param gridy
	 */
	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}
	
	/**
	 * 初始化左上角的位置和所占的列数和行数，单位是格子
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 */
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}
	
	/**
	 * 设置对齐方式
	 * 组件所在格子内的对齐方式
	 * @param anchor
	 */
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}
	
	/**
	 * 设置是否拉伸，已经拉伸方向
	 * @param fill
	 * @return
	 */
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}
	
	/**
	 * 设置x和y方向上的增加
	 * 当拉伸窗口时，组件的变化值权重，100=组件随窗口而变化，0=组件大小不会发生变化
	 * @param weightx
	 * @param weighty
	 * @return
	 */
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}
	
	/**
	 * 设置外部填充
	 * 外部填充，填充的区域是组件与所处格子边框之间的部分，有left，top，right， bottom四个
	 * 参数，不过当组件的fill＝NONE时，指定insets值无意义
	 * @param distance
	 * @return
	 */
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}
	
	/**
	 * 设置外部填充
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 * @return
	 */
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}
	
	/**
	 * 设置内部填充
	 * 内部填充，是指在组件首选大小的基础上x方向上加上ipadx，y方向上加上ipady，这样就可以保证组件不会
	 * 收缩到ipadx，ipady所确定的大小以内，因此我们可以用ipadx，ipady的值来指定组件的大小，而不必直
	 * 接指定组件的大小，否则会有意想不到的效果
	 * @param ipadx
	 * @param ipady
	 * @return
	 */
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
	
	public static void main(String[] args) {
		TestGBCFrame frame = new TestGBCFrame();
		frame.setTitle("TestGBCFrame");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

/**
 * @author gaopan
 *
 * 示例：windows下的画图版框架，包含五个面板：</br>
 * 	上侧工具选择面板，占据1行2列</br>
 * 	左侧的具体工具面板，占据1行1列</br>
 * 	右侧的绘图面板</br>
 * 	下侧的颜色选择面板，占据1行2列</br>
 * 	下侧的状态面板，占据1行2列
 */
@SuppressWarnings("serial")
class TestGBCFrame extends JFrame {
	
	public TestGBCFrame() {
		setLayout(new GridBagLayout());
		addGridBagPanels();
	}
	
	private void addGridBagPanels() {
		// 上侧的工具选择面板
		JPanel toolSelectPanel = new JPanel();
		toolSelectPanel.setBackground(Color.green);
		this.add(toolSelectPanel, 
				new GBC(0, 0, 2, 1)
				.setFill(GBC.BOTH)
				.setIpad(200, 50)
				.setWeight(100, 0));
		
		// 左侧的具体工具面板
		JPanel toolConcretePanel = new JPanel();
		toolConcretePanel.setBackground(Color.yellow);
		this.add(toolConcretePanel, 
				new GBC(0, 1)
				.setFill(GBC.BOTH)
				.setIpad(70, 90)
				.setWeight(0, 100));
		
		// 右侧的绘图面板
		JPanel drawPanel = new JPanel();
		drawPanel.setBackground(Color.white);
		this.add(drawPanel, 
				new GBC(1, 1)
				.setFill(GBC.BOTH));
		
		// 下侧的选择颜色面板
		 JPanel colorPanel = new JPanel();
		 colorPanel.setBackground(Color.lightGray);
		 this.add(colorPanel, 
				 new GBC(0, 2, 2, 1)
				 .setFill(GBC.BOTH)
				 .setIpad(200, 50)
				 .setWeight(100, 0));
		 
		 // 下侧的状态面板
		 JPanel statePanel = new JPanel();
		 statePanel.setBackground(Color.cyan);
		 this.add(statePanel, 
				 new GBC(0, 3, 2, 1)
				 .setFill(GBC.BOTH)
				 .setIpad(200, 20)
				 .setWeight(100, 0));
	}
}