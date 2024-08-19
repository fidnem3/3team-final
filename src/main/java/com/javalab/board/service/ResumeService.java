package com.javalab.board.service;

import com.javalab.board.dto.ResumeDto;
import com.javalab.board.dto.ResumeSkillDto;
import com.javalab.board.repository.ResumeMapper;
import com.javalab.board.repository.ResumeSkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeMapper resumeMapper;

    private final ResumeSkillMapper resumeSkillMapper;


    @Transactional
    public void resumeCreate(ResumeDto resumeDto , MultipartFile file ) throws IOException {

        //이력서 파일 첨부

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath ,fileName);
        file.transferTo((saveFile));
        resumeDto.setFileName(fileName);
        resumeDto.setFilePath("/files/" + fileName);


        // 1. 이력서를 저장합니다.
        resumeMapper.createResume(resumeDto);

        // 2. 기술 리스트를 문자열로 변환하여 저장합니다.
        String skillsAsString = resumeDto.getSkillsAsString();
        if (!skillsAsString.isEmpty()) {
            ResumeSkillDto resumeSkillDto = new ResumeSkillDto();
            resumeSkillDto.setResumeId(resumeDto.getResumeId());
            resumeSkillDto.setSkill(skillsAsString); // 변환된 문자열을 저장
            resumeSkillMapper.resumeSkillCreate(resumeSkillDto);
        }


    }
    public List<ResumeDto> findAll(String jobSeekerId)  {

        return resumeMapper.findAll(jobSeekerId);
    }



//    public List<ResumeDto> findAll() {
//        return resumeMapper.findAll();
//
//    }

//    public void updateHits(int resumeId) {
//        resumeMapper.updateHits(resumeId);
//    }

    public ResumeDto findById(int resumeId) {
        return resumeMapper.findById(resumeId);
    }


    public void updateResume(ResumeDto resumeDto, MultipartFile file) throws IOException {

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath ,fileName);
        file.transferTo((saveFile));
        resumeDto.setFileName(fileName);
        resumeDto.setFilePath("/files/" + fileName);

        resumeMapper.updateResume(resumeDto);
    }

    public void deleteResume(int resumeId) {
        resumeMapper.deleteResume(resumeId);
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
