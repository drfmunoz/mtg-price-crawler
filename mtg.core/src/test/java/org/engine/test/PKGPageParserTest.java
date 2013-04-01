package org.engine.test;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.engine.page.extractor.pkg.PKGEditionCrawler;
import org.cellcore.code.engine.page.extractor.pkg.PKGEditionsExtractor;
import org.cellcore.code.engine.page.extractor.pkg.PKGPageDataExtractor;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PKGPageParserTest {


    @Test
    public void testGetEditions() throws URISyntaxException, IOException {
        Map<String,String> editionsFr=GsonUtils.getSerializer().fromJson(FileUtils.readFileToString(new File(getClass()
                        .getClassLoader().getResource("fr_editions.json").toURI())),
                         Map.class);
        AbstractEditionsExtractor editions = new PKGEditionsExtractor(editionsFr);
        try {
            CrawlConfiguration conf = editions.extractEditions("http://www.parkage.com/produits-list.php?recherche=1&is_unite=1&is_rachat=0&fam=MAG&cat=&description=&foil=&numero=&rarete=&serie=75+-+Magic+2013&type=&tri=en&Submit.x=166&Submit.y=17&Submit=Ok");
            for (String edition : conf.getCardSets().keySet()) {
                System.out.println(edition+" => "+conf.getCardSets().get(edition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDownloadPKGA() {

        PKGPageDataExtractor parser = new PKGPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://www.parkage.com/boue-acide-acidic-slime-normal-751362-rac-0.html");
            assertEquals("Acidic Slime", c.getName());
            assertEquals("acidic slime", c.getiName());
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
    public void testDownloadPKG() {

        PKGPageDataExtractor parser = new PKGPageDataExtractor();
        try {
            Card c = parser.extractFromPage("http://www.parkage.com/assaillante-angelique-angelic-skirmisher-normal-755705-rac-0.html");
            assertEquals("Angelic Skirmisher", c.getName());
            assertEquals("angelic skirmisher", c.getiName());
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
        PKGEditionCrawler mccEditionCrawler = new PKGEditionCrawler("http://www.parkage.com");
        try {
            List<String> cards = mccEditionCrawler.crawlPage("http://www.parkage.com/produits-list.php?recherche=1&is_unite=1&is_rachat=0&fam=MAG&cat=&description=&foil=&numero=&rarete=&serie=75+-+Magic+2013&type=&tri=en&Submit.x=166&Submit.y=17&Submit=Ok");

            assertEquals("http://www.parkage.com/boue-acide-acidic-slime-normal-751362-rac-0.html", cards.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
