package org.cellcore.code.engine.page.extractor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.shared.JsoupUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.Normalizer;
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
 **/
public abstract class AbstractEditionsExtractor {

    protected final Log logger = LogFactory.getLog(getClass());

    private Map<String, String> editions;

    protected AbstractEditionsExtractor(Map<String, String> editions) {
        this.editions = editions;
    }

    public CrawlConfiguration extractEditions(String url) throws IOException {
        CrawlConfiguration crf = new CrawlConfiguration();
        crf.setCardSets(filter(getCardSets(JsoupUtils.getConnection(url).get())));
        crf.setEditionLocationCrawler(getCrawlerClass());
        crf.setPageDataExtractor(getExtractorClass());
        crf.setBaseUrl(getBaseUrl());
        return crf;
    }

    protected Map<String, String> filter(Map<String, String> cardSets) {
        Map<String, String> filtered = new HashMap<String, String>();
        for (Map.Entry<String, String> names : cardSets.entrySet()) {
            String editionCode = null;
            if ((editionCode = checkList(names.getKey())) != null) {
                int i=1;
                while(filtered.containsKey(editionCode)) {
                    editionCode = editionCode + i;
                    i++;
                }
                filtered.put(editionCode, names.getValue());
            }
        }
        return filtered;
    }

    public abstract String getCrawlerClass();

    public abstract String getExtractorClass();

    public abstract String getBaseUrl();

    public abstract Map<String, String> getCardSets(Document doc);

    protected String checkList(String name) {
        if (name.toLowerCase().contains("foil")){
            return null;
        }
        name=name.replaceAll("(^( )+|( )+$)","");
        for (String key : getEditions().keySet()) {
            String iname = Normalizer.normalize(name, Normalizer.Form.NFD).toLowerCase().replaceAll("[^\\p{ASCII}]", "");
            String kname = Normalizer.normalize(key, Normalizer.Form.NFD).toLowerCase().replaceAll("[^\\p{ASCII}]", "");
            int distance = StringUtils.getLevenshteinDistance(iname, kname);
            boolean numeral=false;
            boolean numeralProceed=false;
            if(iname.replaceAll("\\D+","").length()>0){
                numeral=true;
                if(iname.replaceAll("\\D+","").equals(kname.replaceAll("\\D+",""))){
                    numeralProceed=true;
                }
            }
            if (((kname.contains(iname) || iname.contains(kname) || distance <= 2)&&iname.length()>3)&&(numeral==numeralProceed)) {

                logger.info("Found "+iname + " " + getEditions().get(key));
                return getEditions().get(key);
            }
        }
        return null;
    }

    public Map<String, String> getEditions() {
        if (editions == null) {
            this.editions = new HashMap<String, String>();
        }
        return editions;
    }

    public void setEditions(Map<String, String> editions) {
        this.editions = editions;
    }
}
