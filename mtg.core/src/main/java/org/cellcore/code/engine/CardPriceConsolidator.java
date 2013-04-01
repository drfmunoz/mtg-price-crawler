package org.cellcore.code.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.model.Card;
import org.cellcore.code.model.CardName;
import org.cellcore.code.model.CardPrice;
import org.cellcore.code.model.CardPriceSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Hashtable;
import java.util.List;


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
@Component
public class CardPriceConsolidator {


    @Autowired
    private GeneralDao generalDao;

    private static final Log logger = LogFactory.getLog(CardPriceConsolidator.class);

    private Hashtable<String, Card> globalCards = new Hashtable<String, Card>();

    @Transactional
    public void conciliate(Card card) {
        Card dbCard = null;
        if (card.getiName().contains("/")) {
            String[] names = card.getiName().split("/");
            logger.info("TWO SIDE CARD FOUND: " + card.getName());
            for (String name : names) {
                Card c = new Card();
                c.setSources(card.getSources());
                c.setName(name);
                c.setiName(name.trim());
                conciliate(c);
            }
        } else {
            List<Card> cards = generalDao.read(Card.class, "iName", card.getiName());
            if (cards.size() > 0) {
                dbCard = cards.get(0);
            } else {
                String[] otherNames = card.getOtherSearchNames();
                if (otherNames != null) {
                    for (String otherName : otherNames) {
                        List<CardName> cardNames = generalDao.read(CardName.class, "name", otherName);
                        if (!cardNames.isEmpty()) {
                            card = cardNames.get(0).getCard();
                            break;
                        }
                    }
                }
            }
            if (dbCard != null) {
                updateCardSources(dbCard, card);
            } else {
                logger.info("NO CARD FOUND: " + card.getName());
            }
        }
    }

    private void updateCardSources(Card card, Card card1) {
        logger.info("UPDATE CARD: " + card.getName());
        CardPriceSource dbPriceSource = card1.getSources().get(0);

        if (!card.getSources().contains(dbPriceSource)) {
            card.getSources().add(dbPriceSource);
            dbPriceSource.setCard(card);
            for (CardPrice price : dbPriceSource.getPriceSet()) {
                price.setSource(dbPriceSource);
                dbPriceSource.setLastPrice(price.getPrice());
                dbPriceSource.setLastStock(price.getStock());
                dbPriceSource.setLastUpdate(price.getFetchDate());
            }
            logger.info("ADDING A NEW SOURCE PRICE: " + dbPriceSource.getLastPrice() + " (" + dbPriceSource.getSourceType() + ")");
        } else {

            CardPriceSource memCardPrice = card1.getSources().get(0);
            dbPriceSource = card.getSources().get(card.getSources().indexOf(dbPriceSource));
            dbPriceSource.setSourceName(dbPriceSource.getSourceType().getPrintName());

            for (CardPrice memPrice : memCardPrice.getPriceSet()) {
                logger.info("UPDATE PRICE: " + memPrice.getPrice() + " (" + dbPriceSource.getSourceType() + ")");
                CardPrice p = new CardPrice();
                p.setSource(dbPriceSource);
                dbPriceSource.getPriceSet().add(p);
                p.setPrice(memPrice.getPrice());
                p.setStock(memPrice.getStock());
                p.setFetchDate(memPrice.getFetchDate());

                if (dbPriceSource.getLastUpdate().getTime() < memPrice.getFetchDate().getTime()) {
                    /**
                     * fixes the problem of cards with many sources.... and different prices.
                     */
                    boolean update = false;
                    if (!memCardPrice.getUrl().equals(dbPriceSource.getUrl())) {
                        if (dbPriceSource.getLastPrice() > memPrice.getPrice()) {
                            update = true;
                        }
                    } else {
                        update = true;
                    }
                    if (update) {
                        logger.info("UPDATE LAST PRICE: " + memPrice.getPrice() + " (" + memPrice.getFetchDate() + ")");
                        dbPriceSource.setLastPrice(memPrice.getPrice());
                        dbPriceSource.setUrl(memCardPrice.getUrl());
                    }
                    dbPriceSource.setLastStock(memPrice.getStock());
                    dbPriceSource.setLastUpdate(memPrice.getFetchDate());
                }
            }
        }
    }

    public void conciliateGlobal(Card card) {
        if (!this.globalCards.containsKey(card.getiName())) {
            this.globalCards.put(card.getiName(), card);
        } else {
            Card iCard = this.globalCards.get(card.getiName());
            if (card.getSources().get(0).getLastPrice() < iCard.getSources().get(0).getLastPrice() && card.getSources().get(0).getLastPrice() > 0) {
                this.globalCards.put(card.getiName(), card);
            }
        }
    }

    public Hashtable<String, Card> getGlobalCards() {
        return globalCards;
    }

}
