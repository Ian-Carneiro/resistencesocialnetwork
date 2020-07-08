package com.br.starwars.resistencesocialnetwork.repository;

import com.br.starwars.resistencesocialnetwork.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {
}
