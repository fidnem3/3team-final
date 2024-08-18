package com.javalab.board.service;


import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.repository.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {


    final private ApplicationMapper applicationMapper;

    public void applyForJob(int resumeId, Long jobPostId, String jobSeekerId) {
        ApplicationDto application = new ApplicationDto();
        application.setResumeId(resumeId);
        application.setJobPostId(jobPostId);
        application.setJobSeekerId(jobSeekerId);
        application.setStatus("Submitted");

        applicationMapper.insertApplication(application);
    }
}
