package com.example.timetracker.repository;

import com.example.timetracker.domain.Stream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StreamRepository extends JpaRepository<Stream, UUID> {
    boolean existsByNameIgnoreCase(String name);

    @Query("select s from Stream s where s.active = true order by lower(s.name)")
    List<Stream> findActiveStreams();

    @Query("select s from Stream s where s.id = :id and s.active = true")
    Optional<Stream> findActiveById(@Param("id") UUID id);
}

