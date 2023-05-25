package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.Category;
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
    public List<ReviewResponseDto> reviewrecomend(Long contentId, Category category){
        List<Review> reviewlist = new ArrayList<>();
        switch (category.name()){
            case "MOVIE": reviewlist = reviewRepository.findAllByMovieId(contentId);break;
            case "DRAMA": reviewlist = reviewRepository.findAllByTvserisId(contentId); break;
            case "ANIMATION": reviewlist = reviewRepository.findAllByAnimationId(contentId); break;
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
            switch (category.name()){
                case "MOVIE": dto.setContentId(rid.getMovieId()); break;
                case "DRAMA": dto.setContentId(rid.getTvserisId());break;
                case "ANIMATION" : dto.setContentId(rid.getAnimationId());break;
            }
            returnReviewList.add(dto);
        }
        return returnReviewList;
    }

    @Transactional
    public void reviewSave(List<ReviewResponseDto> dtolist, Category category){
        List<Review> reviewList = new ArrayList<>();
        for(ReviewResponseDto dto : dtolist){
            Review saveReivew =  Review.builder()
                    .contentName(dto.getReviewName())
                    .reviewImageurl(dto.getReviewImageurl())
                    .reviewvideoUrl(dto.getVideoId())
                    .view(dto.getView())
                    .build();
            switch (category.name()) {
                case "MOVIE":
                    saveReivew.setMovieId(dto.getContentId()); break;
                case "DRAMA":
                    saveReivew.setTvserisId(dto.getContentId());break;
                case "ANIMATION":
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
