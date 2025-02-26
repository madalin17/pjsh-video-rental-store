package com.pjsh.vrs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "videos")
public class Video {

    @JsonIgnore
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

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public Video() {
    }

    public Video(Long id, String title, String director, String actors, Integer year, String duration, String genre, String description, Integer quantity) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.year = year;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.quantity = quantity;
    }

    public Video(Video video) {
        this.id = video.id;
        this.title = video.title;
        this.director = video.director;
        this.actors = video.actors;
        this.year = video.year;
        this.duration = video.duration;
        this.genre = video.genre;
        this.description = video.description;
        this.quantity = video.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, director, actors, year, duration, genre, description, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(year, video.year) &&
                Objects.equals(quantity, video.quantity) &&
                Objects.equals(id, video.id) &&
                Objects.equals(title, video.title) &&
                Objects.equals(director, video.director) &&
                Objects.equals(actors, video.actors) &&
                Objects.equals(duration, video.duration) &&
                Objects.equals(genre, video.genre) &&
                Objects.equals(description, video.description);
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
