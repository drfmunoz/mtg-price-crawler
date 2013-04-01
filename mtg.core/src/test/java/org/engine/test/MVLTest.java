package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mvl.MVLEditionsCrawler;
import org.cellcore.code.engine.page.extractor.mvl.MVLEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mvl.MVLPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MVLTest {
    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String, String> editionsFr = GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                .getClassLoader().getResource("fr_editions.json").toURI())),
                Map.class);
        AbstractEditionsExtractor editions = new MVLEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://magic-ville.com/fr/register/magicville_sale.php");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownload() {
        MVLPageDataExtractor parser = new MVLPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://magic-ville.com/fr/carte.php?13m151");
            assertEquals("Thundermaw Hellkite", c.getName());
            assertEquals("thundermaw hellkite", c.getiName());
            assertEquals(1, c.getSources().size());
            Gson g = GsonUtils.getSerializer();
            Card c2 = g.fromJson(g.toJson(c), Card.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crawlPage() {
        AbstractEditionLocationCrawler mbEditionCrawler = new MVLEditionsCrawler("http://magic-ville.com/fr/register/");
        try {
            List<String> cards = mbEditionCrawler.crawlPage("http://magic-ville.com/fr/register/show_MV_collec.php?setcode=210");
            assertEquals("http://magic-ville.com/fr/register/../carte.php?13m003", cards.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
