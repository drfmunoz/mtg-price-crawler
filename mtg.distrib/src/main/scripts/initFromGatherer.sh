#!/bin/sh
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

$BASE_SCRIPT ExecPullFromGatherer  -i  $GATHERER