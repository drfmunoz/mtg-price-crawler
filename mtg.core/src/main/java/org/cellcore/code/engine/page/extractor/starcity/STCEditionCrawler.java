package org.cellcore.code.engine.page.extractor.starcity;

import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


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
public class STCEditionCrawler extends AbstractEditionLocationCrawler {
    public STCEditionCrawler(String url) {
        super(url);
    }

    @Override
    protected List<String> extract(Document doc) throws IOException {
        List<String> cards = new LinkedList<String>();

        Elements cardLinks = doc.select(".card_popup");
        for (Element cL : cardLinks) {
            String href = cL.attr("href");
            cards.add(href);
        }

        Elements linksCap = doc.select("#search_table_wrapper").select("tr");
        Elements links = linksCap.get(linksCap.size() - 2).select("a").select(":contains(Next)");
        if (!links.isEmpty()) {
            String nextToCrawl = links.get(0).attr("href");
            logger.info(nextToCrawl);
            cards.addAll(crawlPage(nextToCrawl));
        }

        return cards;
    }
}
