package com.xenoamess.multi_language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * I will not restrict you from building this class's object for your own,
 * but it is strongly suggest that you first get a MultiLanguageX8lFileUtil, load a file,
 * and use MultiLanguageX8lFileUtil.parse to generate this MultiLanguageStructure object.
 */
public class MultiLanguageStructure {
    public Map<String, SingleLanguageStructure> languageMap = new HashMap<String, SingleLanguageStructure>();
    public String currentLanguage;

    public MultiLanguageStructure() {
        currentLanguage = "en";
    }


    public boolean setCurrentLanguage(String newCurrentLanguage) {
        if (languageMap.containsKey(newCurrentLanguage)) {
            currentLanguage = newCurrentLanguage;
            return true;
        } else {
            return false;
        }
    }

    public SingleLanguageStructure getSingleLanguageStructure(String languageName) {
        return languageMap.get(languageName);
    }

    public String getText(String textID) {
        return this.getText(this.currentLanguage, textID);
    }

    public String getText(String languageName, String textID) {
        /*
         * languageName be "" means shall return the raw textID.
         * that is useful when debug,
         * ex. we press open debuge setting and then press a key and all text change to raw code./
         * that is also very useful when you need community translations from players.
         */
        if (languageName.equals("")) {
            return textID;
        }

        SingleLanguageStructure singleLanguageStructure = languageMap.get(languageName);
        if (singleLanguageStructure != null) {
            return singleLanguageStructure.getText(textID);
        } else {
            return null;
        }
    }

    public void putText(String languageName, String textID, String textValue) {
        SingleLanguageStructure singleLanguageStructure = languageMap.get(languageName);
        if (singleLanguageStructure == null) {
            singleLanguageStructure = new SingleLanguageStructure();
            languageMap.put(languageName, singleLanguageStructure);
        }
        singleLanguageStructure.putText(textID, textValue);
    }

    public void show() {
        System.out.println(this.languageMap);
        for (Map.Entry<String, SingleLanguageStructure> entry : languageMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

}
