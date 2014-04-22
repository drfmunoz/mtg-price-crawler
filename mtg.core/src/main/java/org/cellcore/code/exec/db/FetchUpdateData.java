package org.cellcore.code.exec.db;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.UnsupportedCardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
public class FetchUpdateData {


    @Autowired
    ApplicationContext context;

    @Transactional
    public void update(String url, Class<? extends AbstractPageDataExtractor> tClass) {
        AbstractPageDataExtractor extractor = context.getBean(tClass);
        try {
            extractor.extractFromPage(url);
        } catch (IOException e) {
            System.err.println("problem downloading : " + url);
        } catch (UnsupportedCardException e) {
            System.err.println("bad card for " + url);
        }
    }
}
