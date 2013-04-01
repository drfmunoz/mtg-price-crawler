package org.cellcore.code.model;

import org.cellcore.code.shared.GsonExclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Copyright 2013 Freddy Munoz (freddy@cellcore.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ==============================================================
 */
@Entity
@Table(name = "CARD_NAME")
public class CardName extends AbstractJPAEntity {

    private String translatedLang;

    private String language;

    @Column(columnDefinition = "VARCHAR(1000)")
    @org.hibernate.annotations.Index(name = "name_lg_index")
    private String name;
    private String multiverseId;

    @GsonExclude
    @ManyToOne
    private Card card;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMultiverseId() {
        return multiverseId;
    }

    public void setMultiverseId(String multiverseId) {
        this.multiverseId = multiverseId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getTranslatedLang() {
        return translatedLang;
    }

    public void setTranslatedLang(String translatedLang) {
        this.translatedLang = translatedLang;
    }

    @Override
    public String toString() {
        return "CardName{" +
                "translatedLang='" + translatedLang + '\'' +
                ", language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", multiverseId='" + multiverseId + '\'' +
                '}';
    }
}
