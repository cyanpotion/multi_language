package com.xenoamess.multi_language;

import java.util.HashMap;
import java.util.Map;

/*
 * I will not restrict you from building this class's object for your own,
 * but it is strongly suggest that you first get a MultiLanguageX8lFileUtil, load a file,
 * and use MultiLanguageX8lFileUtil.parse to generate this MultiLanguageStructure object.
 */
public class MultiLanguageStructure {
    public static final String arabic = "arabic";
    public static final String bulgarian = "bulgarian";
    public static final String schinese = "schinese";
    public static final String tchinese = "tchinese";
    public static final String czech = "czech";
    public static final String danish = "danish";
    public static final String dutch = "dutch";
    public static final String english = "english";
    public static final String finnish = "finnish";
    public static final String french = "french";
    public static final String german = "german";
    public static final String greek = "greek";
    public static final String hungarian = "hungarian";
    public static final String italian = "italian";
    public static final String japanese = "japanese";
    public static final String koreana = "koreana";
    public static final String norwegian = "norwegian";
    public static final String polish = "polish";
    public static final String portuguese = "portuguese";
    public static final String brazilian = "brazilian";
    public static final String romanian = "romanian";
    public static final String russian = "russian";
    public static final String spanish = "spanish";
    public static final String latam = "latam";
    public static final String swedish = "swedish";
    public static final String thai = "thai";
    public static final String turkish = "turkish";
    public static final String ukrainian = "ukrainian";
    public static final String vietnamese = "vietnamese";


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

    public boolean ifLanguageNeedWordWrap(String languageName) {
        switch (languageName) {
            case schinese:
                return false;
            case tchinese:
                return false;
            case japanese:
                return false;
            case koreana:
                return false;
            default:
                return true;
        }
    }

}
