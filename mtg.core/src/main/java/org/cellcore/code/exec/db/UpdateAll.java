package org.cellcore.code.exec.db;


import org.cellcore.code.config.ContextConfig;
import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.model.CardPriceSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
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
public class UpdateAll {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        GeneralDao dao = context.getBean(GeneralDao.class);
        FetchUpdateData updateData = context.getBean(FetchUpdateData.class);
        List<CardPriceSource> cards = dao.list(CardPriceSource.class);
        for (CardPriceSource card : cards) {
            updateData.update(card.getUrl(), card.getSourceType().getPageDataExtractorClass());
        }
    }
}
