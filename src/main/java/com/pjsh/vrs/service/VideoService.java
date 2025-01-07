package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }

    public List<Video> searchVideosByTitle(String title) {
        return videoRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Video> getVideosByGenre(String genre) {
        return videoRepository.findByGenre(genre);
    }

    public Video addVideo(Video video) {
        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
}

