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

import org.junit.jupiter.api.Test;

import java.io.File;

public class MultiLanguageStructureTest {
    @Test
    public void test() {
        System.out.println(PackageVersion.VERSION);
        try {
            MultiLanguageX8lFileUtil multiLanguageUtil = new MultiLanguageX8lFileUtil();
            multiLanguageUtil.loadFromMerge(new File("demo/merge.x8l"));
            multiLanguageUtil.loadFromMerge(new File("demo/merge2.x8l"));
            multiLanguageUtil.saveToMerge(new File("demo/product.x8l"));
            multiLanguageUtil.trim().saveToMerge(new File("demo/productTrim.x8l"));
            multiLanguageUtil.format().saveToMerge(new File("demo/productFormat.x8l"));
            multiLanguageUtil.completeMissingLanguageNodes().format().saveToMerge(new File("demo" +
                    "/productCompleteMissingLanguageNodeFormat.x8l"));
            multiLanguageUtil.sort().format().saveToMerge(new File("demo/productSortFormat.x8l"));
            multiLanguageUtil.loadFromSplit(new File("demo/split_urzinko.x8l")).format().saveToMerge(new File("demo" +
                    "/productSortFormat2.x8l"));
            multiLanguageUtil.saveToSplit(new File("demo"));
            multiLanguageUtil.completeMissingLanguageNodes().sort().trim().format().saveToMerge(new File("demo" +
                    "/finaloutput.x8l"));
            multiLanguageUtil.getDataTree().show();
            MultiLanguageX8lFileUtil.generate(new File("demo/generate.x8l"), 1000);
            multiLanguageUtil.loadFromMerge(new File("demo/merge.x8l")).parse().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
