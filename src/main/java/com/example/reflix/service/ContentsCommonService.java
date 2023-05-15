package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.domain.repository.ContentsJanreRepository;
import com.example.reflix.web.domain.repository.ContentsKeywordRepository;
import com.example.reflix.web.domain.repository.ContentsLikeListRepository;
import com.example.reflix.web.domain.repository.RecomendContentsRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ContentsCommonService{

    private final RecomendContentsRepository recomendContentsRepository;
    private final ContentsLikeListRepository contentsLikeListRepository;
    private final ContentsJanreRepository contentsJanreRepository;
    private final ContentsKeywordRepository contentsKeywordRepository;
    private final static String URL ="https://image.tmdb.org/t/p/original";


    //콘텐츠상세조회
//    public contentsDetailResponseDto contentdetail(Long contentId){
//        contents contents = contentsRepository.findByContentsId(contentId);
//        Long likelist = contentLikeListRepository.countByContentId(contentId);
//        contentsDetailResponseDto resultdto = contentsDetailResponseDto.builder()
//                .contentImageUrl(contents.getContentImageUrl())
//                .contentName(contents.getContentName())
//                .contentsId(contents.getContentsId())
//                .contentsCategory(contents.getContentsCategory())
//                .year(contents.getYear())
//                .grade(contents.getGrade())
//                .janre(contents.getJanre().getJanre1())
//                .story(contents.getStory())
//                .likelist(likelist)
//                .build();
//        return resultdto;
//    }


    //파이썬 외부 연동 후 리턴값 String타입으로 리턴
    public String pythonEexc(List<String> list) throws IOException {
        String line = null;
        StringBuilder sb= new StringBuilder();
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(list);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr")); // 서브 프로세스가 출력하는 내용을 받기 위해'
        while ((line = br.readLine()) != null) {
            sb.append(line);
            log.info(line);
        }
        return sb.toString();
    }
//
//    //추천되는 콘텐츠들을 유저별로 추천콘텐츠목록에 저장
//    @Transactional
//    public void recomendContentsSave(List<recommendContentsDto> contentsList,Long userId,List<similarContentDto> list){
//        List<recomendContents> recomendContentsList = new ArrayList<>();
//        int count =0;
//        for(contents contents : contentsList){
//            recomendContents recomendContents1 = recomendContents.builder()
//                    .contentsCategory(contents.getContentsCategory())
//                    .contentsName(contents.getContentName())
//                    .similarity(list.get(count++).getSimilarity())
//                    .contentsId(contents.getContentsId())
//                    .userId(userId)
//                    .build();
//            recomendContentsList.add(recomendContents1);
//        }
//        recomendContentsRepository.deleteByUserId(userId);
//        recomendContentsRepository.saveAll(recomendContentsList);
//    }


    //추천되는 콘텐츠들을 유저별로 추천콘텐츠목록에 저장
    @Transactional
    public void recomendContentsSave(List<ContentsRecommendResponseDto> contentsList, Long id){
        List<RecomendContents> recomendContentsList = new ArrayList<>();
        for(ContentsRecommendResponseDto contents : contentsList) {
            RecomendContents recomendContents1 = RecomendContents.builder()
                    .contentsCategory(contents.getContentsCategory())
                    .contentsName(contents.getName())
                    .similarity(contents.getSimir())
                    .contentsId(contents.getContentsId())
                    .userId(id)
                    .build();
            recomendContentsList.add(recomendContents1);
        }
        recomendContentsRepository.deleteByUserId(id);
        recomendContentsRepository.saveAll(recomendContentsList);
    }
    //유저에게 취향받아서 분석 후 유사한 콘텐츠 추천
    //유저별 추천된 콘텐츠 목록 저장
//    @Transactional
//    public List<filterContentsDto> submit(contentFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException {
//        String command = "python3";
//        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/Json_sample.py";
//        List<contents> contentsList = new ArrayList<>();
//        List<filterContentsDto> collect= new ArrayList<>();
//
//        List<String> pyrequestList = new ArrayList<>();
//        pyrequestList.add(command);
//        pyrequestList.add(arg1);
////        pyrequestList.add(contentFavoriteDto.getContentVariation());
////        pyrequestList.add(contentFavoriteDto.getJangre());
////        pyrequestList.add(contentFavoriteDto.getKeword());
////        pyrequestList.add(contentFavoriteDto.getYear());
//
////        String contentString = pythonEexc(pyrequestList);
//        String contentString = pythonEexc(pyrequestList);
//        log.info("response contnetslist data = "+contentString);
//        if(!contentString.isEmpty()){
//            ObjectMapper mapper = new ObjectMapper();
//            List<Long> idlist = new ArrayList<>();
//            contentString="{\n"+contentString;
//            List<similarContentDto> list = Arrays.asList(mapper.readValue(contentString, similarContentDto.class));
//            for( similarContentDto dto : list){
//                idlist.add(dto.getContentid());
//            }
//            contentsList = contentsRepository.findAllById(idlist);
//            log.info(contentsList.get(0).getContentsId());
//            log.info(contentsList.get(0).getContentName());
//            int count =0;
//            for(contents rid : contentsList){
//                filterContentsDto dto = filterContentsDto.builder()
//                        .contentsId(rid.getContentsId())
//                        .contentName(rid.getContentName())
//                        .contentImageUrl(rid.getContentImageUrl())
//                        .build();
//                collect.add(dto);
//            }
//            for(int i=0;i<collect.size();i++){
//                collect.get(i).setSimir(list.get(i).getSimilarity());
//            }
//            recomendContentsSave(contentsList, userPrincipal.getId(), list);
//            return collect;
//        }
//        else{
//            return collect;
//        }
//    }


    public boolean contentLike(Long contentId,int category,userAdapter user){
            Category category1 = null;
            switch (category){
                case 0: category1= Category.MOVIE; break;
                case 1: category1 = Category.DRAMA;break;
                case 2: category1 = Category.ANIMATION; break;
            }
            ContentsLikeList list = ContentsLikeList.builder()
                    .contentId(contentId)
                    .user(user.getUser())
                    .category(category1)
                    .build();
            contentsLikeListRepository.save(list);
            return true;
    }

    public boolean contentDisLike(Long contentId, Long userId){
            Optional<ContentsLikeList> rid = contentsLikeListRepository.findByContentIdAndUser(contentId,userId);
            if(rid.isPresent()) {
                contentsLikeListRepository.delete(rid.get());
                return true;
            }else{
                return false;
            }
    }

    public List<Long> contentRecommend(Long contentsId,int category) {

        List<String> commandList= new ArrayList<>();
        List<Long> idArray = new LinkedList<>();
        String command="python3";
        commandList.add(command);
        String result = null;
        try{
            switch (category){
                case 0:
//                    arg1 =  "/Users/gimjingwon/PycharmProjects/pythonProject1/completion/movie_recommend_genre.py";
                    commandList.add(URL+"movie_recommend_genre.py");
                    break;
                case 1:
                    commandList.add(URL+"tv_content_recommend.py");
                    break;
                case 2:
                    commandList.add(URL+"animation_content_recommend.py");
                    break;
            }
            commandList.add(contentsId.toString());
            result = pythonEexc(commandList);
            ObjectMapper mapper = new ObjectMapper();
            idArray = Arrays.asList(mapper.readValue(result,TmbdIdDto[].class)).stream().map(TmbdIdDto::getTmdbid).toList();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return idArray;
    }
    public void movieadd(MovieAddDto dto){
//
//        contents content = contents.builder()
//                .contentName(dto.getContentName())
//                .year(dto.getYear())
//                .runnigTime(dto.getRunnigTime())
//                .contentImageUrl(dto.getContentImageUrl())
//                .contentsCategory(category.MOVIE)
//                .story(dto.getStory())
//                .grade("grade")
//                .build();
//
//        contentsRepository.save(content);
        return;

    }

    public List<ContentsDetailDto> getmovieten(){
//        List<contents> contnetslist = new ArrayList<>();
//        log.info("뽑기전 "+contnetslist.size());
//
//        contnetslist=contentsRepository.findAll();
//
//        log.info("뽑고나서 "+contnetslist.size());
        List <ContentsDetailDto> returnlist = new ArrayList<>();
//        for(contents rid : contnetslist){
//            if(returnlist.size()==10){
//                break;
//            }
//            contentsDetailDto dto = contentsDetailDto.builder()
//                    .contentImageUrl(rid.getContentImageUrl())
//                    .contentName(rid.getContentName())
//                    .contentsCategory(rid.getContentsCategory())
//                    .contentsId(rid.getContentsId())
//                    .year(rid.getYear())
//                    .build();
//            returnlist.add(dto);
//        }
////        List <contentsDetailDto> returnlist = contnetslist.stream().map(contentsDetailDto::new).collect(Collectors.toList());
//
//
        return returnlist;
    }


    public void savejanreKeyword(ContentsJanre janre,contentsKeword keword){
        contentsJanreRepository.save(janre);
        contentsKeywordRepository.save(keword);
    }
    public List<ContentsDetailDto> seacrhContent(String q) {
        List<ContentsDetailDto> resultDtoList = new LinkedList<>();

        try{
            String searchQ ="https://api.themoviedb.org/3/search/multi?api_key=cb3fbd26fe7fe53cf8af0ba2b6d72370&language=ko-KR&query="+q;
            URL url = new URL(searchQ);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");


            int count =0;
            for(Map<String,Object> rid : results){
                log.info(count++);
                log.info(rid.toString());
                String imageUrl = "";
                if(rid.get("poster_path")!=null){
                    imageUrl = rid.get("poster_path").toString();
                }
                Category category =Category.MOVIE;
                if(rid.get("media_type").toString()=="tv"){
                    category = Category.DRAMA;
                }
                log.info("category");
                String name = null;
                String year = null;
                if(rid.containsKey("name")){
                    name = rid.get("name").toString();
                    year = rid.get("first_air_date").toString();
                    log.info("name");
                }
                else{
                    name = rid.get("title").toString();
                    year = rid.get("release_date").toString();

                    log.info("title");
                }
                ContentsDetailDto dtos = ContentsDetailDto.builder()
                        .contentsId(Long.parseLong(rid.get("id").toString()))
                        .contentName(name)
                        .contentImageUrl(URL+imageUrl)
                        .contentsCategory(category)
                        .year(year)
                        .build();
                log.info(dtos);
                resultDtoList.add(dtos);
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
        }

        //일단 제목과 일치하는
        //이차로 제목과 일치하는 영화와 비슷한 콘텐츠 파이썬 이용
        //두개 합쳐서 나이스하게

        //
//        contentsRepository.findAllBy
        return resultDtoList;

    }


    public String movieCrowling() {
//
//        String command = "python3";
//        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; //
//        Process ps=null;
//        ProcessBuilder Builder = new ProcessBuilder();
//        Builder.command(command,arg1);
//        try{
//            ps = Builder.start();
//            String line=null;
//            StringBuilder sb = new StringBuilder();
//            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
//            while((line = br.readLine())==null){
//                sb.append(line);
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            Set<contents> contentsSet = mapper.readValue(sb.toString(), new TypeReference<Set<contents>>() {});
////            contents content = content.builder()
////                    .contentName(obj.get("contentName").toString())
////                    .image(obj.get("Image").toString())
////                    .grade(Float.parseFloat(obj.get("grade").toString()))
////                    .jangre(obj.get("jangre").toString())
////                    .runnigTime(obj.get("runnigTime").toString())
////                    .year(obj.get("year").toString())
////                    .like(obj.get("like").toString())
////                    .keyWord(obj.get("keyWord").toString())
////                    .build();
//            contentsRepository.deleteAllInBatch();
//            contentsRepository.saveAll(contentsSet);
//            return "sucsess";
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            log.info(e.getMessage().toString());
//            return "error : "+ e.getMessage().toString();
//        }
        return "byte";

    }

    public String reviewCrowling(){
//        String command = "python hello.py";
//        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; //
//        Process ps=null;
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command,arg1);
//        try{
//            ps = processBuilder.start();
//            String line=null;
//            StringBuilder sb = new StringBuilder();
//            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
//            while((line = br.readLine())==null){
//                sb.append(line);
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            Set<contents> contentsSet = mapper.readValue(sb.toString(), new TypeReference<Set<contents>>() {});
//            contentsRepository.deleteAllInBatch();
//            contentsRepository.saveAll(contentsSet);
//            return "sucsess";
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            log.info(e.getMessage().toString());
//            return "error : "+ e.getMessage().toString();
//        }

        return "hi";
    }
}
