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
public class SingleLanguageStructure {
    private final Map<String, String> textMap = new HashMap<>();

    public String getText(String textID) {
        return textMap.get(textID);
    }

    public String putText(String textID, String textValue) {
        return textMap.put(textID, textValue);
    }
}
