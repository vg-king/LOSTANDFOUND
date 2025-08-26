package com.exampleKiitFinder.KittFinder.service;

import com.exampleKiitFinder.KittFinder.Repo.FoundRepo;
import com.exampleKiitFinder.KittFinder.Repo.ItemRepository;
import com.exampleKiitFinder.KittFinder.dto.FoundRequest;
import com.exampleKiitFinder.KittFinder.dto.FoundResponse;
import com.exampleKiitFinder.KittFinder.modell.Found;
import com.exampleKiitFinder.KittFinder.modell.Item;
import com.exampleKiitFinder.KittFinder.modell.ItemStatus;
import com.exampleKiitFinder.KittFinder.modell.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        
        // Fix for null reportedBy - use postedBy if reportedBy is null
        User itemOwner = item.getReportedBy() != null ? item.getReportedBy() : item.getPostedBy();
        if (itemOwner == null) {
            throw new RuntimeException("Item has no owner assigned");
        }
        
        if (itemOwner.getId().equals(finder.getId())){
            throw new RuntimeException("You cannot mark your item as found");
        }
        Optional<Found> existingFound = foundRepo.findByItemAndFinder(item,finder);
        if (existingFound.isPresent()){
            throw new RuntimeException("You have already marked this item as found");
        }
        
        // Create Found entity with proper entity management
        Found found = new Found();
        found.setItem(item);
        found.setFinder(finder);
        found.setOwner(itemOwner);
        found.setFinderMessage(request.getMessage());
        found.setFinderConfirmed(true);
        found.setFinderConfirmedAt(LocalDateTime.now());
        found.setCreatedAt(LocalDateTime.now());
        
        Found saved = foundRepo.save(found);
        return mapToResponse(saved);
    }
    @Transactional
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
        found.setOwnerConfirmedAt(LocalDateTime.now());
        Found updated = foundRepo.save(found);

        if (updated.isBothConfirmed()){
            // Instead of deleting, change item status to RESOLVED to remove from active listings
            Item item = found.getItem();
            item.setStatus(ItemStatus.RESOLVED.getValue());
            itemRepository.save(item);
            
            // Mark all found records for this item as completed
            List<Found> allFoundRecords = foundRepo.findByItem(item);
            for (Found foundRecord : allFoundRecords) {
                foundRecord.setOwnerConfirmed(true);
                foundRecord.setFinderConfirmed(true);
                if (foundRecord.getOwnerConfirmedAt() == null) {
                    foundRecord.setOwnerConfirmedAt(LocalDateTime.now());
                }
                if (foundRecord.getFinderConfirmedAt() == null) {
                    foundRecord.setFinderConfirmedAt(LocalDateTime.now());
                }
            }
            foundRepo.saveAll(allFoundRecords);
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
