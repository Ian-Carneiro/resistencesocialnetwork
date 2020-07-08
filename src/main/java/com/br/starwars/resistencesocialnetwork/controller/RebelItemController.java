package com.br.starwars.resistencesocialnetwork.controller;

import com.br.starwars.resistencesocialnetwork.service.RebelItemService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rebels/items/")
@AllArgsConstructor
public class RebelItemController {
    private RebelItemService rebelItemService;

    @GetMapping("/average")
    @ApiOperation(value = "Retorna a média de items de todos os rebeldes")
    private ResponseEntity<Map<String, String>> averageItems() {
        Map<String, String> percentage = rebelItemService.averageItems();
        return ResponseEntity.status(200).body(percentage);
    }

    @GetMapping("/lost/points")
    @ApiOperation(value = "Retorna a quantidade de pontos que foram perdidos por causa dos traidores.")
    private ResponseEntity<Map<String, Integer>> lostPoints() {
        Map<String, Integer> res = new HashMap<>();
        Integer lostPoints = rebelItemService.lostPoints();
        res.put("lostPoints", lostPoints);
        return ResponseEntity.status(200).body(res);
    }
}
