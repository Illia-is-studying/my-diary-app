package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Repo.IDiaryRepository;
import com.example.mydiaryapp.Repo.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final ITagRepository tagRepository;
    private final IDiaryRepository diaryRepository;

    @Autowired
    public TagService(ITagRepository tagRepository, IDiaryRepository diaryRepository) {
        this.tagRepository = tagRepository;
        this.diaryRepository = diaryRepository;
    }

    public List<TagModel> getAllTagsByDiaryId(Long diaryId) {
        return this.tagRepository.findAllByDiariesId(diaryId);
    }

    public List<TagModel> findContentStartingWith(String startingWithContent) {
        return this.tagRepository.findByContentStartingWithIgnoreCase(startingWithContent);
    }

    public TagModel findByContent(String content) {
        return this.tagRepository.findByContent(content);
    }

    public TagModel save(TagModel tagModel, Long diaryId) {
        TagModel tagModelSaved = this.tagRepository.save(tagModel);

        Optional<DiaryModel> diary = diaryRepository.findById(diaryId);

        if(diary.isPresent()) {
            DiaryModel diaryModel = diary.get();
            diaryModel.getTags().add(tagModel);
            diaryRepository.save(diaryModel);
        }

        return tagModelSaved;
    }

    public void deleteFromDiaries(Long diaryId, Long tagId) {
        Optional<DiaryModel> diary = diaryRepository.findById(diaryId);
        Optional<TagModel> tagModel = tagRepository.findById(tagId);

        if(diary.isPresent() && tagModel.isPresent()) {
            DiaryModel diaryModel = diary.get();
            diaryModel.getTags().remove(tagModel.get());
            diaryRepository.save(diaryModel);
        }
    }

    public Optional<TagModel> findById(Long id) {
        return this.tagRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return this.tagRepository.existsById(id);
    }
}
