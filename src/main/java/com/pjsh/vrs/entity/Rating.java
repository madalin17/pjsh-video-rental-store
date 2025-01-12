package com.pjsh.vrs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating extends Interaction {

    @Column(nullable = false)
    private Integer score;

    public Rating() {
    }

    public Rating(Video video, Customer customer, Integer score) {
        super(video, customer);
        this.score = score;
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
                .append("\n\tscore = ")
                .append(score);

        return builder.toString();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
