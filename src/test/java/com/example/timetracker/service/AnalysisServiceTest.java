package com.example.timetracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.timetracker.repository.TimeEntryRepository;
import com.example.timetracker.repository.TimeEntryRepository.StreamAggregate;
import com.example.timetracker.repository.TimeEntryRepository.StreamTaskAggregate;
import com.example.timetracker.web.dto.AnalysisForm;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AnalysisServiceTest {

    private TimeEntryRepository repository;
    private AnalysisService analysisService;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(TimeEntryRepository.class);
        analysisService = new AnalysisService(repository);
    }

    @Test
    void aggregatesTotals() {
        StreamAggregate agg = new StubStreamAgg("Delivery", 120L);
        StreamTaskAggregate taskAgg = new StubTaskAgg("Delivery", "Build", 90L);
        when(repository.aggregateByStream(any(), any(), any())).thenReturn(List.of(agg));
        when(repository.aggregateByStreamAndTask(any(), any(), any())).thenReturn(List.of(taskAgg));

        AnalysisForm form = new AnalysisForm();
        form.setStartDate(LocalDate.now().minusDays(1));
        form.setEndDate(LocalDate.now());
        var result = analysisService.analyze(form);

        assertThat(result.totalMinutes()).isEqualTo(120L);
        assertThat(result.byStream()).hasSize(1);
        assertThat(result.byStreamTask()).hasSize(1);
    }

    record StubStreamAgg(String name, Long total) implements StreamAggregate {
        @Override
        public String getStreamName() {
            return name;
        }

        @Override
        public Long getTotalMinutes() {
            return total;
        }
    }

    record StubTaskAgg(String stream, String task, Long total) implements StreamTaskAggregate {
        @Override
        public String getStreamName() {
            return stream;
        }

        @Override
        public String getTaskName() {
            return task;
        }

        @Override
        public Long getTotalMinutes() {
            return total;
        }
    }
}

