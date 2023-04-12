package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.Review;
import com.example.reflix.web.domain.ReviewLookList;
import com.example.reflix.web.domain.User;
import com.example.reflix.web.domain.repository.ReviewLookListRepository;
import com.example.reflix.web.domain.repository.ReviewRepository;
import com.example.reflix.web.dto.ReviewLookRequestDto;
import com.example.reflix.web.dto.ReviewLookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.reflix.web.dto.ReviewResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final ReviewRepository reviewRepository;
    private final ReviewLookListRepository reviewLookListRepository;

    @Transactional
    public List<ReviewResponseDto> reviewrecomend(Long contentId, int category){
        List<Review> reviewlist = new ArrayList<>();
        switch (category){
            case 0: reviewlist = reviewRepository.findAllByMovieId(contentId);break;
            case 1: reviewlist = reviewRepository.findAllByTvserisId(contentId); break;
            case 2: reviewlist = reviewRepository.findAllByAnimationId(contentId); break;
        }
//        List<review> reviewlist = reviewRepository.findAllByContentId(contentId);

        List<ReviewResponseDto> returnReviewList = new ArrayList<>();
        for(Review rid : reviewlist){
            if(returnReviewList.size()>4){
                break;
            }
            ReviewResponseDto dto = ReviewResponseDto.builder()
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
    public void reviewSave(List<ReviewResponseDto> dtolist, int category){
        List<Review> reviewList = new ArrayList<>();
        for(ReviewResponseDto dto : dtolist){
            Review saveReivew =  Review.builder()
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
    public Long reviewLookAdd(ReviewLookRequestDto dto, userAdapter userId){
        Optional<Review> review = reviewRepository.findById(dto.getReveiwId());
        ReviewLookList list = ReviewLookList.builder()
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

    @Transactional
    public List<ReviewLookResponseDto> reviewLookDetail(User userId){
        List<ReviewLookList> ReviewLookLists = reviewLookListRepository.findAllByUser(userId);

        List<ReviewLookResponseDto> ResponseList = new ArrayList<>();
        for(ReviewLookList rid: ReviewLookLists){
            ReviewLookResponseDto dto = ReviewLookResponseDto.builder()
                    .reviewId(rid.getReviewId())
                    .reviewName(rid.getReviewname())
                    .reviewImageUrl(rid.getReviewImageUrl())
                    .reviewVideoUrl(rid.getReviewVideoUrl())
                    .contentsId(rid.getContentsId())
                    .category(rid.getCategory())
                    .build();
            ResponseList.add(dto);
        }
        return ResponseList;
    }
}
