package com.example.timetracker.service;

import com.example.timetracker.domain.Stream;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.repository.StreamRepository;
import com.example.timetracker.repository.TimeEntryRepository;
import com.example.timetracker.web.dto.TimeEntryForm;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final StreamRepository streamRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository, StreamRepository streamRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.streamRepository = streamRepository;
    }

    @Transactional(readOnly = true)
    public List<TimeEntry> listRecent() {
        return timeEntryRepository.findAllWithStreamOrderByEntryDateDescCreatedAtDesc();
    }

    @Transactional
    public TimeEntry create(TimeEntryForm form) {
        TimeEntry entry = new TimeEntry();
        applyForm(entry, form);
        return timeEntryRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public TimeEntry get(UUID id) {
        return timeEntryRepository.findByIdWithStream(id)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found"));
    }

    @Transactional
    public TimeEntry update(UUID id, TimeEntryForm form) {
        TimeEntry existing = get(id);
        applyForm(existing, form);
        return timeEntryRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        timeEntryRepository.deleteById(id);
    }

    private void applyForm(TimeEntry entry, TimeEntryForm form) {
        Stream stream = streamRepository.findById(form.getStreamId())
                .orElseThrow(() -> new IllegalArgumentException("Stream not found"));
        entry.setStream(stream);
        entry.setTaskName(form.getTaskName().trim());
        entry.setEntryDate(form.getEntryDate());
        entry.setDurationMinutes(form.getDurationMinutes());
        entry.setStartTime(form.getStartTime());
        entry.setEndTime(form.getEndTime());
        entry.setNotes(form.getNotes());
    }
}

