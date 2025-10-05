package com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(name = "partners")
public class Partner extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "legal_name", length = 100, nullable = false)
    private String legalName;

    @NotNull
    @NotBlank
    @Column(name = "business_name", length = 100, nullable = false)
    private String businessName;

    @NotNull
    @NotBlank
    @Column(name = "tax_id", length = 20, nullable = false, unique = true)
    private String taxId; // RUC, NIT, etc.

    @Email
    @NotNull
    @NotBlank
    @Column(name = "contact_email", length = 100, nullable = false)
    private String contactEmail;

    @NotNull
    @NotBlank
    @Column(name = "contact_phone", length = 20, nullable = false)
    private String contactPhone;

    @NotNull
    @NotBlank
    @Column(name = "contact_person_name", length = 100, nullable = false)
    private String contactPersonName;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_status_id", nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Size(max = 200)
    @Column(name = "website_url", length = 200)
    private String websiteUrl;

    @Size(max = 100)
    @Column(name = "instagram_handle", length = 100)
    private String instagramHandle;

    public Partner() {
        this.subscriptionStatus = SubscriptionStatus.geSubscriptionStatus();
        this.isActive = true;
    }

    public Partner(String legalName, String businessName, String taxId, 
                   String contactEmail, String contactPhone, String contactPersonName) {
        this();
        this.legalName = legalName;
        this.businessName = businessName;
        this.taxId = taxId;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactPersonName = contactPersonName;
    }

    public Partner(CreatePartnerCommand command) {
        this();
        this.legalName = command.legalName();
        this.businessName = command.businessName();
        this.taxId = command.taxId();
        this.contactEmail = command.contactEmail();
        this.contactPhone = command.contactPhone();
        this.contactPersonName = command.contactPersonName();
        this.description = command.description();
        this.websiteUrl = command.websiteUrl();
        this.instagramHandle = command.instagramHandle();
    }

    public void activateSubscription() {
        this.subscriptionStatus = SubscriptionStatus.toSubscriptionStatusFromName("ACTIVE");
    }

    public void suspendSubscription() {
        this.subscriptionStatus = SubscriptionStatus.toSubscriptionStatusFromName("SUSPENDED");
    }

    public void cancelSubscription() {
        this.subscriptionStatus = SubscriptionStatus.toSubscriptionStatusFromName("CANCELLED");
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updateSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public void updateContactInfo(String contactEmail, String contactPhone, String contactPersonName) {
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contactPersonName = contactPersonName;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateSocialMedia(String websiteUrl, String instagramHandle) {
        this.websiteUrl = websiteUrl;
        this.instagramHandle = instagramHandle;
    }
}
