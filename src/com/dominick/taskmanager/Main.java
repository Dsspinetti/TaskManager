package com.dominick.taskmanager;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_FILE_NAME = "taskData";
    public static void main(String[] args) {
        //begin application

        TaskManager taskManager = new TaskManager();
        taskManager.loadTasksFromFile(DEFAULT_FILE_NAME);
        Scanner scnr = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. Mark a task complete");
            System.out.println("4. Mark a task in progress");
            System.out.println("5. Edit task");
            System.out.println("6. List Tasks");
            System.out.println("7. Save Tasks to a file");
            System.out.println("8. Load Tasks from a file");
            System.out.println("9. Exit");

            int userChoice = scnr.nextInt();
            scnr.nextLine();

            switch (userChoice) {
                case 1 -> {
//                    Add task
                    System.out.println("Provide task name:");
                    String taskName = scnr.nextLine();
                    System.out.println("Provide due date (yyyy-MM-dd): ");
                    String dueDateString = scnr.nextLine();
                    Date dueDate = java.sql.Date.valueOf(dueDateString);
                    System.out.println("Provide task status: ");
                    String taskStatus = scnr.nextLine();
                    Task newTask = new Task(taskName, dueDate, taskStatus);
                    taskManager.addTask(newTask);
                    System.out.println("Task added successfully!");
                }
                case 2 -> {
                    // Remove Task
                    listTasks(taskManager);
                    System.out.print("Enter task name to remove: ");
                    String taskToRemove = scnr.nextLine();
                    List<Task> allTasks = taskManager.getAllTasks();
                    for (Task task : allTasks) {
                        if (task.getName().equalsIgnoreCase(taskToRemove)) {
                            taskManager.removeTask(task);
                            System.out.println("Task removed successfully!");
                            break;
                        }
                    }
                }
                case 3 -> {
//                        mark task complete
                    listTasks(taskManager);
                    System.out.println("Enter task name to complete: ");
                    String taskToComplete = scnr.nextLine();
                    taskManager.markTaskAsComplete(taskToComplete);
                }
                case 4 -> {
//                    mark task in progress
                    listTasks(taskManager);
                    System.out.println("Enter task to update:");
                    String taskToUpdate = scnr.nextLine();
                    taskManager.markTaskAsInProgress(taskToUpdate);
                }
                case 5 -> {
//                    edit task
                    listTasks(taskManager);
                    System.out.print("Enter task name to edit: ");
                    String taskToEditName = scnr.nextLine();
                    Task taskToEdit = taskManager.findTaskByName(taskToEditName);
                    if (taskToEdit != null) {
                        System.out.println("Editing Task: " + taskToEdit.getName());
                        System.out.print("Enter new task name: ");
                        String newTaskName = scnr.nextLine();
                        System.out.print("Enter new due date (yyyy-MM-dd): ");
                        String newDueDateString = scnr.nextLine();
                        // Parse the date (you may want to add error handling here)
                        Date newDueDate = java.sql.Date.valueOf(newDueDateString);
                        System.out.print("Enter new task status: ");
                        String newTaskStatus = scnr.nextLine();

                        // Update task details
                        taskToEdit.setName(newTaskName);
                        taskToEdit.setDueDate(newDueDate);
                        taskToEdit.setStatus(newTaskStatus);

                        System.out.println("Task edited successfully!");
                    } else {
                        System.out.println("Task not found.");
                    }
                }
                case 6 -> {
                    // List Tasks
                    listTasks(taskManager);
                }
                case 7 -> {
//                    save tasks to file
                    System.out.println("Input file name you'd like to save to: ");
                    String fileName = scnr.nextLine();
                    taskManager.saveTasksToFile(fileName);
                }
                case 8 -> {

//                            load tasks from file
                    System.out.println("Input file name to load from: ");
                    String fileName = scnr.nextLine();
                    taskManager.loadTasksFromFile(fileName);
                }
                case 9 -> {
                    // Exit the application
                    System.out.println("Exiting Task Manager. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void listTasks(TaskManager taskManager) {
        List<Task> tasks = taskManager.getAllTasks();
        System.out.println("Task List:");
        for (Task task : tasks) {
            System.out.println("Name: " + task.getName() + ", Due Date: " + task.getDueDate() +
                    ", Status: " + task.getStatus());
        }
    }
}
