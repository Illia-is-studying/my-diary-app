package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.MediaFileFragment;
import com.example.mydiaryapp.Repo.IMediaFileFragmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaFileFragmentService {
    private final IMediaFileFragmentRepository mediaFileFragmentRepository;

    @Autowired
    public MediaFileFragmentService(IMediaFileFragmentRepository mediaFileFragmentRepository) {
        this.mediaFileFragmentRepository = mediaFileFragmentRepository;
    }

    public MediaFileFragment saveMedia(MediaFileFragment media) {
        return mediaFileFragmentRepository.save(media);
    }

    public Optional<MediaFileFragment> getMediaById(Long id) {
        return mediaFileFragmentRepository.findById(id);
    }

    public List<MediaFileFragment> getAllMediaFiles() {
        return mediaFileFragmentRepository.findAll();
    }

    public void deleteMedia(Long id) {
        mediaFileFragmentRepository.deleteById(id);
    }
}
