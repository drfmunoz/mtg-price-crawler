package org.cellcore.code.engine.page.extractor.mvl;

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
public class MVLEditionsCrawler extends AbstractEditionLocationCrawler {
    public MVLEditionsCrawler(String url) {
        super(url);
    }

    @Override
    protected List<String> extract(Document doc) throws IOException {
        Elements elements = doc.getElementsByAttributeValueContaining("href", "carte.php");
        List<String> links = new LinkedList<String>();
        for(Element element:elements){
            String href=element.attr("href");
            links.add(getUrl()+href);
        }
        return links;
    }
}
