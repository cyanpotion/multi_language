package com.xenoamess.multi_language;

import com.xenoamess.x8l.AbstractTreeNode;
import com.xenoamess.x8l.ContentNode;
import com.xenoamess.x8l.TextNode;
import com.xenoamess.x8l.X8lTree;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * @author XenoAmess
 */
public class MultiLanguageX8lFileUtil {
    public static final String VERSION = "0.42.0";
    public static final String MERGE = "merge";
    public static final String SPLIT = "split";
    public static final String LANGUAGE = "language";
    public X8lTree dataTree;


    public MultiLanguageX8lFileUtil() {
        dataTree = new X8lTree(null);
        dataTree.root = new ContentNode(null);
        ContentNode nowNode = new ContentNode(dataTree.root);
        nowNode.addAttribute(MERGE);
        nowNode.addAttribute("version=" + VERSION);
    }

    public MultiLanguageX8lFileUtil loadFromMerge(File file) throws IOException {
        X8lTree newX8lTree = null;
        newX8lTree = X8lTree.loadFromFile(file);
        if (!newX8lTree.root.getContentNodesFromChildren(1).get(0).attributes.containsKey(MERGE)) {
            throw new WrongFileTypeException();
        }
        if (this.dataTree == null) {
            this.dataTree = newX8lTree;
        } else {
            mergeFromMerge(newX8lTree);
        }
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromSplit(File file) throws IOException {
        X8lTree newX8lTree = null;

        newX8lTree = X8lTree.loadFromFile(file);
        if (!newX8lTree.root.getContentNodesFromChildren(1).get(0).attributes.containsKey(SPLIT)) {
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
        String languageName = nowRoot2.attributes.get(LANGUAGE);

        Map<String, ContentNode> contentNodes = new HashMap<>(nowRoot1.children.size());

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode1.getName();
            contentNodes.put(nowID, nowNode1);
        }

        for (AbstractTreeNode treeNode2 : nowRoot2.children) {
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
            String nowID2 = nowNode2.getName();
            if (!contentNodes.containsKey(nowID2)) {
                ContentNode contentNode2 = new ContentNode(nowRoot1);
                contentNode2.addAttribute(nowID2);
                treeNode2.parent = null;
                ((ContentNode) treeNode2).removeAttribute(nowID2);
                ((ContentNode) treeNode2).addAttribute(languageName);
                treeNode2.changeParentAndRegister(contentNode2);
            } else {
                ContentNode nowNode1 = contentNodes.get(nowID2);
                for (AbstractTreeNode treeNode11 : nowNode1.children) {
                    if ((treeNode11 instanceof ContentNode) &&
                            !((ContentNode) treeNode11).attributesKeyList.isEmpty() &&
                            ((ContentNode) treeNode11).getName().equals(languageName)) {
                        treeNode11.close();
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
//            if (key.equals(LANGUAGE)) {
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

        Map<String, ContentNode> contentNodes = new HashMap<>(nowRoot1.children.size());

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode1.getName();
            contentNodes.put(nowID, nowNode1);
        }

        for (AbstractTreeNode treeNode2 : nowRoot2.children) {
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
            String nowID2 = nowNode2.getName();
            if (!contentNodes.containsKey(nowID2)) {
                treeNode2.parent = null;
                treeNode2.changeParentAndRegister(nowRoot1);
                continue;
            }

            Map<String, TextNode> languageMap = new HashMap<>(nowRoot1.children.size());
            for (AbstractTreeNode treeNode11 : contentNodes.get(nowID2).children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }

                if (contentNode11.children.isEmpty() || !(contentNode11.children.get(0) instanceof AbstractTreeNode)) {
                    continue;
                }
                TextNode textNode11 = (TextNode) contentNode11.children.get(0);
                String key = contentNode11.getName();
                languageMap.put(key, textNode11);
            }


            for (AbstractTreeNode treeNode12 : nowNode2.children) {
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
                if (contentNode12.children.isEmpty() || !(contentNode12.children.get(0) instanceof AbstractTreeNode)) {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                    continue;
                }
                TextNode textNode12 = (TextNode) contentNode12.children.get(0);
                String key = contentNode12.getName();
                if (languageMap.containsKey(key)) {
                    languageMap.get(key).textContent = textNode12.textContent;
                } else {
                    treeNode12.parent = null;
                    treeNode12.changeParentAndRegister(contentNodes.get(nowID2));
                }
            }
        }
    }


    public void saveToMerge(File file) throws IOException {
        X8lTree.saveToFile(file, this.dataTree);
    }

    public void saveToSplit(File folderFile) throws IOException {
        if (folderFile.exists() && !folderFile.isDirectory()) {
            throw new WrongFileTypeException("saveToSplit need a folder as folderFile.");
        } else if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        Map<String, X8lTree> x8lTreeMap = splitToSplit();

        for (Map.Entry<String, X8lTree> entry : x8lTreeMap.entrySet()) {
            X8lTree.saveToFile(new File(folderFile.getAbsolutePath() + "/output_split_" + entry.getKey() + ".x8l"),
                    entry.getValue().trim().format());
        }
//        X8lTree.saveToFile(file, this.dataTree);
    }


    public Map<String, X8lTree> splitToSplit() {
        X8lTree newX8lTree = X8lTree.loadFromString(X8lTree.saveToString(this.dataTree));
        ContentNode nowRoot1 = newX8lTree.root.getContentNodesFromChildren(1).get(0);

        Map<String, X8lTree> res = new HashMap<>(nowRoot1.children.size());

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }

            List<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();
            for (AbstractTreeNode treeNode11 : nowNode1.children) {
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
                    nowRoot2.addAttribute(SPLIT);
                    nowRoot2.attributesKeyList.addAll(nowRoot1.attributesKeyList);
                    nowRoot2.attributes.putAll(nowRoot1.attributes);
                    nowRoot2.removeAttribute(MERGE);
                    nowRoot2.addAttribute(LANGUAGE, languageName);
                }
                nowRoot2 = x8lTree.root.getContentNodesFromChildren(1).get(0);
                ContentNode nowNode2 = new ContentNode(nowRoot2);
                nowNode2.addAttribute(nowNode1.getName());
                for (AbstractTreeNode treeNode : treeNodes) {
                    treeNode.parent = null;
                    treeNode.changeParentAndRegister(nowNode2);
                }
                for (AbstractTreeNode treeNode : nowNode11.children) {
                    treeNode.parent = null;
                    treeNode.changeParentAndRegister(nowNode2);
                }
                treeNodes = new ArrayList<AbstractTreeNode>();
            }
        }
        return res;
    }

    public MultiLanguageX8lFileUtil trim() {
        if (this.dataTree != null) {
            this.dataTree.trim();
        }
        return this;
    }

    public MultiLanguageX8lFileUtil format() {
        if (this.dataTree != null) {
            this.dataTree.format();
        }
        return this;
    }


    protected void sortSingleNode(Comparator<String> comparator, ContentNode nowNode1) {
        if (nowNode1.attributesKeyList.isEmpty()) {
            return;
        }
        Map<String, List<AbstractTreeNode>> contentNodeMap = new TreeMap<String, List<AbstractTreeNode>>(comparator);
        List<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();
        for (AbstractTreeNode treeNode11 : nowNode1.children) {
            treeNodes.add(treeNode11);
            if (!(treeNode11 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode11 = (ContentNode) treeNode11;
            if (nowNode11.attributesKeyList.isEmpty()) {
                continue;
            }
            String nowID = nowNode11.getName();
            contentNodeMap.put(nowID, treeNodes);
            treeNodes = new ArrayList<AbstractTreeNode>();
        }

        nowNode1.children.clear();
        for (Map.Entry<String, List<AbstractTreeNode>> entry : contentNodeMap.entrySet()) {
            nowNode1.children.addAll(entry.getValue());
        }
        nowNode1.children.addAll(treeNodes);
    }

    /**
     * for user's convenience, we completeMissingLanguageNodes before we sortLanguageNodes
     */
    public MultiLanguageX8lFileUtil sort(Comparator<String> comparator) {
        this.completeMissingLanguageNodes();
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            this.sortSingleNode(comparator, nowNode1);
        }
        this.sortSingleNode(comparator, nowRoot1);

        return this;
    }

    public MultiLanguageX8lFileUtil sort() {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
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


    public MultiLanguageX8lFileUtil completeMissingLanguageNodes() {
        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);
        Set<String> languageSet1 = new HashSet<String>();
        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            for (AbstractTreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }
                languageSet1.add(contentNode11.getName());
            }
        }

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            Set<String> languageSet2 = new HashSet<String>(languageSet1);

            for (AbstractTreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode contentNode11 = (ContentNode) treeNode11;
                if (contentNode11.attributesKeyList.isEmpty()) {
                    continue;
                }
                languageSet2.remove(contentNode11.getName());
            }

            for (String languageName : languageSet2) {
                ContentNode contentNode = new ContentNode(nowNode1);
                contentNode.addAttribute(languageName);
                new TextNode(contentNode, "");
            }
        }
        return this;
    }


    public MultiLanguageStructure parse() {
        this.completeMissingLanguageNodes().sort().trim();
        MultiLanguageStructure res = new MultiLanguageStructure();

        ContentNode nowRoot1 = this.dataTree.root.getContentNodesFromChildren(1).get(0);

        for (AbstractTreeNode treeNode1 : nowRoot1.children) {
            if (!(treeNode1 instanceof ContentNode)) {
                continue;
            }
            ContentNode nowNode1 = (ContentNode) treeNode1;
            if (nowNode1.attributesKeyList.isEmpty()) {
                continue;
            }
            String textID = nowNode1.getName();
            for (AbstractTreeNode treeNode11 : nowNode1.children) {
                if (!(treeNode11 instanceof ContentNode)) {
                    continue;
                }
                ContentNode nowNode11 = (ContentNode) treeNode11;
                if (nowNode11.attributesKeyList.isEmpty()) {
                    continue;
                }
                String languageName = nowNode11.getName();
                for (AbstractTreeNode treeNode111 : nowNode11.children) {
                    if (!(treeNode111 instanceof TextNode)) {
                        continue;
                    }
                    String textValue = ((TextNode) treeNode111).textContent;
                    res.putText(languageName, X8lTree.untranscode(textID), X8lTree.untranscode(textValue));
                }
            }
        }
        return res;
    }


    public static void generate(File file, int num) throws IOException {
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            out.println("<merge version=" + VERSION + ">");
            for (int i = 0; i < num; i++) {
                out.println("<" + i + ">");
                out.println("<ch>><en>>");
                out.println(">");
            }
            out.println(">");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        MultiLanguageX8lFileUtil multiLanguageUtil = new MultiLanguageX8lFileUtil();
        multiLanguageUtil.loadFromMerge(file);
        multiLanguageUtil.sort().trim().format().saveToMerge(file);
    }
}
