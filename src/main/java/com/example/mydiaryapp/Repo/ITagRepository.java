package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITagRepository extends JpaRepository<TagModel, Integer> {
}
