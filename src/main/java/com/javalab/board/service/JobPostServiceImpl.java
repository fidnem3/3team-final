package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.repository.JobPostMapper;
import com.javalab.board.vo.JobPostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {

    private final JobPostMapper jobPostMapper;

    @Override
    public void createJobPost(CreateJobPostRequestDto createJobPostRequestDto) {
        JobPostVo jobPost = new JobPostVo();
        jobPost.setTitle(createJobPostRequestDto.getTitle());
        jobPost.setContent(createJobPostRequestDto.getContent());
        jobPost.setPosition(createJobPostRequestDto.getPosition());
        jobPost.setSalary(createJobPostRequestDto.getSalary());
        jobPost.setExperience(createJobPostRequestDto.getExperience());
        jobPost.setEducation(createJobPostRequestDto.getEducation());
        jobPost.setHomepage(createJobPostRequestDto.getHomepage());
        jobPost.setLogoPath(createJobPostRequestDto.getLogoPath());
        jobPost.setEndDate(createJobPostRequestDto.getEndDate());

        jobPostMapper.save(jobPost);
    }

    @Override
    public void updateJobPostStatus(Long jobPostId, String status) {
        jobPostMapper.updateStatus(jobPostId, status);
    }

    @Override
    public CreateJobPostRequestDto getJobPostById(Long jobPostId) {
        JobPostVo jobPostVo = jobPostMapper.findById(jobPostId);
        return convertToDto(jobPostVo);
    }

    @Override
    public List<CreateJobPostRequestDto> getAllJobPosts() {
        List<JobPostVo> jobPostVos = jobPostMapper.findAll();
        return jobPostVos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CreateJobPostRequestDto convertToDto(JobPostVo jobPostVo) {
        CreateJobPostRequestDto dto = new CreateJobPostRequestDto();
        dto.setTitle(jobPostVo.getTitle());
        dto.setContent(jobPostVo.getContent());
        dto.setPosition(jobPostVo.getPosition());
        dto.setSalary(jobPostVo.getSalary());
        dto.setExperience(jobPostVo.getExperience());
        dto.setEducation(jobPostVo.getEducation());
        dto.setHomepage(jobPostVo.getHomepage());
        dto.setLogoPath(jobPostVo.getLogoPath());
        dto.setEndDate(jobPostVo.getEndDate());
        return dto;
    }
}
