package org.cellcore.code.exec.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.engine.page.GathererDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.model.CardSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class FetchGathererData {

    @Autowired
    private GeneralDao generalDao;

    private static final Log logger = LogFactory.getLog(FetchGathererData.class);

    @Transactional
    public void extract(String setName, List<String> cardIds) {
        CardSet set = null;

        List<CardSet> sets = generalDao.read(CardSet.class, "name", setName);

        if (sets.isEmpty()) {
            set = new CardSet();
            set.setName(setName);
            generalDao.save(set);
        } else {
            set = sets.get(0);
        }
        GathererDataExtractor extractor = new GathererDataExtractor();

        for (String cardId : cardIds) {
            try {
                Card c = extractor.getCard(cardId);
                List<Card> existing = generalDao.read(Card.class, "iName", c.getiName());
                /**
                 * enforce that there is only one card
                 */
                if (!existing.isEmpty()) {
                    logger.info("existing card");

                    Card d = existing.get(0);
                    if(!d.getMultiverseId().equals(c.getMultiverseId())){
                        logger.info("updating multiverse ID");
                        d.setMultiverseId(c.getMultiverseId());
//                        d.getNames().clear();
//                        d.setNames(c.getNames());
//                        logger.info("updating otherNames");
//                        for(CardName cname:d.getNames()){
//                            cname.setCard(d);
//                            generalDao.save(cname);
//                        }
                    }
                    c=d;
                }
                if (!set.getCards().contains(c)) {
                    set.getCards().add(c);
                    c.getSets().add(set);
                }
                logger.info("DONE: " + c.getName());
            } catch (Throwable e) {
                logger.error("ERROR PROCESSING ID " + cardId, e);
            }
        }
        if (sets.isEmpty()) {
            generalDao.merge(set);
        }
    }

    @Transactional
    public void extractSingle(String setName, final String cardId) {
        extract(setName, new ArrayList<String>() {{
            add(cardId);
        }});
    }
}
