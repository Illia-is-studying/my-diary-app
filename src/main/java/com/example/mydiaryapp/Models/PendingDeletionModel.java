package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "pending_deletion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingDeletionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "my_app_user_id", nullable = false)
    private MyAppUser myAppUser;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private DiaryModel diary;

    @Column(nullable = false)
    private LocalDateTime deletionDate;
}
