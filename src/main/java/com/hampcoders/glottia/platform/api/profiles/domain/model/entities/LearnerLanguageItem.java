package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * LearnerLanguageItem entity representing a language that a learner is learning or knows.
 * @summary
 * Includes relationships to CEFRLevel, Learner, and Language entities.
 * Provides methods to update the CEFR level and learning status.
 * Includes convenience methods to get the language name and CEFR level name.
 * Extends AuditableModel to include createdAt and updatedAt timestamps.
 */
@Getter
@Entity
@Table(name = "learner_languages")
public class LearnerLanguageItem extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The CEFR level associated with this learner language item.
     */
    @NotNull
    @OneToOne
    @JoinColumn(name = "cefr_level_id", nullable = false)   
    private CEFRLevel cefrLevel;

    /**
     * The learner associated with this language item.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "learner_id", nullable = false)
    private Learner learner;
    
    /**
     * The language associated with this learner language item.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    /**
     * Indicates whether the learner is currently learning this language.
     */
    @NotNull
    @Column(name = "is_learning", nullable = false)
    private boolean isLearning;

    /**
     * Default constructor for JPA.
     */
    protected LearnerLanguageItem() {
    }

    /**
     * Constructor to create a LearnerLanguageItem with specified learner, language, CEFR level, and learning status.
     * @param learner
     * @param language
     * @param cefrLevel
     * @param isLearning
     */
    public LearnerLanguageItem(Learner learner, Language language, CEFRLevel cefrLevel, boolean isLearning) {
        this.learner = learner;
        this.language = language;
        this.cefrLevel = cefrLevel;
        this.isLearning = isLearning;
    }

    /**
     * Updates the CEFR level of this learner language item.
     * @param newLevel
     */
    public void updateLevel(CEFRLevel newLevel) {
        this.cefrLevel = newLevel;
    }

    /**
     * Updates the learning status of this learner language item.
     * @param isLearning
     */
    public void updateLearningStatus(boolean isLearning) {
        this.isLearning = isLearning;
    }

    /**
     * Gets the name of the language associated with this learner language item.
     * @return the language name
     */
    public String getLanguageName() {
        return language.getStringName();
    }

    /**
     * Gets the name of the CEFR level associated with this learner language item.
     * @return the CEFR level name  
     */
    public String getCefrLevelName() {
        return cefrLevel.getStringName();
    }
}