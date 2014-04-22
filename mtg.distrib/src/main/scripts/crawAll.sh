#!/bin/sh
#
# launches all the site crawlers in parallel
#
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

BASE_SCRIPT_CC=$MTGCRAWL_BIN"/script/crawl_consolidate.sh"
echo $BASE_SCRIPT_CC
echo "starting MB crawl"
$BASE_SCRIPT_CC 'mb' 'MB'
echo "MB crawl completed"
echo "starting MCC crawl"
$BASE_SCRIPT_CC 'mcc' 'MCC'
echo "MCC crawl completed"
echo "starting MTGF crawl"
$BASE_SCRIPT_CC 'mtgf' 'MTGF'
echo "MTGF crawl completed"
echo "starting MFRAG crawl"
$BASE_SCRIPT_CC 'mfrag' 'MFRAG'
echo "MFRAG crawl completed"
echo "starting PKG crawl"
$BASE_SCRIPT_CC 'pkg' 'PKG'
echo "PKG crawl completed"
echo "starting MKT crawl"
$BASE_SCRIPT_CC 'mkt' 'MKT'
echo "MKT crawl completed"
echo "starting MVL crawl"
$BASE_SCRIPT_CC 'mvl' 'MVL'
echo "MVL crawl completed"
