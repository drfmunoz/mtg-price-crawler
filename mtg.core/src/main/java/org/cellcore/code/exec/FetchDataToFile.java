package org.cellcore.code.exec;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Author: Freddy Munoz (freddy@cellcore.org)
 *
 *
 */
public class FetchDataToFile {

    final Gson gson = GsonUtils.getSerializer();

    private static final Log logger = LogFactory.getLog(FetchDataToFile.class);

    private void fetchCard(String cardUrl, Set<Card> cards, Set<String> cardErrors, AbstractPageDataExtractor extractor,
                           int cardCount, int cardIndex, int retry, long sleep) {
        try {
            Card c = extractor.extractFromPage(cardUrl);
            logger.info("(" + cardIndex + " / " + cardCount + ")" + " CARD FETCHED: " + c.getName()
                    + " (" + (c.getSources().get(0).getPriceSet() != null
                    ? c.getSources().get(0).getPriceSet().iterator().next() : "-")
                    + ") - " + cardUrl);
            cards.add(c);
            if(sleep>0){
                Thread.sleep(sleep);
            }
        } catch (Throwable e) {
            if(e instanceof UnsupportedCardException){
                logger.error("UNSUPPORTED CARD",e);
                return;
            }

            if (retry > 3) {
                logger.error("GIVING UP ON " + cardUrl);
                logger.error("(" + cardIndex + " / " + cardCount + ")" + " CARD FETCH FAILED :" + cardUrl, e);
                cardErrors.add(cardUrl);
            } else {
                long sleep_ = 1000 * (long) Math.pow(3, (retry + 1));
                logger.info(retry + " RETYING (after " + sleep_ + "): " + cardUrl);
                try {

                    Thread.sleep(sleep_);
                } catch (InterruptedException e1) {
                    logger.error("SOMETHING WENT TERRIBLY WRONG....  " + cardUrl);
                }
                this.fetchCard(cardUrl, cards, cardErrors, extractor, cardCount, cardIndex, retry + 1,sleep);
            }
        }
    }


    private String[] fetchData(Map.Entry<String, String> entry, AbstractEditionLocationCrawler crawler, AbstractPageDataExtractor extractor, long sleep) {
        Set<Card> cards = new HashSet<Card>();
        Set<String> cardErrors = new HashSet<String>();
        try {
            List<String> cardUrls = crawler.crawlPage(entry.getValue());
            int j = 0;
            int cardCount = cardUrls.size();
            for (String cardUrl : cardUrls) {
                this.fetchCard(cardUrl, cards, cardErrors, extractor, cardCount, j, 0,sleep);
                j++;
            }
        } catch (Throwable e) {
            logger.error("ERROR CRAWLING: " + entry.getValue(), e);
        }
        logger.info("DONE: " + entry.getKey());
        String data = gson.toJson(cards);
        String errors = gson.toJson(cardErrors);
        return new String[]{ data, errors };
    }


    public void fetch(Map<String, String> editions, AbstractEditionLocationCrawler crawler, AbstractPageDataExtractor extractor, File path, long sleep) throws IOException {
        Date d = new Date();
        File ibase = FileUtils.getFile(path, extractor.getSource().toString());
        int i = 1;
        for (Map.Entry<String, String> entry : editions.entrySet()) {
            logger.info("(" + i + " / " + editions.size() + ")" + " PROCESSING " + entry.getKey() + " -> " + entry.getValue());
            File pbase = FileUtils.getFile(ibase, entry.getKey());
            if (!pbase.exists()) {
                pbase.mkdirs();
            }

            File out = FileUtils.getFile(pbase, "data-" + d.getTime() + ".data.json");
            File err = FileUtils.getFile(pbase, "error-" + d.getTime() + ".error.json");
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            BufferedWriter ba = new BufferedWriter(new FileWriter(err));
            try {
                String[] data = this.fetchData(entry, crawler, extractor,sleep);
                logger.info("writting data");
                bw.write(data[0]);
                ba.write(data[1]);
            } finally {
                ba.close();
                bw.close();
            }
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("SOMETHING WENT TERRIBLY WRONG", e);
            }
        }


    }


}
