package com.example.reflix.web.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecomendContents extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String contentsName;

    @Column(nullable = false)
    private Category contentsCategory;

    @Column(nullable = false)
    @ColumnDefault("0")
    private long similarity;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(name = "contentsId")
    private Long contentsId;

    @Column(name = "userId")
    private Long userId;
}
