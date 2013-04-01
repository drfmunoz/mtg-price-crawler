package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mcc.MCCEditionCrawler;
import org.cellcore.code.engine.page.extractor.mcc.MCCEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mcc.MCCPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MCCPageParserTest {

    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String,String> editionsFr=GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                        .getClassLoader().getResource("fr_editions.json").toURI())),
                        Map.class);
        AbstractEditionsExtractor editions = new MCCEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://boutique.magiccorporation.com/cartes-magic-edition-121-innistrad.html");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownloadMB() {
        MCCPageDataExtractor parser = new MCCPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://boutique.magiccorporation.com/carte-magic-22140-escouflenfer-foudregueule.html");
            assertEquals("Thundermaw Hellkite", c.getName());
            assertEquals("thundermaw hellkite", c.getiName());
            assertEquals(1, c.getSources().size());
            Gson g = GsonUtils.getSerializer();
            Card c2 = g.fromJson(g.toJson(c), Card.class);
            assertEquals(c, c2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownloadUnavail() {
        MCCPageDataExtractor parser = new MCCPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://boutique.magiccorporation.com/carte-magic-23026-indolent-des-egouts.html");
            assertEquals("Gutter Skulk", c.getName());
            assertEquals("gutter skulk", c.getiName());
            assertEquals(1, c.getSources().size());
            assertEquals("http://boutique.magiccorporation.com/carte-magic-23026-indolent-des-egouts.html", c.getSources().get(0).getUrl());
//            assertEquals(-1,c.getSources().get(0).getLastPrice(), 0.001);
//            assertEquals( -1,c.getSources().get(0).getPriceSet().iterator().next().getPrice(), 0.001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crawlPage() {
        MCCEditionCrawler mccEditionCrawler = new MCCEditionCrawler("http://boutique.magiccorporation.com");
        try {
            List<String> cards = mccEditionCrawler.crawlPage("http://boutique.magiccorporation.com/cartes-magic-edition-121-innistrad.html");
            assertEquals("http://boutique.magiccorporation.com/carte-magic-19801-ange-du-vol-dalbatre.html", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
