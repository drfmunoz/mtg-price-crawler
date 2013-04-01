package org.cellcore.code.model;

import org.cellcore.code.engine.page.extractor.AbstractPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mb.MBPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mcc.MCCPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mfrag.MFRAGPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mkt.MKTPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mtgf.MTGFPageDataExtractor;
import org.cellcore.code.engine.page.extractor.mvl.MVLPageDataExtractor;
import org.cellcore.code.engine.page.extractor.pkg.PKGPageDataExtractor;
import org.cellcore.code.engine.page.extractor.starcity.STCPageDataExtractor;

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
public enum PriceSources {
    MB("magicbazar.fr", MBPageDataExtractor.class),//www.magicbazar.fr
    MCC("magiccorporation.fr", MCCPageDataExtractor.class),//www.magiccorporation.fr
    MKT("magiccardmarket.eu", MKTPageDataExtractor.class),//www.magiccardmarket.eu/
    MFRAG("magicfrag.fr", MFRAGPageDataExtractor.class),//http://www.magicfrag.fr/
    MTGF("mtgfrance.com", MTGFPageDataExtractor.class),
    STC("starcitygames.com", STCPageDataExtractor.class),
    PKG("parkage.com", PKGPageDataExtractor.class),
    MVL("magic-ville.com", MVLPageDataExtractor.class);

    private final String printName;
    private final Class<? extends AbstractPageDataExtractor> pageDataExtractorClass;

    PriceSources(String name, Class<? extends AbstractPageDataExtractor> pageDataExtractorClass) {
        this.printName = name;
        this.pageDataExtractorClass = pageDataExtractorClass;
    }

    public String getPrintName() {
        return this.printName;
    }

    public Class<? extends AbstractPageDataExtractor> getPageDataExtractorClass() {
        return pageDataExtractorClass;
    }
}
