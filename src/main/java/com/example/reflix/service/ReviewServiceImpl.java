package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.review;
import com.example.reflix.web.domain.reviewLookList;
import com.example.reflix.web.domain.reviewLookListRepository;
import com.example.reflix.web.domain.reviewRepository;
import com.example.reflix.web.dto.reviewLookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.reflix.web.dto.reviewResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final reviewRepository reviewRepository;
    private final reviewLookListRepository reviewLookListRepository;

    @Transactional
    public List<reviewResponseDto> reviewrecomend(Long contentId,int category){
        List<review> reviewlist = new ArrayList<>();
        switch (category){
            case 0: reviewlist = reviewRepository.findAllByMovieId(contentId);break;
            case 1: reviewlist = reviewRepository.findAllByTvserisId(contentId); break;
            case 2: reviewlist = reviewRepository.findAllByAnimationId(contentId); break;
        }
//        List<review> reviewlist = reviewRepository.findAllByContentId(contentId);

        List<reviewResponseDto> returnReviewList = new ArrayList<>();
        for(review rid : reviewlist){
            if(returnReviewList.size()>4){
                break;
            }
            reviewResponseDto dto = reviewResponseDto.builder()
                    .view(rid.getView())
                    .reviewImageurl(rid.getReviewImageurl())
                    .videoId(rid.getReviewvideoUrl())
                    .reviewName(rid.getContentName())
                    .build();
            switch (category){
                case 0: dto.setContentId(rid.getMovieId()); break;
                case 1: dto.setContentId(rid.getTvserisId());break;
                case 2: dto.setContentId(rid.getAnimationId());break;
            }
            returnReviewList.add(dto);
        }
        return returnReviewList;
    }

    @Transactional
    public void reviewSave(List<reviewResponseDto> dtolist,int category){
        List<review> reviewList = new ArrayList<>();
        for(reviewResponseDto dto : dtolist){
            review saveReivew =  review.builder()
                    .contentName(dto.getReviewName())
                    .reviewImageurl(dto.getReviewImageurl())
                    .reviewvideoUrl(dto.getVideoId())
                    .view(dto.getView())
                    .build();
            switch (category) {
                case 0:
                    saveReivew.setMovieId(dto.getContentId()); break;
                case 1:
                    saveReivew.setTvserisId(dto.getContentId());break;
                case 2:
                    saveReivew.setAnimationId(dto.getContentId());break;
            }
            reviewList.add(saveReivew);
        }
        reviewRepository.saveAll(reviewList);
    }

    @Transactional
    public Long reviewLookAdd(reviewLookRequestDto dto, userAdapter userId){
        Optional<review> review = reviewRepository.findById(dto.getReveiwId());
        reviewLookList list = reviewLookList.builder()
                .contentsId(dto.getContentsId())
                .reviewImageUrl(review.get().getReviewImageurl())
                .reviewname(review.get().getContentName())
                .reviewId(dto.getReveiwId())
                .reviewVideoUrl(review.get().getReviewvideoUrl())
                .user(userId.getUser())
                .category(dto.getCategory())
                .build();
        reviewLookListRepository.save(list);
        return list.getReviewId();
    }
}
