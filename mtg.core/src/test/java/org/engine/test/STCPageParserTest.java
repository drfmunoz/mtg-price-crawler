package org.engine.test;

import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.starcity.STCEditionCrawler;
import org.cellcore.code.engine.page.extractor.starcity.STCEditionsExtractor;
import org.cellcore.code.engine.page.extractor.starcity.STCPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Create By : freddy
 * Date: 2/11/13
 * Time: 11:51 PM
 */
public class STCPageParserTest {
    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String, String> editionsFr = GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                .getClassLoader().getResource("fr_editions.json").toURI())),
                Map.class);
        AbstractEditionsExtractor editions = new STCEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://www.starcitygames.com/cardsets/english_singles");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void singleCard() throws IOException, UnsupportedCardException {
        STCPageDataExtractor extractor = new STCPageDataExtractor();
        Card c = extractor.extractFromPage("http://sales.starcitygames.com/carddisplay.php?product=413365");
        //json:callback=as&product=427861&qty=1&mode=login
        c.getName();

    }

    @Test(expected = UnsupportedCardException.class)
    public void singleCardFoil() throws IOException, UnsupportedCardException {
        STCPageDataExtractor extractor = new STCPageDataExtractor();
        Card c = extractor.extractFromPage("http://sales.starcitygames.com/carddisplay.php?product=55902");
        //json:callback=as&product=427861&qty=1&mode=login
        c.getName();

    }

    @Test
    public void singleCard2() {
        STCPageDataExtractor extractor = new STCPageDataExtractor();
        try {
            Card c = extractor.extractFromPage("http://sales.starcitygames.com/carddisplay.php?product=427924");
            //json:callback=as&product=427861&qty=1&mode=login
            c.getName();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crawlPage() {
        STCEditionCrawler mccEditionCrawler = new STCEditionCrawler("http://sales.starcitygames.com");
        try {
            List<String> cards = mccEditionCrawler.crawlPage("http://sales.starcitygames.com/category.php?t=a&cat=5243");

            assertEquals("http://sales.starcitygames.com/carddisplay.php?product=412756", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
