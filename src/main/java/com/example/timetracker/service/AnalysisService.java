package com.example.timetracker.service;

import com.example.timetracker.repository.TimeEntryRepository;
import com.example.timetracker.web.dto.AnalysisForm;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {
    private final TimeEntryRepository timeEntryRepository;

    public AnalysisService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Transactional(readOnly = true)
    public AnalysisResult analyze(AnalysisForm form) {
        LocalDate start = form.getStartDate();
        LocalDate end = form.getEndDate();
        UUID streamId = form.getStreamId();

        List<StreamSummary> byStream = timeEntryRepository.aggregateByStream(start, end, streamId)
                .stream()
                .map(row -> new StreamSummary(row.getStreamName(), row.getTotalMinutes()))
                .toList();

        List<StreamTaskSummary> byStreamTask = timeEntryRepository.aggregateByStreamAndTask(start, end, streamId)
                .stream()
                .map(row -> new StreamTaskSummary(row.getStreamName(), row.getTaskName(), row.getTotalMinutes()))
                .toList();

        long total = byStream.stream().mapToLong(StreamSummary::totalMinutes).sum();
        return new AnalysisResult(total, byStream, byStreamTask);
    }

    public record AnalysisResult(long totalMinutes, List<StreamSummary> byStream, List<StreamTaskSummary> byStreamTask) {
        public String totalFormatted() {
            return TimeFormatting.formatMinutes(totalMinutes);
        }
    }

    public record StreamSummary(String streamName, long totalMinutes) {
        public String formatted() {
            return TimeFormatting.formatMinutes(totalMinutes);
        }
    }

    public record StreamTaskSummary(String streamName, String taskName, long totalMinutes) {
        public String formatted() {
            return TimeFormatting.formatMinutes(totalMinutes);
        }
    }
}

