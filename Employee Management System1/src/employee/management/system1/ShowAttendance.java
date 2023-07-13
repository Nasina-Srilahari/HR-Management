package employee.management.system1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

public class ShowAttendance extends JFrame {
    private JTable attendanceTable;
    private JButton backButton;

    ShowAttendance() {
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Attendance Data");
        heading.setFont(new Font("Raleway", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        add(heading, BorderLayout.NORTH);

        attendanceTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchAttendanceData();

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            setVisible(false);
            new Home();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchAttendanceData() {
        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM attendance";
            ResultSet rs = conn.s.executeQuery(query);

            // Convert ResultSet to DefaultTableModel
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee ID");
            model.addColumn("Date");
            model.addColumn("Status");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("empId"));
                row.add(rs.getDate("date"));
                row.add(rs.getString("status"));
                data.add(row);
            }

            // Sort the data based on the date in descending order
            data.sort(Comparator.comparing(row -> (Date) row.get(1), Comparator.reverseOrder()));

            // Add the sorted data to the model
            for (Vector<Object> row : data) {
                model.addRow(row);
            }

            attendanceTable.setModel(model);

            // Increase font size of the table
            attendanceTable.setFont(new Font("Arial", Font.PLAIN, 16));

            // Adjust column widths
            TableColumnModel columnModel = attendanceTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(100);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching attendance data.");
        }
    }

    public static void main(String[] args) {
        new ShowAttendance();
    }
}
