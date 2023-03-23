package com.example.reflix.service;

import com.example.reflix.web.domain.review;
import com.example.reflix.web.domain.reviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.reflix.web.dto.reviewResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class reviewService {

    private final reviewRepository reviewRepository;

    @Transactional
    public List<reviewResponseDto> reviewrecomend(String contentName){

        List<review> reviewlist = reviewRepository.findByContentName(contentName);

        List<reviewResponseDto> returnReviewList = new ArrayList<>();
        for(review rid : reviewlist){
            reviewResponseDto dto = reviewResponseDto.builder()
                    .view(rid.getView())
                    .reviewImageurl(rid.getReviewImageurl())
                    .videoId(rid.getReviewvideoUrl())
                    .contnetId(rid.getContentId())
                    .reviewName(rid.getContentName())
                    .build();
            returnReviewList.add(dto);
        }
        return returnReviewList;
    }

    @Transactional
    public void reviewSave(List<reviewResponseDto> dtolist){
        List<review> reviewList = new ArrayList<>();
        for(reviewResponseDto dto : dtolist){
            review saveReivew =  review.builder()
                    .contentId(dto.getContnetId())
                    .contentName(null)
                    .reviewImageurl(dto.getReviewImageurl())
                    .reviewvideoUrl(dto.getVideoId())
                    .view(dto.getView())
                    .build();
            reviewList.add(saveReivew);
        }
        reviewRepository.saveAll(reviewList);
    }
}
