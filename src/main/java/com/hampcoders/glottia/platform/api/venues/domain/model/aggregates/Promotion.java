package com.hampcoders.glottia.platform.api.venues.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.CreatePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

import io.jsonwebtoken.lang.Strings;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "promotions")
public class Promotion extends AuditableAbstractAggregateRoot<Promotion> {

    @Embedded
    private PartnerId partnerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotion_type_id", nullable = false)
    private PromotionType promotionType;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Constructor JPA
    protected Promotion() {
        this.partnerId = null;
        this.name = Strings.EMPTY;
        this.description = Strings.EMPTY;
        this.promotionType = null;
        this.value = null;
        this.isActive = true;
    }

    // Constructor de negocio
    public Promotion(CreatePromotionCommand command, PromotionType promotionType) {
        this();
        validatePartnerId(command.partnerId());
        validateName(command.name());
        validateDescription(command.description());
        validatePromotionType(promotionType);
        validateValue(command.value(), promotionType);

        this.partnerId = command.partnerId();
        this.name = command.name();
        this.description = command.description();
        this.promotionType = promotionType; // ✅ entidad persistida
        this.value = command.value();
    }

    // Validaciones (privadas)
    private void validatePartnerId(PartnerId partnerId) {
        if (partnerId == null) throw new IllegalArgumentException("Partner ID required");
    }
    private void validateName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (name.length() > 100) throw new IllegalArgumentException("Name too long");
    }
    private void validateDescription(String description) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description required");
        if (description.length() > 500) throw new IllegalArgumentException("Description too long");
    }
    private void validatePromotionType(PromotionType type) {
        if (type == null) throw new IllegalArgumentException("Promotion type required");
    }
    private void validateValue(Integer value, PromotionType type) {
        if (value == null || value < 0) throw new IllegalArgumentException("Value must be non-negative");
        if (type.getStringName().equals("DISCOUNT_PERCENT") && value > 100) throw new IllegalArgumentException("Percent max 100");
    }

    // Métodos de negocio
    public void updateDetails(String name, String description, Integer value) {
        validateName(name);
        validateDescription(description);
        validateValue(value, this.promotionType);
        this.name = name;
        this.description = description;
        this.value = value;
    }
    public void deactivate() {
        if (!isActive) throw new IllegalStateException("Already inactive");
        this.isActive = false;
    }
    public void activate() {
        if (isActive) throw new IllegalStateException("Already active");
        this.isActive = true;
    }
}
