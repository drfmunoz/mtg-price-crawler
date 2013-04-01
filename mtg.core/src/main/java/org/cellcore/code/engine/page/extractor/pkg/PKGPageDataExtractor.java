package org.cellcore.code.engine.page.extractor.pkg;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class PKGPageDataExtractor extends AbstractPageDataExtractor {

    @Override
    protected String[] getOtherNames(Document doc) {
        String names = ((Element) doc.getElementsByAttributeValueStarting("src", "./docs/illustrations/").get(0).parent().parent().childNodes().get(5)).select("td").get(1).select("font").text();
        String fName = names.substring(0, names.indexOf("(") - 1);
        return new String[]{fName};
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        String names = ((Element) doc.getElementsByAttributeValueStarting("src", "./docs/illustrations/").get(0).parent().parent().childNodes().get(5)).select("td").get(1).select("font").text();
        String vname = names.substring(names.indexOf("(") + 2, names.indexOf(")") - 1);
        if (names.contains("Foil")) {
            throw new UnsupportedCardException(names);
        }

        return vname;
    }

    @Override
    protected float getPrice(Document doc) {

        String price = ((Element) doc.getElementsByAttributeValueStarting("src", "./docs/illustrations/").get(0).parent().parent().childNodes().get(5)).select("tbody").get(1).select("font").get(0).select("b").text();
        if(price==null||price.equals("")){
            price=doc.select("form").get(2).select("b").get(0).text();
        }
        price = cleanPriceString(price);
        return Float.parseFloat(price);
    }

    @Override
    protected int getStock(Document doc) {
        String stock=doc.select("option").last().text();
        return Integer.parseInt(stock);
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.PKG;
    }

}
