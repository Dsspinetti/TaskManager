package com.dominick.taskmanager;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private final TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskManagerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;

        initializeUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManager taskManager = new TaskManager();
            TaskManagerGUI gui = new TaskManagerGUI(taskManager);
            gui.setVisible(true);
        });
    }

    private void initializeUI() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Initialize the table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Status");

        // Create the table with the table model
        taskTable = new JTable(tableModel);

        // Allow the status column to be editable with a dropdown
        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Incomplete", "In progress", "Complete"});
        taskTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(statusDropdown));
        // Configure the date column to use a custom renderer and editor
        taskTable.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
        taskTable.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor());
        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(taskTable);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(e -> removeTask());

        JButton saveButton = new JButton("Save Tasks");
        saveButton.addActionListener(e -> saveTasks());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
        updateTaskTable();
    }

    private void updateTaskTable() {
        List<Task> tasks = taskManager.getAllTasks();
        tableModel.setRowCount(0); // Clear existing rows

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Task task : tasks) {
            String formattedDate = dateFormat.format(task.getDueDate());
            JDateChooser jd = new JDateChooser();

            // Add a row to the table model
            tableModel.addRow(new Object[]{task.getName(), formattedDate, task.getStatus()});
        }
    }

    private void addTask() {
        String taskName = JOptionPane.showInputDialog("Enter task name:");
        if (taskName != null && !taskName.trim().isEmpty()) {
            // Get due date
            JDateChooser jd = new JDateChooser();
            String message = "Choose start date:\n";
            Object[] params = {message, jd};
            JOptionPane.showConfirmDialog(null, params, "Due date", JOptionPane.PLAIN_MESSAGE);

            // Get status
            String[] statusOptions = {"Incomplete", "In progress", "Complete"};
            JComboBox<String> statusDropdown = new JComboBox<>(statusOptions);
            statusDropdown.setSelectedIndex(0); // Set the default status
            Object[] statusParams = {"Choose task status:", statusDropdown};
            JOptionPane.showConfirmDialog(null, statusParams, "Task status", JOptionPane.PLAIN_MESSAGE);
            String status = (String) statusDropdown.getSelectedItem();

            Task newTask = new Task(taskName, jd.getDate(), status);
            taskManager.addTask(newTask);
            updateTaskTable();
        }
    }

    private void removeTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            taskManager.removeTask(selectedRow);
            updateTaskTable();
        }
    }

    private void saveTasks() {
        taskManager.saveTasksToFile("taskData");
    }

    class TaskTableModel extends AbstractTableModel {
        private List<Task> tasks;

        @Override
        public int getRowCount() {
            return tasks == null ? 0 : tasks.size();
        }

        @Override
        public int getColumnCount() {
            return 3; // Three columns: Name, Due Date, Status
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Task task = tasks.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return task.getName();
                case 1:
                    return task.getDueDate();
                case 2:
                    return task.getStatus();
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1 || columnIndex == 2; // Allow editing for Due Date and Status columns
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            Task task = tasks.get(rowIndex);
            switch (columnIndex) {
                case 1:
                    task.setDueDate((Date) value);
                    break;
                case 2:
                    task.setStatus((String) value);
                    break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            fireTableDataChanged();
        }
    }

    class DateCellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                value = dateFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JDateChooser dateChooser = new JDateChooser();

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Date) {
                dateChooser.setDate((Date) value);
            }
            return dateChooser;
        }

        @Override
        public Object getCellEditorValue() {
            return dateChooser.getDate();
        }
    }
}