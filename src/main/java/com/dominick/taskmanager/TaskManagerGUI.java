package com.dominick.taskmanager;

import com.dominick.taskmanager.Task;
import com.toedter.calendar.JDateChooser;
import javafx.scene.control.DatePicker;

//import org.jdatepicker.JDatePicker;
//import org.jdatepicker.UtilDateModel;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Task task : tasks) {
            String formattedDate = dateFormat.format(task.getDueDate());

            String taskDetails = "Name: " + task.getName() +
                    ", Due Date: " + task.getFormattedDueDate() +
                    ", Status: " + task.getStatus();

            taskListModel.addElement(taskDetails);
        }
    }

    private void addTask() {
        String taskName = JOptionPane.showInputDialog("Enter task name:");
        if (taskName != null && !taskName.trim().isEmpty()) {
            // Get due date
            JDateChooser jd = new JDateChooser();
            String message ="Choose start date:\n";
            Object[] params = {message,jd};
            JOptionPane.showConfirmDialog(null,params,"Start date", JOptionPane.PLAIN_MESSAGE);


            // Get status
            String status = JOptionPane.showInputDialog("Enter task status:");

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
        int selectedIndex = taskList.getSelectedIndex();
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
