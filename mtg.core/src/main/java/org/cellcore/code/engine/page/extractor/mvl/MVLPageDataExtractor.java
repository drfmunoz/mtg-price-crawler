package org.cellcore.code.engine.page.extractor.mvl;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class MVLPageDataExtractor extends AbstractPageDataExtractor {
    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        Elements nameEl = doc.select(".S16");
        return nameEl.get(1).text().trim();
    }

    private Map.Entry<Float, Integer> getPriceAndStock(Document doc) {
        Elements elements = doc.getElementsByAttributeValueContaining("name", "qte");
        Map.Entry entry;
        float priceL = Long.MAX_VALUE;
        int qtt = 0;
        for (Element element : elements) {
            Element priceEl = element.parent().parent().children().get(1);
            String val = element.select("option").last().text();
            String price = priceEl.text();
            float p = Float.parseFloat(this.cleanPriceString(price));
            if (p < priceL) {
                priceL = p;
                try {
                    qtt = Integer.parseInt(val);
                } catch (Throwable t) {
                    qtt = -1;
                }
            }
        }
        final float fprice = priceL;
        final int fqtt = qtt;
        return new Map.Entry<Float, Integer>() {
            @Override
            public Float getKey() {
                return fprice;
            }

            @Override
            public Integer getValue() {
                return fqtt;
            }

            @Override
            public Integer setValue(Integer aLong) {
                return null;
            }
        };
    }

    @Override
    protected float getPrice(Document doc) {
        try {
            return getPriceAndStock(doc).getKey();
        } catch (Throwable t) {
            t.printStackTrace();
            return -1;
        }

    }

    @Override
    protected int getStock(Document doc) {
        try {
            return getPriceAndStock(doc).getValue().intValue();
        } catch (Throwable t) {
            return -1;
        }
    }

    @Override
    protected String[] getOtherNames(Document doc) {
        String frName = doc.select(".S16").get(0).text().trim();
        return new String[]{frName};
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MVL;
    }
}
