package com.javalab.board.service;

import com.javalab.board.repository.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ApplicationMapper applicationMapper;

    public int countUnreadApplications(String compId) {
        return applicationMapper.countUnreadApplications(compId);
    }

}
