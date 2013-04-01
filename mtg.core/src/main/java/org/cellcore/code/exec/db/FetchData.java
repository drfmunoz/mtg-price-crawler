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
