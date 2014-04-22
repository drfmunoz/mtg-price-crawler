Set the classpath
=================

The base class path for the MTG crawler will be the installation directory. In my particular case `/Users/freddy/Documents/tools/crawl`.

```
export MTGCRAWL_LOCATION="/Users/freddy/Documents/tools/crawl"
export MTGCRAWL_BIN=$MTGCRAWL_LOCATION"/app"
```

all executable classes are located in the package `org.cellcore.code.exec`.

Initialize the Database
=======================
In order for everything to work you will need to populate the database with card from the official MTG database ``The Gatherer``.


The executable class ``ExecPullFromGatherer`` will help you accomplish this. This executable requires a single paramenters in order to work.

* `-i` Gatherer editions index. (A sample gatherer file can be found [here](https://github.com/drfmunoz/mtg-price-crawler/blob/master/mtg.distrib/src/main/resources/data/gatherer.json) **Notice that the sample file only contains 5 editions. Feel free to complete the file with other editions and ensure that the target page is the edition's checklist view**)

```
java -cp $MTGCRAWL_BIN"/lib/*":$MTGCRAWL_LOCATION"/settings" \
org.cellcore.code.exec.ExecPullFromGatherer \
-i gatherer.json
```


Generating the configuration files (editions)
=============================================

Before crawling the prices in the different MTG webstores, you will need to generate a file describing the location of the editions (refered as `crawl config file`). This file will be later used by the crawler to download scrap the pages of the available editions.

The executable class ``ExecFetchPageList`` will help you accomplish this. This executable requires four paramenters in order to work.

* `-c EXTRACTOR_CLASS_NAME` specify the extractor class name. Each site have a different structure and therefore there is a particular extractor class for each supported webstore. For example, the "Magic Bazar" extractor is `org.cellcore.code.engine.page.extractor.mb.MBEditionsExtractor`. All extractos can be found under the package name  `org.cellcore.code.engine.page.extractor.*.*`

* `-url WEBSTORE_EDITIONS_URL` specify the webstore master index. Each webstore has a page were all the available MTG edition can be found.

* `-o OUTPUT_FILE` specify the program result location. The result of the execution of this program is a file, refered as`crawl config file`, that contains a list of the webstore edition location and configuration on how to crawl the webstore.

* `-f FILTER_FILE` specify the available editions and the possible names. The filter file is a json map containing localized names and MTG edition codes . For example, the french name "La Cinquième Aube" with the code "5DN". Such a file must be mantained periodically with new MTG releases.

Example execution (a sample editions file can be found [here](https://github.com/drfmunoz/mtg-price-crawler/blob/master/mtg.distrib/src/main/resources/data/editions.json)):


```
java -cp $MTGCRAWL_BIN"/lib/*":$MTGCRAWL_LOCATION"/settings" \
org.cellcore.code.exec.ExecFetchPageList \
-c org.cellcore.code.engine.page.extractor.mb.MBEditionsExtractor \
-url "http://www.magicbazar.fr/acheter-cartes-magic.php" \
-o mb-crawl-conf.json" \
-f editions.json
```

Crawling the price data
=======================

Once you have generated the `crawl config file` for the webstores you want to crawl, you may proceed to actually extract the price data. **Notice that the price data is not directly persisted into the database but rather stored into intermediate files that contains the prices. These files can be later merged into the database.**

The executable class ``ExecFetch`` will help you accomplish this. This executable requires three paramenters in order to work.

* `-i INPUT_LOCATIONS_FILE` specify the webstore `crawl config file`.
* `-o PRICE_OUTPUT_DIRECTORY` specify were the crawl data will be written.
* `-s MILISECONDS` (optional) specify an optional sleeptime between each page crawl in milliseconds. Sometime you may want to avoid overloading a server to avoid receiving wrong responses. In such cases you will use the sleep time option.

Example execution (a sample `crawl config file` can be found [here](https://github.com/drfmunoz/mtg-price-crawler/blob/master/mtg.distrib/src/main/resources/data/mb/mb-crawl-conf.json)).

```
java -cp $MTGCRAWL_BIN"/lib/*":$MTGCRAWL_LOCATION"/settings" \
org.cellcore.code.exec.ExecFetch \
-i mb-crawl-conf.json \
-o mb-data \
-s 2000
```



Data consolidation
=========================================

As mentioned before, crawling the site prices won't persist the data into the database but rather generate a bunch of files with the prices. In order to persist (consolidate or merge) the prices into the database you may use the executable class ``ExecConsolidate``. This executable requires three paramenters in order to work. 

* `-i` specify the crawl result directory.
* `-g` (optional) specify whethe to merge all the files in memory before persisting the content into the database.

```
java -cp $MTGCRAWL_BIN"/lib/*":$MTGCRAWL_LOCATION"/settings" \
org.cellcore.code.exec.ExecConsolidate \
-i mb-data -g
```


Using the bundled scripts
=========================

Under the distribution directory (typically named `Crawler-1.0-SNAPSHOT`) you will find a series of directories:

* `app`
	* `scripts` a series of pre-bunlded scripts to execute the crawler.
	* `lib` contains the binary libraries (you may place the mysql driver here).
	* `database` contains the database creating script.
* `settings` contains the database configuration;
* `data` the base bundled data. You may want to modify `editions.json` and `gatherer.json`.
* `logs` this directory will be created automatically.


Before using any of the bundled scripts you should make them executable ``chmod +x app/script/*`` ** Notice that these scripts will only work on Linux/OSX. Windows user may find themselves unsupported.**

* `bash app/script/initFromGatherer.sh`: initializes the database
* `bash app/script/generateIndexes.sh`: generates the `crawl config files` for the supported webstores.
* `bash app/script/crawAll.sh`: sequentially crawls each webstore and merges the data into the database
* `bash app/script/crawl_consolidate.sh 'webstore_code' 'WEBSTORE_CODE'`: crawls the webstore specified by webstore_code. For example `bash app/script/crawl_consolidate.sh 'mb' 'MB'`.


