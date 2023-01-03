package com.toy.webshop.controlloer;

import com.toy.webshop.entity.point.PointDto;
import com.toy.webshop.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/point/{id}")
    public String getMyPoint(@PathVariable Long id, Model model) {
        List<PointDto> pointDtos = pointService.myPoint(id);
        model.addAttribute("myPoint", pointDtos);
        return "point/pointList";
    }
}
