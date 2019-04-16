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
 * �Զ����׺������
 *
 */
public class ChooSufsView {
	private JFrame frame;	//������
	private JPanel panel;	//���
	
	private JCheckBox[] jCheckBoxs;		//���ڴ洢��׺���ĸ�ѡ��
	private JCheckBox seleAll;	//ȫѡ��ѡ��
	
	private List<String> sufList = new ArrayList<>();	//Ҫ���յĺ�׺������
	
	public ChooSufsView(CodeCount codeCount, JFrame jf) {
		
		frame = new JFrame("��Ŀ�°�����׺��");
		
		// ���ô�����ز���
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	//�������
			frame.setIconImage(ImageIO.read(new FileInputStream(new File("img\\tit.jpg")))); // ����ͼ��
			frame.setBounds(400, 100, 400, 500); // λ�ü���С
			frame.setResizable(false); // ���ڴ�С���ɸı�
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // ���ô��ڲ��ɹر�
			frame.setLayout(null); // ��������Ϊ��
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/********************���ѡ���ļ�/�ļ��е����**********************/
		panel = new JPanel();
		panel.setLayout(null); // ��������Ϊ��
		panel.setBounds(10, 10, 380, 400);
		panel.setBackground(Color.white);
		panel.setFont(new Font("����", Font.BOLD, 26));
		panel.setBorder(BorderFactory.createEtchedBorder());
		
		frame.add(panel);
		
		//��ӡ�ȷ������ť
		JButton sureBtn = new JButton("ȷ��");
		sureBtn.setBounds(320, 420, 60, 35);
		sureBtn.setFont(new Font("����", Font.PLAIN, 20));
		//���ð�ť�е����ֿ������������ť
		sureBtn.setBorder(BorderFactory.createEtchedBorder());
		sureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				codeCount.getSufList().clear();
				//������׺����ѡ��,����ָ���ĺ�׺��
				List<String> newSufList = new ArrayList<>();
				for (int i = 0; i < jCheckBoxs.length; i++) {
					if(jCheckBoxs[i].isSelected()) {
						newSufList.add(sufList.get(i));
					}
				}
				//��ѡ���ļ��ϷŽ�ͳ������
				codeCount.setSufList(newSufList);
				//�˴��ڹرպ�,������ת���ɱ༭״̬
				jf.setEnabled(true);
				//���ȷ����ſɹرմ���
				frame.dispose();
			}
		});
		frame.add(sureBtn);
		
		frame.setVisible(true);
		
	}
	
	/**
	 * �Զ����׺������
	 * @param sufList
	 * @return
	 */
	public void chooseSufs(List<String> sufList) {
		//�ȸ���һ�ݼ���
		this.sufList.addAll(sufList);
		//������ѡ������,�����洢��׺������
		jCheckBoxs = new JCheckBox[sufList.size()];
		//ѭ��������ָ��Ŀ¼���������ļ���׺��,��������Ӧ��ѡ��
		for (int i = 0; i < sufList.size(); i++) {
			//��������
			jCheckBoxs[i] = new JCheckBox("."+sufList.get(i));	
			//���ø�ѡ�򱳾�ɫΪ��ɫ
			jCheckBoxs[i].setBackground(Color.WHITE);
			//����iֵ������ѡ�����õ���ͬ����,��������,ͳ������Ϊ35���׺����1��ȫѡ��׺��
			if(i<12) {
				jCheckBoxs[i].setBounds(10, i*31+10, 120, 30);
			}else if(i>=12 && i<24){
				jCheckBoxs[i].setBounds(130, (i-12)*31+10, 120, 30);
			}else {
				jCheckBoxs[i].setBounds(250, (i-24)*31+10, 120, 30);
			}
			//��ѡ��Ĭ�ϱ�ѡ��
			jCheckBoxs[i].setSelected(true);
			panel.add(jCheckBoxs[i]);
		}
		
		//ˢ�½���,���ⰾ����ʹ���������ʾ
		frame.update(frame.getGraphics());	
		//��������ȫѡ��ѡ��ĺ���,���ݺ�׺������������λ��
		setSeleAll();
		
	}
	
	/**
	 * ����ȫѡ��ѡ��ĺ���
	 */
	public void setSeleAll() {
		seleAll = new JCheckBox("ѡ��ȫ��");
		seleAll.setBackground(Color.WHITE);
		seleAll.setFont(new Font("����", Font.BOLD, 18));
		seleAll.setForeground(Color.blue);
		seleAll.setBounds(250, (35-24)*31+10, 120, 30);
		seleAll.setSelected(true);
		//����ȫѡ��ѡ���Ƿ�ѡ��
		seleAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(seleAll.isSelected()) {
					//���ȫѡ��ѡ��ѡ����ô���еĶ���ѡ��
					for (JCheckBox jCheckBox : jCheckBoxs) {
						jCheckBox.setSelected(true);
					}
				}else {
					//���ȫѡ��ѡ��û�б�ѡ����ô���е�ȡ������ѡ��
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
