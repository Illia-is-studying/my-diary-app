package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITagRepository extends JpaRepository<TagModel, Long> {
    List<TagModel> findAllByDiariesId(Long id);

    TagModel findByContent(String content);

    TagModel findByContentAndDiariesId(String content, Long diaryId);

    boolean existsById(Long id);
}
