package com.javalab.board.service;

import com.javalab.board.dto.ResumeDto;
import com.javalab.board.dto.ResumeSkillDto;
import com.javalab.board.repository.ResumeMapper;
import com.javalab.board.repository.ResumeSkillMapper;
import com.javalab.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeMapper resumeMapper;

    private final ResumeSkillMapper resumeSkillMapper;


    @Transactional
    public void resumeCreate(ResumeDto resumeDto) {
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
