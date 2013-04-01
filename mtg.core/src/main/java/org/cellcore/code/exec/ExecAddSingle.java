package org.cellcore.code.exec;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cellcore.code.config.ContextConfig;
import org.cellcore.code.exec.db.FetchGathererData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Author: Freddy Munoz (freddy@cellcore.org)
 * fetch and add a single card from the gatherer to the database.
 *
 */
public class ExecAddSingle extends AbstractExec {

    private static final Log logger = LogFactory.getLog(ExecAddSingle.class);

    public static void main(String[] args) throws IOException {
        new ExecPullFromGatherer().execute(args);
    }

    @Override
    protected void execute(CommandLine commandLine) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        FetchGathererData fgd = context.getBean(FetchGathererData.class);
        fgd.extractSingle(commandLine.getOptionValue("cardSet"), commandLine.getOptionValue("cardId"));
    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                new Option("-c", "cardId", true, "multiverse card id") {{
                    setRequired(true);
                }},
                new Option("-s", "cardSet", true, "cardSet ID") {{
                    setRequired(true);
                }}
        };
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
