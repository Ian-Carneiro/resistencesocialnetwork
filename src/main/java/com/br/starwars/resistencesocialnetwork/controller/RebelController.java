package com.br.starwars.resistencesocialnetwork.controller;

import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.LocationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.NegotiationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.RebelDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.TreasonReportDTO;
import com.br.starwars.resistencesocialnetwork.exception.NegotiationException;
import com.br.starwars.resistencesocialnetwork.exception.RebelNotExistsException;
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
            return ResponseEntity.status(400).body(new HashMap<String, String>() {{
                put("message", err.getMessage());
            }});
        }
    }

    @PutMapping("/{idRebel}")
    @ApiOperation(value = "Atualiza a localização atual do rebelde.",
            response = RebelDTO.class
    )
    private ResponseEntity updateLocation(@RequestBody LocationDTO location, @PathVariable String idRebel) {
        try{
            return ResponseEntity.status(200).body(rebelService.updateLocation(location, idRebel));
        } catch (NullPointerException | RebelNotExistsException err){
            return ResponseEntity.status(400).body(new HashMap<String, String>() {{
                put("message", err.getMessage());
            }});
        }
    }

    @PutMapping("/treason/report")
    @ApiOperation(value = "Reporta a traição de um rebelde.")
    private ResponseEntity treasonReport(@RequestBody TreasonReportDTO report) {
        boolean reported;
        Map<String, String> message = new HashMap<>();

        try{
            reported = rebelService.treasonReport(report.getIdAccuser(), report.getIdAccused());
        } catch (NullPointerException | RebelNotExistsException err){
            message.put("message", err.getMessage());
            return ResponseEntity.status(400).body(message);
        }
        if (reported) {
            message.put("message", "Rebel reported.");
            return ResponseEntity.status(200).body(message);
        }
        message.put("message", "Rebel already been reported by you.");
        return ResponseEntity.status(401).body(message);
    }

    @PutMapping("/negotiate")
    @ApiOperation(value = "Realiza a troca de itens entre os rebeldes.")
    private ResponseEntity negotiate(@RequestBody NegotiationDTO negotiationDTO) {
        Map<String, String> message = new HashMap<>();
        try{
            rebelService.negotiate(negotiationDTO);
        } catch (NullPointerException | NegotiationException err){
            message.put("message", err.getMessage());
            return ResponseEntity.status(400).body(message);
        }

        message.put("message", "Item exchange successful.");
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/info/percentage/rebels")
    @ApiOperation(value = "Recebe a porcentagem de todos os rebeldes.")
    private ResponseEntity percentageOfRebels() {
        Integer percentage = rebelService.percentageOfRebels();
        Map<String, String> message = new HashMap<>();
        message.put("percentage", percentage.toString());
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/info/percentage/treason")
    @ApiOperation(value = "Recebe a porcentagem de todos os traidores da resistência.")
    private ResponseEntity percentageOfTreason() {
        Integer percentage = rebelService.percentageOfTreason();
        Map<String, String> message = new HashMap<>();
        message.put("percentage", percentage.toString());
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/isTraitor/{idTraitor}")
    @ApiOperation(value = "Identifica se um rebelde é um traidor.")
    private ResponseEntity isTraitor(@PathVariable String idTraitor) {
        Boolean isTraitor;
        Map<String, String> message = new HashMap<>();
        try{
            isTraitor = rebelService.isTraitor(idTraitor);
        }catch (RebelNotExistsException err){
            message.put("message", err.getMessage());
            return ResponseEntity.status(400).body(message);
        }
        return ResponseEntity.status(200).body(new HashMap<String, String>() {{
            put("message", isTraitor.toString());
        }});
    }
}
