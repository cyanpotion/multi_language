import com.xenoamess.multi_language.DataCenter;

import java.io.File;

public class TestDataCenter {
    public static void main(String args[]) {
        DataCenter dataCenter = new DataCenter();
        dataCenter.loadFromMerge(new File("demo/merge.x8l"));
        dataCenter.loadFromMerge(new File("demo/merge2.x8l"));
        dataCenter.saveToMerge(new File("demo/product.x8l"));
        dataCenter.trim().saveToMerge(new File("demo/productTrim.x8l"));
        dataCenter.format().saveToMerge(new File("demo/productFormat.x8l"));
        dataCenter.completeMissingLanguageNodes().format().saveToMerge(new File("demo/productCompleteMissingLanguageNodeFormat.x8l"));
        dataCenter.sortLanguageNodes().format().saveToMerge(new File("demo/productSortFormat.x8l"));
        dataCenter.dataTree.show();
    }
}
