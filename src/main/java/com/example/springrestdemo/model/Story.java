package com.example.springrestdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STORIES")
public class Story {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STORY")
    private String story;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @Column(name = "USER_ID")
    private Long userId;
}
