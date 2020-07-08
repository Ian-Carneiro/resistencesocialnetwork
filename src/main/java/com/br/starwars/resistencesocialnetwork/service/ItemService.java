package com.br.starwars.resistencesocialnetwork.service;

import com.br.starwars.resistencesocialnetwork.domain.Item;
import com.br.starwars.resistencesocialnetwork.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ItemService {
    private ItemRepository itemRepository;

    public List<Item> findAllById(List<String> itemsIds) {
        return itemRepository.findAllById(itemsIds);
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> findById(String id) {
        return itemRepository.findById(id);
    }
}
