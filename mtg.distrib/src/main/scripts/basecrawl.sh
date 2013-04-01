#!/bin/sh
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

CRAWL_COMMAND="java -cp $MTGCRAWL_BIN"/lib/*":$MTGCRAWL_LOCATION"/settings" org.cellcore.code.exec".$@

echo "$0 - `date` - start $@"
$CRAWL_COMMAND 2>&1
echo "$0 - `date` - end $@"