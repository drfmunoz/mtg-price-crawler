package org.cellcore.code.engine.page.extractor.mfrag;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

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
@Component
public class MFRAGPageDataExtractor extends AbstractPageDataExtractor {
    @Override
    protected String[] getOtherNames(Document doc) {
        String fr = doc.select(".prod-det_s-titre").text();
        return new String[]{fr};
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        String name = doc.select(".prod-det_s-titre-gb").text();
        return name;
    }

    @Override
    protected float getPrice(Document doc) {
        String var = doc.select(".prod-det_prix").text();
        return Float.parseFloat(this.cleanPriceString(var));
    }

    @Override
    protected int getStock(Document doc) {
        Elements trs = doc.select("#Tableau").get(0).children().get(0).children();
        float iPrice = Float.MAX_VALUE;
        int iStock = 0;
        for (int i = 1; i < trs.size(); i++) {
            Element tr = trs.get(i);
            String val = tr.select("td").get(3).select("strong").get(0).childNodes().get(0).attr("text");
            String stockV = tr.select("td").get(4).select("option").last().childNodes().get(0).attr("text");
            val = cleanPriceString(val);
            float price = Float.parseFloat(val);

            if (price < iPrice) {
                iPrice = price;
                iStock = Integer.parseInt(stockV.replaceAll("\\(", "").replaceAll("\\)", ""));
            }
        }
        return iStock;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MFRAG;
    }
}
