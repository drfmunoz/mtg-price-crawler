package org.cellcore.code.engine.page.extractor.pkg;

import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Copyright 2013 Freddy Munoz (freddy@cellcore.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ==============================================================
 */
public class PKGEditionsExtractor extends AbstractEditionsExtractor {

    public PKGEditionsExtractor(Map<String, String> editions) {
        super(editions);
    }

    @Override
    public String getCrawlerClass() {
        return PKGEditionCrawler.class.getCanonicalName();
    }

    @Override
    public String getExtractorClass() {
        return PKGPageDataExtractor.class.getCanonicalName();
    }

    @Override
    public String getBaseUrl() {
        return "http://www.parkage.com";
    }

    private String getReplacedURL() {
        return "http://www.parkage.com/produits-list.php?recherche=1&is_unite=1&is_rachat=0&fam=MAG&cat=&description=&foil=&numero=&rarete=&serie=EDITION_VALUE&type=&tri=en&Submit.x=166&Submit.y=17&Submit=Ok";
    }

    @Override
    public Map<String, String> getCardSets(Document doc) {
        Elements elements = doc.select("select[name=serie]").select("option");
        Map<String, String> cardSets = new HashMap<String, String>();

        for (Element element : elements) {
            String name = element.attr("value");
            String url = "";
            try {
                url = getReplacedURL().replaceAll("EDITION_VALUE", URLEncoder.encode(element.attr("value"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
            if(name.length()>3){
                name=name.substring(name.indexOf("-")+2,name.length());
            }
            cardSets.put(name, url);
        }
        return cardSets;
    }
}
