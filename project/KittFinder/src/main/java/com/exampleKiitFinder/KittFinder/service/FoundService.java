package com.exampleKiitFinder.KittFinder.service;

import com.exampleKiitFinder.KittFinder.Repo.FoundRepo;
import com.exampleKiitFinder.KittFinder.Repo.ItemRepository;
import com.exampleKiitFinder.KittFinder.dto.FoundRequest;
import com.exampleKiitFinder.KittFinder.dto.FoundResponse;
import com.exampleKiitFinder.KittFinder.modell.Found;
import com.exampleKiitFinder.KittFinder.modell.Item;
import com.exampleKiitFinder.KittFinder.modell.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoundService {
    @Autowired
    private FoundRepo foundRepo;
    @Autowired
    private ItemRepository itemRepository;

    public FoundResponse markItemAsFound(FoundRequest request, User finder){
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(()->new RuntimeException("Item not Found"));
        if (item.getReportedBy().getId().equals(finder.getId())){
            throw new RuntimeException("You cannot mark your item as found");
        }
        Optional<Found> existingFound = foundRepo.findByItemAndFinder(item,finder);
        if (existingFound.isPresent()){
            throw new RuntimeException("You have already  marked this item as found ");
        }
        Found found = new Found(item,finder,item.getPostedBy(),request.getMessage());
        Found saved = foundRepo.save(found);
        return mapToResponse(saved);
    }
    public FoundResponse confirmFound(Long foundId,User owner){
        Found found = foundRepo.findById(foundId)
                .orElseThrow(()->new RuntimeException("Found record not found"));
        if (!found.getOwner().getId().equals(owner.getId())){
            throw new RuntimeException("You can only confirm your own item");
        }
        if (found.isOwnerConfirmed()){
            throw new RuntimeException("You have already confirmed this found item");
        }
        found.setOwnerConfirmed(true);
        Found updated = foundRepo.save(found);

        if (updated.isBothConfirmed()){
            itemRepository.delete(found.getItem());
        }

        return mapToResponse(updated);

    }
    public List<FoundResponse> getPendingConfirmation(User owner){
        return foundRepo.findByOwnerAndFinderConfirmedTrueAndOwnerConfirmedFalse(owner)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<FoundResponse> getItemFoundByUser(User finder){
        return foundRepo.findByFinder(finder)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public boolean hasFoundPending(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()->new RuntimeException("Item not found"));
        return foundRepo.existsByItem(item);
    }
    public List<FoundResponse> getFoundRecordsForItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return foundRepo.findByItem(item)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public void cancelFoundMarking(Long foundId, User finder) {
        Found found = foundRepo.findById(foundId)
                .orElseThrow(() -> new RuntimeException("Found record not found"));

        if (!found.getFinder().getId().equals(finder.getId())) {
            throw new RuntimeException("You can only cancel your own found markings");
        }

        if (found.isOwnerConfirmed()) {
            throw new RuntimeException("Cannot cancel after owner has confirmed");
        }

        foundRepo.delete(found);
    }
    private FoundResponse mapToResponse(Found found) {
        return new FoundResponse(
                found.getId(),
                found.getItem().getId(),
                found.getItem().getTitle(),
                found.getFinder().getName(),
                found.getOwner().getName(),
                found.getFinder().getId(),
                found.getOwner().getId(),
                found.isFinderConfirmed(),
                found.isOwnerConfirmed(),
                found.getFinderConfirmedAt() != null ? found.getFinderConfirmedAt().toString() : null,
                found.getOwnerConfirmedAt() != null ? found.getOwnerConfirmedAt().toString() : null,
                found.getCreatedAt().toString(),
                found.getFinderMessage(),
                found.isBothConfirmed()
        );
    }
}
