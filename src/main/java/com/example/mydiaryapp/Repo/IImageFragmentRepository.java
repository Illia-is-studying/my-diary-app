package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.ImageFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageFragmentRepository extends JpaRepository<ImageFragment, Long> {
}