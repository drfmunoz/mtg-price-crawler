package org.cellcore.code.engine.page.extractor.mvl;

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
public class MVLEditionsExtractor  extends AbstractEditionsExtractor {
    public MVLEditionsExtractor(Map<String, String> editions) {
        super(editions);
    }

    @Override
    public String getCrawlerClass() {
        return MVLEditionsCrawler.class.getCanonicalName();
    }

    @Override
    public String getExtractorClass() {
        return MVLPageDataExtractor.class.getCanonicalName();
    }

    @Override
    public String getBaseUrl() {
        return "http://magic-ville.com/fr/register/";
    }

    @Override
    public Map<String, String> getCardSets(Document doc) {
        Map<String, String> cardSets = new HashMap<String, String>();
        Elements elements = doc.getElementsByAttributeValueContaining("href", "show_MV_collec.php");
        for(Element element:elements){
            String href=element.attr("href");
            if(!href.contains("color")){
                if(element.parent().select(".S10").size()>0){
                    Element nameEl = element.parent().select(".S10").get(1);
                    String name=nameEl.text();

                    cardSets.put(name,this.getBaseUrl()+href);
                }
            }
        }

        return cardSets;
    }
}
