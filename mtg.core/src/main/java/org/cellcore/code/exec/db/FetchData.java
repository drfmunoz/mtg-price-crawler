package org.cellcore.code.exec.db;

import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.model.CardSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
public class FetchData {
    @Autowired
    GeneralDao generalDao;

    @Autowired
    ApplicationContext context;

    @Transactional
    public void fetch(Map<String, String> editions, AbstractEditionLocationCrawler crawler, Class<? extends AbstractPageDataExtractor> tClass) {

        AbstractPageDataExtractor extractor = context.getBean(tClass);
        for (Map.Entry<String, String> entry : editions.entrySet()) {
            try {
                CardSet set = null;
                List<CardSet> sets = generalDao.read(CardSet.class, "name", entry.getKey());
                if (sets.isEmpty()) {
                    set = new CardSet();
                    set.setName(entry.getKey());
                    generalDao.save(set);
                } else {
                    set = sets.get(0);
                }

                List<String> cards = crawler.crawlPage(entry.getValue());

                for (String card : cards) {
                    try {
                        Card c = extractor.extractFromPage(card);
                        if (!set.getCards().contains(c)) {
                            set.getCards().add(c);
                            c.getSets().add(set);
                            System.out.println("new: " + c.getName());
                        } else {
                            System.out.println(c.getName());
                        }
                    } catch (Throwable e) {
                        System.err.println("failed card:" + card);
                        //  e.printStackTrace();
                    }
                }
                if (sets.isEmpty()) {
                    generalDao.merge(set);
                }

            } catch (Throwable e) {
                System.err.println("ERROR LOADING CARD SET " + entry.getKey() + " FROM " + entry.getValue());
                e.printStackTrace();
            }
        }
    }
}
