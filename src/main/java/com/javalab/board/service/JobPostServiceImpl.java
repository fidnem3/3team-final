package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.repository.JobPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostMapper jobPostMapper;

    @Override
    public void createJobPost(CreateJobPostRequestDto createJobPostRequestDto, MultipartFile file) {
        String logoPath = uploadFile(file);
        createJobPostRequestDto.setLogoPath(logoPath);
        jobPostMapper.insertJobPost(createJobPostRequestDto);
        // Redirect to payment page logic here
    }

    @Override
    public void updateJobPostStatus(Long jobPostId, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("jobPostId", jobPostId);
        params.put("status", status);
        jobPostMapper.updateJobPostStatus(params);
    }

    @Override
    public CreateJobPostRequestDto getJobPostById(Long jobPostId) {
        return jobPostMapper.selectJobPostById(jobPostId);
    }

    @Override
    public List<CreateJobPostRequestDto> getAllJobPosts() {
        return jobPostMapper.selectAllJobPosts();
    }

    private String uploadFile(MultipartFile file) {
        // Logic to upload file to server and return file path
        try {
            String fileName = file.getOriginalFilename();
            String filePath = "path/to/upload/" + fileName;
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
