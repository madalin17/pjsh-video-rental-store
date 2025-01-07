package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public Optional<Video> getVideoById(@PathVariable Long id) {
        return videoService.getVideoById(id);
    }

    @GetMapping("/search")
    public List<Video> searchVideosByTitle(@RequestParam String title) {
        return videoService.searchVideosByTitle(title);
    }

    @GetMapping("/genre/{genre}")
    public List<Video> getVideosByGenre(@PathVariable String genre) {
        return videoService.getVideosByGenre(genre);
    }

    @PostMapping
    public Video addVideo(@RequestBody Video video) {
        return videoService.addVideo(video);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}

