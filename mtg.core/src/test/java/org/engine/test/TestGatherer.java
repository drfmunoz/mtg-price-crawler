package org.engine.test;

import org.cellcore.code.engine.page.GathererDataExtractor;
import org.cellcore.code.engine.page.GathererEditionLocationCrawler;
import org.cellcore.code.model.Card;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Create By : freddy
 * Date: 1/17/13
 * Time: 4:31 PM
 */
public class TestGatherer {

    @Test
    public void testGathererFetch() throws IOException {
        GathererDataExtractor gathererDataExtractor = new GathererDataExtractor();
        Card card = gathererDataExtractor.getCard("253700");
        assertEquals("Thundermaw Hellkite", card.getName());
        assertEquals("thundermaw hellkite", card.getiName());
        assertEquals("253700", card.getMultiverseId());
        System.out.println(card);
    }
    @Test
     public void testGathererFetch2() throws IOException {
         GathererDataExtractor gathererDataExtractor = new GathererDataExtractor();
         Card card = gathererDataExtractor.getCard("96923");
         assertEquals("Steam Vents", card.getName());
         assertEquals("steam vents", card.getiName());
         assertEquals("253682", card.getMultiverseId());
         System.out.println(card);
     }

    @Test
     public void testGathererCumulox() throws IOException {
         GathererDataExtractor gathererDataExtractor = new GathererDataExtractor();
         Card card = gathererDataExtractor.getCard("47800");
         assertEquals("Qumulox", card.getName());
         assertEquals("qumulox", card.getiName());
         assertEquals("222739", card.getMultiverseId());
         System.out.println(card);
     }

    @Test
    public void crawlPage() {
        GathererEditionLocationCrawler mccEditionCrawler = new GathererEditionLocationCrawler("http://gatherer.wizards.com");
        try {
            List<String> cards = mccEditionCrawler.crawlPage("http://gatherer.wizards.com/Pages/Search/Default.aspx?output=checklist&set=%5b%22Avacyn+Restored%22%5d");
            assertEquals("240017", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void crawlPageTSH() {
        GathererEditionLocationCrawler mccEditionCrawler = new GathererEditionLocationCrawler("http://gatherer.wizards.com");
        try {
            List<String> cards = mccEditionCrawler.crawlPage("http://gatherer.wizards.com/Pages/Search/Default.aspx?output=compact&action=advanced&set=%5b%22Timeshifted%22%5d");
            assertEquals("193871", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
