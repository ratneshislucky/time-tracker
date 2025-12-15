package com.example.timetracker.repository;

import com.example.timetracker.domain.TimeEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, UUID> {

    @Query("select e from TimeEntry e join fetch e.stream s order by e.entryDate desc, e.createdAt desc")
    List<TimeEntry> findAllWithStreamOrderByEntryDateDescCreatedAtDesc();

    @Query("select e from TimeEntry e join fetch e.stream s where e.id = :id")
    java.util.Optional<TimeEntry> findByIdWithStream(@Param("id") UUID id);

    @Query("""
            select s.name as streamName, sum(e.durationMinutes) as totalMinutes
            from TimeEntry e
            join e.stream s
            where e.entryDate between :start and :end
              and (:streamId is null or s.id = :streamId)
            group by s.name
            order by sum(e.durationMinutes) desc
            """)
    List<StreamAggregate> aggregateByStream(@Param("start") LocalDate start,
                                            @Param("end") LocalDate end,
                                            @Param("streamId") UUID streamId);

    @Query("""
            select s.name as streamName, e.taskName as taskName, sum(e.durationMinutes) as totalMinutes
            from TimeEntry e
            join e.stream s
            where e.entryDate between :start and :end
              and (:streamId is null or s.id = :streamId)
            group by s.name, e.taskName
            order by s.name asc, sum(e.durationMinutes) desc
            """)
    List<StreamTaskAggregate> aggregateByStreamAndTask(@Param("start") LocalDate start,
                                                       @Param("end") LocalDate end,
                                                       @Param("streamId") UUID streamId);

    interface StreamAggregate {
        String getStreamName();

        Long getTotalMinutes();
    }

    interface StreamTaskAggregate {
        String getStreamName();

        String getTaskName();

        Long getTotalMinutes();
    }
}

