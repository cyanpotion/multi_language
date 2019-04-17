import com.xenoamess.multi_language.MultiLanguageX8lFileUtil;

import java.io.File;

public class Tester {
    public static void main(String args[]) {
        try {
            MultiLanguageX8lFileUtil multiLanguageUtil = new MultiLanguageX8lFileUtil();
            multiLanguageUtil.loadFromMerge(new File("demo/merge.x8l"));
            multiLanguageUtil.loadFromMerge(new File("demo/merge2.x8l"));
            multiLanguageUtil.saveToMerge(new File("demo/product.x8l"));
            multiLanguageUtil.trim().saveToMerge(new File("demo/productTrim.x8l"));
            multiLanguageUtil.format().saveToMerge(new File("demo/productFormat.x8l"));
            multiLanguageUtil.completeMissingLanguageNodes().format().saveToMerge(new File("demo/productCompleteMissingLanguageNodeFormat.x8l"));
            multiLanguageUtil.sort().format().saveToMerge(new File("demo/productSortFormat.x8l"));
            multiLanguageUtil.loadFromSplit(new File("demo/split_urzinko.x8l")).format().saveToMerge(new File("demo/productSortFormat2.x8l"));
            multiLanguageUtil.saveToSplit(new File("demo"));
            multiLanguageUtil.completeMissingLanguageNodes().sort().trim().format().saveToMerge(new File("demo/finaloutput.x8l"));
            multiLanguageUtil.dataTree.show();
            MultiLanguageX8lFileUtil.generate(new File("demo/generate.x8l"), 1000);
            multiLanguageUtil.loadFromMerge(new File("demo/merge.x8l")).parse().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
