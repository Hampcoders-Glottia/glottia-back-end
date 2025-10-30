package com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates;

import java.util.List;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.LearnerLanguage;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;

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

    public Learner() {
        this.learnerLanguage = new LearnerLanguage();

    }

    public Learner(CreateLearnerCommand command) {
        this();
        /* this.address = command.address(); */ 
    }

    public void addLanguage(Language language, CEFRLevel cefrLevel, boolean isLearning) {
        /*this.learnerLanguage.addLearnerLanguageItem(this, language, cefrLevel, isLearning);*/
    }

    public void addLanguages(List<LearnerLanguageItem> languageItems) {
        /*  this.learnerLanguage.addItems(languageItems); */
    }

    public List<LearnerLanguageItem> getLanguages() {
        return this.learnerLanguage.getLearnerLanguageItems();
    }

    public void removeLanguage(Language language) {
        this.learnerLanguage.removeLanguage(language);
    }
}
