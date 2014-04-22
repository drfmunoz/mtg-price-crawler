package org.cellcore.code.exec;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;

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
public abstract class AbstractExec {

    protected final void execute(String[] args) {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        for (Option option : getOptions()) {
            options.addOption(option);
        }
        options.addOption(new Option("h", "help", false, "display this menu"));
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                displayHelp(options);
                return;
            }
            displayArgs(args);
            execute(commandLine);
            displayMemoryUsageAndExit();
        } catch (ParseException e) {
            displayHelp(options);
        }
    }

    private void displayMemoryUsageAndExit() {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        getLogger().info(
                "Memory : " + (total - free) / 1024 / 1024 + "Mb used on " + total / 1024 / 1024 + "Mb allocated.");
    }


    protected abstract void execute(CommandLine commandLine);

    private void displayHelp(Options options) {
        new HelpFormatter().printHelp(getClass().getSimpleName(), options);
    }

    private void displayArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s + " ");
        }
        getLogger().info("Console Args :" + sb);
    }

    protected abstract Option[] getOptions();

    protected abstract Log getLogger();
}
