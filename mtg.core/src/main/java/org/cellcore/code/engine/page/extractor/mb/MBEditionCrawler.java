package org.cellcore.code.engine.page.extractor.mb;

import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class MBEditionCrawler extends AbstractEditionLocationCrawler {


    public MBEditionCrawler(String url) {
        super(url);
    }

    @Override
    protected List<String> extract(Document doc) {
        Elements cards = doc.select("#contenu_tres_large").get(0).getElementsByAttributeValue("target", "_blank");
        List<String> urls = new LinkedList<String>();
        for (Element element : cards) {
            String link = element.attr("href");
            urls.add(getUrl() + "/" + link);
        }
        return urls;
    }
}
