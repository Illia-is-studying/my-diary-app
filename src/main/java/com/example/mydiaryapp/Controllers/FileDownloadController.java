package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.ImageFragment;
import com.example.mydiaryapp.Models.MediaFileFragment;
import com.example.mydiaryapp.Services.FragmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class FileDownloadController {
    private final FragmentService fragmentService;

    @Autowired
    public FileDownloadController(FragmentService fragmentService) {
        this.fragmentService = fragmentService;
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fragmentId") Long id) {
        Optional<FragmentModel> optionalFragment = fragmentService.findById(id);
        if(optionalFragment.isPresent()) {
            FragmentModel fragmentModel = optionalFragment.get();

            if (fragmentModel.getMediaFileFragment() != null) {
                MediaFileFragment mediaFileFragment = fragmentModel.getMediaFileFragment();

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mediaFileFragment.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + mediaFileFragment.getFileName() + "\"")
                        .body(mediaFileFragment.getMediaData());
            } else if (fragmentModel.getImageFragment() != null) {
                ImageFragment imageFragment = fragmentModel.getImageFragment();

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(imageFragment.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + imageFragment.getFileName() + "\"")
                        .body(imageFragment.getImageData());
            }
        }

        return ResponseEntity.notFound().build();
    }
}
