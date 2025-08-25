package com.exampleKiitFinder.KittFinder.Repo;

import com.exampleKiitFinder.KittFinder.modell.Found;
import com.exampleKiitFinder.KittFinder.modell.Item;
import com.exampleKiitFinder.KittFinder.modell.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoundRepo extends JpaRepository<Found,Long> {
    // Find all found records for an item
    List<Found> findByItem(Item item);

    // Find found records where user is the finder
    List<Found> findByFinder(User finder);

    // Find found records where user is the owner
    List<Found> findByOwner(User owner);

    // Find found records for a specific item and finder
    Optional<Found> findByItemAndFinder(Item item, User finder);

    // Find found records for a specific item and owner
    Optional<Found> findByItemAndOwner(Item item, User owner);

    // Find all pending confirmations for an owner (where finder confirmed
    List<Found> findByOwnerAndFinderConfirmedTrueAndOwnerConfirmedFalse(User owner);

    // Find fully confirmed found items
    List<Found> findByFinderConfirmedTrueAndOwnerConfirmedTrue();

    // Check if an item has any found records
    boolean existsByItem(Item item);

    // Check if both parties have confirmed for a specific item
    Optional<Found> findByItemAndFinderConfirmedTrueAndOwnerConfirmedTrue(Item item);
}
