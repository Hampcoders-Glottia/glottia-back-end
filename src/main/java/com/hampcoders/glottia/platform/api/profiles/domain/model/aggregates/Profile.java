package com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Entity
@Getter
@Table(name = "profiles")
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Min(0)
    @Max(100)
    @Column(name = "age", columnDefinition = "smallint", nullable = false)
    private int age;

    @Email
    @NotNull
    @NotBlank
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_role_id", nullable = false)
    private BusinessRole businessRole;

    // Profile can be either a Learner or a Partner, but not both
    // Using nullable foreign keys - only one should be populated
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "learner_id")
    private Learner learner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    // --- Constructors ---

    public Profile() {
    }

    public Profile(String firstName, String lastName, int age, String email, BusinessRole businessRole) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.businessRole = businessRole;

    }

    public Profile(CreateProfileCommand command) {
        this();
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.age = command.age();
        this.email = command.email();
        this.businessRole = BusinessRole.toBusinessRoleFromName(command.businessRole());
        
    }

    // --- Role assignment ---

    /**
     * Assigns this profile as a Learner with address information
     * Business rule: Profile can only be one type at a time
     */
    public void assignAsLearner(Learner learner) {
        if (this.partner != null) {
            throw new IllegalStateException("Cannot assign Learner. Profile is already assigned as Partner.");
        }
        this.learner = learner;
    }


    public void assignAsLearner(
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        float latitude,
        float longitude) {
    
        if (this.partner != null) {
            throw new IllegalStateException("Cannot assign Learner. Profile is already assigned as Partner.");
        }
        // FIX: Create the Learner entity here
        Learner newLearner = new Learner(street, number, city, postalCode, country, latitude, longitude);
        this.learner = newLearner;
    }
    
    /**
     * Assigns this profile as a Partner with business information  
     * Business rule: Profile can only be one type at a time
     */
    public void assignAsPartner(Partner partner) {
        if (this.learner != null) {
            throw new IllegalStateException("Cannot assign Partner. Profile is already assigned as Learner.");
        }
        this.partner = partner;
    }
    public void assignAsPartner(
        String legalName,
        String businessName,
        String taxId,
        String contactEmail,
        String contactPhone,
        String contactPersonName,
        String description) {
        
        if (this.learner != null) {
            throw new IllegalStateException("Cannot assign Partner. Profile is already assigned as Learner.");
        }
        // FIX: Create the Partner entity here
        Partner newPartner = new Partner(legalName, businessName, taxId, contactEmail, contactPhone, contactPersonName, description);
        this.partner = newPartner;
    }

    // --- Updates ---

    public void updateInformation(UpdateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.age = command.age();
        this.email = command.email();
    }

    // --- Type checking methods ---

    /**
     * Checks if profile is assigned as learner
     */
    public boolean isLearner() {
        return this.learner != null;
    }

    /**
     * Checks if profile is assigned as partner
     */
    public boolean isPartner() {
        return this.partner != null;
    }

    /**
     * Checks if profile has no type assignment yet
     */
    public boolean isUnassigned() {
        return this.learner == null && this.partner == null;
    }

    // --- Utility methods ---

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Gets the primary language from learner's languages (if learner), otherwise null
     */
    public String getLanguage() {
        // TODO: Implement language management for learners
        return null;
    }

    /**
     * Gets the primary level from learner's languages (if learner), otherwise null
     */
    public String getLevel() {
        // TODO: Implement CEFR level management for learners
        return null;
    }

    /**
     * Gets the country from learner's address if learner, otherwise null
     * Partners don't manage addresses as they represent business relationships
     */
    public String getCountry() {
        if (this.learner != null) {
            return this.learner.getCountry();
        }
        return null;
    }

}
