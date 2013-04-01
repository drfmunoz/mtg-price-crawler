package org.cellcore.code.model;

import org.cellcore.code.shared.GsonExclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Table(name = "CARD")
public class Card extends AbstractJPAEntity {

    @Column(columnDefinition = "VARCHAR(1000)")
    @org.hibernate.annotations.Index(name = "name_index")
    private String name;

    @Column(columnDefinition = "VARCHAR(1000)")
    @org.hibernate.annotations.Index(name = "iname_index")
    private String iName;

    private String multiverseId;

    @Transient
    private String[] otherSearchNames;

    @GsonExclude
    @ManyToMany
    private Set<CardSet> sets;

    @OneToMany(mappedBy = "card", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<CardName> names;


    @OneToMany(mappedBy = "card", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CardPriceSource> sources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardPriceSource> getSources() {
        if (this.sources == null) {
            this.sources = new ArrayList<CardPriceSource>();
        }
        return sources;
    }

    public void setSources(List<CardPriceSource> sources) {
        this.sources = sources;
    }

    public Set<CardSet> getSets() {
        if (sets == null) {
            sets = new HashSet<CardSet>();
        }
        return sets;
    }

    public void setSets(Set<CardSet> set) {
        this.sets = set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (iName != null ? !iName.equals(card.iName) : card.iName != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sets != null ? sets.hashCode() : 0);
        return result;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public String getMultiverseId() {
        return multiverseId;
    }

    public void setMultiverseId(String multiverseId) {
        this.multiverseId = multiverseId;
    }

    public Set<CardName> getNames() {
        if (names == null) {
            names = new HashSet<CardName>();
        }
        return names;
    }

    public void setNames(Set<CardName> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", iName='" + iName + '\'' +
                ", multiverseId='" + multiverseId + '\'' +
                ", names=" + names +
                ", sources=" + sources +
                '}';
    }


    public String[] getOtherSearchNames() {
        return otherSearchNames;
    }

    public void setOtherSearchNames(String[] otherSearchNames) {
        this.otherSearchNames = otherSearchNames;
    }
}
