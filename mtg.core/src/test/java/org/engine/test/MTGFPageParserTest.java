package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mtgf.MTGFEditionCrawler;
import org.cellcore.code.engine.page.extractor.mtgf.MTGFEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mtgf.MTGFPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MTGFPageParserTest {


    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String,String> editionsFr=GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                        .getClassLoader().getResource("fr_editions.json").toURI())),
                         Map.class);
        AbstractEditionsExtractor editions = new MTGFEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://cartes.mtgfrance.com/set.php");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDownloadMB() {
        MTGFPageDataExtractor parser = new MTGFPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://cartes.mtgfrance.com/card-18213-en-thundermaw-hellkite");
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
     public void testDownloadFaile() {
         MTGFPageDataExtractor parser = new MTGFPageDataExtractor();
         try {
             Card c = parser.extractFromPage("http://cartes.mtgfrance.com/card-12719-en-nomad-mythmaker");
             assertEquals("Nomad Mythmaker", c.getName());
             assertEquals("nomad mythmaker", c.getiName());
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
        MTGFEditionCrawler mbEditionCrawler = new MTGFEditionCrawler("http://cartes.mtgfrance.com");
        try {
            List<String> cards = mbEditionCrawler.crawlPage("http://cartes.mtgfrance.com/set-103-magic-2013-magic-2013");
            assertEquals("http://cartes.mtgfrance.com/card-18269-en-akromas-memorial", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
