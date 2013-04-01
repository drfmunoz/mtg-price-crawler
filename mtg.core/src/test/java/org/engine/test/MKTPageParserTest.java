package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mkt.MKTEditionsCrawler;
import org.cellcore.code.engine.page.extractor.mkt.MKTEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mkt.MKTPageDataExtractor;
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
 * Date: 3/3/13
 * Time: 12:49 PM
 */
public class MKTPageParserTest {
    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String, String> editionsFr = GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                .getClassLoader().getResource("fr_editions.json").toURI())),
                Map.class);
        AbstractEditionsExtractor editions = new MKTEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("https://www.magiccardmarket.eu/?mainPage=browseCategory&idCategory=1&idRarity=&onlyAvailable=true&idExpansion=70");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownload() {
        MKTPageDataExtractor parser = new MKTPageDataExtractor();
        try {
            Card c = parser.extractFromPage("https://www.magiccardmarket.eu/Thundermaw_Hellkite_Magic_2013.c1p256793.prod");
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
        AbstractEditionLocationCrawler mbEditionCrawler = new MKTEditionsCrawler("https://www.magiccardmarket.eu/");
        try {
            List<String> cards = mbEditionCrawler.crawlPage("https://www.magiccardmarket.eu/?mainPage=browseCategory&idCategory=1&idRarity=&onlyAvailable=true&idExpansion=70");
            assertEquals("https://www.magiccardmarket.eu/Angel_of_Salvation_Future_Sight.c1p14993.prod", cards.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
