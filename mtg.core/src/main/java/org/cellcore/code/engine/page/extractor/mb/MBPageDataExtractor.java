package org.cellcore.code.engine.page.extractor.mb;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;
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
public class MBPageDataExtractor extends AbstractPageDataExtractor {

    protected String[] getOtherNames(Document doc) {
        String frName = doc.select(".text").get(1).childNodes().get(0).attr("text").trim();
        return new String[]{frName};
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    protected String getName(Document doc) throws UnsupportedCardException {

        return doc.select(".text").get(2).childNodes().get(0).attr("text").trim();
    }

    protected float getPrice(Document doc) {
        if (!doc.getElementsContainingText("Cette carte n'est pas disponible en stock").isEmpty()) {
            return -1;
        }
        Elements tr = doc.select(".stock").get(0).getElementsByTag("tr");
        float iPrice = Float.MAX_VALUE;
        for (int i = 1; i < tr.size(); i++) {
            String val = tr.get(i).getElementsByTag("td").get(3).childNodes().get(0).attr("text");
            val = cleanPriceString(val);
            float price = Float.parseFloat(val);
            if (price < iPrice) {
                iPrice = price;
            }
        }
        return iPrice;
    }

    @Override
    protected int getStock(Document doc) {
        if (!doc.getElementsContainingText("Cette carte n'est pas disponible en stock").isEmpty()) {
            return 0;
        }
        Elements tr = doc.select(".stock").get(0).getElementsByTag("tr");
        float iPrice = Float.MAX_VALUE;
        int iStock=0;
        for (int i = 1; i < tr.size(); i++) {
            String val = tr.get(i).getElementsByTag("td").get(3).childNodes().get(0).attr("text");
            String stockV = tr.get(i).getElementsByTag("td").get(4).childNodes().get(1).attr("text");
            val = cleanPriceString(val);
            float price = Float.parseFloat(val);

            if (price < iPrice) {
                iPrice = price;
                iStock=Integer.parseInt(stockV.replaceAll("\\(","").replaceAll("\\)",""));
            }
        }
        return iStock;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MB;
    }
}
