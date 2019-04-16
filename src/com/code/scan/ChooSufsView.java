package com.code.scan;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * 自定义后缀名的类
 *
 */
public class ChooSufsView {
	private JFrame frame;	//主窗口
	private JPanel panel;	//面板
	
	private JCheckBox[] jCheckBoxs;		//用于存储后缀名的复选框
	private JCheckBox seleAll;	//全选复选框
	
	private List<String> sufList = new ArrayList<>();	//要接收的后缀名集合
	
	public ChooSufsView(CodeCount codeCount, JFrame jf) {
		
		frame = new JFrame("项目下包含后缀名");
		
		// 设置窗口相关参数
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	//设置外观
			frame.setIconImage(ImageIO.read(new FileInputStream(new File("img\\tit.jpg")))); // 设置图标
			frame.setBounds(400, 100, 400, 500); // 位置及大小
			frame.setResizable(false); // 窗口大小不可改变
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 设置窗口不可关闭
			frame.setLayout(null); // 布局设置为空
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/********************添加选择文件/文件夹的面板**********************/
		panel = new JPanel();
		panel.setLayout(null); // 布局设置为空
		panel.setBounds(10, 10, 380, 400);
		panel.setBackground(Color.white);
		panel.setFont(new Font("楷体", Font.BOLD, 26));
		panel.setBorder(BorderFactory.createEtchedBorder());
		
		frame.add(panel);
		
		//添加“确定”按钮
		JButton sureBtn = new JButton("确定");
		sureBtn.setBounds(320, 420, 60, 35);
		sureBtn.setFont(new Font("宋体", Font.PLAIN, 20));
		//设置按钮中的文字可以填充整个按钮
		sureBtn.setBorder(BorderFactory.createEtchedBorder());
		sureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				codeCount.getSufList().clear();
				//遍历后缀名复选框,返回指定的后缀名
				List<String> newSufList = new ArrayList<>();
				for (int i = 0; i < jCheckBoxs.length; i++) {
					if(jCheckBoxs[i].isSelected()) {
						newSufList.add(sufList.get(i));
					}
				}
				//将选定的集合放进统计量中
				codeCount.setSufList(newSufList);
				//此窗口关闭后,父窗口转到可编辑状态
				jf.setEnabled(true);
				//点击确定后才可关闭窗口
				frame.dispose();
			}
		});
		frame.add(sureBtn);
		
		frame.setVisible(true);
		
	}
	
	/**
	 * 自定义后缀名函数
	 * @param sufList
	 * @return
	 */
	public void chooseSufs(List<String> sufList) {
		//先复制一份集合
		this.sufList.addAll(sufList);
		//创建复选框数组,用来存储后缀名属性
		jCheckBoxs = new JCheckBox[sufList.size()];
		//循环遍历在指定目录搜索到的文件后缀名,并创建对应复选框
		for (int i = 0; i < sufList.size(); i++) {
			//创建对象
			jCheckBoxs[i] = new JCheckBox("."+sufList.get(i));	
			//设置复选框背景色为白色
			jCheckBoxs[i].setBackground(Color.WHITE);
			//根据i值，将复选框设置到不同的列,共计三列,统计上限为35类后缀名，1个全选后缀名
			if(i<12) {
				jCheckBoxs[i].setBounds(10, i*31+10, 120, 30);
			}else if(i>=12 && i<24){
				jCheckBoxs[i].setBounds(130, (i-12)*31+10, 120, 30);
			}else {
				jCheckBoxs[i].setBounds(250, (i-24)*31+10, 120, 30);
			}
			//复选框默认被选中
			jCheckBoxs[i].setSelected(true);
			panel.add(jCheckBoxs[i]);
		}
		
		//刷新界面,以免熬延致使组件不能显示
		frame.update(frame.getGraphics());	
		//调用设置全选复选框的函数,根据后缀名的数量设置位置
		setSeleAll();
		
	}
	
	/**
	 * 设置全选复选框的函数
	 */
	public void setSeleAll() {
		seleAll = new JCheckBox("选择全部");
		seleAll.setBackground(Color.WHITE);
		seleAll.setFont(new Font("宋体", Font.BOLD, 18));
		seleAll.setForeground(Color.blue);
		seleAll.setBounds(250, (35-24)*31+10, 120, 30);
		seleAll.setSelected(true);
		//监听全选复选框是否被选择
		seleAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(seleAll.isSelected()) {
					//如果全选复选框被选择那么所有的都被选上
					for (JCheckBox jCheckBox : jCheckBoxs) {
						jCheckBox.setSelected(true);
					}
				}else {
					//如果全选复选框没有被选择那么所有的取消都被选上
					for (JCheckBox jCheckBox : jCheckBoxs) {
						jCheckBox.setSelected(false);
					}
				}
			}
		});
		panel.add(seleAll);
		
		frame.update(frame.getGraphics());
	}
	
}
