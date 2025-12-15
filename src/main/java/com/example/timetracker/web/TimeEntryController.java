package com.example.timetracker.web;

import com.example.timetracker.domain.Stream;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.service.StreamService;
import com.example.timetracker.service.TimeEntryService;
import com.example.timetracker.web.dto.TimeEntryForm;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entries")
public class TimeEntryController {
    private final TimeEntryService timeEntryService;
    private final StreamService streamService;

    public TimeEntryController(TimeEntryService timeEntryService, StreamService streamService) {
        this.timeEntryService = timeEntryService;
        this.streamService = streamService;
    }

    @ModelAttribute("activeStreams")
    public List<Stream> activeStreams() {
        return streamService.listActive();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("entries", timeEntryService.listRecent());
        return "entries/list";
    }

    @GetMapping("/new")
    public String newEntry(Model model) {
        TimeEntryForm form = new TimeEntryForm();
        form.setEntryDate(LocalDate.now());
        model.addAttribute("timeEntryForm", form);
        return "entries/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("timeEntryForm") TimeEntryForm form,
                         BindingResult bindingResult,
                         Model model) {
        validateTimes(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "entries/form";
        }
        timeEntryService.create(form);
        return "redirect:/entries";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") UUID id, Model model) {
        TimeEntry entry = timeEntryService.get(id);
        TimeEntryForm form = toForm(entry);
        model.addAttribute("timeEntryForm", form);
        model.addAttribute("entryId", id);
        return "entries/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") UUID id,
                         @Valid @ModelAttribute("timeEntryForm") TimeEntryForm form,
                         BindingResult bindingResult,
                         Model model) {
        validateTimes(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("entryId", id);
            return "entries/form";
        }
        timeEntryService.update(id, form);
        return "redirect:/entries";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") UUID id) {
        timeEntryService.delete(id);
        return "redirect:/entries";
    }

    private TimeEntryForm toForm(TimeEntry entry) {
        TimeEntryForm form = new TimeEntryForm();
        form.setStreamId(entry.getStream().getId());
        form.setTaskName(entry.getTaskName());
        form.setEntryDate(entry.getEntryDate());
        form.setDurationMinutes(entry.getDurationMinutes());
        form.setStartTime(entry.getStartTime());
        form.setEndTime(entry.getEndTime());
        form.setNotes(entry.getNotes());
        return form;
    }

    private void validateTimes(TimeEntryForm form, BindingResult bindingResult) {
        LocalTime start = form.getStartTime();
        LocalTime end = form.getEndTime();
        if (start != null && end != null && !end.isAfter(start)) {
            bindingResult.rejectValue("endTime", "invalidTime", "End time must be after start time");
        }
    }
}

