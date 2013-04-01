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
