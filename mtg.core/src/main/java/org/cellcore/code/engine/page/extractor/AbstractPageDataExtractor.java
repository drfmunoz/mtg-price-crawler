package org.cellcore.code.engine.page.extractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.model.*;
import org.cellcore.code.shared.JsoupUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
public abstract class AbstractPageDataExtractor {

    protected final Log logger = LogFactory.getLog(getClass());

    public Card extractFromPage(String url) throws IOException, UnsupportedCardException {
        return extract(JsoupUtils.getConnection(url).get(), url);
    }

    protected abstract String getName(Document doc) throws UnsupportedCardException;

    protected abstract float getPrice(Document doc);
    protected abstract int getStock(Document doc);

    protected String cleanPriceString(String val) {
        Pattern p = Pattern.compile("-?\\d+(\\.)*+(\\d+)?");
        Matcher m = p.matcher(val.replaceAll(",", "\\."));
        m.find();
        return m.group();
    }

    protected abstract String[] getOtherNames(final Document doc);

    private Card extract(Document doc, String url) throws UnsupportedCardException {
        Card card = getCard(getName(doc));
        card.setOtherSearchNames(getOtherNames(doc));
        float iPrice = getPrice(doc);
        if (iPrice == Float.MAX_VALUE) {
            throw new UnsupportedCardException("Unavailable");
        }
        int stock=getStock(doc);

        setPriceDetails(card, iPrice,stock, url, getSource());

        logger.debug(card.getName() + " => " + iPrice+" ("+stock+")");
        return card;
    }

    public abstract Currency getCurrency();

    public abstract PriceSources getSource();

    private Card getCard(String enName) {
        String trimName = enName.trim().toLowerCase();
        Card card;
        card = new Card();
        card.setName(enName);
        card.setiName(trimName);
        return card;
    }
    private void setPriceDetails(Card card, float iPrice,int stock, String url, PriceSources sources) {
        CardPriceSource source = new CardPriceSource();
        source.setSourceType(sources);
        source.setUrl(url);
        source.setSourceName(getSource().getPrintName());

        if (!card.getSources().contains(source)) {
            card.getSources().add(source);
            source.setCard(card);
        } else {
            source = card.getSources().get(card.getSources().indexOf(source));
        }
        source.setCurrency(getCurrency());
        source.setLastUpdate(new Date());
        source.setLastPrice(iPrice);
        source.setLastStock(stock);
        CardPrice price = new CardPrice();
        source.getPriceSet().add(price);
        price.setSource(source);
        price.setStock(stock);
        price.setFetchDate(new Date());
        price.setPrice(iPrice);
    }
}
