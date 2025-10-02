package com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.StreetAddress;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.NivelCefr;
import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Entity
@Table(name = "profiles")
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    @Getter
    @NotNull
    @NotBlank
    @Column(name = "full_name", length = 50, nullable = false)
    private String fullName;

    @Getter
    @Min(0)
    @Max(100)
    @Column(name = "age", columnDefinition = "smallint", nullable = false)
    private int age;

    @Email
    @NotNull
    @NotBlank
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", length = 20, nullable = false)
    private Languages language;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", length = 10, nullable = false)
    private NivelCefr level;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "street", column = @Column(name = "address_street", length = 100, nullable = false))
    })
    private StreetAddress address;

    // ---------------------------------------------------
    public Profile(String fullName, int age, String email, Languages language, NivelCefr level, String country) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.language = language;
        this.level = level;
        this.address = new StreetAddress(country);
    }

    public Profile() {}

    public void updateStreetAddress(String country) {
        this.address = new StreetAddress(country);
    }

    public String getAddress() { return address.street(); }

    public String getCountry() { return address.street(); }

    public String getEmail() { return email; }

    public String getLanguage() { return language.name(); }

    public String getLevel() { return level.name(); }

    // ---------------------------------------------------
    public Profile(CreateProfileCommand command) {
        this.fullName = command.name();
        this.age = command.age();
        this.email = command.email();
        this.language = parseLanguage(command.language());
        this.level = parseLevel(command.level());
        this.address = new StreetAddress(command.country());
    }

    public Profile updateInformation(String fullName, int age, String email, String language, String level, String country) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.language = parseLanguage(language);
        this.level = parseLevel(level);
        this.address = new StreetAddress(country);
        return this;
    }

    private Languages parseLanguage(String value) {
        return Languages.valueOf(value.toUpperCase());
    }

    private NivelCefr parseLevel(String value) {
        return NivelCefr.valueOf(value.toUpperCase());
    }
}
