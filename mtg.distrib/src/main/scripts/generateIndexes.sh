#!/bin/sh
if [ -z "$BASE_SCRIPT" ]; then
	SCRIPT_PATH=`dirname $0`
	source $SCRIPT_PATH"/settings.cfg"
fi

$BASE_SCRIPT ExecFetchPageList -c org.cellcore.code.engine.page.extractor.mb.MBEditionsExtractor -url "http://www.magicbazar.fr/acheter-cartes-magic.php" -o $RESOURCE"/mb/mb-crawl-conf.json" -f $FILE_FILTER    > $LOGS"/editionsMB_"$CURRENT_DATE".log" &
$BASE_SCRIPT ExecFetchPageList -c org.cellcore.code.engine.page.extractor.mcc.MCCEditionsExtractor -url "http://boutique.magiccorporation.com/cartes-magic-edition-121-innistrad.html" -o $RESOURCE"/mcc/mcc-crawl-conf.json" -f $FILE_FILTER  > $LOGS"/editionsMCC_"$CURRENT_DATE".log" &
$BASE_SCRIPT ExecFetchPageList -c org.cellcore.code.engine.page.extractor.mfrag.MFRAGEditionsExtractor -url "http://www.magicfrag.fr/liste-edition-carte-magic-20.html" -o $RESOURCE"/mfrag/mfrag-crawl-conf.json" -f $FILE_FILTER > $LOGS"/editionsMFRAG_"$CURRENT_DATE".log" &
$BASE_SCRIPT ExecFetchPageList -c org.cellcore.code.engine.page.extractor.mtgf.MTGFEditionsExtractor -url "http://cartes.mtgfrance.com/set.php" -o $RESOURCE"/mtgf/mtgf-crawl-conf.json" -f $FILE_FILTER > $LOGS"/editionsMTGF_"$CURRENT_DATE".log" &
$BASE_SCRIPT ExecFetchPageList -c org.cellcore.code.engine.page.extractor.pkg.PKGEditionsExtractor -url "http://www.parkage.com/produits-list.php?recherche=1&is_unite=1&is_rachat=0&fam=MAG&cat=&description=&foil=&numero=&rarete=&serie=75+-+Magic+2013&type=&tri=en&Submit.x=166&Submit.y=17&Submit=Ok" -o  $RESOURCE"/pkg/pkg-crawl-conf.json" -f $FILE_FILTER  > $LOGS"/editionsPKG_"$CURRENT_DATE".log" &
