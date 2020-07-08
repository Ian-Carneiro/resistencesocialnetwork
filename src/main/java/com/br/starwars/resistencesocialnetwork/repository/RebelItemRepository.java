package com.br.starwars.resistencesocialnetwork.repository;

import com.br.starwars.resistencesocialnetwork.domain.RebelItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface RebelItemRepository extends JpaRepository<RebelItem, String> {
    @Query("select ri.item.name, sum(ri.numItems)/cast(count(ri.item.name) as float) from RebelItem ri " +
            "where ri.numItems<>0" +
            "group by ri.item.name")
    public List<Object[]> averageItems();

    @Query("select sum(ri.item.points*ri.numItems) from RebelItem ri where ri.rebel.treasonReportNum > 2")
    public Integer lostPoints();
}
