package org.cellcore.code.engine.page.extractor.mcc;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
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
 **/
@Component
public class MCCPageDataExtractor extends AbstractPageDataExtractor {

    @Override
    protected String[] getOtherNames(Document doc) {
        String frName = doc.select("#blockContent").get(4).select("b").get(0).childNodes().get(0).attr("text");
        return new String[]{frName};
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    protected String getName(Document doc) {
        return doc.select("#blockContent").get(3).select("b").get(0).childNodes().get(0).attr("text");
    }


    @Override
    protected float getPrice(Document doc) {
        Elements tr = doc.select("#blockContent").get(5).select("tr");
        float iPrice = Float.MAX_VALUE;
        for (int i = 0; i < tr.size(); i++) {
            try {
                String val = tr.get(i).getElementsByTag("td").get(3).childNodes().get(0).attr("text");
                val = cleanPriceString(val);
                float price = Float.parseFloat(val);
                if (price < iPrice) {
                    iPrice = price;
                }
            } catch (Throwable t) {

            }
        }
        if (iPrice == Float.MAX_VALUE) {
            iPrice = -1;
        }
        return iPrice;
    }

    @Override
    protected int getStock(Document doc) {
        Elements tr = doc.select("#blockContent").get(5).select("tr");
        float iPrice = Float.MAX_VALUE;
        int iStock=0;
        for (int i = 0; i < tr.size(); i++) {
            try {
                String val = tr.get(i).getElementsByTag("td").get(3).childNodes().get(0).attr("text");
                String stockV=tr.get(i).getElementsByTag("td").get(4).select("option").last().childNodes().get(0).attr("text");
                val = cleanPriceString(val);
                float price = Float.parseFloat(val);
                if (price < iPrice) {
                    iPrice = price;
                    iStock=Integer.parseInt(stockV.replaceAll("\\(","").replaceAll("\\)",""));
                }
            } catch (Throwable t) {

            }
        }
        return iStock;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.MCC;
    }


}
