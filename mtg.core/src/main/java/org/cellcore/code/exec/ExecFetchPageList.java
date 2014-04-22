package org.cellcore.code.exec;

import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.engine.page.extractor.AbstractEditionsExtractor;
import org.cellcore.code.engine.page.extractor.CrawlConfiguration;
import org.cellcore.code.shared.GsonUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 *  Extract the editions list from the different data source pages
 *  (MTG Card dealers).
 *
 */
public class ExecFetchPageList extends AbstractExec {

    private static final Log logger = LogFactory.getLog(ExecFetchPageList.class);

    public static void main(String[] args) throws IOException {
        new ExecFetchPageList().execute(args);
    }

    @Override
    protected void execute(CommandLine commandLine) {
        try {
            Class<? extends AbstractEditionsExtractor> crawlerClass = (Class<? extends AbstractEditionsExtractor>) ExecFetchPageList.class.getClassLoader().loadClass(commandLine.getOptionValue("class"));
            Constructor constructor = crawlerClass.getConstructor(Map.class);
            AbstractEditionsExtractor crawler = (AbstractEditionsExtractor) constructor.newInstance(GsonUtils.getSerializer().fromJson(FileUtils
                    .readFileToString(new File(commandLine.getOptionValue("filter"))),
                                    Map.class));
            CrawlConfiguration conf = crawler.extractEditions(commandLine.getOptionValue("url"));
            Gson gson = GsonUtils.getSerializer();
            FileUtils.writeStringToFile(new File(commandLine.getOptionValue("out")), gson.toJson(conf));
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (NoSuchMethodException e) {
            logger.error("", e);
        } catch (InvocationTargetException e) {
            logger.error("", e);
        }
    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                new Option("c", "class", true, "full qualified name of the index extractor class") {{
                    setRequired(true);
                }},
                new Option("u", "url", true, "url to process") {{
                    setRequired(true);
                }},
                new Option("o", "out", true, "output configuration file") {{
                    setRequired(true);
                }},
                new Option("f", "filter", true, "json file containing name filters") {{
                    setRequired(true);
                }}
        };
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
