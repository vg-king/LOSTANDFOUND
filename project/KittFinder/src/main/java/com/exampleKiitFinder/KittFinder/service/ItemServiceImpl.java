package com.exampleKiitFinder.KittFinder.service;

import com.exampleKiitFinder.KittFinder.Repo.FoundRepo;
import com.exampleKiitFinder.KittFinder.Repo.ItemRepository;
import com.exampleKiitFinder.KittFinder.dto.ItemRequest;
import com.exampleKiitFinder.KittFinder.dto.ItemResponse;
import com.exampleKiitFinder.KittFinder.modell.Item;
import com.exampleKiitFinder.KittFinder.modell.ItemStatus;
import com.exampleKiitFinder.KittFinder.modell.User;
import com.exampleKiitFinder.KittFinder.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FoundRepo foundRepository; // Add this

    public ItemResponse createItem(ItemRequest itemRequest, User postedBy) {
        Item item = new Item();
        item.setTitle(itemRequest.getTitle());
        item.setDescription(itemRequest.getDescription());
        item.setLocation(itemRequest.getLocation());
        item.setCategory(itemRequest.getCategory());
        item.setStatus(itemRequest.getStatus());
        item.setReward(itemRequest.getReward());
        item.setPostedBy(postedBy);
        item.setImageUrl(itemRequest.getImageUrl());
        item.setApproved(false); // New items need approval
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        Item saved = itemRepository.save(item);
        return mapToResponse(saved);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return mapToResponse(item);
    }

    @Override
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResponse> getItemsByUser(User user) {
        return itemRepository.findByPostedBy(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getItemsByStatus(String status) {
        return itemRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> searchItemsByLocation(String location) {
        return itemRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ItemResponse mapToResponse(Item item) {
        // Get found markings count for this item
        int foundMarkingsCount = foundRepository.findByItem(item).size();
        boolean hasFoundMarkings = foundMarkingsCount > 0;
        
        // Determine status based on found markings
        String status = ItemStatus.LOST.getValue(); // Default status
        if (hasFoundMarkings) {
            // Check if any found record has both confirmations
            boolean hasConfirmedFound = foundRepository.findByItemAndFinderConfirmedTrueAndOwnerConfirmedTrue(item).isPresent();
            if (hasConfirmedFound) {
                status = ItemStatus.FOUND_CONFIRMED.getValue();
            } else {
                status = ItemStatus.FOUND_PENDING.getValue();
            }
        }

        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getLocation(),
                item.getCategory(),
                item.getImageUrl(),
                item.getCreatedAt() != null ? item.getCreatedAt().toString() : null,
                item.getUpdatedAt() != null ? item.getUpdatedAt().toString() : null,
                DateUtil.formatTimeAgo(item.getCreatedAt()), // New formatted date field
                item.getReward(),
                item.getPostedBy() != null ? item.getPostedBy().getName() : null,
                item.getPostedBy() != null ? item.getPostedBy().getId() : null,
                hasFoundMarkings,
                foundMarkingsCount,
                status
        );
    }

    public ItemResponse updateItem(Long id, ItemRequest itemRequest, User currentUser) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        // Only allow the owner or admin to update
        if (!item.getPostedBy().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to update this item");
        }

        item.setTitle(itemRequest.getTitle());
        item.setDescription(itemRequest.getDescription());
        item.setLocation(itemRequest.getLocation());
        item.setCategory(itemRequest.getCategory());
        item.setStatus(itemRequest.getStatus());
        item.setReward(itemRequest.getReward());
        item.setImageUrl(itemRequest.getImageUrl());
        item.setUpdatedAt(LocalDateTime.now());

        Item updated = itemRepository.save(item);
        return mapToResponse(updated);
    }
}