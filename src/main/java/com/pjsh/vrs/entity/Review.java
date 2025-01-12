package com.pjsh.vrs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends Interaction {

    @Column(nullable = false, length = 1000)
    private String description;

    public Review() {
    }

    public Review(Video video, Customer customer, String description) {
        super(video, customer);
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("\n")
                .append("\n\tvideo = ")
                .append(super.getVideo())
                .append("\n\tcustomer = ")
                .append(super.getCustomer())
                .append("\n\tdescription = ")
                .append(description);

        return builder.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
