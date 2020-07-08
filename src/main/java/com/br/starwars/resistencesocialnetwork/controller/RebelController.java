package com.br.starwars.resistencesocialnetwork.controller;

import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.LocationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.NegotiationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.RebelDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.TreasonReportDTO;
import com.br.starwars.resistencesocialnetwork.service.RebelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/rebels")
@AllArgsConstructor
public class RebelController {
    private RebelService rebelService;

    @PostMapping("/")
    @ApiOperation(value = "Persiste um novo rebelde no banco de dados.",
                response = RebelDTO.class
    )
    private ResponseEntity create(@ApiParam(value = "informações necessárias para persistir um rebelde no " +
            "banco de dados. Os ids dos itens são passados, além da quantidade dos mesmos. Os itens serão buscados no" +
            "banco de dados e atribuídos ao rebelde.")
            @RequestBody RebelDTO rebel) {
        try{
            return ResponseEntity.status(201).body(rebelService.create(rebel));
        } catch (NullPointerException err){
            return ResponseEntity.status(400).body(err.getMessage());
        }
    }

    @PutMapping("/{idRebel}")
    @ApiOperation(value = "Atualiza a localização atual do rebelde.",
            response = RebelDTO.class
    )
    private ResponseEntity updateLocation(@RequestBody LocationDTO location, @PathVariable String idRebel) {
        try{
            return ResponseEntity.status(200).body(rebelService.updateLocation(location, idRebel));
        } catch (NullPointerException err){
            return ResponseEntity.status(400).body(err.getMessage());
        }
    }

    @PutMapping("/treason/report")
    @ApiOperation(value = "Reporta a traição de um rebelde.")
    private ResponseEntity treasonReport(@RequestBody TreasonReportDTO report) {
        boolean reported = false;
        try{
            reported = rebelService.treasonReport(report.getIdAccuser(), report.getIdAccused());
        } catch (NullPointerException err){
            return ResponseEntity.status(400).body(err.getMessage());
        }
        Map<String, String> message = new HashMap<>();
        if (reported) {
            message.put("message", "Rebel reported.");
            return ResponseEntity.status(200).body(message);
        }
        message.put("message", "Rebel not registered in the database or has already been reported by you.");
        return ResponseEntity.status(401).body(message);
    }

    @PutMapping("/negotiate")
    @ApiOperation(value = "Realiza a troca de itens entre os rebeldes.")
    private ResponseEntity negotiate(@RequestBody NegotiationDTO negotiationDTO) {
        boolean negotiated = false;
        try{
            negotiated= rebelService.negotiate(negotiationDTO);
        } catch (NullPointerException err){
            return ResponseEntity.status(400).body(err.getMessage());
        }
        Map<String, String> message = new HashMap<>();
        if (negotiated) {
            message.put("message", "Item exchange successful.");
            return ResponseEntity.status(200).body(message);
        }
        message.put("message", "Item exchange not performed. Perhaps more points are needed, " +
                "or items are being negotiated that either party does not own.");
        return ResponseEntity.status(401).body(message);
    }

    @GetMapping("/info/percentage/rebels")
    @ApiOperation(value = "Recebe a porcentagem de todos os rebeldes.")
    private ResponseEntity<Map<String, String>> percentageOfRebels() {
        Integer percentage = rebelService.percentageOfRebels();
        Map<String, String> message = new HashMap<>();
        message.put("percentage", percentage.toString());
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/info/percentage/treason")
    @ApiOperation(value = "Recebe a porcentagem de todos os traidores da resistência.")
    private ResponseEntity<Map<String, String>> percentageOfTreason() {
        Integer percentage = rebelService.percentageOfTreason();
        Map<String, String> message = new HashMap<>();
        message.put("percentage", percentage.toString());
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/isTraitor/{idTraitor}")
    @ApiOperation(value = "Identifica se um rebelde é um traidor.")
    private ResponseEntity<Map<String, String>> isTraitor(@PathVariable String idTraitor) {
        boolean isTraitor = rebelService.isTraitor(idTraitor);
        return ResponseEntity.status(200).body(new HashMap<String, String>() {{
            put("message", isTraitor ? "The rebel betrayed the resistance" : "the rebel is not a traitor");
        }});
    }
}
