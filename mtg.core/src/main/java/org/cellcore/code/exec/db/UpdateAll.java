package org.cellcore.code.exec.db;


import org.cellcore.code.config.ContextConfig;
import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.model.CardPriceSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class UpdateAll {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        GeneralDao dao = context.getBean(GeneralDao.class);
        FetchUpdateData updateData = context.getBean(FetchUpdateData.class);
        List<CardPriceSource> cards = dao.list(CardPriceSource.class);
        for (CardPriceSource card : cards) {
            updateData.update(card.getUrl(), card.getSourceType().getPageDataExtractorClass());
        }
    }
}
