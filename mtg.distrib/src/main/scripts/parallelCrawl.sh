#!/bin/sh
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

BASE_SCRIPT_CC=$MTGCRAWL_BIN"/script/crawl_consolidate.sh"
# nohup $BASE_SCRIPT_CC SOURCE_LOWER SOURCE_UPPER [SLEEP_TIME_MS] &
echo $BASE_SCRIPT_CC
nohup $BASE_SCRIPT_CC 'mb' 'MB'  &
echo "MB crawl launched"
nohup $BASE_SCRIPT_CC 'mcc' 'MCC' &
echo "MCC crawl launched"
nohup $BASE_SCRIPT_CC 'mtgf' 'MTGF' & 
echo "MTGF crawl launched"
nohup $BASE_SCRIPT_CC 'mfrag' 'MFRAG' & 
echo "MFRAG crawl launched"
nohup $BASE_SCRIPT_CC 'pkg' 'PKG' & 
echo "PKG crawl launched"
#nohup $BASE_SCRIPT_CC 'stc' 'STC' 4000 &
#echo "STC crawl launched"
nohup $BASE_SCRIPT_CC 'mkt' 'MKT'&
echo "MKT crawl launched"
nohup $BASE_SCRIPT_CC 'mvl' 'MVL'&
echo "MVL crawl launched"
