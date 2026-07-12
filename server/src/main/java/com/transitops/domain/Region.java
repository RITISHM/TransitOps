package com.transitops.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Domain entity representing a Region in the TransitOps system.
 */
@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public Region() {
    }

    public Region(Long id, String name, String code, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static RegionBuilder builder() {
        return new RegionBuilder();
    }

    public static class RegionBuilder {
        private Long id;
        private String name;
        private String code;
        private LocalDateTime createdAt;
        public RegionBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RegionBuilder name(String name) {
            this.name = name;
            return this;
        }
        public RegionBuilder code(String code) {
            this.code = code;
            return this;
        }
        public RegionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Region build() {
            return new Region(this.id, this.name, this.code, this.createdAt);
        }
    }
}
