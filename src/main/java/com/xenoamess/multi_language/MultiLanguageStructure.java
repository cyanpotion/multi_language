/*
 * MIT License
 *
 * Copyright (c) 2019 XenoAmess
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.xenoamess.multi_language;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XenoAmess
 * <p>
 * I will not restrict you from building this class's object for your own,
 * but it is strongly suggest that you first get a MultiLanguageX8lFileUtil, load a file,
 * and use MultiLanguageX8lFileUtil.parse to generate this MultiLanguageStructure object.
 */
public class MultiLanguageStructure {
    public static final String ARABIC = "arabic";
    public static final String BULGARIAN = "bulgarian";
    public static final String SCHINESE = "schinese";
    public static final String TCHINESE = "tchinese";
    public static final String CZECH = "czech";
    public static final String DANISH = "danish";
    public static final String DUTCH = "dutch";
    public static final String ENGLISH = "english";
    public static final String FINNISH = "finnish";
    public static final String FRENCH = "french";
    public static final String GERMAN = "german";
    public static final String GREEK = "greek";
    public static final String HUNGARIAN = "hungarian";
    public static final String ITALIAN = "italian";
    public static final String JAPANESE = "japanese";
    public static final String KOREANA = "koreana";
    public static final String NORWEGIAN = "norwegian";
    public static final String POLISH = "polish";
    public static final String PORTUGUESE = "portuguese";
    public static final String BRAZILIAN = "brazilian";
    public static final String ROMANIAN = "romanian";
    public static final String RUSSIAN = "russian";
    public static final String SPANISH = "spanish";
    public static final String LATAM = "latam";
    public static final String SWEDISH = "swedish";
    public static final String THAI = "thai";
    public static final String TURKISH = "turkish";
    public static final String UKRAINIAN = "ukrainian";
    public static final String VIETNAMESE = "vietnamese";


    private final Map<String, SingleLanguageStructure> languageMap = new HashMap<>();
    private String currentLanguage;

    public MultiLanguageStructure() {
        setCurrentLanguage("en");
    }


    /**
     * if the newCurrentLanguage not in languageMap, then do nothing and return false.
     *
     * @param newCurrentLanguage the new currentLanguage you want to set.
     * @return true if succeed, false if fails.
     */
    public boolean setCurrentLanguage(String newCurrentLanguage) {
        if (getLanguageMap().containsKey(newCurrentLanguage)) {
            currentLanguage = newCurrentLanguage;
            return true;
        } else {
            return false;
        }
    }

    public SingleLanguageStructure getSingleLanguageStructure(String languageName) {
        return getLanguageMap().get(languageName);
    }

    public String getText(String textID) {
        return this.getText(this.getCurrentLanguage(), textID);
    }

    public String getText(String languageName, String textID) {
        /*
         * languageName be "" means shall return the raw textID.
         * that is useful when debug,
         * ex. we press open debuge setting and then press a key and all text change to raw code./
         * that is also very useful when you need community translations from players.
         */
        if ("".equals(languageName)) {
            return textID;
        }

        SingleLanguageStructure singleLanguageStructure = getLanguageMap().get(languageName);
        if (singleLanguageStructure != null) {
            return singleLanguageStructure.getText(textID);
        } else {
            return null;
        }
    }

    public void putText(String languageName, String textID, String textValue) {
        SingleLanguageStructure singleLanguageStructure = getLanguageMap().get(languageName);
        if (singleLanguageStructure == null) {
            singleLanguageStructure = new SingleLanguageStructure();
            getLanguageMap().put(languageName, singleLanguageStructure);
        }
        singleLanguageStructure.putText(textID, textValue);
    }

    public void show() {
        System.out.println(this.getLanguageMap());
        for (Map.Entry<String, SingleLanguageStructure> entry : getLanguageMap().entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    public boolean ifLanguageNeedWordWrap(String languageName) {
        switch (languageName) {
            case SCHINESE:
            case TCHINESE:
            case JAPANESE:
            case KOREANA:
                return false;
            default:
                return true;
        }
    }

    public Map<String, SingleLanguageStructure> getLanguageMap() {
        return languageMap;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
