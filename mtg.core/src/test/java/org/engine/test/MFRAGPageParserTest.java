package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.mfrag.MFRAGEditionCrawler;
import org.cellcore.code.engine.page.extractor.mfrag.MFRAGEditionsExtractor;
import org.cellcore.code.engine.page.extractor.mfrag.MFRAGPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class MFRAGPageParserTest {

    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String,String> editionsFr=GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                        .getClassLoader().getResource("fr_editions.json").toURI())),
                        Map.class);
        AbstractEditionsExtractor editions = new MFRAGEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://www.magicfrag.fr/liste-edition-carte-magic-20.html");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownloadMB() {

        MFRAGPageDataExtractor parser = new MFRAGPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://www.magicfrag.fr/magic-2013-thundermaw-hellkite-escouflenfer-foudregueule-carte-magic-47239.html");
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
        MFRAGEditionCrawler mbEditionCrawler = new MFRAGEditionCrawler("http://www.magicfrag.fr");
        try {
            List<String> cards = mbEditionCrawler.crawlPage("http://www.magicfrag.fr/edition-carte-magic-653.html");
            assertEquals("http://www.magicfrag.fr/magic-2013-healer-of-the-pride-guerisseur-de-la-bande-carte-magic-47106.html", cards.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
