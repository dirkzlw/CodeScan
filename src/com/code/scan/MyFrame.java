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
 * ������
 *
 */
@SuppressWarnings("all")
public class MyFrame implements ActionListener {
	// ����������
	private JFrame frame;
	// ���������
	private JPanel panel;
	private JButton clearBtn;
	private JButton chooseBtn;
	private JButton nameBtn;
	private JButton countBtn;
	// ���ü������
	private JPanel selectPanel;
	private JTextField selectText;
	private JButton selectBtn;
	private JButton outBtn;
	// ���ö������
	private JPanel topPanel;
	// �ļ�ѡ����ı���
	private JTextField inFileField = new JTextField(30);
	// �м����
	private JPanel centerPanel;
	// ��Ҫ�������ļ�/Ŀ¼
	private File chooseFile;
	private List<Code> files;
	// �������м����Ķ���
	private CodeCount codeCount = null;
	private CodeCount codeCount2 = null;	//�м���

	//�����б�����Ϣ
	private String[] tableName = { "�ļ�Ŀ¼", "������", "ע����", "������", "������" };
	private Object[][] data = { { "δѡ���ļ�", 0, 0, 0, 0 } };
	private JTable table;

	// ���ڱ�־���ú�׺����ť�Ƿ񱻵��
	static boolean nameDoClick = false;

	//�޲ι�����
	public MyFrame() {
		try {
			// �������
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//��ʼ���ı�����ʾ��Ϣ
		inFileField.setText(" ����ѡ��Ҫͳ�Ƶ��ļ���Ŀ¼");
		inFileField.setBackground(Color.WHITE);
		inFileField.setForeground(Color.red);

		// ���ô��ڼ���������
		frame = new JFrame();
		try {
			// ����ͼ��
			frame.setIconImage(ImageIO.read(new FileInputStream(new File("img\\tit.jpg"))));
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		frame.setBackground(Color.WHITE);
		frame.setBounds(400, 100, 710, 500); // λ�ü���С
		frame.setResizable(false); // ���ڴ�С���ɸı�
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // �رմ��ڳ������

		//��ʼ����弰�����
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setFont(new Font("����", Font.BOLD, 26));
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), "ͳ�Ƶ��ļ�/�ļ���",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("����", 0, 20)));

		// �����հ�ť
		clearBtn = new JButton("���");
		clearBtn.addActionListener(this);

		// ���ѡ���ļ���ť
		chooseBtn = new JButton("ѡ�񹤳�");
		chooseBtn.addActionListener(this);

		// ���ͳ�ƺ�׺����ť
		nameBtn = new JButton("ѡ���׺��");
		nameBtn.addActionListener(this);

		// ���ͳ�ƴ�������ť
		countBtn = new JButton("ͳ�ƴ�����");
		countBtn.addActionListener(this);

		// ����������
		topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		inFileField.setFont(new Font("����", Font.PLAIN, 18));
		inFileField.setEditable(false); // �����ļ�Ŀ¼���ı��򲻿ɱ༭
		topPanel.add(inFileField);
		topPanel.add(clearBtn);
		topPanel.add(chooseBtn);
		topPanel.add(nameBtn);
		topPanel.add(countBtn);
		panel.add(topPanel, BorderLayout.NORTH);

		// ��ʼ���в����
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(new BorderLayout());
		// ���ò���
		selectPanel = new JPanel();
		selectPanel.setLayout(new BorderLayout());
		// �������
		selectText = new JTextField(30);
		selectBtn = new JButton("ɸѡ");
		selectBtn.addActionListener(this);
		outBtn = new JButton("������Excel");
		outBtn.addActionListener(this);
		selectPanel.setBackground(Color.white);
		
		//ɸѡ�������
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

		// ���ñ������
		table = new JTable(data, tableName) {
			// ���ñ�񲻿ɱ༭
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.getTableHeader().setReorderingAllowed(false); // ���������ƶ�
		table.getTableHeader().setResizingAllowed(false); // �����������
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
		//��հ�ť������
		if (e.getSource() == clearBtn) {
			inFileField.setText(" ����ѡ��Ҫͳ�Ƶ��ļ���Ŀ¼");
			inFileField.setForeground(Color.red);
		} else if (e.getSource() == chooseBtn) {	//ѡ�񹤳̰�ť������
			JFileChooser fileChooser = new JFileChooser("D:\\"); // �ļ�ѡ����+Ĭ��·��
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // ����ѡ������ѡ�ļ���·��
			int resultVal = fileChooser.showOpenDialog(frame); // ��ʾѡ����
			if (resultVal == JFileChooser.APPROVE_OPTION) {
				chooseFile = fileChooser.getSelectedFile();
				if (chooseFile != null) {
					inFileField.setForeground(Color.black);
					inFileField.setText(chooseFile.getPath()); // ��ѡ���·��/�ļ�������ı���
				}
			} else {
				inFileField.setText("δѡ���ļ�");
			}
		} else if (e.getSource() == nameBtn) {	//ѡ���׺��������
			//���ı����л�ȡ�û�ָ��·��
			chooseFile = new File(inFileField.getText());

			if (!chooseFile.exists()) {
				JOptionPane.showMessageDialog(null, "δѡ���ļ���Ŀ¼!!!", "����", JOptionPane.WARNING_MESSAGE, null);
			} else {
				nameBtn.setText("ͳ����...");
				frame.update(frame.getGraphics()); // ˢ�½��棬ʹ��ͳ����...����̬��ʾ�ڽ�����
				codeCount = new CodeCount(); // ��ʼ��ͳ�����Ķ���
				frame.setEnabled(false); // ���ô��ڲ��ɱ༭
				codeCount.countSufs(chooseFile); // ͳ�ƺ�׺��
				if (nameDoClick) {
					nameDoClick = false;
				} else {
					// �����Զ����׺���Ķ���
					ChooSufsView cSufsView = new ChooSufsView(codeCount, frame);
					// ʵ���Զ����׺���ķ���
					cSufsView.chooseSufs(codeCount.getSufList());
				}
				// �Ļذ�ť����
				nameBtn.setText("ѡ���׺��");
			}
		} else if (e.getSource() == countBtn) {
			//����ͳ���������Ƿ�Ϊ�ս����ж��Ƿ�ͳ���˺�׺��
			if (codeCount == null) {
				// ���Ƚ���־��Ϊtrue
				nameDoClick = true;
				// �˶���Ϊ�գ�˵����ûͳ�ƺ�׺��,ִ��ͳ�ƺ�׺��
				nameBtn.doClick();
			}

			// ���ı����л�ȡ�û�ָ��·��
			chooseFile = new File(inFileField.getText());

			if (chooseFile.exists()) { // �ж��ļ�/Ŀ¼�Ƿ����
				countBtn.setText("ͳ����...");
				frame.setEnabled(false);
				frame.update(frame.getGraphics()); // ˢ�½��棬ʹ��ͳ����...����̬��ʾ�ڽ�����
				// ��ʼִ��ͳ��
				codeCount.countCodes(chooseFile,codeCount.getSufList());
				files = codeCount.outMsg(table, inFileField.getText());
				// �ָ�����
				countBtn.setText("ͳ�ƴ�����");
				frame.setEnabled(true);
			} 
			//���ƶ���
			codeCount2 = codeCount;
			// ��ͳ������������Ϊ��
			codeCount = null;
		} else if (e.getSource() == selectBtn) {
			if (files == null) {
				JOptionPane.showMessageDialog(null, "���Ƚ��й���ͳ��!!!", "����", JOptionPane.WARNING_MESSAGE, null);
			} else {
				if (selectBtn.getText().equals("ɸѡ")) {
					selectBtn.setText("ȡ��ɸѡ");
					Vector vName = new Vector();
					vName.add("�ļ�Ŀ¼");
					vName.add("������");
					vName.add("ע����");
					vName.add("������");
					vName.add("������");
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
					selectBtn.setText("ɸѡ");
					selectText.setText("");
					codeCount2.outMsg(table, inFileField.getText());
				}
			}

		} else if (e.getSource() == outBtn) {
			if(codeCount2==null) {
				JOptionPane.showMessageDialog(null, "���Ƚ��й���ͳ��!!!", "����", JOptionPane.WARNING_MESSAGE, null);
			}else {
				//������Excel
				codeCount2.exportToFile(codeCount2,inFileField.getText(),frame);
			}
		}
	}
}
