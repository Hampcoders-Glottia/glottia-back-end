package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

/**
 * Value object representing the languages associated with a learner
 * @summary
 * including their proficiency levels and learning status.
 * It encapsulates a list of LearnerLanguageItem entities.
 */
@ToString
@Getter
@Embeddable
public class LearnerLanguage {

    /**
     * List of LearnerLanguageItem entities associated with the learner.
     * Each item represents a language, its CEFR level, and whether the learner is currently learning it.
     */
    @Getter
    @OneToMany(mappedBy = "learner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearnerLanguageItem> learnerLanguageItems;

    /**
     * Default constructor initializing the learnerLanguageItems list.
     */
    public LearnerLanguage() {
        this.learnerLanguageItems = new ArrayList<>();
    }

    /**
     * Adds a new language to the learner's profile.
     * @param learner The learner to whom the language is being added.
     * @param language The language being added.
     * @param cefrLevel The CEFR level of the language.
     * @param isLearning Indicates whether the learner is currently learning this language.
     */
    public void addLanguage(Learner learner, Language language, CEFRLevel cefrLevel, boolean isLearning) {
        if (hasLanguage(language))
            throw new IllegalArgumentException("Language " + language.getStringName() + " already exists for this learner");

        this.learnerLanguageItems.add(new LearnerLanguageItem(learner, language, cefrLevel, isLearning));
    }

    /**
     * Adds multiple language items to the learner's profile.
     * @param items List of LearnerLanguageItem entities to be added.
     */
    public void addLanguages(List<LearnerLanguageItem> items) {
        this.learnerLanguageItems.addAll(items);
    }

    /**
     * Removes a language from the learner's profile.
     * @param language The language to be removed.
     */
    public void removeLanguage(Language language) {
        this.learnerLanguageItems.removeIf(item -> item.getLanguage().getId().equals(language.getId()));
    }

    /**
     * Checks if the learner already has a specific language.
     * @param language The language to check.
     * @return True if the learner has the language, false otherwise.
     */
    public boolean hasLanguage(Language language) {
        return this.learnerLanguageItems.stream()
                .anyMatch(item -> item.getLanguage().equals(language));
    }

    /**
     * Updates the CEFR level of a specific language for the learner.
     * @param language The language whose level is to be updated.
     * @param newLevel The new CEFR level to be set.
     */
    public void updateLanguageLevel(Language language, CEFRLevel newLevel) {
        var item = getLanguageItem(language);
        if (item.isPresent()) {
            item.get().updateLevel(newLevel);
        } else {
            throw new IllegalArgumentException("Language " + language.getStringName() + " not found for this learner");
        }
    }

    /**
     * Updates the learning status of a specific language for the learner.
     * @param language The language whose learning status is to be updated.
     * @param isLearning The new learning status to be set.
     */
    public void updateLanguageLearningStatus(Language language, boolean isLearning) {
        var item = getLanguageItem(language);
        if (item.isPresent()) {
            item.get().updateLearningStatus(isLearning);
        } else {
            throw new IllegalArgumentException("Language " + language.getStringName() + " not found for this learner");
        }
    }

    /**
     * Gets the LearnerLanguageItem associated with a specific CEFR level.
     * @param level The CEFR level.
     * @return An Optional containing the LearnerLanguageItem if found, or empty if not found.
     */
    public Optional<LearnerLanguageItem> getCEFRLevel(CEFRLevel level) {
        return this.learnerLanguageItems.stream()
                .filter(item -> item.getCefrLevel().equals(level))
                .findFirst();
    }

    /**
     * Gets the LearnerLanguageItem associated with a specific language.
     * @param language The language.
     * @return An Optional containing the LearnerLanguageItem if found, or empty if not found.
     */
    public Optional<LearnerLanguageItem> getLanguageItem(Language language) {
        return this.learnerLanguageItems.stream()
                .filter(item -> item.getLanguage().equals(language))
                .findFirst();
    }   

    /**
     * Gets a list of all languages known by the learner.
     * @return A list of Language entities known by the learner.
     */
    public List<Language> getKnownLanguages() {
        return learnerLanguageItems.stream()
                .map(LearnerLanguageItem::getLanguage)
                .toList();
    }

    /**
     * Gets a list of all languages the learner is currently learning.
     * @return A list of Language entities the learner is currently learning.
     */
    public List<Language> getLanguagesCurrentlyLearning() {
        return learnerLanguageItems.stream()
            .filter(LearnerLanguageItem::isLearning)
            .map(LearnerLanguageItem::getLanguage)
            .toList();
    }

    /**
     * Gets a list of LearnerLanguageItem entities filtered by a specific CEFR level.
     * @param level The CEFR level to filter by.
     * @return A list of LearnerLanguageItem entities matching the specified CEFR level.
     */
    public List<LearnerLanguageItem> getLanguageItemsByLevel(CEFRLevel level) {
        return learnerLanguageItems.stream()
            .filter(item -> item.getCefrLevel().getId().equals(level.getId()))
            .toList();
    }

    /**
     * Gets the total number of languages associated with the learner.
     * @return The total number of languages.
     */
    public int getTotalLanguages() {
        return learnerLanguageItems.size();
    }

    /**
     * Checks if the learner has no associated languages.
     * @return True if there are no languages, false otherwise.
     */
    public boolean isEmpty() {
        return learnerLanguageItems.isEmpty();
    }
}