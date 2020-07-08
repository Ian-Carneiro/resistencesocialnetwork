package com.br.starwars.resistencesocialnetwork.service;

import com.br.starwars.resistencesocialnetwork.repository.RebelItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RebelItemService {
    private RebelItemRepository rebelItemRepository;

    public Map<String, String> averageItems() {
        Map<String, String> result = new HashMap<>();
        List<Object[]> resultQuery = rebelItemRepository.averageItems();
        for (Object[] res : resultQuery) {
            result.put(res[0].toString(), res[1].toString());
        }
        ;
        return result;
    }

    ;

    public Integer lostPoints() {
        return rebelItemRepository.lostPoints();
    }
}
