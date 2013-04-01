package org.cellcore.code.engine.page.extractor.mkt;

import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class MKTEditionsCrawler extends AbstractEditionLocationCrawler {

    public MKTEditionsCrawler(String url) {
        super(url);
    }

    @Override
    protected List<String> extract(Document doc) throws IOException {
        List<String> cards = new LinkedList<String>();


        Elements cardLinks = doc.getElementsByAttributeValueMatching("href", "^([A-Z]|[0-9])+.");
        for (Element element : cardLinks) {
            cards.add(getUrl() + element.attr("href"));
        }

        Elements links = doc.getElementsByAttributeValue("alt","nextResultsPage");
        Set<String> linkSet = new HashSet<String>();

        for (Element link : links) {
            linkSet.add(getUrl() + link.parent().select("a").attr("href"));
        }
        for (String link : linkSet) {
            cards.addAll(crawlPage(link));
        }
        return cards;
    }
}
