package org.cellcore.code.exec.db;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class FetchUpdateData {


    @Autowired
    ApplicationContext context;

    @Transactional
    public void update(String url, Class<? extends AbstractPageDataExtractor> tClass) {
        AbstractPageDataExtractor extractor = context.getBean(tClass);
        try {
            extractor.extractFromPage(url);
        } catch (IOException e) {
            System.err.println("problem downloading : " + url);
        } catch (UnsupportedCardException e) {
            System.err.println("bad card for " + url);
        }
    }
}
