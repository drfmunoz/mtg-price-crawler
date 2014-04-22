package org.cellcore.code.exec;

import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.engine.page.extractor.AbstractEditionLocationCrawler;
import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

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
 * fetch data from a single data source (use a json source descriptor).
 * The crawling result will be stored into a json file.
 * e.g:
 *  -i data/resources/mb-crawl-conf.json
 *  -o data/result/mb
 *  [-s] 1000 (optional sleep time in miliseconds)
 */
public class ExecFetch extends AbstractExec {


    private static final Log logger = LogFactory.getLog(ExecFetch.class);

    public static void main(String[] args) throws IOException {
        new ExecFetch().execute(args);
    }

    @Override
    protected void execute(CommandLine commandLine) {

        final FetchDataToFile fetchData = new FetchDataToFile();

        File sourceLocations = new File(commandLine.getOptionValue("input"));
        final File crawlResult = new File(commandLine.getOptionValue("output"));
        Collection<File> sourceFiles = FileUtils.listFiles(sourceLocations, new String[]{"json"}, false);
        long sleep=-1;
        if(commandLine.hasOption("sleep")){
            sleep=Long.parseLong(commandLine.getOptionValue("sleep"));
        }

        for (File source : sourceFiles) {
            Gson gson = new Gson();
            try {
                String data = FileUtils.readFileToString(source, "UTF-8");
                final CrawlConfiguration conf = gson.fromJson(data, CrawlConfiguration.class);
                Class<? extends AbstractEditionLocationCrawler> crawlerClass = (Class<? extends AbstractEditionLocationCrawler>) ExecFetch.class.getClassLoader().loadClass(conf.getEditionLocationCrawler());
                Class<? extends AbstractPageDataExtractor> extractorClass = (Class<? extends AbstractPageDataExtractor>) ExecFetch.class.getClassLoader().loadClass(conf.getPageDataExtractor());
                final AbstractPageDataExtractor extractor = extractorClass.newInstance();
                Constructor constructor = crawlerClass.getConstructor(String.class);
                final AbstractEditionLocationCrawler crawler = (AbstractEditionLocationCrawler) constructor.newInstance(conf.getBaseUrl());
                fetchData.fetch(conf.getCardSets(), crawler, extractor, crawlResult,sleep);
            } catch (IOException e) {
                logger.error("", e);
            } catch (ClassNotFoundException e) {
                logger.error("", e);
            } catch (InstantiationException e) {
                logger.error("", e);
            } catch (IllegalAccessException e) {
                logger.error("", e);
            } catch (NoSuchMethodException e) {
                logger.error("", e);
            } catch (InvocationTargetException e) {
                logger.error("", e);
            }
        }
    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                new Option("i", "input", true, "input directory containing json files") {{
                    setRequired(true);
                }},
                new Option("o", "output", true, "crawl output directory") {{
                    setRequired(true);
                }},
                new Option("s", "sleep", true, "sleep time in ms between each crawl") {{
                    setRequired(false);
                }}
        };
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
