package com.whl.hotelService.domain.entity;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.*;

import java.util.Date;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
public class BoardEntity extends TimeEntity{
    @Id
    @Unsigned
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 10, nullable = false)
    private String writer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public BoardEntity(Long id, String title, String content, String writer) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}
