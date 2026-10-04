package com.example.taskapi.dto;

public record TaskStats(
    long totalTasks, long completedTasks, long pendingTasks, long overdueTasks) {}
