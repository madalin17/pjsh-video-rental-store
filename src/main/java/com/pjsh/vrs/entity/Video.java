package com.pjsh.vrs.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String title;

    @Column(nullable = false, length = 30)
    private String director;

    @Column(nullable = false, length = 150)
    private String actors;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 10)
    private String duration;

    @Column(nullable = false, length = 30)
    private String genre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public Video() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("\n")
                .append("\n\ttitle = ")
                .append(title)
                .append("\n\tdirector = ")
                .append(director)
                .append("\n\tactors = ")
                .append(actors)
                .append("\n\tyear = ")
                .append(year)
                .append("\n\tduration = ")
                .append(duration)
                .append("\n\tgenre = ")
                .append(genre)
                .append("\n\tdescription = ")
                .append(description)
                .append("\n\tquantity = ")
                .append(quantity);

        return builder.toString();
    }

    public boolean isAvailable() {
        return this.quantity > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
