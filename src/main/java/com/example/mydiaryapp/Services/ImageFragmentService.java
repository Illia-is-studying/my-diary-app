package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.ImageFragment;
import com.example.mydiaryapp.Repo.IImageFragmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageFragmentService {
    private final IImageFragmentRepository imageFragmentRepository;

    @Autowired
    public ImageFragmentService(IImageFragmentRepository imageFragmentRepository) {
        this.imageFragmentRepository = imageFragmentRepository;
    }

    public ImageFragment saveImage(ImageFragment image) {
        return imageFragmentRepository.save(image);
    }

    public Optional<ImageFragment> getImageById(Long id) {
        return imageFragmentRepository.findById(id);
    }

    public List<ImageFragment> getAllImages() {
        return imageFragmentRepository.findAll();
    }

    public void deleteImage(Long id) {
        imageFragmentRepository.deleteById(id);
    }
}
