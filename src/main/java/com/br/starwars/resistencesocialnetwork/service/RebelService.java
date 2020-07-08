package com.br.starwars.resistencesocialnetwork.service;

import com.br.starwars.resistencesocialnetwork.domain.*;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.ItemIdDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.LocationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.NegotiationDTO;
import com.br.starwars.resistencesocialnetwork.domain.dataTransfer.RebelDTO;
import com.br.starwars.resistencesocialnetwork.exception.NegotiationException;
import com.br.starwars.resistencesocialnetwork.exception.RebelNotExistsException;
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

    public RebelDTO updateLocation(LocationDTO location, String idRebel) throws RebelNotExistsException {
        Optional<Rebel> rebel = rebelRepository.findById(idRebel);
        if (!rebel.isPresent()) {
            throw new RebelNotExistsException();
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

    public boolean treasonReport(String idAccuser, String idAccused) throws RebelNotExistsException {
        Optional<Rebel> accused = rebelRepository.findById(idAccused);
        Optional<Rebel> accuser = rebelRepository.findById(idAccuser);
        if(!accused.isPresent() && !accuser.isPresent()){
            throw new RebelNotExistsException();
        }
        if (accused.get().getAccusers().indexOf(accuser.get()) != -1)
            return false;
        accused.get().treasonReport();
        accused.get().setAccuser(accuser.get());
        accused.get().setAccused();
        rebelRepository.save(accused.get());
        return true;
    }

    public void negotiate(NegotiationDTO negotiation) throws NegotiationException {
        Optional<Rebel> negotiator = rebelRepository.findById(negotiation.getNegotiatorId());
        Optional<Rebel> involved = rebelRepository.findById(negotiation.getInvolvedId());
        int totalPointItemsNegotiator = 0;
        int totalPointItemsInvolved = 0;
        if (negotiator.isPresent() && involved.isPresent()) {
            if (negotiator.get().isTreason()){
                throw new NegotiationException("The rebel with id "+negotiator.get().getId()+" is a traitor");
            }
            if (involved.get().isTreason()){
                throw new NegotiationException("The rebel with id "+negotiator.get().getId()+" is a traitor");
            }
            List<String> itemsIdNegotiatorNegotiation = negotiation.getNegotiatorItems().stream().map(ItemIdDTO::getId)
                    .collect(Collectors.toList());
            totalPointItemsNegotiator = transferItems(negotiator, involved, itemsIdNegotiatorNegotiation,
                    negotiation.getNegotiatorItems());

            List<String> itemsIdInvolvedNegotiation = negotiation.getInvolvedItemsTraded().stream().map(ItemIdDTO::getId)
                    .collect(Collectors.toList());
            totalPointItemsInvolved = transferItems(negotiator, involved, itemsIdInvolvedNegotiation,
                    negotiation.getInvolvedItemsTraded());
        }else {
            throw new NegotiationException("Either the negotiator, or the other rebel involved in the negotiation does not exist.");
        }

        if (totalPointItemsNegotiator >= totalPointItemsInvolved && totalPointItemsNegotiator != 0) {
            rebelRepository.save(negotiator.get());
            rebelRepository.save(involved.get());
            return;
        }
        throw new NegotiationException("The quantity of items does not match");
    }

    //Método utilizado somente em negotiate
    private int transferItems(Optional<Rebel> otherRebel, Optional<Rebel> rebel, List<String> itemsIdRebelNegotiation,
                        List<ItemIdDTO> ItemDTORebelNegotiation) throws NegotiationException {
        List<String> itemsIdOtherRebel = otherRebel.get().getRebelItemList().stream()
                .map(rebelItem -> rebelItem.getItem().getId()).collect(Collectors.toList());
        int totalPointItemsRebel = 0;
        for (RebelItem item : rebel.get().getRebelItemList()) {
            int index = itemsIdRebelNegotiation.indexOf(item.getItem().getId());
            if (index != -1) {
                int numItemsRebelNegotiation = ItemDTORebelNegotiation.get(index).getNumItems();
                if (numItemsRebelNegotiation > item.getNumItems()) {
                    throw new NegotiationException("The quantity of items does not match");
                }
                totalPointItemsRebel += (item.getItem().getPoints() * numItemsRebelNegotiation);
                item.removeNumItems(numItemsRebelNegotiation);
                index = itemsIdOtherRebel.indexOf(item.getItem().getId());
                if (index != -1) {
                    otherRebel.get().getRebelItemList().get(index).addNumItems(numItemsRebelNegotiation);
                } else {
                    otherRebel.get().addRebelItem(new RebelItem(item.getItem(), numItemsRebelNegotiation));
                }
            }
        }
        return totalPointItemsRebel;
    }

    public Integer percentageOfRebels() {
        return rebelRepository.percentageOfRebels();
    }

    public Integer percentageOfTreason() {
        return rebelRepository.percentageOfTreason();
    }

    public boolean isTraitor(String id) throws RebelNotExistsException {
        if(rebelRepository.existsById(id))
            return rebelRepository.existsRebelByIdAndTreasonReportNumGreaterThan(id, 2);
        throw new RebelNotExistsException();
    }
}


//    private int process(Integer totalPointItemsRebel,
//                        int numItemsRebel,
//                        RebelItem item,
//                        List<String> itemsIdOtherRebel,
//                        Optional<Rebel> otherRebel, int index) {
//        if (numItemsRebel > item.getNumItems()) {
////            return false;
//        }
//        totalPointItemsRebel += (item.getItem().getPoints() * numItemsRebel);
//        item.removeNumItems(numItemsRebel);
//        index = itemsIdOtherRebel.indexOf(item.getItem().getId());
//        if (index != -1) {
//            otherRebel.get().getRebelItemList().get(index).addNumItems(numItemsRebel);
//        } else {
//            otherRebel.get().addRebelItem(new RebelItem(item.getItem(), numItemsRebel));
//        }
//        return totalPointItemsRebel;
//    }
