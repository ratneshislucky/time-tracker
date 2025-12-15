package com.example.timetracker.service;

import com.example.timetracker.domain.Stream;
import com.example.timetracker.repository.StreamRepository;
import com.example.timetracker.web.dto.StreamForm;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StreamService {
    private final StreamRepository streamRepository;

    public StreamService(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    @Transactional(readOnly = true)
    public List<Stream> listActive() {
        return streamRepository.findActiveStreams();
    }

    @Transactional(readOnly = true)
    public List<Stream> listAll() {
        return streamRepository.findAll();
    }

    @Transactional
    public Stream create(StreamForm form) {
        if (streamRepository.existsByNameIgnoreCase(form.getName())) {
            throw new DuplicateStreamException("Stream name already exists");
        }
        Stream stream = new Stream();
        stream.setName(form.getName().trim());
        stream.setDescription(form.getDescription());
        stream.setActive(true);
        return streamRepository.save(stream);
    }

    @Transactional
    public void deactivate(UUID id) {
        Optional<Stream> stream = streamRepository.findById(id);
        stream.ifPresent(s -> {
            s.setActive(false);
            streamRepository.save(s);
        });
    }

    @Transactional(readOnly = true)
    public Stream get(UUID id) {
        return streamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Stream not found"));
    }

    public static class DuplicateStreamException extends RuntimeException {
        public DuplicateStreamException(String message) {
            super(message);
        }
    }
}

