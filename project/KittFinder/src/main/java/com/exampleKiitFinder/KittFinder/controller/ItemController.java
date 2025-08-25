package com.exampleKiitFinder.KittFinder.controller;

import com.exampleKiitFinder.KittFinder.Repo.ItemRepository;
import com.exampleKiitFinder.KittFinder.dto.ItemRequest;
import com.exampleKiitFinder.KittFinder.dto.ItemResponse;
import com.exampleKiitFinder.KittFinder.modell.Item;
import com.exampleKiitFinder.KittFinder.modell.User;
import com.exampleKiitFinder.KittFinder.service.ItemService;
import com.exampleKiitFinder.KittFinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {"https://kiitfinderui-abbi.vercel.app", "https://lostandfound-1-p1l9.onrender.com", "http://localhost:*"})

public class ItemController {
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems(){
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }
    
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest){
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ItemResponse item = itemService.createItem(itemRequest, currentUser);
        return ResponseEntity.ok(item);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id){
        ItemResponse item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id){
        // Get current authenticated user for authorization check
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user owns the item or is admin
        ItemResponse item = itemService.getItemById(id);
        if (!Objects.equals(item.getPostedById(), currentUser.getId()) &&
            !currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("You don't have permission to delete this item");
        }
        
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }
    
    @GetMapping("/my-items")
    public ResponseEntity<List<ItemResponse>> getMyItems(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<ItemResponse> items = itemService.getItemsByUser(currentUser);
        return ResponseEntity.ok(items);
    }
    // ItemController.java mein ye method add karo
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> searchItems(@RequestParam String query) {
        List<ItemResponse> items = itemService.searchItemsByLocation(query);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/filter/category/{category}")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@PathVariable String category) {
        List<ItemResponse> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/filter/status/{status}")
    public ResponseEntity<List<ItemResponse>> getItemsByStatus(@PathVariable String status) {
        List<ItemResponse> items = itemService.getItemsByStatus(status);
        return ResponseEntity.ok(items);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody ItemRequest itemRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ItemResponse updatedItem = itemService.updateItem(id, itemRequest, currentUser);
        return ResponseEntity.ok(updatedItem);
    }
}
