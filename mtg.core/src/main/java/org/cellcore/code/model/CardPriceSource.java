package org.cellcore.code.model;

import org.cellcore.code.shared.GsonExclude;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
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
@Table(name = "CARD_PRICE_SOURCE")
public class CardPriceSource extends AbstractJPAEntity {

    @Enumerated(EnumType.STRING)
    private PriceSources sourceType;

    private String sourceName;

    /**
     * by default the currency is euros
     */
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.EUR;

    @Column(columnDefinition = "VARCHAR(300)")
    private String url;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @ManyToOne
    @GsonExclude
    private Card card;

    @OneToMany(mappedBy = "source", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private Set<CardPrice> priceSet;
    private float lastPrice;

    private int lastStock=-1;

    public PriceSources getSourceType() {
        return sourceType;
    }

    public void setSourceType(PriceSources sourceType) {
        this.sourceType = sourceType;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<CardPrice> getPriceSet() {
        if (priceSet == null) {
            priceSet = new HashSet<CardPrice>();
        }
        return priceSet;
    }

    public void setPriceSet(Set<CardPrice> priceSet) {
        this.priceSet = priceSet;
    }

    @Override
    public String toString() {
        return "CardPriceSource{" +
                "sourceType=" + sourceType +
                ", url='" + url + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", priceSet=" + priceSet +
                '}';
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardPriceSource)) return false;

        CardPriceSource that = (CardPriceSource) o;

        if (sourceType != that.sourceType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceType != null ? sourceType.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public int getLastStock() {
        return lastStock;
    }

    public void setLastStock(int lastStock) {
        this.lastStock = lastStock;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
