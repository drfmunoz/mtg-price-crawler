package org.cellcore.code.engine.page.extractor.mtgf;

import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class MTGFEditionsExtractor extends AbstractEditionsExtractor {
    public MTGFEditionsExtractor(Map<String, String> editions) {
        super(editions);
    }

    @Override
    public String getCrawlerClass() {
        return MTGFEditionCrawler.class.getCanonicalName();
    }

    @Override
    public String getExtractorClass() {
        return MTGFPageDataExtractor.class.getCanonicalName();
    }

    @Override
    public String getBaseUrl() {
        return "http://cartes.mtgfrance.com";
    }

    @Override
    public Map<String, String> getCardSets(Document doc) {
        Elements elements = doc.select(".linker");
        Map<String, String> cardSets = new HashMap<String, String>();
        for (Element element : elements) {
            String name = element.select("h3").get(0).text();
            String url = getBaseUrl() + "/" + element.select("a").attr("href");
            cardSets.put(name, url);
        }
        return cardSets;
    }
}
