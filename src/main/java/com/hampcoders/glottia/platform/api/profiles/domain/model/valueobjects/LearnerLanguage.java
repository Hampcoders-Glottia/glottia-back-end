package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Embeddable
public class LearnerLanguage {

    @Getter
    @OneToMany(mappedBy = "learner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearnerLanguageItem> learnerLanguageItems;

    public LearnerLanguage() {

        this.learnerLanguageItems = new ArrayList<>();
    }

    public void addLanguage(Learner learner, Language language, CEFRLevel cefrLevel, boolean isLearning) {
        if (hasLanguage(language)) 
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + " already exists for this learner");
        
        this.learnerLanguageItems.add(new LearnerLanguageItem(learner, language, cefrLevel, isLearning));
    }

    public void addLanguages(List<LearnerLanguageItem> items) {
        this.learnerLanguageItems.addAll(items);
    }

    public void removeLanguage(Language language) {
        this.learnerLanguageItems.removeIf(item -> item.getLanguage().getId().equals(language.getId()));
    }

    public boolean hasLanguage(Language language) {
        return this.learnerLanguageItems.stream()
                .anyMatch(item -> item.getLanguage().equals(language));
    }

    public void updateLanguageLevel(Language language, CEFRLevel newLevel) {
        var item = getLanguageItem(language);
        if (item.isPresent()) {
            item.get().updateLevel(newLevel);
        } else {
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + " not found for this learner");
        }
    }

    public void updateLanguageLearningStatus(Language language, boolean isLearning) {
        var item = getLanguageItem(language);
        if (item.isPresent()) {
            item.get().updateLearningStatus(isLearning);
        } else {
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + " not found for this learner");
        }
    }

    public Optional<LearnerLanguageItem> getLanguageItem(Language language) {
        return this.learnerLanguageItems.stream()
                .filter(item -> item.getLanguage().equals(language))
                .findFirst();
    }   

    public List<Language> getKnownLanguages() {
        return learnerLanguageItems.stream()
                .map(LearnerLanguageItem::getLanguage)
                .toList();
    }

    public List<Language> getLanguagesCurrentlyLearning() {
        return learnerLanguageItems.stream()
            .filter(LearnerLanguageItem::isLearning)
            .map(LearnerLanguageItem::getLanguage)
            .toList();
    }

    public List<LearnerLanguageItem> getLanguageItemsByLevel(CEFRLevel level) {
        return learnerLanguageItems.stream()
            .filter(item -> item.getCefrLevel().getId().equals(level.getId()))
            .toList();
    }

    public int getTotalLanguages() {
        return learnerLanguageItems.size();
    }

    public boolean isEmpty() {
        return learnerLanguageItems.isEmpty();
    }
}