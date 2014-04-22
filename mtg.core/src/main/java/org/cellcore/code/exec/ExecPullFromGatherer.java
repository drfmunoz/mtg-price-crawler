package org.cellcore.code.exec;

import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.config.ContextConfig;
import org.cellcore.code.dao.GeneralDao;
import org.cellcore.code.engine.page.GathererEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.exec.db.FetchGathererData;
import org.cellcore.code.shared.GsonUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
 * Author: Freddy Munoz (freddy@cellcore.org)
 * Get card information from the gatherer. The card data is directly stored into the database.
 *
 */
public class ExecPullFromGatherer extends AbstractExec {

    private static final Log logger = LogFactory.getLog(ExecPullFromGatherer.class);

    public static void main(String[] args) throws IOException {
        new ExecPullFromGatherer().execute(args);
    }

    @Override
    protected void execute(CommandLine commandLine) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        FetchGathererData fgd = context.getBean(FetchGathererData.class);
        GeneralDao dao = context.getBean(GeneralDao.class);
        Gson gson = GsonUtils.getSerializer();
        String configString = null;
        try {
            configString = FileUtils.readFileToString(new File(commandLine.getOptionValue("input")));
            CrawlConfiguration config = gson.fromJson(configString, CrawlConfiguration.class);
            GathererEditionLocationCrawler crawler = new GathererEditionLocationCrawler("");

            for (Map.Entry<String, String> cardSet : config.getCardSets().entrySet()) {
                List<String> cardIds = crawler.crawlPage(cardSet.getValue());
                logger.info("PROCESSING: " + cardSet + " " + cardIds.size() + " cards");
                fgd.extract(cardSet.getKey(), cardIds);
            }
        } catch (IOException e) {
            logger.error("", e);
        }

    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                new Option("i", "input", true, "input file containing gathered crawling data.") {{
                    setRequired(true);
                }}
        };
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
