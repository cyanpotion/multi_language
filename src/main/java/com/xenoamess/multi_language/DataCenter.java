package com.xenoamess.multi_language;

import com.xenoamess.x8l.ContentNode;
import com.xenoamess.x8l.TextNode;
import com.xenoamess.x8l.TreeNode;
import com.xenoamess.x8l.X8lTree;
import org.w3c.dom.Text;
import sun.reflect.generics.tree.Tree;

import javax.swing.text.AbstractDocument;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DataCenter {
    public X8lTree dataTree;

    public void loadFromMerge(File file) {
        X8lTree newX8lTree = null;
        try {
            newX8lTree = X8lTree.GetX8lTree(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (this.dataTree == null) {
            this.dataTree = newX8lTree;
        } else {
            mergeFromMerge(newX8lTree);
        }
    }

    public void mergeFromMerge(X8lTree newX8lTree) {
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);
        ContentNode nowRoot2 = newX8lTree.root.getContentNodesFromChildren(1).get(0);
        for (String key : nowRoot2.attributesKeyList) {
//            if (key.equals("language")) {
//                continue;
//            }
            String value = nowRoot2.attributes.get(key);
            if (nowRoot1.attributes.containsKey(key)) {
                nowRoot1.attributes.put(key, value);
            } else {
                nowRoot1.attributesKeyList.add(key);
                nowRoot1.attributes.put(key, value);
            }
        }

        ContentNode contentNodes[] = new ContentNode[nowRoot1.children.size()];

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode = (ContentNode) treeNode1;
            if (nowNode.attributesKeyList.isEmpty()) {
                continue;
            }
            int nowID = Integer.parseInt(nowNode.attributesKeyList.get(0));
            contentNodes[nowID] = nowNode;
        }

        for (TreeNode treeNode2 : nowRoot2.children) {
            if (!(treeNode2 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode = (ContentNode) treeNode2;
            if (nowNode.attributesKeyList.isEmpty()) {
                continue;
            }
            int nowID = Integer.parseInt(nowNode.attributesKeyList.get(0));
            Map<String, String> languageMap = new HashMap<String, String>();
            for (TreeNode treeNode11 : contentNodes[nowID].children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }

                if (contentNode11.children.isEmpty() || !(contentNode11.children.get(0) instanceof TreeNode)) {
                    continue;
                }
                TextNode textNode11 = (TextNode) contentNode11.children.get(0);
                String key = contentNode11.attributesKeyList.get(0);
                String value = textNode11.textContent;
                languageMap.put(key, value);
            }

            for (TreeNode treeNode12 : nowNode.children) {
                if (!(treeNode12 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode12 = (ContentNode) treeNode12;
                if (contentNode12.attributesKeyList.isEmpty()) {
                    continue;
                }
                if (contentNode12.children.isEmpty() || !(contentNode12.children.get(0) instanceof TreeNode)) {
                    continue;
                }
                TextNode textNode12 = (TextNode) contentNode12.children.get(0);
                String key = contentNode12.attributesKeyList.get(0);
                String value = textNode12.textContent;
                languageMap.put(key, value);
            }

            for (TreeNode treeNode11 : contentNodes[nowID].children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }

                if (contentNode11.children.isEmpty() || !(contentNode11.children.get(0) instanceof TreeNode)) {
                    continue;
                }
                TextNode textNode11 = (TextNode) contentNode11.children.get(0);
                String key = contentNode11.attributesKeyList.get(0);
//                String value = textNode11.textContent;
                textNode11.textContent = languageMap.get(key);
                languageMap.remove(key);
            }

//            for (Map.Entry<String, String> entry : languageMap.entrySet()) {
//                ContentNode contentNode = new ContentNode(contentNodes[nowID]);
//                contentNode.addAttribute(entry.getKey());
//                new TextNode(contentNode, entry.getValue());
//            }

            for (TreeNode treeNode12 : nowNode.children) {
//                int nowID = Integer.parseInt(nowNode.attributesKeyList.get(0));
//                contentNodes[nowID] = nowNode;

                if (!(treeNode12 instanceof ContentNode)) {
                    treeNode12.parent = contentNodes[nowID];
                    contentNodes[nowID].children.add(treeNode12);
                    continue;
                }
                ContentNode contentNode12 = (ContentNode) treeNode12;
                if (contentNode12.attributesKeyList.isEmpty()) {
                    treeNode12.parent = contentNodes[nowID];
                    contentNodes[nowID].children.add(treeNode12);
                    continue;
                }

                if (contentNode12.children.isEmpty() || !(contentNode12.children.get(0) instanceof TreeNode)) {
                    treeNode12.parent = contentNodes[nowID];
                    contentNodes[nowID].children.add(treeNode12);
                    continue;
                }
                TextNode textNode11 = (TextNode) contentNode12.children.get(0);
                String key = contentNode12.attributesKeyList.get(0);
//                String value = textNode11.textContent;
                if (languageMap.containsKey(key)) {
                    treeNode12.parent = contentNodes[nowID];
                    contentNodes[nowID].children.add(treeNode12);
                }
            }
        }
    }

    public void saveToMerge(File file) {
        try {
            Writer writer = new FileWriter(file);
            this.dataTree.output(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataCenter trim() {
        if (this.dataTree != null) {
            this.dataTree.trim();
        }
        return this;
    }

    public DataCenter format() {
        if (this.dataTree != null) {
            this.dataTree.format();
        }
        return this;
    }
}
