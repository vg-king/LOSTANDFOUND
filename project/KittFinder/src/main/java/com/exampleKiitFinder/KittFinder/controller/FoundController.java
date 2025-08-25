package com.exampleKiitFinder.KittFinder.controller;

import com.exampleKiitFinder.KittFinder.dto.FoundRequest;
import com.exampleKiitFinder.KittFinder.dto.FoundResponse;
import com.exampleKiitFinder.KittFinder.modell.User;
import com.exampleKiitFinder.KittFinder.service.FoundService;
import com.exampleKiitFinder.KittFinder.service.UserService;
import org.apache.catalina.mbeans.SparseUserDatabaseMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/found")
@CrossOrigin(origins = {"https://kiitfinderui-abbi.vercel.app", "https://lostandfound-1-p1l9.onrender.com", "http://localhost:*"})
public class FoundController {
    @Autowired
    private FoundService foundService;
    @Autowired
    private UserService userService;

    @PostMapping("/mark")
    public ResponseEntity<FoundResponse> markItemAsFound(@RequestBody FoundRequest request){
        try {
            User currentUser = getCurrentUser();
            FoundResponse response = foundService.markItemAsFound(request,currentUser);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null );
        }
    }
    @PostMapping("/confirm/{foundId}")
    public ResponseEntity<Map<String, String>> confirmFound(@PathVariable Long foundId) {
        try {
            User currentUser = getCurrentUser();
            FoundResponse response = foundService.confirmFound(foundId, currentUser);

            String message = response.isBothConfirmed() ?
                    "Item confirmed as found and removed from listings!" :
                    "Found confirmation saved!";

            return ResponseEntity.ok(Map.of(
                    "message", message,
                    "bothConfirmed", String.valueOf(response.isBothConfirmed())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

    }
    @GetMapping("/pending-confirmation")
    public ResponseEntity<List<FoundResponse>> getPendingConfirmations(){
        User currentUser = getCurrentUser();
        List<FoundResponse> pending = foundService.getPendingConfirmation(currentUser);
        return ResponseEntity.ok(pending);

    }
    @GetMapping("/my-found-items")
    public ResponseEntity<List<FoundResponse>> getMyFoundItem(){
        User currentUser = getCurrentUser();
        List<FoundResponse> foundItems = foundService.getItemFoundByUser(currentUser);
        return ResponseEntity.ok(foundItems);
    }
    @GetMapping("/check/{itemId}")
    public ResponseEntity<Map<String,Boolean>> checkIfItemHasFoundMarkings(@PathVariable Long itemId){
        boolean hasFound = foundService.hasFoundPending(itemId);
        return ResponseEntity.ok(Map.of("hasFoundMarkings", hasFound));
    }
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<FoundResponse>> getFoundRecordsForItem(@PathVariable Long itemId) {
        List<FoundResponse> foundRecords = foundService.getFoundRecordsForItem(itemId);
        return ResponseEntity.ok(foundRecords);
    }
    @DeleteMapping("/cancel/{foundId}")
    public ResponseEntity<Map<String, String>> cancelFoundMarking(@PathVariable Long foundId) {
        try {
            User currentUser = getCurrentUser();
            foundService.cancelFoundMarking(foundId, currentUser);
            return ResponseEntity.ok(Map.of("message", "Found marking cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
