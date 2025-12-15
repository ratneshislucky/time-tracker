package com.example.timetracker.web;

import com.example.timetracker.service.StreamService;
import com.example.timetracker.web.dto.StreamForm;
import jakarta.validation.Valid;
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
@RequestMapping("/streams")
public class StreamController {
    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("streams", streamService.listAll());
        return "streams/list";
    }

    @GetMapping("/new")
    public String newStream(Model model) {
        model.addAttribute("streamForm", new StreamForm());
        return "streams/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("streamForm") StreamForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "streams/form";
        }
        try {
            streamService.create(form);
            return "redirect:/streams";
        } catch (StreamService.DuplicateStreamException ex) {
            bindingResult.rejectValue("name", "duplicate", ex.getMessage());
            return "streams/form";
        }
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable("id") UUID id) {
        streamService.deactivate(id);
        return "redirect:/streams";
    }
}

