package org.cellcore.code.model;

import org.cellcore.code.shared.GsonExclude;

import javax.persistence.*;
import java.util.Date;

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
@Table(name = "CARD_PRICE")
public class CardPrice extends AbstractJPAEntity {

    private float price;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fetchDate;

    private int stock=-1;

    @GsonExclude
    @ManyToOne
    private CardPriceSource source;

    public CardPriceSource getSource() {
        return source;
    }

    public void setSource(CardPriceSource source) {
        this.source = source;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getFetchDate() {
        return fetchDate;
    }

    public void setFetchDate(Date fetchDate) {
        this.fetchDate = fetchDate;
    }

    @Override
    public String toString() {
        return "CardPrice{" +
                "price=" + price +
                ", fetchDate=" + fetchDate +
                '}';
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
