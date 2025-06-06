// Feedback.java - Model sınıfı
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private String feedbackId;
    private String username;
    private String title;
    private String message;
    private String feedbackDate;
    private FeedbackStatus status;
    private String adminResponse;

    public enum FeedbackStatus {
        PENDING, REVIEWED, RESOLVED
    }

    public Feedback(String feedbackId, String username, String title, String message) {
        this.feedbackId = feedbackId;
        this.username = username;
        this.title = title;
        this.message = message;
        this.feedbackDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = FeedbackStatus.PENDING;
        this.adminResponse = "";
    }

    // Getters
    public String getFeedbackId() {
        return feedbackId;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    // Setters
    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId='" + feedbackId + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", feedbackDate='" + feedbackDate + '\'' +
                '}';
    }
}