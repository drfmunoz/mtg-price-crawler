package org.cellcore.code.engine.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.model.Card;
import org.cellcore.code.model.CardName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
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
public class GathererDataExtractor {

    private String baseUrl = "http://gatherer.wizards.com/";
    private String detailPath = "Pages/Card/Details.aspx?multiverseid=";
    private String langPath = "Pages/Card/Languages.aspx?multiverseid=";
    private String detailUrl = baseUrl + detailPath;
    private String langUrl = baseUrl + langPath;

    private static final Log logger = LogFactory.getLog(GathererDataExtractor.class);

    public Card getCard(String cardId) throws IOException {
        Card c = new Card();

        Document document = Jsoup.connect(detailUrl + cardId).get();
        try{
            c.setMultiverseId(getGathererId(document));
        }
        catch (Throwable t){
            c.setMultiverseId(cardId);
            logger.info("ERROR GETTING ID... falling back to default id");
        }

        String name = fetchName(document);
        Set<CardName> otherNames = fetchOtherNames(c.getMultiverseId(), c);
        /**
         * add english as another name
         */
        CardName cname = new CardName();
        cname.setMultiverseId(cardId);
        cname.setLanguage("English");
        cname.setTranslatedLang("English");
        cname.setName(name);
        cname.setCard(c);
        otherNames.add(cname);


        c.setName(name);
        c.setiName(name.trim().toLowerCase());
        c.setNames(otherNames);
        return c;
    }

    private Set<CardName> fetchOtherNames(String cardId, Card card) throws IOException {
        Document document = Jsoup.connect(langUrl + cardId).get();
        Elements elements = document.select(".cardItem");
        Set<CardName> cardNames = new HashSet<CardName>();
        for (Element element : elements) {

            Elements tds = element.select("td");
            String lang = tds.get(1).text();
            if (!skip(lang)) {
                String name = tds.get(0).text();
                String multiverseId = tds.get(0).select("a").get(0).attr("href");
                multiverseId = multiverseId.substring(multiverseId.indexOf("=") + 1, multiverseId.length());
                String transLang = tds.get(2).text();
                CardName cn = new CardName();
                cn.setTranslatedLang(transLang);
                cn.setLanguage(lang);
                cn.setName(name);
                cn.setMultiverseId(multiverseId);
                cn.setCard(card);
                cardNames.add(cn);
            }
        }
        return cardNames;
    }

    private boolean skip(String lang) {
        return ("Russian".equals(lang)
                || "Japanese".equals(lang)
                || "Chinese Simplified".equals(lang)
                || "Portuguese (Brazil)".equals(lang)
                || "German".equals(lang)
                || "Italian".equals(lang)
                || "Korean".equals(lang)
                || "Chinese Traditional".equals(lang));

    }

    private String fetchName(Document document) throws IOException {
        return document.select(".contentTitle").get(0).text();
    }
    private String getGathererId(Document document) throws Exception {
        Elements ids = document.getElementsByAttributeValueEnding("id", "otherSetsValue").select("a");
        long lastID=0;
        String mRet="";
        for(Element element:ids){
            String multiverseId = element.attr("href");
            multiverseId = multiverseId.substring(multiverseId.indexOf("=") + 1, multiverseId.length());
            long cID=Long.parseLong(multiverseId);
            if(cID>lastID){
                lastID=cID;
                mRet=multiverseId;
            }
        }
        if(lastID==0){
            throw new Exception("No other sets found");
        }
        return mRet;
    }

}
