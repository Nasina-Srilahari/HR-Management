package employee.management.system1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener{

    JButton view, add, update, remove, attendance,showattendance,leavemanagement ;
    
    Home() {
        
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/home1.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1120, 630, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1150, 630);
        add(image);
        
        JLabel heading = new JLabel("Employee Management");
        heading.setBounds(670, 20, 400, 40);
        heading.setFont(new Font("Raleway", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);
        image.add(heading);
        
        
        add = new JButton("Add Employee");
        add.setBounds(650, 80, 150, 40);
        add.addActionListener(this);
        image.add(add);
        
        view = new JButton("View Employees");
        view.setBounds(820, 80, 150, 40);
        view.addActionListener(this);
        image.add(view);
        
        update = new JButton("Update Employee");
        update.setBounds(650, 140, 150, 40);
        update.addActionListener(this);
        image.add(update);
        
        remove = new JButton("Remove Employee");
        remove.setBounds(820, 140, 150, 40);
        remove.addActionListener(this);
        image.add(remove);
        
        attendance = new JButton("Attendance Tracker");
        attendance.setBounds(650, 200, 150, 40);
        attendance.addActionListener(this);
        image.add(attendance);
        
        showattendance = new JButton("Check Attendance");
        showattendance.setBounds(820, 200, 150, 40);
        showattendance.addActionListener(this);
        image.add(showattendance);
        
        leavemanagement = new JButton("Leave Management");
        leavemanagement.setBounds(650, 260, 150, 40);
        leavemanagement.addActionListener(this);
        image.add(leavemanagement);
        
        setSize(1170, 650);
        setLocation(50, 10);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            setVisible(false);
            new AddEmployee();
        } else if (ae.getSource() == view) {
            setVisible(false);
            new ViewEmployee();
        } else if (ae.getSource() == update) {
            setVisible(false);
            new ViewEmployee();
        } else if (ae.getSource() == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (ae.getSource() == attendance) {
            setVisible(false);
            new AttendanceTracker();
        } else if (ae.getSource() == showattendance) {
            showAttendance();
        } else if (ae.getSource() == leavemanagement) {
            new LeaveManagement();
        }
    }
    private void showAttendance() {
        setVisible(false);
        new ShowAttendance();
    }
    
    public static void main(String[] args) {
        new Home();
    }
}
