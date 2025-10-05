package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

@ToString
@Embeddable
public class LearnerLanguage {

    @Getter
    @OneToMany(mappedBy = "learner", cascade = CascadeType.ALL)
    private List<LearnerLanguageItem> learnerLanguageItems;

    public LearnerLanguage() {
        this.learnerLanguageItems = new ArrayList<>();
    }

    public void addLearnerLanguageItem(Learner learner, Language language, CEFRLevel cefrLevel, boolean isLearning) {
        this.learnerLanguageItems.add(new LearnerLanguageItem(learner, language, cefrLevel, isLearning));
    }

    public void addItems(List<LearnerLanguageItem> languageItems) {
        this.learnerLanguageItems.addAll(languageItems);
    }

    public void removeLanguage(Language language) {
        this.learnerLanguageItems.removeIf(item -> item.getLanguage().equals(language));
    }
}