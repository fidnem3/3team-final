package com.javalab.board.service;

import com.javalab.board.dto.ResumeDto;
import com.javalab.board.dto.ResumeFileDto;
import com.javalab.board.repository.ResumeMapper;
import com.javalab.board.repository.ResumeSkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeMapper resumeMapper;

    private final ResumeSkillMapper resumeSkillMapper;


    public void resumeCreate(ResumeDto resumeDto) throws IOException {
        if (resumeDto.getResumeFile().isEmpty()) {
            // 파일 없다.
            resumeDto.setFileAttached(0);
            resumeMapper.resumeCreate(resumeDto);
        } else {
            // 파일 있다.
            resumeDto.setFileAttached(1);
            // 게시글 저장 후 id값 활용을 위해 리턴 받음.
            ResumeDto savedResume = resumeMapper.resumeCreate(resumeDto);
            // 파일만 따로 가져오기
            MultipartFile boardFile = resumeDto.getResumeFile();
            // 파일 이름 가져오기
            String originalFilename = boardFile.getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);
            // 저장용 이름 만들기
            System.out.println(System.currentTimeMillis());
            String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
            System.out.println("storedFileName = " + storedFileName);
            // BoardFileDTO 세팅
            ResumeFileDto resumeFileDto = new ResumeFileDto();
            resumeFileDto.setOriginalFileName(originalFilename);
            resumeFileDto.setStoredFileName(storedFileName);
            resumeFileDto.setResumeId(savedResume.getResumeId());
            // 파일 저장용 폴더에 파일 저장 처리
//                String savePath = "/Users/codingrecipe/development/intellij_community/spring_upload_files/" + storedFileName; // mac
            String savePath = "C:/upload/" + storedFileName;
            boardFile.transferTo(new File(savePath));
            // board_file_table 저장 처리
            resumeMapper.saveFile(resumeFileDto);
        }
    }



//    @Transactional
//    public void resumeCreate(ResumeDto resumeDto) {
//        // 1. 이력서를 저장합니다.
//        resumeMapper.resumeCreate(resumeDto);
//
//        // 2. 기술 리스트를 문자열로 변환하여 저장합니다.
//        String skillsAsString = resumeDto.getSkillsAsString();
//        if (!skillsAsString.isEmpty()) {
//            ResumeSkillDto resumeSkillDto = new ResumeSkillDto();
//            resumeSkillDto.setResumeId(resumeDto.getResumeId());
//            resumeSkillDto.setSkill(skillsAsString); // 변환된 문자열을 저장
//            resumeSkillMapper.resumeSkillCreate(resumeSkillDto);
//        }
//
//
//    }

public List<ResumeDto> findAll() {
    return resumeMapper.findAll();

}

//    public void updateHits(int resumeId) {
//        resumeMapper.updateHits(resumeId);
//    }

public ResumeDto findById(int resumeId) {
    return resumeMapper.findById(resumeId);
}


public void updateResume(ResumeDto resumeDto) {
    resumeMapper.updateResume(resumeDto);
}

public void deleteResume(int resumeId) {
    resumeMapper.deleteResume(resumeId);
}

    public ResumeFileDto findFile(int resumeId) {
        return resumeMapper.findFile(resumeId);
    }
}



//스킬들 각각 DB에 저장
//    @Transactional
//    public void save(ResumeDto resumeDto) {
//        // 1. 이력서를 저장합니다.
//        resumeMapper.save(resumeDto);
//
//
//
//        // 2. 기술 리스트를 저장합니다.
//        List<String> skills = resumeDto.getSkills();
//        if (skills != null && !skills.isEmpty()) {
//            for (String skill : skills) {
//                ResumeSkillDto resumeSkillDto = new ResumeSkillDto();
//                resumeSkillDto.setResumeId(resumeDto.getResumeId());
//                resumeSkillDto.setSkill(skill);
//                resumeSkillMapper.save(resumeSkillDto);




//
//                List<String> skills = resumeDto.getSkills();
//        if (skills != null && !skills.isEmpty()) {
//            for (String skill : skills) {
//                ResumeSkillDto resumeSkillDto = new ResumeSkillDto();
//                resumeSkillDto.setResumeId(resumeDto.getResumeId());
//                resumeSkillDto.setSkill(skill);
//                resumeSkillMapper.save(resumeSkillDto);
