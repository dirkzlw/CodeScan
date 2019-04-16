package com.code.scan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * 主界面
 *
 */
@SuppressWarnings("all")
public class MyFrame implements ActionListener {
	// 设置主窗口
	private JFrame frame;
	// 设置主面板
	private JPanel panel;
	private JButton clearBtn;
	private JButton chooseBtn;
	private JButton nameBtn;
	private JButton countBtn;
	// 设置检索面板
	private JPanel selectPanel;
	private JTextField selectText;
	private JButton selectBtn;
	private JButton outBtn;
	// 设置顶部面板
	private JPanel topPanel;
	// 文件选择的文本框
	private JTextField inFileField = new JTextField(30);
	// 中间面板
	private JPanel centerPanel;
	// 所要遍历的文件/目录
	private File chooseFile;
	private List<Code> files;
	// 创建进行计数的对象
	private CodeCount codeCount = null;
	private CodeCount codeCount2 = null;	//中间量

	//界面中表格的信息
	private String[] tableName = { "文件目录", "代码量", "注释量", "空行数", "总行数" };
	private Object[][] data = { { "未选择文件", 0, 0, 0, 0 } };
	private JTable table;

	// 用于标志设置后缀名按钮是否被点击
	static boolean nameDoClick = false;

	//无参构造器
	public MyFrame() {
		try {
			// 设置外观
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//初始化文本框提示信息
		inFileField.setText(" 请先选择要统计的文件或目录");
		inFileField.setBackground(Color.WHITE);
		inFileField.setForeground(Color.red);

		// 设置窗口及其他属性
		frame = new JFrame();
		try {
			// 设置图标
			frame.setIconImage(ImageIO.read(new FileInputStream(new File("img\\tit.jpg"))));
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		frame.setBackground(Color.WHITE);
		frame.setBounds(400, 100, 710, 500); // 位置及大小
		frame.setResizable(false); // 窗口大小不可改变
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口程序结束

		//初始化面板及其参数
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setFont(new Font("楷体", Font.BOLD, 26));
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), "统计的文件/文件夹",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("楷体", 0, 20)));

		// 添加清空按钮
		clearBtn = new JButton("清空");
		clearBtn.addActionListener(this);

		// 添加选择文件按钮
		chooseBtn = new JButton("选择工程");
		chooseBtn.addActionListener(this);

		// 添加统计后缀名按钮
		nameBtn = new JButton("选择后缀名");
		nameBtn.addActionListener(this);

		// 添加统计代码量按钮
		countBtn = new JButton("统计代码量");
		countBtn.addActionListener(this);

		// 顶部面板设计
		topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		inFileField.setFont(new Font("宋体", Font.PLAIN, 18));
		inFileField.setEditable(false); // 设置文件目录的文本框不可编辑
		topPanel.add(inFileField);
		topPanel.add(clearBtn);
		topPanel.add(chooseBtn);
		topPanel.add(nameBtn);
		topPanel.add(countBtn);
		panel.add(topPanel, BorderLayout.NORTH);

		// 初始化中部面板
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(new BorderLayout());
		// 设置布局
		selectPanel = new JPanel();
		selectPanel.setLayout(new BorderLayout());
		// 设置组件
		selectText = new JTextField(30);
		selectBtn = new JButton("筛选");
		selectBtn.addActionListener(this);
		outBtn = new JButton("导出至Excel");
		outBtn.addActionListener(this);
		selectPanel.setBackground(Color.white);
		
		//筛选功能面板
		JPanel lPanel = new JPanel();
		lPanel.setBackground(Color.white);
		lPanel.add(selectText);
		lPanel.add(selectBtn);
		selectPanel.add(lPanel,BorderLayout.WEST);
		JPanel rPanel = new JPanel();
		rPanel.setBackground(Color.white);
		rPanel.add(outBtn);
		selectPanel.add(rPanel,BorderLayout.EAST);
		centerPanel.add(selectPanel, BorderLayout.SOUTH);
		panel.add(centerPanel, BorderLayout.CENTER);

		// 设置表格属性
		table = new JTable(data, tableName) {
			// 设置表格不可编辑
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
		table.setRowHeight(25);
		table.getColumnModel().getColumn(0).setPreferredWidth(400);

		JScrollPane sp = new JScrollPane(table);
		panel.add(sp, BorderLayout.SOUTH);

		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//清空按钮监听器
		if (e.getSource() == clearBtn) {
			inFileField.setText(" 请先选择要统计的文件或目录");
			inFileField.setForeground(Color.red);
		} else if (e.getSource() == chooseBtn) {	//选择工程按钮监听器
			JFileChooser fileChooser = new JFileChooser("D:\\"); // 文件选择器+默认路径
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 设置选择器可选文件和路径
			int resultVal = fileChooser.showOpenDialog(frame); // 显示选择器
			if (resultVal == JFileChooser.APPROVE_OPTION) {
				chooseFile = fileChooser.getSelectedFile();
				if (chooseFile != null) {
					inFileField.setForeground(Color.black);
					inFileField.setText(chooseFile.getPath()); // 将选择的路径/文件输出到文本框
				}
			} else {
				inFileField.setText("未选择文件");
			}
		} else if (e.getSource() == nameBtn) {	//选择后缀名监听器
			//从文本框中获取用户指定路径
			chooseFile = new File(inFileField.getText());

			if (!chooseFile.exists()) {
				JOptionPane.showMessageDialog(null, "未选择文件或目录!!!", "警告", JOptionPane.WARNING_MESSAGE, null);
			} else {
				nameBtn.setText("统计中...");
				frame.update(frame.getGraphics()); // 刷新界面，使“统计中...”动态显示在界面中
				codeCount = new CodeCount(); // 初始化统计量的对象
				frame.setEnabled(false); // 设置窗口不可编辑
				codeCount.countSufs(chooseFile); // 统计后缀名
				if (nameDoClick) {
					nameDoClick = false;
				} else {
					// 创建自定义后缀名的对象
					ChooSufsView cSufsView = new ChooSufsView(codeCount, frame);
					// 实现自定义后缀名的方法
					cSufsView.chooseSufs(codeCount.getSufList());
				}
				// 改回按钮名字
				nameBtn.setText("选择后缀名");
			}
		} else if (e.getSource() == countBtn) {
			//根据统计量对象是否为空进行判断是否统计了后缀名
			if (codeCount == null) {
				// 首先将标志改为true
				nameDoClick = true;
				// 此对象为空，说明还没统计后缀名,执行统计后缀名
				nameBtn.doClick();
			}

			// 从文本框中获取用户指定路径
			chooseFile = new File(inFileField.getText());

			if (chooseFile.exists()) { // 判断文件/目录是否存在
				countBtn.setText("统计中...");
				frame.setEnabled(false);
				frame.update(frame.getGraphics()); // 刷新界面，使“统计中...”动态显示在界面中
				// 开始执行统计
				codeCount.countCodes(chooseFile,codeCount.getSufList());
				files = codeCount.outMsg(table, inFileField.getText());
				// 恢复界面
				countBtn.setText("统计代码量");
				frame.setEnabled(true);
			} 
			//复制对象
			codeCount2 = codeCount;
			// 将统计量对象设置为空
			codeCount = null;
		} else if (e.getSource() == selectBtn) {
			if (files == null) {
				JOptionPane.showMessageDialog(null, "请先进行工程统计!!!", "警告", JOptionPane.WARNING_MESSAGE, null);
			} else {
				if (selectBtn.getText().equals("筛选")) {
					selectBtn.setText("取消筛选");
					Vector vName = new Vector();
					vName.add("文件目录");
					vName.add("代码量");
					vName.add("注释量");
					vName.add("空行数");
					vName.add("总行数");
					DefaultTableModel model = null;
					String rex = selectText.getText();
					String prefix = inFileField.getText();
					Vector vData = new Vector();
					for (Code c : files) {
						if (c.getFileDir().replace(prefix, "").indexOf(rex) > 0) {
							Vector row = new Vector();
							row.add(c.getFileDir().replace(prefix, ""));
							row.add(c.getLineNum() - c.getNoteNum() - c.getSpaceNum());
							row.add(c.getNoteNum());
							row.add(c.getSpaceNum());
							row.add(c.getLineNum());
							vData.add(row.clone());
						}
					}
					model = new DefaultTableModel(vData, vName);
					table.setModel(model);
					table.getColumnModel().getColumn(0).setPreferredWidth(400);
				} else {
					selectBtn.setText("筛选");
					selectText.setText("");
					codeCount2.outMsg(table, inFileField.getText());
				}
			}

		} else if (e.getSource() == outBtn) {
			if(codeCount2==null) {
				JOptionPane.showMessageDialog(null, "请先进行工程统计!!!", "警告", JOptionPane.WARNING_MESSAGE, null);
			}else {
				//导出至Excel
				codeCount2.exportToFile(codeCount2,inFileField.getText(),frame);
			}
		}
	}
}
