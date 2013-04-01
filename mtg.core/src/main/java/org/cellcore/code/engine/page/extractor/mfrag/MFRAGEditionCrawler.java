package org.cellcore.code.engine.page.extractor.mfrag;

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
public class MFRAGEditionCrawler extends AbstractEditionLocationCrawler {
    public MFRAGEditionCrawler(String url) {
        super(url);
    }

    @Override
    protected List<String> extract(Document doc) throws IOException {
        Elements elements = doc.select("#contenu").get(0).getElementsByAttributeValueContaining("src", "../image/manas");
        Set<String> links = new HashSet<String>();
        for (Element element : elements) {
            links.add(element.parent().attr("href"));
        }
        List<String> cards = new LinkedList<String>();
        cards.addAll(links);
        return cards;
    }
}
