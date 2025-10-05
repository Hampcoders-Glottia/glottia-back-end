package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.CEFRLevels;

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
@Table(name = "cefr_levels")
public class CEFRLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="level",length = 20)
    private CEFRLevels level;

    public CEFRLevel(CEFRLevels level) {
        this.level = level;
    }

    public String getStringCefrLevelName() {
        return level.name();
    }

    static CEFRLevel toCefrLevelFromName(String name) {
        return new CEFRLevel(CEFRLevels.valueOf(name));
    }

}
