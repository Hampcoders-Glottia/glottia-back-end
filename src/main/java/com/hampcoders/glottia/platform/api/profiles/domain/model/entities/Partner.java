package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_status_id")
    private SubscriptionStatus subscriptionStatus;

    public Partner(String legalName, String businessName, String taxId, 
                   String contactEmail, String contactPhone, String contactPersonName,
                   String description) {
        this.legalName = legalName;
        this.businessName = businessName;
        this.taxId = taxId;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactPersonName = contactPersonName;
        this.description = description;
    }

    // Update partner information
    public void updateInformation(String legalName, String businessName, String taxId,
                                 String contactEmail, String contactPhone, String contactPersonName,
                                 String description) {
        this.legalName = legalName;
        this.businessName = businessName;
        this.taxId = taxId;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactPersonName = contactPersonName;
        this.description = description;
    }

    public void updateContactInformation(String contactEmail, String contactPhone, String contactPersonName) {
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactPersonName = contactPersonName;
    }

    // Update subscription status
    public void updateSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getDisplayName() {
        return businessName != null ? businessName : legalName;
    }
}