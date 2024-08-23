package com.javalab.board.service;


import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.repository.ApplicationMapper;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.vo.JobPostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {


    final private ApplicationMapper applicationMapper;

    final private List<SseEmitter> emitters = new ArrayList<>();

    final CompanyMapper companyMapper;

    private final JobPostService jobPostService;



    public void applyForJob(int resumeId, Long jobPostId, String jobSeekerId) {
        ApplicationDto application = new ApplicationDto();
        application.setResumeId(resumeId);
        application.setJobPostId(jobPostId);
        application.setJobSeekerId(jobSeekerId);

        applicationMapper.insertApplication(application);

        newApplicationReceived();

//        notifyCompanyNewApplication(jobPostId);

    }


    public List<ApplicationDto> getApplicationsByJobSeekerId(String jobSeekerId) {
        return applicationMapper.selectApplicationsByJobSeekerId(jobSeekerId);
    }

    public void deleteApplicationById(Long applicationId) {
        applicationMapper.deleteApplicationById(applicationId);
    }

    public List<ApplicationDto> getApplicationsByCompanyId(String compId) {
        return applicationMapper.selectApplicationsByCompanyId(compId);
    }

    // 예시 메소드: 지원서와 연결된 JobPost 정보를 가져오는 메소드
    public JobPostVo getJobPostByApplicationId(Long applicationId) {
        // 지원서 정보를 가져옴
        ApplicationDto application = applicationMapper.getApplicationById(applicationId);
        if (application != null) {
            // JobPost 정보를 가져옴
            Long jobPostId = application.getJobPostId();
            return jobPostService.getJobPostById(jobPostId);
        }
        return null; // 지원서가 존재하지 않거나 공고가 없는 경우
    }





    //    지원 알림 기능
    public void registerEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }


//    public void sendNotification(String notification) {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event().name("newResume").data(notification));
//            } catch (IOException e) {
//                deadEmitters.add(emitter);
//            }
//        });
//        emitters.removeAll(deadEmitters);
//    }

    public void sendNotification(String notification) {
        // 컬렉션을 안전하게 수정하기 위해 Iterator를 사용합니다.
        Iterator<SseEmitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(SseEmitter.event().name("newResume").data(notification));
            } catch (IOException e) {
                iterator.remove(); // 오류 발생 시 안전하게 제거합니다.
            }
        }
    }


    public void newApplicationReceived() {
        // 새로운 이력서가 접수되었을 때 호출됩니다.
        sendNotification("지원 완료");
    }



//    private void notifyCompanyNewApplication(Long jobPostId) {
//        // jobPostId를 사용하여 해당 공고의 compId를 가져와 알림 상태를 저장합니다.
//        String compId = applicationMapper.getCompanyIdByJobPostId(jobPostId);
//        applicationMapper.markNewApplicationAsUnread(compId);
//    }

    // 총 지원서 수를 반환하는 메서드 추가
    public int getTotalApplications() {
        return applicationMapper.countApplications();
    }


}
