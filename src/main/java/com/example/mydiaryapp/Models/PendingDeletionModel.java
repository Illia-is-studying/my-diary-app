package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "pending_deletion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"my_app_user_id", "diary_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingDeletionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "my_app_user_id", nullable = false)
    private Long myAppUserId;

    @Column(name = "diary_id", nullable = false)
    private Long diaryId;

    @Column(nullable = false)
    private LocalDateTime deletionDate;
}
