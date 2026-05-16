package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FareReceipt {
    private String rideId;
    private double amount;
    private LocalDateTime generatedAt;

    public FareReceipt(String rideId, double amount, LocalDateTime generatedAt) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = generatedAt;
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "FareReceipt{" +
                "rideId='" + rideId + '\'' +
                ", amount=$" + String.format("%.2f", amount) +
                ", generatedAt=" + generatedAt.format(formatter) +
                '}';
    }
}

