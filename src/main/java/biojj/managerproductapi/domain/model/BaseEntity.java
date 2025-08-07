package br.com.claro.catalogregulatoryoffers.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    private static final String DEFAULT_USER = "MCR";

    @Column(name = "CD_USUARIO_CRIACAO", length = 30)
    private String createdBy = DEFAULT_USER;

    @Column(name = "DT_CRIACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "CD_USUARIO_ATUALIZACAO", length = 30)
    private String updatedBy = DEFAULT_USER;

    @Column(name = "DT_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (createdBy == null) {
            createdBy = DEFAULT_USER;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        if (updatedBy == null) {
            updatedBy = DEFAULT_USER;
        }
    }
}