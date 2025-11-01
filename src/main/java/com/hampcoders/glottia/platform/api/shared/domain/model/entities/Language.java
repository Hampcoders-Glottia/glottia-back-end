package com.hampcoders.glottia.platform.api.shared.domain.model.entities;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Language Entity
 * Catalog entity representing a language.
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "languages")
public class Language {

    /**
     * Unique identifier for the Language.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the Language.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private Languages name;    

    /**
     * Constructor to create Language from Languages enum.
     * @param name The Languages enum value.
     * @return Language instance.
     */
    public Language(Languages name) {
        this.name = name;
    }

    /**
     * Get the string name of the Language.
     * @return String name
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the integer value of the Language.
     * @return int value
     */
    public int getNameValue() {
        return name.getValue();
    }

    /**
     * Convert from String name to Language entity.
     * @param name
     * @return Language instance
     */
    public static Language toLanguageFromName(String name) {
        return new Language(Languages.valueOf(name));
    }

    /**
     * Convert from Long id to Language entity.
     * @param id
     * @return Language instance
     */
    public static Language toLanguageFromId(Long id) {
        return new Language(Languages.fromValue(id.intValue()));
    }


}