package com.pjsh.vrs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends Interaction {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
                .append(super.getVideo().toString())
                .append("\n\tcustomer = ")
                .append(super.getCustomer().toString())
                .append("\n\tdescription = ")
                .append(description);

        return builder.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
