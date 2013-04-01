package org.cellcore.code.engine.page.extractor.mkt;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.jsoup.nodes.Document;

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
public class MKTPageDataExtractor extends AbstractPageDataExtractor {
    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        String name=doc.select("[property=v:title]").select("[typeof=v:Breadcrumb]").text();

        return name;
    }

    @Override
    protected float getPrice(Document doc) {
        String price=doc.select(".availTable").select("tr").get(1).children().get(1).text();
        price=this.cleanPriceString(price);
        return Float.parseFloat(price);
    }

    @Override
    protected int getStock(Document doc) {
        String stock=doc.select(".availTable").select("tr").get(0).children().get(1).text();
        return Integer.parseInt(stock);
    }

    @Override
    protected String[] getOtherNames(Document doc) {
        return new String[0];
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MKT;
    }
}
