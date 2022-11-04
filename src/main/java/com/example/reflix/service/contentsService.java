package com.example.reflix.service;

import com.example.reflix.web.domain.contents;
import com.example.reflix.web.domain.contentsRepository;
import com.example.reflix.web.domain.user1;
import com.example.reflix.web.domain.user1Repository;
import com.example.reflix.web.dto.contentFavoriteRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class contentsService {

    private final user1Repository user1Repository;
    private final contentsRepository contentsRepository;


    public JSONObject submit(contentFavoriteRequestDto contentFavoriteDto){
        String command = "python hello.py";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; // 인

        JSONObject object = null;
        ProcessBuilder builder = new ProcessBuilder(command, arg1);

        String a= contentFavoriteDto.toString();
        log.info(a);
        Long Id=1L;
        Optional<user1> Ouser = user1Repository.findById(Id);
        if(!Ouser.isPresent()){
            return object;
        }
        user1 user = Ouser.get();
        user.setLikelist(contentFavoriteDto.toString());
        user1Repository.save(user);

        try{
            Process process = builder.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            JSONParser parser = new JSONParser();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr")); // 서브 프로세스가 출력하는 내용을 받기 위해
            String line;
            StringBuilder sb= new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            object = (JSONObject) parser.parse(sb.toString());

            return object;
        }
        catch (Exception e){
            e.printStackTrace();
            return object;
        }
        //dto에서 분리해서getter를 통해 분리해서 서버에 보낸다
        //여기서 취향을 통해서 파이썬을 불러와서 리턴받는다 필터링된 영화목록들 그리고 영화목록들의 리뷰영상크롤링해서 리뷰영상 정보까지 리턴받는다.
        //processbuilder를 통해 파이썬파일 실행
        // 실행되고 결과값을 bufferReader를 통해 String으로 받아옴
        //받아온 데이터  contentFavoriteReponseDto형식으로 바뀔수잇게 builder한다.
        //그리고 http상태코드와 responsedto를 리턴시킨다.
    }

    @Transactional
    public void reviewStartSubmit(Long number){
        //user1Repository.savenumber(number)
        //db다시 제대로 만들어서 시청한영상에 저장
        //
    }

    public String movieCrowling() {
        String command = "python hello.py";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/hello.py"; //
        Process ps=null;

        ProcessBuilder processBuilder = new ProcessBuilder(command,arg1);

        try{
            ps = processBuilder.start();
//            JSONParser parser = new JSONParser();
            String line=null;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            while((line = br.readLine())==null){
                sb.append(line);
            }
//            JSONObject obj = (JSONObject) parser.parse(sb.toString());
//            JSONArray array;
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
