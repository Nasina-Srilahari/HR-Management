package employee.management.system1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LeaveManagement extends JFrame implements ActionListener {
    private JTextField empIdField, fromDateField, toDateField, reasonField;
    private JButton addLeaveButton, viewLeaveButton, approveRejectButton;
    private JTextArea leaveTextArea;

    LeaveManagement() {
        setLayout(null);

        JLabel heading = new JLabel("Leave Management");
        heading.setBounds(180, 20, 200, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 20));
        add(heading);

        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setBounds(50, 70, 100, 30);
        add(empIdLabel);

        empIdField = new JTextField();
        empIdField.setBounds(160, 70, 150, 30);
        add(empIdField);

        JLabel fromDateLabel = new JLabel("From Date (yyyy-mm-dd):");
        fromDateLabel.setBounds(50, 120, 150, 30);
        add(fromDateLabel);

        fromDateField = new JTextField();
        fromDateField.setBounds(220, 120, 150, 30);
        add(fromDateField);

        JLabel toDateLabel = new JLabel("To Date (yyyy-mm-dd):");
        toDateLabel.setBounds(50, 170, 150, 30);
        add(toDateLabel);

        toDateField = new JTextField();
        toDateField.setBounds(220, 170, 150, 30);
        add(toDateField);

        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setBounds(50, 220, 100, 30);
        add(reasonLabel);

        reasonField = new JTextField();
        reasonField.setBounds(160, 220, 250, 30);
        add(reasonField);

        addLeaveButton = new JButton("Add Leave");
        addLeaveButton.setBounds(120, 270, 120, 30);
        addLeaveButton.addActionListener(this);
        add(addLeaveButton);

        viewLeaveButton = new JButton("View Leave");
        viewLeaveButton.setBounds(250, 270, 120, 30);
        viewLeaveButton.addActionListener(this);
        add(viewLeaveButton);

        approveRejectButton = new JButton("Approve/Reject");
        approveRejectButton.setBounds(180, 320, 150, 30);
        approveRejectButton.addActionListener(this);
        add(approveRejectButton);

        leaveTextArea = new JTextArea();
        leaveTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(leaveTextArea);
        scrollPane.setBounds(50, 370, 380, 180);
        add(scrollPane);

        setSize(480, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addLeaveButton) {
            addLeave();
        } else if (ae.getSource() == viewLeaveButton) {
            viewLeave();
        } else if (ae.getSource() == approveRejectButton) {
            approveRejectLeave();
        }
    }

    private void addLeave() {
        String empId = empIdField.getText();
        String fromDate = fromDateField.getText();
        String toDate = toDateField.getText();
        String reason = reasonField.getText();

        if (empId.isEmpty() || fromDate.isEmpty() || toDate.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.");
        } else {
            try {
                Conn conn = new Conn();
                String query = "INSERT INTO leave_requests (empId, fromDate, toDate, reason, status) " +
                        "VALUES ('" + empId + "', '" + fromDate + "', '" + toDate + "', '" + reason + "', 'Pending')";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Leave request added successfully.");
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while adding leave request.");
            }
        }
    }

    private void viewLeave() {
        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM leave_requests";
            ResultSet rs = conn.s.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            sb.append("Employee ID\tFrom Date\tTo Date\tReason\tStatus\n");
            sb.append("--------------------------------------------------\n");
            while (rs.next()) {
                String empId = rs.getString("empId");
                String fromDate = rs.getString("fromDate");
                String toDate = rs.getString("toDate");
                String reason = rs.getString("reason");
                String status = rs.getString("status");
                sb.append(empId).append("\t").append(fromDate).append("\t").append(toDate).append("\t")
                        .append(reason).append("\t").append(status).append("\n");
            }

            leaveTextArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching leave requests.");
        }
    }

    private void approveRejectLeave() {
        String selectedLeave = leaveTextArea.getSelectedText();
        if (selectedLeave == null || selectedLeave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a leave request to approve/reject.");
        } else {
            String[] leaveDetails = selectedLeave.split("\t");
            if (leaveDetails.length < 5) {
                JOptionPane.showMessageDialog(this, "Invalid leave request selected.");
            } else {
                String empId = leaveDetails[0];
                String fromDate = leaveDetails[1];
                String toDate = leaveDetails[2];
                String reason = leaveDetails[3];
                String status = leaveDetails[4];

                String[] options = {"Approve", "Reject"};
                int choice = JOptionPane.showOptionDialog(this, "Select Action", "Approve/Reject Leave",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                String newStatus;
                if (choice == 0) {
                    newStatus = "Approved";
                } else {
                    newStatus = "Rejected";
                }

                try {
                    Conn conn = new Conn();
                    String query = "UPDATE leave_requests SET status = '" + newStatus + "' " +
                            "WHERE empId = '" + empId + "' AND fromDate = '" + fromDate + "' AND toDate = '" + toDate + "'";
                    conn.s.executeUpdate(query);
                    JOptionPane.showMessageDialog(this, "Leave request " + newStatus.toLowerCase() + " successfully.");
                    viewLeave();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error occurred while updating leave request status.");
                }
            }
        }
    }

    private void clearFields() {
        empIdField.setText("");
        fromDateField.setText("");
        toDateField.setText("");
        reasonField.setText("");
    }

    public static void main(String[] args) {
        new LeaveManagement();
    }
}
