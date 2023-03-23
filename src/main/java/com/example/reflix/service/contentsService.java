package com.example.reflix.service;

import com.example.reflix.config.auth.userPrinciple;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class contentsService {

    private final contentsRepository contentsRepository;
    private final recomendContentsRepository recomendContentsRepository;
    private final contentLikeListRepository contentLikeListRepository;

    //콘텐츠상세조회
    public contentsDetailResponseDto contentdetail(Long contentId){
        contents contents = contentsRepository.findByContentsId(contentId);
        contentsDetailResponseDto resultdto = contentsDetailResponseDto.builder()
                .contentImageUrl(contents.getContentImageUrl())
                .contentName(contents.getContentName())
                .contentsId(contents.getContentsId())
                .contentsCategory(contents.getContentsCategory())
                .year(contents.getYear())
                .grade(contents.getGrade())
                .janre(null)
                .build();
        return resultdto;
    }


    //파이썬 외부 연동 후 리턴값 String타입으로 리턴
    public String pythonEexc(String command,String arg1) throws IOException {
        String line = null;
        StringBuilder sb= new StringBuilder();
        ProcessBuilder builder = new ProcessBuilder();
//        builder.directory(new File(HomeDe))
        builder.command("python3","/Users/gimjingwon/PycharmProjects/pythonProject1/json_sample.py");
//        bu

//        ProcessBuilder builder = new ProcessBuilder(arg1,command);

        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr")); // 서브 프로세스가 출력하는 내용을 받기 위해'
        if(br.readLine()==null){
            log.info("피이썬에서 리턴값없음");
        }
        while ((line = br.readLine()) != null) {
            sb.append(line);
            log.info(line);
        }
        return sb.toString();
    }

    //추천되는 콘텐츠들을 유저별로 추천콘텐츠목록에 저장
    @Transactional
    public void recomendContentsSave(List<contents> contentsList,Long userId,List<similarContentDto> list){
        List<recomendContents> recomendContentsList = new ArrayList<>();
        int count =0;
        for(contents contents : contentsList){
            recomendContents recomendContents1 = recomendContents.builder()
                    .contentsCategory(contents.getContentsCategory())
                    .contentsName(contents.getContentName())
                    .similarity(list.get(count++).getSimilarity())
                    .contentsId(contents.getContentsId())
                    .userId(userId)
                    .build();
            recomendContentsList.add(recomendContents1);
        }
        recomendContentsRepository.deleteByUserId(userId);
        recomendContentsRepository.saveAll(recomendContentsList);
    }

    //유저에게 취향받아서 분석 후 유사한 콘텐츠 추천
    //유저별 추천된 콘텐츠 목록 저장
    @Transactional
    public List<filterContentsDto> submit(contentFavoriteRequestDto contentFavoriteDto, user userPrincipal) throws IOException {
        String command = "python3 json_sample.py";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/json_sample.py";
        List<contents> contentsList = new ArrayList<>();
        List<filterContentsDto> collect= new ArrayList<>();
        String contentString = pythonEexc(command,arg1);
        log.info("response contnetslist data = "+contentString);
        if(!contentString.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            List<Long> idlist = new ArrayList<>();
            contentString="{\n"+contentString;
            List<similarContentDto> list = Arrays.asList(mapper.readValue(contentString, similarContentDto.class));
            for( similarContentDto dto : list){
                idlist.add(dto.getContentid());
            }
            contentsList = contentsRepository.findAllById(idlist);
            log.info(contentsList.get(0).getContentsId());
            log.info(contentsList.get(0).getContentName());
            int count =0;
            for(contents rid : contentsList){
                filterContentsDto dto = filterContentsDto.builder()
                        .contentsId(rid.getContentsId())
                        .contentName(rid.getContentName())
                        .contentImageUrl(rid.getContentImageUrl())
                        .build();
                collect.add(dto);
            }
            for(int i=0;i<collect.size();i++){
                collect.get(i).setSimir(list.get(i).getSimilarity());
            }
            recomendContentsSave(contentsList, userPrincipal.getId(), list);
            return collect;
        }
        else{
            return collect;
        }
    }


    public boolean contentLike(Long contentId, Long userId){
        try{
            contentLikeList list = contentLikeList.builder()
                    .userId(userId)
                    .contentId(contentId)
                    .build();

            contentLikeListRepository.save(list);
            Optional<contents> contents = contentsRepository.findById(contentId);
            contents savecontent = contents.get();
            savecontent.setLikelist(savecontent.getLikelist()+1);
            contentsRepository.save(savecontent);
            return true;
        }catch (Exception e){
            log.info(e.getMessage());
        }

        return false;
    }
    public String movieCrowling() {
        String command = "python3";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; //
        Process ps=null;
        ProcessBuilder Builder = new ProcessBuilder();
        Builder.command(command,arg1);
        try{
            ps = Builder.start();
            String line=null;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            while((line = br.readLine())==null){
                sb.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            Set<contents> contentsSet = mapper.readValue(sb.toString(), new TypeReference<Set<contents>>() {});
//            contents content = content.builder()
//                    .contentName(obj.get("contentName").toString())
//                    .image(obj.get("Image").toString())
//                    .grade(Float.parseFloat(obj.get("grade").toString()))
//                    .jangre(obj.get("jangre").toString())
//                    .runnigTime(obj.get("runnigTime").toString())
//                    .year(obj.get("year").toString())
//                    .like(obj.get("like").toString())
//                    .keyWord(obj.get("keyWord").toString())
//                    .build();
            contentsRepository.deleteAllInBatch();
            contentsRepository.saveAll(contentsSet);
            return "sucsess";
        }
        catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage().toString());
            return "error : "+ e.getMessage().toString();
        }

    }

    public String reviewCrowling(){
        String command = "python hello.py";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; //
        Process ps=null;

        ProcessBuilder processBuilder = new ProcessBuilder(command,arg1);
        try{
            ps = processBuilder.start();
            String line=null;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            while((line = br.readLine())==null){
                sb.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            Set<contents> contentsSet = mapper.readValue(sb.toString(), new TypeReference<Set<contents>>() {});
            contentsRepository.deleteAllInBatch();
            contentsRepository.saveAll(contentsSet);
            return "sucsess";
        }
        catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage().toString());
            return "error : "+ e.getMessage().toString();
        }

    }
}
