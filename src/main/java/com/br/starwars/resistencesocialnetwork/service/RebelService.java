package com.br.starwars.resistencesocialnetwork.service;

import com.br.starwars.resistencesocialnetwork.domain.*;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.ItemIdDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.LocationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.NegotiationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.RebelDTO;
import com.br.starwars.resistencesocialnetwork.repository.RebelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RebelService {
    private RebelRepository rebelRepository;
    private ItemService itemService;

    public RebelDTO create(RebelDTO rebel) {
        List<String> itemsIds = rebel.getItems().stream().map(ItemIdDTO::getId)
                .collect(Collectors.toList());
        List<Item> items = itemService.findAllById(itemsIds);
        List<RebelItem> rebelItemList = new ArrayList<>();
        for (int i = 0; i < itemsIds.size(); i++) {
            ItemIdDTO itemId = rebel.getItems().get(i);
            Item item = items.stream().filter(item1 -> item1.getId().equals(itemId.getId())).collect(Collectors.toList()).get(0);
            if (itemsIds.get(i).equals(items.get(i).getId())) {
                rebelItemList.add(new RebelItem(item, itemId.getNumItems()));
            }
        }
        LocationDTO locationDTO = rebel.getLocation();
        Rebel newRebel = new Rebel(rebel.getName(), rebel.getAge(), rebel.getGenre(),
                new Location(locationDTO.getName(), locationDTO.getLongitude(), locationDTO.getLatitude()),
                rebelItemList);
        Rebel createdRebel = rebelRepository.save(newRebel);
        return new RebelDTO(
                createdRebel.getId(),
                createdRebel.getName(),
                createdRebel.getAge(),
                createdRebel.getGenre(),
                locationDTO,
                new ArrayList<>(createdRebel.getRebelItemList().stream()
                        .map(rebelItem -> new ItemIdDTO(rebelItem.getItem().getId(), rebelItem.getNumItems()))
                        .collect(Collectors.toList())));
    }

    public RebelDTO updateLocation(LocationDTO location, String idRebel) {
        Optional<Rebel> rebel = rebelRepository.findById(idRebel);
        if (!rebel.isPresent()) {
            return new RebelDTO();
        }
        rebel.get().setLocation(new Location(location.getName(), location.getLongitude(), location.getLatitude()));
        Rebel createdRebel = rebelRepository.save(rebel.get());
        return new RebelDTO(
                createdRebel.getId(),
                createdRebel.getName(),
                createdRebel.getAge(),
                createdRebel.getGenre(),
                location,
                new ArrayList<>(createdRebel.getRebelItemList().stream()
                        .map(rebelItem -> new ItemIdDTO(rebelItem.getItem().getId(), rebelItem.getNumItems()))
                        .collect(Collectors.toList())));
    }

    public boolean treasonReport(String idAccuser, String idAccused) {
        Optional<Rebel> accused = rebelRepository.findById(idAccused);
        Optional<Rebel> accuser = rebelRepository.findById(idAccuser);
        if (!accused.isPresent() && !accuser.isPresent() || accused.get().getAccusers().indexOf(accuser.get()) != -1)
            return false;
        accused.get().treasonReport();
        accused.get().setAccuser(accuser.get());
        accused.get().setAccused();
        rebelRepository.save(accused.get());
        return true;
    }

    public boolean negotiate(NegotiationDTO negotiation) {
        Optional<Rebel> negotiator = rebelRepository.findById(negotiation.getNegotiatorId());
        Optional<Rebel> involved = rebelRepository.findById(negotiation.getInvolvedId());
        int totalPointItemsNegotiator = 0;
        int totalPointItemsInvolved = 0;
        if (negotiator.isPresent() && involved.isPresent()) {
            List<String> itemsIdInvolved = involved.get().getRebelItemList().stream()
                    .map(rebelItem -> rebelItem.getItem().getId()).collect(Collectors.toList());
            List<String> itemsIdNegotiatorNegotiation = negotiation.getNegotiatorItems().stream().map(ItemIdDTO::getId)
                    .collect(Collectors.toList());
            for (RebelItem item : negotiator.get().getRebelItemList()) {
                int index = itemsIdNegotiatorNegotiation.indexOf(item.getItem().getId());
                if (index != -1) {
                    int numItemsNegotiation = negotiation.getNegotiatorItems().get(index).getNumItems();
                    if (numItemsNegotiation > item.getNumItems()) {
                        return false;
                    }
                    totalPointItemsNegotiator += (item.getItem().getPoints() * numItemsNegotiation);
                    item.removeNumItems(numItemsNegotiation);
                    index = itemsIdInvolved.indexOf(item.getItem().getId());
                    if (index != -1) {
                        involved.get().getRebelItemList().get(index).addNumItems(numItemsNegotiation);
                    } else {
                        involved.get().addRebelItem(new RebelItem(item.getItem(), numItemsNegotiation));
                    }
                }
            }
            List<String> itemsIdNegotiator = negotiator.get().getRebelItemList().stream()
                    .map(rebelItem -> rebelItem.getItem().getId()).collect(Collectors.toList());
            List<String> itemsIdInvolvedNegotiation = negotiation.getInvolvedItemsTraded().stream().map(ItemIdDTO::getId)
                    .collect(Collectors.toList());
            for (RebelItem item : involved.get().getRebelItemList()) {
                int index = itemsIdInvolvedNegotiation.indexOf(item.getItem().getId());
                if (index != -1) {
                    int numItemsNegotiation = negotiation.getInvolvedItemsTraded().get(index).getNumItems();
                    if (numItemsNegotiation > item.getNumItems()) {
                        return false;
                    }
                    totalPointItemsInvolved += (item.getItem().getPoints() * numItemsNegotiation);
                    item.removeNumItems(numItemsNegotiation);
                    index = itemsIdNegotiator.indexOf(item.getItem().getId());
                    if (index != -1) {
                        negotiator.get().getRebelItemList().get(index).addNumItems(numItemsNegotiation);
                    } else {
                        negotiator.get().addRebelItem(new RebelItem(item.getItem(), numItemsNegotiation));
                    }
                }
            }
        }

        if (totalPointItemsNegotiator >= totalPointItemsInvolved && totalPointItemsNegotiator != 0) {
            rebelRepository.save(negotiator.get());
            rebelRepository.save(involved.get());
            return true;
        }
        return false;
    }

    public Integer percentageOfRebels() {
        Integer p = rebelRepository.percentageOfRebels();
        System.out.println(p);
        return p;
    }

    public Integer percentageOfTreason() {
        return rebelRepository.percentageOfTreason();
    }

    public boolean isTraitor(String id) {
        return rebelRepository.existsRebelByIdAndTreasonReportNumGreaterThan(id, 2);
    }
}
