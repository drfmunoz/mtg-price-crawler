package org.cellcore.code.shared;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

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
public class JsoupUtils {

    public static Connection getConnection(String url) {
        /**
         * 30 seconds timeout
         */
        return Jsoup.connect(url).followRedirects(true).timeout(30000);
    }
}
