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
public class SingleLanguageStructure {
    public Map<String, String> textMap = new HashMap<String, String>();

    public String getText(String textID) {
        return textMap.get(textID);
    }

    public String putText(String textID, String textValue) {
        return textMap.put(textID, textValue);
    }
}
