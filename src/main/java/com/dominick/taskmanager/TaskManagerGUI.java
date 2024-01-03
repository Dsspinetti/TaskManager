package com.dominick.taskmanager;

import com.toedter.calendar.JDateChooser;
import javafx.scene.control.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private DatePicker datePicker;

    private final TaskManager taskManager;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;  // Declare taskList as an instance variable

    public TaskManagerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);  // Initialize taskList

        JScrollPane scrollPane = new JScrollPane(taskList);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        JButton saveButton = new JButton("Save Tasks");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTasks();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateTaskList();
    }

    private void updateTaskList() {
        List<Task> tasks = taskManager.getAllTasks();
        taskListModel.clear();

        taskListModel.addElement("<html><b>Name</b>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>Due Date</b>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>Status</b></html>");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Task task : tasks) {
            String formattedDate = dateFormat.format(task.getDueDate());

            String taskDetails = String.format("<html><b>"
                    + task.getName()
                    + "</b>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>"
                    + task.getFormattedDueDate()
                    + "</b>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>"
                    + task.getStatus()+"</b></html>");
            taskListModel.addElement(taskDetails);
        }
    }

    private void addTask() {
        String taskName = JOptionPane.showInputDialog("Enter task name:");
        if (taskName != null && !taskName.trim().isEmpty()) {
            // Get due date
            JDateChooser jd = new JDateChooser();
            String message = "Choose start date:\n";
            Object[] params = {message, jd};
            JOptionPane.showConfirmDialog(null, params, "Start date", JOptionPane.PLAIN_MESSAGE);


            // Get status
            String[] statusOptions = {"Incomplete", "In progress", "Complete"};
            JComboBox<String> statusDropdown = new JComboBox<>(statusOptions);
            statusDropdown.setSelectedIndex(0); // Set the default status
            Object[] statusParams = {"Choose task status:", statusDropdown};
            JOptionPane.showConfirmDialog(null, statusParams, "Task status", JOptionPane.PLAIN_MESSAGE);
            String status = (String) statusDropdown.getSelectedItem();

            Task newTask = new Task(taskName, jd.getDate(), status);
            taskManager.addTask(newTask);
            updateTaskList();
        }
    }

    // Helper method to parse date from string (add appropriate error handling)
    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception, e.g., show an error message
            e.printStackTrace();
            return null;
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex() - 1;
        if (selectedIndex != -1) {
            Task selectedTask = taskManager.getAllTasks().get(selectedIndex);
            taskManager.removeTask(selectedTask);
            updateTaskList();

            // Ensure the selected index is within the updated list bounds
            int newSelectedIndex = Math.min(selectedIndex, taskListModel.size() - 1);
            taskList.setSelectedIndex(newSelectedIndex);
        }
    }

    private void saveTasks() {
        taskManager.saveTasksToFile("taskData");

    }
}
