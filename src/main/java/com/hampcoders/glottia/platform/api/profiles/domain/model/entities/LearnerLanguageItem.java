package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;

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

@Getter
@Entity
@Table(name = "learner_languages")
public class LearnerLanguageItem extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "cefr_level_id", nullable = false)   
    private CEFRLevel cefrLevel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "learner_id", nullable = false)
    private Learner learner;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @NotNull
    @Column(name = "is_learning", nullable = false)
    private boolean isLearning;

    public LearnerLanguageItem() {
    }

    public LearnerLanguageItem(Learner learner, Language language, CEFRLevel cefrLevel, boolean isLearning) {
        this.learner = learner;
        this.language = language;
        this.cefrLevel = cefrLevel;
        this.isLearning = isLearning;
    }

    public void updateLevel(CEFRLevel newLevel) {
        this.cefrLevel = newLevel;
    }

    public void updateLearningStatus(boolean isLearning) {
        this.isLearning = isLearning;
    }

    public String getLanguageName() {
        return language.getStringLanguageName();
    }

    public String getCefrLevelName() {
        return cefrLevel.getStringCefrLevelName();
    }
}