package com.giannis.core_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    // Το μοναδικό αναγνωριστικό (Primary Key) για κάθε εγγραφή στη βάση
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Συμπληρώνεται αυτόματα με την ώρα και μέρα που δημιουργείται η εγγραφή
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Ανανεώνεται αυτόματα κάθε φορά που κάνουμε update την εγγραφή
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}