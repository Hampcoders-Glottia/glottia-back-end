package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import java.util.List;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.LearnerLanguage;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "learners")
public class Learner extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private final LearnerLanguage learnerLanguage;

    protected Learner() {
        this.learnerLanguage = new LearnerLanguage();
    }
    
    public Learner(CreateLearnerCommand command) {
        this();
        this.address = command.address();
    }

    public Learner(Address address) {
        this.address = address;
        this.learnerLanguage = new LearnerLanguage();   
    }

    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }

    public void addLanguage(Language language, CEFRLevel cefrLevel, boolean isLearning) {
        this.learnerLanguage.addLanguage(this, language, cefrLevel, isLearning);
    }

    public void addLanguages(List<LearnerLanguageItem> languageItems) {
        this.learnerLanguage.addLanguages(languageItems);
    }

    public List<LearnerLanguageItem> getLanguages() {
        return this.learnerLanguage.getLearnerLanguageItems();
    }

    public void removeLanguage(Language language) {
        this.learnerLanguage.removeLanguage(language);
    }

    public void updateLanguageLevel(Language language, CEFRLevel newLevel) {
        this.learnerLanguage.updateLanguageLevel(language, newLevel);
    }

    public void updateLanguageLearningStatus(Language language, boolean isLearning) {
        this.learnerLanguage.updateLanguageLearningStatus(language, isLearning);
    }

    public List<LearnerLanguageItem> getLearnerLanguageItems() {
        return learnerLanguage.getLearnerLanguageItems();
    }

    public List<LearnerLanguageItem> getLanguageItemsByLevel(CEFRLevel level) {
        return learnerLanguage.getLanguageItemsByLevel(level);
    }

    public List<Language> getKnownLanguages() {
        return learnerLanguage.getKnownLanguages();
    }

    public List<Language> getLanguagesCurrentlyLearning() {
        return learnerLanguage.getLanguagesCurrentlyLearning();
    }

    public boolean hasLanguage(Language language) {
        return learnerLanguage.hasLanguage(language);
    }

    public int getTotalLanguages() {
        return learnerLanguage.getTotalLanguages();
    }

    public String getFullAddress() {
        return this.address.toString();
    }
}
