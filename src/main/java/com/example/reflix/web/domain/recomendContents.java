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
public class recomendContents extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String contentsName;

    @Column(nullable = false)
    private String contentsCategory;

    @Column(nullable = false)
    @ColumnDefault("0")
    private long similarity;

    @OneToOne
    @JoinColumn(name = "contentsId")
    private contents contents;

    @ManyToOne
    @JoinColumn(name = "userId")
    private user user;
}
