package com.dominick.taskmanager;

import javax.swing.*;

public class Main {
    private static final String DEFAULT_FILE_NAME = "taskData";

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.loadTasksFromFile(DEFAULT_FILE_NAME);

        // Set up the GUI
        SwingUtilities.invokeLater(() -> {
            TaskManagerGUI taskManagerGUI = new TaskManagerGUI(taskManager);
            taskManagerGUI.setVisible(true);
        });
    }
}
