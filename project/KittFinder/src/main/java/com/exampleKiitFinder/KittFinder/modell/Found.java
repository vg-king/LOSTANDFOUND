package com.exampleKiitFinder.KittFinder.modell;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "found_items")
public class Found {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "finder_id") // Person who found the item
    private User finder;

    @ManyToOne
    @JoinColumn(name = "owner_id") // Original owner who posted the lost item
    private User owner;

    private boolean finderConfirmed = false; // Finder clicked "Found"
    private boolean ownerConfirmed = false;  // Owner clicked "Confirm Found"

    private LocalDateTime finderConfirmedAt;
    private LocalDateTime ownerConfirmedAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    private String finderMessage; // Optional message from finder

    // Constructors
    public Found() {}

    public Found(Item item, User finder, User owner, String finderMessage) {
        this.item = item;
        this.finder = finder;
        this.owner = owner;
        this.finderMessage = finderMessage;
        this.finderConfirmed = true;
        this.finderConfirmedAt = LocalDateTime.now();
    }

    // Method to check if both parties confirmed
    public boolean isBothConfirmed() {
        return finderConfirmed && ownerConfirmed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getFinder() {
        return finder;
    }

    public void setFinder(User finder) {
        this.finder = finder;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isFinderConfirmed() {
        return finderConfirmed;
    }

    public void setFinderConfirmed(boolean finderConfirmed) {
        this.finderConfirmed = finderConfirmed;
        if (finderConfirmed && finderConfirmedAt == null) {
            this.finderConfirmedAt = LocalDateTime.now();
        }
    }

    public boolean isOwnerConfirmed() {
        return ownerConfirmed;
    }

    public void setOwnerConfirmed(boolean ownerConfirmed) {
        this.ownerConfirmed = ownerConfirmed;
        if (ownerConfirmed && ownerConfirmedAt == null) {
            this.ownerConfirmedAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getFinderConfirmedAt() {
        return finderConfirmedAt;
    }

    public void setFinderConfirmedAt(LocalDateTime finderConfirmedAt) {
        this.finderConfirmedAt = finderConfirmedAt;
    }

    public LocalDateTime getOwnerConfirmedAt() {
        return ownerConfirmedAt;
    }

    public void setOwnerConfirmedAt(LocalDateTime ownerConfirmedAt) {
        this.ownerConfirmedAt = ownerConfirmedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinderMessage() {
        return finderMessage;
    }

    public void setFinderMessage(String finderMessage) {
        this.finderMessage = finderMessage;
    }
}