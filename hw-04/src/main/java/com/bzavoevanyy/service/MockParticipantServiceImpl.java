package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Participant;
import org.springframework.stereotype.Service;

@Service
public class MockParticipantServiceImpl implements ParticipantService {
    @Override
    public Participant create(String name) {
        return new Participant(name);
    }
}
