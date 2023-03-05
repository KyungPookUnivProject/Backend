package com.example.reflix.web.domain;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentsKeword extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kewordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentsId", insertable = false, updatable = false)
    private contents contents;

    @Column(nullable = false)
    private String keword;
    //한컬럼에 여러키워드 박는다. 구분자를 사용 ex) 사랑,판타지,군대,소총,바다,전쟁
    //인공지능파트랑 협의해봐야됨

}
