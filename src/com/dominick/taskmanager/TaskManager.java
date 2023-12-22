package com.dominick.taskmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void markTaskAsComplete(String taskName) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                task.markAsComplete();
                System.out.println("Task marked as complete!");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void markTaskAsInProgress(String taskName) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                task.markAsInProgress();
                System.out.println("Task marked as in progress!");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public Task findTaskByName(String taskName) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }
    public void saveTasksToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving tasks to file.");
        }
    }

    public void loadTasksFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            tasks = (List<Task>) ois.readObject();
            System.out.println("Tasks loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading tasks from file.");
        }
    }
}

