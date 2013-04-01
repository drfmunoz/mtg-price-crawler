MTG price crawler
=================

This is the back-end piece of the MTG price crawler that powers [leStack](http://www.lestack.fr)

Requirements
============

* Bash
* Java 1.6+
* Maven 3.0 (required for building)
* MysqlDriver - mysql-connector-java 5.1.18 (required to use a Mysql database)
* A Mysql database server (if you are using Mysql as your database backend)
* [mtg-price-viewer](http://github.com/drfmunoz/mtg-price-crawler) for prices visualization.

Building
========

Just execute `mvn clean install` and it will execute all the tests and produce the binary bundle (build result under `mtg.distrib/target/mtgcrawler-1.0-SNAPSHOT.zip`).

Setup
=====

* Set your base installation directory in the file `settings.cfg` (variable `MTGCRAWL_LOCATION` in the script located at app/script/settings.cfg)
** NOTE: If you just downloaded or produced `mtgcrawler-1.0-SNAPSHOT.zip`, the the root location for your installation will be the unzipped directory `mtgcrawler-1.0-SNAPSHOT` (use the fullpath to that directory e.g /home/freddy/Download/mtgcrawler-1.0-SNAPSHOT).
* If you are using [Mysql](http://www.mysql.com/) as your backend database, copy the Mysql driver ([mysql-connector-java 5.1.18](http://dev.mysql.com/downloads/connector/j/5.0.html)) into your installation directory library location (`$MTGCRAWL_LOCATION/app/lib`).
* Create the database structure using the script `database.sql` (original file created for Mysql, located at `$MTGCRAWL_LOCATION/database/database.sql`).

Launch the crawler
=============

* Launch the initial crawl using  ` bash app/script/parallelCrawl.sh` (from the root of your installation directory `$MTGCRAWL_LOCATION`)

License
=======

This software is distributed free of charge under the terms of the Apache-2.0 license.