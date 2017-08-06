package net.doughughes.service;

import net.doughughes.bean.Event;
import net.doughughes.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekStrongService {

    private final ProgramRepository programRepository;

    @Autowired
    public WeekStrongService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<Event> getProgramEventHistory(Integer id) {
        List<Event> history = this.programRepository.getProgramHistory(id);

        return history;
    }
}
