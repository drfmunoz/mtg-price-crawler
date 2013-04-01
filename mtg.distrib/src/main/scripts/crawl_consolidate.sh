#!/bin/sh
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

SLEEP_OPT=""
if [[ $3 ]]; then
	SLEEP_OPT=" -s "$3
fi
# echo $CURRENT_DATE
$BASE_SCRIPT ExecFetch $SLEEP_OPT -i  $RESOURCE"/"$1 -o  $RESULTS > $LOGS"/crawl_"$1"_"$CURRENT_DATE".log"
$BASE_SCRIPT ExecConsolidate -g -i  $RESULTS"/"$2 > $LOGS"/concile_"$1"_"$CURRENT_DATE".log"