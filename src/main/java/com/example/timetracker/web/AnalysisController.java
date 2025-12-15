package com.example.timetracker.web;

import com.example.timetracker.service.AnalysisService;
import com.example.timetracker.service.StreamService;
import com.example.timetracker.web.dto.AnalysisForm;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final StreamService streamService;

    public AnalysisController(AnalysisService analysisService, StreamService streamService) {
        this.analysisService = analysisService;
        this.streamService = streamService;
    }

    @GetMapping
    public String view(Model model) {
        AnalysisForm form = new AnalysisForm();
        form.setStartDate(LocalDate.now().minusDays(7));
        form.setEndDate(LocalDate.now());
        model.addAttribute("analysisForm", form);
        model.addAttribute("streams", streamService.listActive());
        model.addAttribute("result", null);
        return "analysis/index";
    }

    @PostMapping
    public String analyze(@Valid @ModelAttribute("analysisForm") AnalysisForm form,
                          BindingResult bindingResult,
                          Model model) {
        if (form.getStartDate() != null && form.getEndDate() != null &&
                form.getEndDate().isBefore(form.getStartDate())) {
            bindingResult.rejectValue("endDate", "invalidRange", "End date must be on or after start date");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("streams", streamService.listActive());
            model.addAttribute("result", null);
            return "analysis/index";
        }

        AnalysisService.AnalysisResult result = analysisService.analyze(form);
        model.addAttribute("streams", streamService.listActive());
        model.addAttribute("result", result);
        return "analysis/index";
    }
}

