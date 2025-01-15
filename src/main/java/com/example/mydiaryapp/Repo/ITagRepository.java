package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITagRepository extends JpaRepository<TagModel, Long> {
    List<TagModel> findByContentStartingWithIgnoreCase(String contentStartingWith);

    List<TagModel> findAllByDiariesId(Long id);

    TagModel findByContent(String content);

    boolean existsById(Long id);

    //void deleteAllFromDiaries(Long diaryId, List<TagModel> tags);
}
