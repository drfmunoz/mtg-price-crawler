package org.cellcore.code.engine.page.extractor.mtgf;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;
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
public class MTGFPageDataExtractor extends AbstractPageDataExtractor {
    @Override
    protected String[] getOtherNames(Document doc) {
        return new String[0];
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        String name = doc.select(".name").get(0).select("h1").text();
        return name;
    }

    @Override
    protected float getPrice(Document doc) {
        String val = cleanPriceString(doc.select(".card-buy").get(0).select(".price").get(0).text());
        return Float.parseFloat(val);
    }

    @Override
    protected int getStock(Document doc) {
        if(!doc.select(".card-buy").select("option").isEmpty()){
            String val = doc.select(".card-buy").select("option").last().text();
            return Integer.parseInt(val);
        }
        return 0;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MTGF;
    }
}
