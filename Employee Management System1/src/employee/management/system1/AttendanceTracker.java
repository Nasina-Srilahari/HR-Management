package employee.management.system1;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AttendanceTracker extends JFrame implements ActionListener {
    private JComboBox<String> empIdComboBox;
    private JDateChooser dateChooser;
    private JButton markAttendanceButton, back,showAttendanceButton;
    private Map<String, String> attendanceMap;

    AttendanceTracker() {
        setLayout(null);

        attendanceMap = new HashMap<>();

        JLabel empIdLabel = new JLabel("Employee ID");
        empIdLabel.setBounds(50, 50, 100, 30);
        add(empIdLabel);

        empIdComboBox = new JComboBox<>();
        empIdComboBox.setBounds(150, 50, 150, 30);
        add(empIdComboBox);

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setBounds(50, 100, 100, 30);
        add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(150, 100, 150, 30);
        add(dateChooser);

        markAttendanceButton = new JButton("Mark Attendance");
        markAttendanceButton.setBounds(50, 150, 150, 30);
        markAttendanceButton.addActionListener(this);
        add(markAttendanceButton);

        back = new JButton("Back");
        back.setBounds(220, 150, 150, 30);
        back.addActionListener(this);
        add(back);

        populateEmployeeIds();

        setSize(450, 250);
        setLocation(400, 200);
        setVisible(true);
    }

    private void populateEmployeeIds() {
        try {
            Conn conn = new Conn();
            String query = "SELECT empId FROM employee";
            ResultSet rs = conn.s.executeQuery(query);
            while (rs.next()) {
                String empId = rs.getString("empId");
                empIdComboBox.addItem(empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == markAttendanceButton) {
            markAttendance();
        }  else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    private void markAttendance() {
    String empId = (String) empIdComboBox.getSelectedItem();
    java.util.Date selectedDate = dateChooser.getDate();

    if (empId == null || empId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select the employee ID.");
    } else if (selectedDate == null) {
        JOptionPane.showMessageDialog(this, "Please select the date.");
    } else {
        String date = new java.sql.Date(selectedDate.getTime()).toString();

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM attendance WHERE empId = ? AND date = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setString(1, empId);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String existingStatus = rs.getString("status");
                JOptionPane.showMessageDialog(this, "Attendance already marked for employee ID: " + empId + " on " + date
                        + "\nExisting Status: " + existingStatus);
            } else {
                String[] options = {"Present", "Absent", "Leave"};
                int choice = JOptionPane.showOptionDialog(this, "Select Attendance Status", "Mark Attendance",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                String status;
                if (choice == 0) {
                    status = "Present";
                } else if (choice == 1) {
                    status = "Absent";
                } else {
                    status = "Leave";
                }

                query = "INSERT INTO attendance (empId, date, status) VALUES (?, ?, ?)";
                pstmt = conn.c.prepareStatement(query);
                pstmt.setString(1, empId);
                pstmt.setString(2, date);
                pstmt.setString(3, status);
                pstmt.executeUpdate();

                attendanceMap.put(empId, status);
                JOptionPane.showMessageDialog(this, "Attendance marked as " + status + " for employee ID: " + empId + " on " + date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while marking attendance.");
        }
    }
}


    public static void main(String[] args) {
        new AttendanceTracker();
    }
}
