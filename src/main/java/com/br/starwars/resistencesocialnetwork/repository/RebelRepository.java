package com.br.starwars.resistencesocialnetwork.repository;

import com.br.starwars.resistencesocialnetwork.domain.Rebel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RebelRepository extends JpaRepository<Rebel, String> {
    @Query("select ((select count(r1) from Rebel r1 where r1.treasonReportNum < 3)/cast(count(r2) as float))*100 from Rebel r2")
    public Integer percentageOfRebels();

    @Query("select ((select count(r1) from Rebel r1 where r1.treasonReportNum > 2)/cast(count(r2) as float))*100 from Rebel r2")
    public Integer percentageOfTreason();

    public boolean existsRebelByIdAndTreasonReportNumGreaterThan(String id, Integer treasonReportNum);

}
