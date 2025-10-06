package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import java.util.List;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.LearnerLanguage;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "learners")
public class Learner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Address information
    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Embedded
    private final LearnerLanguage learnerLanguages;

    // Constructor
    public Learner() {
        this.learnerLanguages = new LearnerLanguage();
    }
   
    
    public Learner(String street, String number, String city, String postalCode, 
                   String country, Float latitude, Float longitude) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.learnerLanguages = new LearnerLanguage();
    }

    // Update address information
    public void updateAddress(String street, String number, String city, 
                             String postalCode, String country, Float latitude, Float longitude) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFullAddress() {
        return String.format("%s %s, %s, %s %s", 
            street != null ? street : "",
            number != null ? number : "",
            city != null ? city : "",
            postalCode != null ? postalCode : "",
            country != null ? country : "").trim();
    }

    public void addLanguage(Language language, CEFRLevel cefrLevel, boolean isLearning) {
        this.learnerLanguages.addLanguages(List.of(new LearnerLanguageItem(this, language, cefrLevel, isLearning)));
    }

    public void addLanguages(List<LearnerLanguageItem> items) {
        this.learnerLanguages.addLanguages(items);
    }

    public void removeLanguage(Language language) {
        this.learnerLanguages.removeLanguage(language);
    }

    public void updateLanguageLevel(Language language, CEFRLevel newLevel) {
        this.learnerLanguages.updateLanguageLevel(language, newLevel);
    }

    public void updateLanguageLearningStatus(Language language, boolean isLearning) {
        this.learnerLanguages.updateLanguageLearningStatus(language, isLearning);
    }

    public List<LearnerLanguageItem> getLearnerLanguageItems() {
        return learnerLanguages.getLearnerLanguageItems();
    }

    public List<Language> getKnownLanguages() {
        return learnerLanguages.getKnownLanguages();
    }

    public List<Language> getLanguagesCurrentlyLearning() {
        return learnerLanguages.getLanguagesCurrentlyLearning();
    }

    public boolean hasLanguage(Language language) {
        return learnerLanguages.hasLanguage(language);
    }

    public int getTotalLanguages() {
        return learnerLanguages.getTotalLanguages();
    }
}