package com.soft.ui.util;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.springframework.stereotype.Component;
 
//本类实现了ActionListener接口. 一个ActionListener可以响应JMenuItem 和JButton的动作
//本类实现FocusListener接口,一个FocusListener可以响应JTextField,JButton等
//JButton响应多个事件接口
@Component
public class MyGuiFrame extends JFrame implements ActionListener, FocusListener {
    private JTextField jtf1;
    private JTextField jtf2;
    private JTextField jtf3;
    private JButton jb1;
     
    public MyGuiFrame() {
        // ----------窗口属性的设置----------
        setTitle("窗口应用程序");// 窗口标题
        setSize(380, 120);// 窗口大小
        setLocationRelativeTo(null);// 窗口居于屏幕中央
        setDefaultCloseOperation(EXIT_ON_CLOSE);// 点击关闭窗口后退出jvm虚拟机
        getContentPane().setLayout(new BorderLayout(5, 3));// 边界布局,水平间距5,垂直间距3
 
        // 菜单栏组件初始化
        initMenu();
 
        // 主要面板的初始化
        initPanel();
    }
 
    private void initPanel() {
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER));// 流式布局
        jtf1 = new JTextField(8);
        jtf1.addFocusListener(this);// 添加焦点响应
        JLabel jl = new JLabel("+");
        jtf2 = new JTextField(8);
        jtf2.addFocusListener(this);
        jb1 = new JButton("=");
        jb1.addActionListener(this);// 添加动作响应
        jb1.addFocusListener(this);// 添加焦点响应
 
        jtf3 = new JTextField(8);
        jtf3.setEditable(false);
 
        jp.add(jtf1);
        jp.add(jl);
        jp.add(jtf2);
        jp.add(jb1);
        jp.add(jtf3);
        getContentPane().add(jp, BorderLayout.CENTER);
 
    }
 
    private void initMenu() {// 菜单栏的初始化和 设置
        JMenuBar jmb = new JMenuBar();
        JMenu jm1 = new JMenu("系统");
        JMenuItem jmi101 = new JMenuItem("退出");
        jmi101.addActionListener(this);// 添加动作响应
        JMenu jm2 = new JMenu("帮助");
        JMenuItem jmi201 = new JMenuItem("功能说明");
        jmi201.addActionListener(this);
        jm1.add(jmi101);
        jm2.add(jmi201);
        jmb.add(jm1);
        jmb.add(jm2);
        setJMenuBar(jmb);//设置菜单栏
    }
 
    // main方法, 创建对象窗口, 并且设置可见
    public static void main(String[] args) {
        new MyGuiFrame().setVisible(true);
    }
 
    // 动作响应处理
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();//根据命令来区分不同的操作
        if (cmd.equals("退出")) {
            System.exit(0);
        }
        if (cmd.equals("功能说明")) {
            JOptionPane.showMessageDialog(this, "加法");
        }
        if (cmd.equals("=")) {
             
            String s1 = jtf1.getText().trim();
            String s2 = jtf2.getText().trim();
            if(s1.equals("")){
                s1="0";
            }
            if(s2.equals("")){
                s2="0";
            }
            double num1 = Double.parseDouble(s1);//从字符串转小数
            double num2 = Double.parseDouble(s2);
            jtf3.setText((num1 + num2) + "");// 数字类型转字符串类型
 
        }
    }
 
    // 焦点响应处理
    public void focusGained(FocusEvent e) {//获得焦点
        JComponent cmp = (JComponent) e.getComponent();//根据事件来源组件来区分不同的操作
        if (cmp == jtf1 || cmp == jtf2) {
            cmp.setBorder(BorderFactory.createLineBorder(Color.BLUE));// 设置边框
        }
        if (cmp == jb1) {
            jb1.setForeground(Color.RED);//设置文字颜色
        }
    }
 
    public void focusLost(FocusEvent e) {//失去焦点
        JComponent cmp = (JComponent) e.getComponent();
        if (cmp == jtf1 || cmp == jtf2) {
            cmp.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        if (cmp == jb1) {
            jb1.setForeground(Color.BLACK);
        }
    }
 
}