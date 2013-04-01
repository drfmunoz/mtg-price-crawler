package org.engine.test;

import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mb.MBEditionCrawler;
import org.cellcore.code.engine.page.extractor.mb.MBEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mb.MBPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class MBPageParserTest {

    @Test
    public void testGetEditions() throws IOException, URISyntaxException {
        Map<String,String> editionsFr=GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                        .getClassLoader().getResource("fr_editions.json").toURI())),
                        Map.class);
        AbstractEditionsExtractor editions = new MBEditionsExtractor(editionsFr);

        CrawlConfiguration conf = editions.extractEditions("http://www.magicbazar.fr/acheter-cartes-magic.php");
        for (String edition : conf.getCardSets().keySet()) {
            System.out.println(edition+" => "+conf.getCardSets().get(edition));
        }
    }

    @Test
    public void testDownloadMB() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&ext=207&id=16788");
        assertEquals("Exquisite Blood", c.getName());
        assertEquals("exquisite blood", c.getiName());
        assertEquals(1, c.getSources().size());
        assertEquals("http://www.magicbazar.fr/recherche/index.php?page=3&ext=207&id=16788", c.getSources().get(0).getUrl());

    }

    @Test
    public void testDownloadMB2() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&ext=80&id=2827");
        assertEquals("Merfolk Looter", c.getName());
        assertEquals("merfolk looter", c.getiName());

    }

    @Test
    public void testDownloadMB3() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&ext=80&id=8339");
        assertEquals("Ravenous Rats", c.getName());
        assertEquals("ravenous rats", c.getiName());

    }

    @Test
    public void testDownloadMB4() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&ext=214&id=17603");
        assertEquals("Boros Reckoner", c.getName());
        assertEquals("boros reckoner", c.getiName());

    }

    @Test
    public void testDownloadMB5() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&ext=211&id=17162");
        assertEquals("Azorius Guildgate", c.getName());
        assertEquals("azorius guildgate", c.getiName());

    }

    @Test
    public void testDownloadUnavail() throws IOException, UnsupportedCardException {

        MBPageDataExtractor parser = new MBPageDataExtractor();
        Card c = parser.extractFromPage("http://www.magicbazar.fr/recherche/index.php?page=3&id=8963");
        assertEquals("Mox Lotus", c.getName());
        assertEquals("mox lotus", c.getiName());
        assertEquals(1, c.getSources().size());
        assertEquals("http://www.magicbazar.fr/recherche/index.php?page=3&id=8963", c.getSources().get(0).getUrl());
        assertEquals(c.getSources().get(0).getLastPrice(), -1, 0.001);
        assertEquals(c.getSources().get(0).getPriceSet().iterator().next().getPrice(), -1, 0.001);

    }

    @Test
    public void crawlPage() throws IOException {
        MBEditionCrawler mbEditionCrawler = new MBEditionCrawler("http://www.magicbazar.fr");
        List<String> cards = mbEditionCrawler.crawlPage("http://www.magicbazar.fr/extension.php?ext=211");
        assertEquals("http://www.magicbazar.fr/recherche/index.php?page=3&ext=211&id=17196", cards.get(0));
    }

    @Test
    public void crawlPage2() throws IOException {
        MBEditionCrawler mbEditionCrawler = new MBEditionCrawler("http://www.magicbazar.fr");
        List<String> cards = mbEditionCrawler.crawlPage("http://www.magicbazar.fr/extension.php?ext=65");


    }
}
