package com.xenoamess.multi_language;

import com.xenoamess.x8l.ContentNode;
import com.xenoamess.x8l.TextNode;
import com.xenoamess.x8l.TreeNode;
import com.xenoamess.x8l.X8lTree;

import java.io.*;
import java.math.BigInteger;
import java.util.*;


public class DataCenter {
    public X8lTree dataTree;


    public DataCenter() {
        dataTree = new X8lTree(null);
        dataTree.root = new ContentNode(null);
        ContentNode nowNode = new ContentNode(dataTree.root);
        nowNode.addAttribute("merge");
        nowNode.addAttribute("version=0.00");
    }

    public DataCenter loadFromMerge(File file) {
        X8lTree newX8lTree = null;
        newX8lTree = X8lTree.LoadFromFile(file);
        if (!newX8lTree.root.getContentNodesFromChildren(1).get(0).attributes.containsKey("merge")) {
            throw new WrongFileTypeException();
        }
        if (this.dataTree == null) {
            this.dataTree = newX8lTree;
        } else {
            mergeFromMerge(newX8lTree);
        }

        return this;
    }

    public DataCenter loadFromSplit(File file) {
        X8lTree newX8lTree = null;

        newX8lTree = X8lTree.LoadFromFile(file);
        if (!newX8lTree.root.getContentNodesFromChildren(1).get(0).attributes.containsKey("split")) {
            throw new WrongFileTypeException();
        }

        if (this.dataTree == null) {
            this.dataTree = newX8lTree;
        } else {
            mergeFromSplit(newX8lTree);
        }
        return this;
    }

    public void mergeFromSplit(X8lTree newX8lTree) {
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);
        ContentNode nowRoot2 = newX8lTree.root.getContentNodesFromChildren(1).get(0);
        String languageName = nowRoot2.attributes.get("language");

        Map<String, ContentNode> contentNodes = new HashMap<String, ContentNode>();

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode1.attributesKeyList.get(0);
            contentNodes.put(nowID, nowNode1);
        }

        for (TreeNode treeNode2 : nowRoot2.children) {
            if (!(treeNode2 instanceof ContentNode)) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }
            ContentNode nowNode2 = (ContentNode) treeNode2;
            if (nowNode2.attributesKeyList.isEmpty()) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }
            String nowID2 = nowNode2.attributesKeyList.get(0);
            if (!contentNodes.containsKey(nowID2)) {
                ContentNode contentNode2 = new ContentNode(nowRoot1);
                contentNode2.addAttribute(nowID2);
                treeNode2.parent = null;
                ((ContentNode) treeNode2).removeAttribute(nowID2);
                ((ContentNode) treeNode2).addAttribute(languageName);
                treeNode2.changeParentAndRegister(contentNode2);
            } else {
                ContentNode nowNode1 = contentNodes.get(nowID2);
                for (TreeNode treeNode11 : nowNode1.children) {
                    if ((treeNode11 instanceof ContentNode) &&
                            !((ContentNode) treeNode11).attributesKeyList.isEmpty() &&
                            ((ContentNode) treeNode11).attributesKeyList.get(0).equals(languageName)) {
                        treeNode11.destroy();
                        break;
                    }
                }
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowNode1);
                nowNode2.removeAttribute(nowID2);
                nowNode2.addAttribute(languageName);
            }
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

//        ContentNode contentNodes[] = new ContentNode[nowRoot1.children.size()];

        Map<String, ContentNode> contentNodes = new HashMap<String, ContentNode>();

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode1.attributesKeyList.get(0);
            contentNodes.put(nowID, nowNode1);
        }

        for (TreeNode treeNode2 : nowRoot2.children) {
            if (!(treeNode2 instanceof ContentNode)) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }
            ContentNode nowNode2 = (ContentNode) treeNode2;
            if (nowNode2.attributesKeyList.isEmpty()) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }
            String nowID2 = nowNode2.attributesKeyList.get(0);
            if (!contentNodes.containsKey(nowID2)) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }

            Map<String, TextNode> languageMap = new HashMap<String, TextNode>();
            for (TreeNode treeNode11 : contentNodes.get(nowID2).children) {
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
                languageMap.put(key, textNode11);
            }


            for (TreeNode treeNode12 : nowNode2.children) {
                if (!(treeNode12 instanceof ContentNode)) {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                    continue;
                }
                ContentNode contentNode12 = (ContentNode) treeNode12;
                if (contentNode12.attributesKeyList.isEmpty()) {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                    continue;
                }
                if (contentNode12.children.isEmpty() || !(contentNode12.children.get(0) instanceof TreeNode)) {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                    continue;
                }
                TextNode textNode12 = (TextNode) contentNode12.children.get(0);
                String key = contentNode12.attributesKeyList.get(0);
                if (languageMap.containsKey(key)) {
                    languageMap.get(key).textContent = textNode12.textContent;
                } else {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                }
            }

//            for (TreeNode treeNode11 : contentNodes.get(nowID).children) {
//                if (!(treeNode11 instanceof ContentNode)) {
//                    continue;
//                }
//                ContentNode contentNode11 = (ContentNode) treeNode11;
//                if (contentNode11.attributesKeyList.isEmpty()) {
//                    continue;
//                }
//
//                if (contentNode11.children.isEmpty() || !(contentNode11.children.get(0) instanceof TreeNode)) {
//                    continue;
//                }
//                TextNode textNode11 = (TextNode) contentNode11.children.get(0);
//                String key = contentNode11.attributesKeyList.get(0);
////                String value = textNode11.textContent;
//                textNode11.textContent = languageMap.get(key);
//                languageMap.remove(key);
//            }
//
////            for (Map.Entry<String, String> entry : languageMap.entrySet()) {
////                ContentNode contentNode = new ContentNode(contentNodes[nowID]);
////                contentNode.addAttribute(entry.getKey());
////                new TextNode(contentNode, entry.getValue());
////            }
//
//            for (TreeNode treeNode12 : nowNode.children) {
////                String nowID = Integer.parseInt(nowNode.attributesKeyList.get(0));
////                contentNodes[nowID] = nowNode;
//
//                if (!(treeNode12 instanceof ContentNode)) {
//                    treeNode12.changeParentAndRegister(contentNodes[nowID]);
////                    treeNode12.parent = contentNodes[nowID];
////                    contentNodes[nowID].children.add(treeNode12);
//                    continue;
//                }
//                ContentNode contentNode12 = (ContentNode) treeNode12;
//                if (contentNode12.attributesKeyList.isEmpty()) {
//                    treeNode12.changeParentAndRegister(contentNodes[nowID]);
////                    treeNode12.parent = contentNodes[nowID];
////                    contentNodes[nowID].children.add(treeNode12);
//                    continue;
//                }
//
//                if (contentNode12.children.isEmpty() || !(contentNode12.children.get(0) instanceof TreeNode)) {
//                    treeNode12.changeParentAndRegister(contentNodes[nowID]);
////                    treeNode12.parent = contentNodes[nowID];
////                    contentNodes[nowID].children.add(treeNode12);
//                    continue;
//                }
//                TextNode textNode11 = (TextNode) contentNode12.children.get(0);
//                String key = contentNode12.attributesKeyList.get(0);
////                String value = textNode11.textContent;
//                if (languageMap.containsKey(key)) {
//                    treeNode12.changeParentAndRegister(contentNodes[nowID]);
////                    treeNode12.parent = contentNodes[nowID];
////                    contentNodes[nowID].children.add(treeNode12);
//                }
//            }
        }
    }


    public void saveToMerge(File file) {
        X8lTree.SaveToFile(file, this.dataTree);
    }

    public void saveToSplit(File folderFile) {
        if (folderFile.exists() && !folderFile.isDirectory()) {
            throw new WrongFileTypeException("saveToSplit need a folder as folderFile.");
        } else if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        Map<String, X8lTree> x8lTreeMap = splitToSplit();

        for (Map.Entry<String, X8lTree> entry : x8lTreeMap.entrySet()) {
            X8lTree.SaveToFile(new File(folderFile.getAbsolutePath() + "/output_split_" + entry.getKey() + ".x8l"), entry.getValue().trim().format());
        }
//        X8lTree.SaveToFile(file, this.dataTree);
    }


    public Map<String, X8lTree> splitToSplit() {
        Map<String, X8lTree> res = new HashMap<String, X8lTree>();
        X8lTree newX8lTree = X8lTree.LoadFromString(X8lTree.SaveToString(this.dataTree));

        ContentNode nowRoot1 = newX8lTree.root.getContentNodesFromChildren(1).get(0);

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }

            List<TreeNode> treeNodes = new ArrayList<TreeNode>();
            for (TreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    treeNodes.add(treeNode11);
                    continue;
                }
                ContentNode nowNode11 = (ContentNode) treeNode11;
                if (nowNode11.attributesKeyList.isEmpty()) {
                    treeNodes.add(treeNode11);
                    continue;
                }
                String languageName = nowNode11.getName();
                X8lTree x8lTree = null;
                ContentNode nowRoot2 = null;
                if (res.containsKey(languageName)) {
                    x8lTree = res.get(languageName);
                } else {
                    x8lTree = new X8lTree(null);
                    res.put(languageName, x8lTree);
                    x8lTree.root = new ContentNode(null);
                    nowRoot2 = new ContentNode(x8lTree.root);
                    nowRoot2.addAttribute("split");
                    nowRoot2.attributesKeyList.addAll(nowRoot1.attributesKeyList);
                    nowRoot2.attributes.putAll(nowRoot1.attributes);
                    nowRoot2.removeAttribute("merge");
                    nowRoot2.addAttribute("language", languageName);
                }
                nowRoot2 = x8lTree.root.getContentNodesFromChildren(1).get(0);
                ContentNode nowNode2 = new ContentNode(nowRoot2);
                nowNode2.addAttribute(nowNode1.getName());
                for (TreeNode treeNode : treeNodes) {
                    treeNode.parent = null;
                    treeNode.changeParentAndRegister(nowNode2);
                }
                for (TreeNode treeNode : nowNode11.children) {
                    treeNode.parent = null;
                    treeNode.changeParentAndRegister(nowNode2);
                }
                treeNodes = new ArrayList<TreeNode>();
            }
        }
        return res;
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


    protected void sortSingleNode(Comparator<String> comparator, ContentNode nowNode1) {
        if (nowNode1.attributesKeyList.isEmpty()) {
            return;
        }
        Map<String, List<TreeNode>> contentNodeMap = new TreeMap<String, List<TreeNode>>(comparator);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (TreeNode treeNode11 : nowNode1.children) {
            treeNodes.add(treeNode11);
            if (!(treeNode11 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode11 = (ContentNode) treeNode11;
            if (nowNode11.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode11.attributesKeyList.get(0);
            contentNodeMap.put(nowID, treeNodes);
            treeNodes = new ArrayList<TreeNode>();
        }

        nowNode1.children.clear();
        for (Map.Entry<String, List<TreeNode>> entry : contentNodeMap.entrySet()) {
            nowNode1.children.addAll(entry.getValue());
        }
        nowNode1.children.addAll(treeNodes);
    }

    /*
     * for user's convenience, we completeMissingLanguageNodes before we sortLanguageNodes
     */
    public DataCenter sort(Comparator<String> comparator) {
        this.completeMissingLanguageNodes();
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            this.sortSingleNode(comparator, nowNode1);
        }
        this.sortSingleNode(comparator, nowRoot1);

        return this;
    }

    public DataCenter sort() {
        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String s1, String s2) {
                BigInteger int1 = null;
                BigInteger int2 = null;

                boolean flag1 = false;
                boolean flag2 = false;

                try {
                    int1 = new BigInteger(s1);
                    flag1 = true;
                } catch (NumberFormatException e) {
                }

                try {
                    int2 = new BigInteger(s2);
                    flag2 = true;
                } catch (NumberFormatException e) {
                }
                if (flag1 && !flag2) {
                    return -1;
                }
                if (flag2 && !flag1) {
                    return 1;
                }
                if (flag1 && flag2) {
                    return int1.compareTo(int2);
                }
                return s1.compareTo(s2);
            }
        };

        return this.sort(comparator);
    }


    public DataCenter completeMissingLanguageNodes() {
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);

        Set<String> languageSet1 = new HashSet<String>();

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            for (TreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }
                languageSet1.add(contentNode11.attributesKeyList.get(0));
            }
        }

        for (TreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            Set<String> languageSet2 = new HashSet<String>(languageSet1);

            for (TreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }
                languageSet2.remove(contentNode11.attributesKeyList.get(0));
            }

            for (String languageName : languageSet2) {
                ContentNode contentNode = new ContentNode(nowNode1);
                contentNode.addAttribute(languageName);
                new TextNode(contentNode, "");
            }
        }
        return this;
    }
}
