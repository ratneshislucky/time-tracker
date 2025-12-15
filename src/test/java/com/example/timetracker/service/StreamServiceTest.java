package com.example.timetracker.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.timetracker.domain.Stream;
import com.example.timetracker.repository.StreamRepository;
import com.example.timetracker.web.dto.StreamForm;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StreamServiceTest {

    private StreamRepository streamRepository;
    private StreamService streamService;

    @BeforeEach
    void setup() {
        streamRepository = Mockito.mock(StreamRepository.class);
        streamService = new StreamService(streamRepository);
    }

    @Test
    void preventsDuplicateNames() {
        StreamForm form = new StreamForm();
        form.setName("Delivery");
        when(streamRepository.existsByNameIgnoreCase("Delivery")).thenReturn(true);

        assertThatThrownBy(() -> streamService.create(form))
                .isInstanceOf(StreamService.DuplicateStreamException.class);
    }

    @Test
    void savesNewStream() {
        StreamForm form = new StreamForm();
        form.setName("Ops");
        Stream saved = new Stream();
        saved.setName("Ops");
        saved.setActive(true);
        when(streamRepository.save(any(Stream.class))).thenReturn(saved);

        streamService.create(form);

        verify(streamRepository).save(any(Stream.class));
    }

    @Test
    void deactivatesStream() {
        Stream s = new Stream();
        s.setName("Ops");
        s.setActive(true);
        when(streamRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(s));

        streamService.deactivate(UUID.randomUUID());

        verify(streamRepository).save(s);
    }
}

