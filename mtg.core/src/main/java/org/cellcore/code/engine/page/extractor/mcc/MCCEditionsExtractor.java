package org.cellcore.code.engine.page.extractor.mcc;

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
public class MCCEditionsExtractor extends AbstractEditionsExtractor {
    public MCCEditionsExtractor(Map<String, String> editions) {
        super(editions);
    }

    @Override
    public String getCrawlerClass() {
        return MCCEditionCrawler.class.getCanonicalName();
    }

    @Override
    public String getExtractorClass() {
        return MCCPageDataExtractor.class.getCanonicalName();
    }

    @Override
    public String getBaseUrl() {
        return "http://boutique.magiccorporation.com";
    }

    @Override
    public Map<String, String> getCardSets(Document doc) {
        Elements elements = doc.select("#background").get(0).select("tr").get(1).select("a");
        Map<String, String> cardSets = new HashMap<String, String>();
        for (Element element : elements) {
            String name = element.text();
            String url = getBaseUrl() + "/" + element.attr("href");
            cardSets.put(name, url);
        }
        return cardSets;
    }
}
