package service;

import model.Feedback;
import model.Feedback.FeedbackStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackService {
    // Singleton Pattern
    private static FeedbackService instance;
    private List<Feedback> feedbacks;
    private int nextFeedbackId;

    private FeedbackService() {
        this.feedbacks = new ArrayList<>();
        this.nextFeedbackId = 1;

        // Örnek feedback'ler
        addSampleFeedbacks();
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            instance = new FeedbackService();
        }
        return instance;
    }

    private void addSampleFeedbacks() {
        // Örnek feedback'ler ekle
        Feedback sample1 = new Feedback("FB0001", "john", "Yeni Rota Talebi",
                "Ankara-Erzurum arası uçak seferi olsa harika olur. Bu rota çok talep görmektedir.");

        Feedback sample2 = new Feedback("FB0002", "john", "Otobüs Kalitesi",
                "İzmir-İstanbul otobüs yolculuğu çok rahat geçti. Teşekkürler!");
        sample2.setStatus(FeedbackStatus.REVIEWED);
        sample2.setAdminResponse("Olumlu geri bildiriminiz için teşekkür ederiz!");

        feedbacks.add(sample1);
        feedbacks.add(sample2);
        nextFeedbackId = 3;
    }

    public String submitFeedback(String username, String title, String message) {
        String feedbackId = "FB" + String.format("%04d", nextFeedbackId++);
        Feedback feedback = new Feedback(feedbackId, username, title, message);
        feedbacks.add(feedback);
        return feedbackId;
    }

    public List<Feedback> getUserFeedbacks(String username) {
        return feedbacks.stream()
                .filter(feedback -> feedback.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Feedback> getAllFeedbacks() {
        return new ArrayList<>(feedbacks);
    }

    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        return feedbacks.stream()
                .filter(feedback -> feedback.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean updateFeedbackStatus(String feedbackId, FeedbackStatus status, String adminResponse) {
        Feedback feedback = findFeedbackById(feedbackId);
        if (feedback != null) {
            feedback.setStatus(status);
            if (adminResponse != null && !adminResponse.trim().isEmpty()) {
                feedback.setAdminResponse(adminResponse);
            }
            return true;
        }
        return false;
    }

    public Feedback findFeedbackById(String feedbackId) {
        return feedbacks.stream()
                .filter(feedback -> feedback.getFeedbackId().equals(feedbackId))
                .findFirst()
                .orElse(null);
    }

    public int getPendingFeedbackCount() {
        return (int) feedbacks.stream()
                .filter(feedback -> feedback.getStatus() == FeedbackStatus.PENDING)
                .count();
    }
}