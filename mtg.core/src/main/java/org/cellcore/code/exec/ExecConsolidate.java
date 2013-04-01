package org.cellcore.code.exec;

import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.config.ContextConfig;
import org.cellcore.code.engine.CardPriceConsolidator;
import org.cellcore.code.model.Card;
import org.cellcore.code.shared.GsonUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Author: Freddy Munoz (freddy@cellcore.org)
 *
 * consolidate the crawling data from the json archives to the database.
 */
public class ExecConsolidate extends AbstractExec {

    private static final Log logger = LogFactory.getLog(ExecConsolidate.class);

    public static void main(String[] args) throws IOException {
        new ExecConsolidate().execute(args);
    }

    @Override
    protected void execute(CommandLine commandLine) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        final File crawlResultDir = new File(commandLine.getOptionValue("input"));
        boolean global = commandLine.hasOption("global");
        Collection<File> resultFiles = FileUtils.listFiles(crawlResultDir, new String[]{"data.json"}, true);
        CardPriceConsolidator conciliatePriceCrawlData = context.getBean(CardPriceConsolidator.class);
        Gson gson = GsonUtils.getSerializer();
        for (File crawlResultFile : resultFiles) {
            logger.info("processing file " + crawlResultFile.getAbsolutePath());
            try {
                String fetched = FileUtils.readFileToString(crawlResultFile);
                try {
                    Card[] cards = gson.fromJson(fetched, Card[].class);
                    for (Card card : cards) {
                        if (card != null) {
                            if (global) {
                                conciliatePriceCrawlData.conciliateGlobal(card);
                            } else {
                                conciliatePriceCrawlData.conciliate(card);
                            }
                        }
                    }

                } catch (Throwable t) {
                    logger.error("ERROR PROCESSING FILE: " + crawlResultFile.getAbsolutePath(), t);
                }
            } catch (Throwable t) {
                logger.error("ERROR READING FILE: " + crawlResultFile.getAbsolutePath(), t);
            }
        }

        if (global) {
            for (Map.Entry<String, Card> entry : conciliatePriceCrawlData.getGlobalCards().entrySet()) {
                conciliatePriceCrawlData.conciliate(entry.getValue());
            }
        }
    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                new Option("i", "input", true, "input directory containing crawl result json files") {{
                    setRequired(true);
                }},
                new Option("g", "global", false, "tell whether this concile showld perform a global preprocess ") {{
                    setRequired(false);
                }}
        };
    }

    @Override
    protected Log getLogger() {
        return logger;
    }

}
