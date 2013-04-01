package org.cellcore.code.engine.page.extractor.starcity;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.cellcore.code.model.Card;
import org.cellcore.code.model.Currency;
import org.cellcore.code.model.PriceSources;
import org.cellcore.code.shared.GsonUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
 *
 */
public class STCPageDataExtractor extends AbstractPageDataExtractor {


    protected Card jsonProc(String id, Document doc) {
        HttpClient httpClient = new HttpClient();

        try {
            String callbackVal = "JQueryS_23ss3sLIJ2211";
            GetMethod getMethod = new GetMethod("http://sales.starcitygames.com/cart_product_ajax.php?callback=" + callbackVal + "&product=" + id + "&qty=1&mode=login");
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode == 200) {
                String resp = getMethod.getResponseBodyAsString().replaceAll(callbackVal, "").replaceAll("\\(", "").replaceAll("\\)", "");
                getMethod.releaseConnection();
                Map<String, Object> values = GsonUtils.getSerializer().fromJson(resp, Map.class);
                String name = "";
                String price = "-1";
                if ((Boolean)values.get("success")) {
                    String feedback = (String)values.get("feedback");
                    name = feedback.substring(0, feedback.lastIndexOf("was added"));
                    price = feedback.substring(feedback.lastIndexOf("$") + 1, feedback.length() - 1);
                }
                else{
                    String error = (String)values.get("error_message");
                    name = error.substring(error.lastIndexOf(" on ")+3, error.lastIndexOf(","));
                }

                doc.append("<div id=\"custom_card_name_STC\">" + name + "</div>");
                doc.append("<div id=\"custom_card_price_STC\">" + price + "</div>");
            }

        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        } catch (HttpException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    protected String getName(Document doc) throws UnsupportedCardException {
        if(!doc.select("h3").select(":contains(Foil)").isEmpty()){
            throw new UnsupportedCardException("foil");
        }
        String href = doc.baseUri();
        String code = href.substring(href.lastIndexOf("=") + 1, href.length());
        jsonProc(code, doc);
        if (doc.getElementById("custom_card_name_STC") != null) {
            return doc.getElementById("custom_card_name_STC").text();
        }
        return null;
    }

    @Override
    protected float getPrice(Document doc) {
        if (doc.getElementById("custom_card_price_STC") != null) {
            return Float.parseFloat(doc.getElementById("custom_card_price_STC").text());
        }
        return -1;
    }

    @Override
    protected int getStock(Document doc) {
        Elements items = doc.select("span").select(":contains(in stock)");
        if (items.isEmpty()||items.text().replaceAll("\\D+", "").length()==0) {
            return 0;
        }
        return Integer.parseInt(items.text().replaceAll("\\D+", ""));
    }

    @Override
    protected String[] getOtherNames(Document doc) {
        return new String[0];
    }

    @Override
    public Currency getCurrency() {
        return Currency.USD;
    }

    @Override
    public PriceSources getSource() {
        return PriceSources.STC;
    }
}
