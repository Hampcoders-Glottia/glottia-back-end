package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Languages;

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

@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="language",length = 20, unique = true)
    private Languages language;

    public Language(Languages language) {
        this.language = language;
    }

    public String getStringLanguageName() {
        return language.name();
    }

    static Language toLanguageFromName(String name) {
        return new Language(Languages.valueOf(name));
    }
    

}
