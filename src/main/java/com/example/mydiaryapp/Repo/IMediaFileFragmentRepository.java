package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.MediaFileFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMediaFileFragmentRepository extends JpaRepository<MediaFileFragment, Long> {
}