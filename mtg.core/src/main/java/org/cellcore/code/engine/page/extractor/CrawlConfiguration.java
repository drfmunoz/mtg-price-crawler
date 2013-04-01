package org.cellcore.code.engine.page.extractor;

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
public class CrawlConfiguration {
    private String baseUrl;
    private String editionLocationCrawler;
    private String pageDataExtractor;
    private Map<String, String> cardSets;

    public String getEditionLocationCrawler() {
        return editionLocationCrawler;
    }

    public void setEditionLocationCrawler(String editionLocationCrawler) {
        this.editionLocationCrawler = editionLocationCrawler;
    }

    public String getPageDataExtractor() {
        return pageDataExtractor;
    }

    public void setPageDataExtractor(String pageDataExtractor) {
        this.pageDataExtractor = pageDataExtractor;
    }

    public Map<String, String> getCardSets() {
        return cardSets;
    }

    public void setCardSets(Map<String, String> cardSets) {
        this.cardSets = cardSets;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
