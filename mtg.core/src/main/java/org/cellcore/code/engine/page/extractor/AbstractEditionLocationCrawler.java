package org.cellcore.code.engine.page.extractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.shared.JsoupUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
public abstract class AbstractEditionLocationCrawler {

    protected final Log logger = LogFactory.getLog(AbstractEditionLocationCrawler.class);

    private String url = "";

    public AbstractEditionLocationCrawler(String url) {
        this.url = url;
    }

    public List<String> crawlPage(String url) throws IOException {
        return this.extract(JsoupUtils.getConnection(url).get());
    }

    protected abstract List<String> extract(Document doc) throws IOException;

    public String getUrl() {
        return url;
    }
}
