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

import com.xenoamess.x8l.AbstractTreeNode;
import com.xenoamess.x8l.ContentNode;
import com.xenoamess.x8l.TextNode;
import com.xenoamess.x8l.X8lTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.vfs2.FileObject;

/**
 * @author XenoAmess
 */
public class MultiLanguageX8lFileUtil {
    public static final String MERGE = "merge";
    public static final String SPLIT = "split";
    public static final String LANGUAGE = "language";
    private final X8lTree dataTree = new X8lTree();


    public MultiLanguageX8lFileUtil() {
        ContentNode nowNode = new ContentNode(getDataTree().getRoot());
        nowNode.addAttribute(MERGE);
        nowNode.addAttribute("version", PackageVersion.VERSION.toString());
    }

    public MultiLanguageX8lFileUtil loadFromMerge(File file) throws IOException {
        this.loadFromMerge(X8lTree.load(file));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromMerge(FileObject fileObject) throws IOException {
        this.loadFromMerge(X8lTree.load(fileObject));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromMerge(InputStream inputStream) throws IOException {
        this.loadFromMerge(X8lTree.load(inputStream));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromMerge(X8lTree x8lTree) {
        X8lTree newX8lTree = new X8lTree(x8lTree);
        if (!newX8lTree.getRoot().getContentNodesFromChildren(1).get(0).getAttributes().containsKey(MERGE)) {
            throw new WrongFileTypeException("merge file does not contain merge attribute.");
        }
        mergeFromMerge(newX8lTree);
        return this;
    }


    public MultiLanguageX8lFileUtil loadFromSplit(File file) throws IOException {
        this.loadFromSplit(X8lTree.load(file));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromSplit(FileObject fileObject) throws IOException {
        this.loadFromSplit(X8lTree.load(fileObject));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromSplit(InputStream inputStream) throws IOException {
        this.loadFromSplit(X8lTree.load(inputStream));
        return this;
    }

    public MultiLanguageX8lFileUtil loadFromSplit(X8lTree x8lTree) throws IOException {
        X8lTree newX8lTree = new X8lTree(x8lTree);
        if (!newX8lTree.getRoot().getContentNodesFromChildren(1).get(0).getAttributes().containsKey(SPLIT)) {
            throw new WrongFileTypeException("split file does not contain split attribute.");
        }
        mergeFromSplit(newX8lTree);
        return this;
    }


    public static Map<String, ContentNode> generateChildrenNameToChildrenNodeMap(ContentNode nowRoot) {
        Map<String, ContentNode> contentNodes = new HashMap<>(nowRoot.getChildren().size());
        for (AbstractTreeNode treeNode1 : nowRoot.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                if (!nowNode1.getAttributesKeyList().isEmpty()) {
                    String nowID = nowNode1.getName();
                    contentNodes.put(nowID, nowNode1);
                }
            }
        }
        return contentNodes;
    }

    public void mergeFromSplit(X8lTree newX8lTree) {
        ContentNode nowRoot1 = this.getDataTree().getRoot().getContentNodesFromChildren(1).get(0);
        ContentNode nowRoot2 = newX8lTree.getRoot().getContentNodesFromChildren(1).get(0);
        String languageName = nowRoot2.getAttributes().get(LANGUAGE);
        Map<String, ContentNode> contentNodes = generateChildrenNameToChildrenNodeMap(nowRoot1);

        for (AbstractTreeNode treeNode2 : nowRoot2.getChildren()) {
            if (treeNode2 instanceof ContentNode) {
                ContentNode nowNode2 = (ContentNode) treeNode2;
                if (!nowNode2.getAttributesKeyList().isEmpty()) {
                    String nowID2 = nowNode2.getName();
                    if (contentNodes.containsKey(nowID2)) {
                        ContentNode nowNode1 = contentNodes.get(nowID2);
                        for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
                            if ((treeNode11 instanceof ContentNode) &&
                                    !((ContentNode) treeNode11).getAttributesKeyList().isEmpty() &&
                                    ((ContentNode) treeNode11).getName().equals(languageName)) {
                                treeNode11.close();
                                break;
                            }
                        }
                        treeNode2.setParent(null);
                        treeNode2.changeParentAndRegister(nowNode1);
                        nowNode2.removeAttribute(nowID2);
                        nowNode2.addAttribute(languageName);
                    } else {
                        ContentNode contentNode2 = new ContentNode(nowRoot1);
                        contentNode2.addAttribute(nowID2);
                        treeNode2.setParent(null);
                        ((ContentNode) treeNode2).removeAttribute(nowID2);
                        ((ContentNode) treeNode2).addAttribute(languageName);
                        treeNode2.changeParentAndRegister(contentNode2);
                    }
                } else {
                    treeNode2.setParent(null);
                    treeNode2.changeParentAndRegister(nowRoot1);
                }
            } else {
                appendButIfBothBeTextNodeThenMerge(treeNode2, nowRoot1);
            }
        }
    }

    public void mergeFromMerge(X8lTree newX8lTree) {
        ContentNode nowRoot1 = this.getDataTree().getRoot().getContentNodesFromChildren(1).get(0);
        ContentNode nowRoot2 = newX8lTree.getRoot().getContentNodesFromChildren(1).get(0);
        for (String key : nowRoot2.getAttributesKeyList()) {
            String value = nowRoot2.getAttributes().get(key);
//            if (!nowRoot1.getAttributes().containsKey(key)) {
//                nowRoot1.getAttributesKeyList().add(key);
//            }
            nowRoot1.addAttribute(key, value);
        }

        Map<String, ContentNode> contentNodes = generateChildrenNameToChildrenNodeMap(nowRoot1);

        for (AbstractTreeNode treeNode2 : nowRoot2.getChildren()) {
            boolean fail0 = true;
            if (treeNode2 instanceof ContentNode) {
                ContentNode nowNode2 = (ContentNode) treeNode2;
                if (!nowNode2.getAttributesKeyList().isEmpty()) {
                    String nowID2 = nowNode2.getName();
                    if (contentNodes.containsKey(nowID2)) {
                        fail0 = false;
                        Map<String, TextNode> languageMap = new HashMap<>(nowRoot1.getChildren().size());
                        for (AbstractTreeNode treeNode11 : contentNodes.get(nowID2).getChildren()) {
                            if (treeNode11 instanceof ContentNode) {
                                ContentNode contentNode11 = (ContentNode) treeNode11;
                                if (!contentNode11.getAttributesKeyList().isEmpty() && !contentNode11.getChildren().isEmpty()) {
                                    AbstractTreeNode abstractTreeNode11 = contentNode11.getChildren().get(0);
                                    if (abstractTreeNode11 instanceof TextNode) {
                                        TextNode textNode11 = (TextNode) abstractTreeNode11;
                                        String key = contentNode11.getName();
                                        languageMap.put(key, textNode11);
                                    }
                                }
                            }
                        }

                        for (AbstractTreeNode treeNode12 : nowNode2.getChildren()) {
                            boolean fail1 = true;
                            if (treeNode12 instanceof ContentNode) {
                                ContentNode contentNode12 = (ContentNode) treeNode12;
                                if (!contentNode12.getAttributesKeyList().isEmpty() && !contentNode12.getChildren().isEmpty()) {
                                    AbstractTreeNode abstractTreeNode12 = contentNode12.getChildren().get(0);
                                    if (abstractTreeNode12 instanceof TextNode) {
                                        TextNode textNode12 = (TextNode) abstractTreeNode12;
                                        String key = contentNode12.getName();
                                        if (languageMap.containsKey(key)) {
                                            fail1 = false;
                                            languageMap.get(key).setTextContent(textNode12.getTextContent());
                                        }
                                    }
                                }
                            }
                            if (fail1) {
                                appendButIfBothBeTextNodeThenMerge(treeNode12, contentNodes.get(nowID2));
                            }
                        }
                    }
                }
            }
            if (fail0) {
                appendButIfBothBeTextNodeThenMerge(treeNode2, nowRoot1);
            }
        }
    }


    public void saveToMerge(File file) throws IOException {
        X8lTree.save(file, this.getDataTree());
    }

    public void saveToMerge(FileObject fileObject) throws IOException {
        X8lTree.save(fileObject, this.getDataTree());
    }

    public void saveToMerge(OutputStream outputStream) throws IOException {
        X8lTree.save(outputStream, this.getDataTree());
    }

    public void saveToSplit(File folderFile) throws IOException {
        if (folderFile.exists() && !folderFile.isDirectory()) {
            throw new WrongFileTypeException("saveToSplit need a folder as folderFile.");
        } else if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        Map<String, X8lTree> x8lTreeMap = splitToSplit();

        for (Map.Entry<String, X8lTree> entry : x8lTreeMap.entrySet()) {
            X8lTree.save(new File(folderFile.getAbsolutePath() + "/output_split_" + entry.getKey() +
                            ".x8l"),
                    entry.getValue().trim().format());
        }
    }

    public void saveToSplit(FileObject folderFileObject) throws IOException {
        if (folderFileObject.exists() && !folderFileObject.isFolder()) {
            throw new WrongFileTypeException("saveToSplit need a folder as folderFile.");
        } else if (!folderFileObject.exists()) {
            folderFileObject.createFolder();
        }
        Map<String, X8lTree> x8lTreeMap = splitToSplit();

        for (Map.Entry<String, X8lTree> entry : x8lTreeMap.entrySet()) {
            FileObject childFileObject = folderFileObject.getChild("output_split_" + entry.getKey() +
                    ".x8l");
            X8lTree.save(childFileObject,
                    entry.getValue().trim().format());
        }
    }

    public Map<String, X8lTree> splitToSplit() {
        X8lTree newX8lTree = X8lTree.load(X8lTree.save(this.getDataTree()));
        ContentNode nowRoot1 = newX8lTree.getRoot().getContentNodesFromChildren(1).get(0);

        Map<String, X8lTree> res = new HashMap<>(nowRoot1.getChildren().size());

        for (AbstractTreeNode treeNode1 : nowRoot1.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                if (!nowNode1.getAttributesKeyList().isEmpty()) {
                    List<AbstractTreeNode> treeNodes = new ArrayList<>();
                    for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
                        if (treeNode11 instanceof ContentNode) {
                            ContentNode nowNode11 = (ContentNode) treeNode11;
                            if (!nowNode11.getAttributesKeyList().isEmpty()) {
                                String languageName = nowNode11.getName();
                                X8lTree x8lTree = null;
                                ContentNode nowRoot2 = null;
                                if (res.containsKey(languageName)) {
                                    x8lTree = res.get(languageName);
                                } else {
                                    x8lTree = new X8lTree();
                                    res.put(languageName, x8lTree);
                                    nowRoot2 = new ContentNode(x8lTree.getRoot());
                                    nowRoot2.addAttribute(SPLIT);
//                                    nowRoot2.getAttributesKeyList().addAll(nowRoot1.getAttributesKeyList());
                                    for (Map.Entry<String, String> entry : nowRoot1.getAttributes().entrySet()) {
                                        nowRoot2.addAttribute(entry.getKey(), entry.getValue());
                                    }

                                    nowRoot2.removeAttribute(MERGE);
                                    nowRoot2.addAttribute(LANGUAGE, languageName);
                                }
                                nowRoot2 = x8lTree.getRoot().getContentNodesFromChildren(1).get(0);
                                ContentNode nowNode2 = new ContentNode(nowRoot2);
                                nowNode2.addAttribute(nowNode1.getName());
                                for (AbstractTreeNode treeNode : treeNodes) {
                                    appendButIfBothBeTextNodeThenMerge(treeNode, nowNode2);
                                }
                                for (AbstractTreeNode treeNode : nowNode11.getChildren()) {
                                    appendButIfBothBeTextNodeThenMerge(treeNode, nowNode2);
                                }
                                treeNodes = new ArrayList<>();
                            } else {
                                treeNodes.add(treeNode11);
                            }
                        } else {
                            treeNodes.add(treeNode11);
                        }
                    }
                }
            }
        }
        return res;
    }

    public MultiLanguageX8lFileUtil trim() {
        this.getDataTree().trim();
        return this;
    }

    public MultiLanguageX8lFileUtil format() {
        this.getDataTree().format();
        return this;
    }


    protected void sortSingleNode(Comparator<String> comparator, ContentNode nowNode1) {
        if (nowNode1.getAttributesKeyList().isEmpty()) {
            return;
        }
        Map<String, List<AbstractTreeNode>> contentNodeMap = new TreeMap<>(comparator);
        List<AbstractTreeNode> treeNodes = new ArrayList<>();
        for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
            treeNodes.add(treeNode11);
            if (treeNode11 instanceof ContentNode) {
                ContentNode nowNode11 = (ContentNode) treeNode11;
                if (!nowNode11.getAttributesKeyList().isEmpty()) {
                    String nowID = nowNode11.getName();
                    contentNodeMap.put(nowID, treeNodes);
                    treeNodes = new ArrayList<>();
                }
            }
        }

        nowNode1.getChildren().clear();
        for (Map.Entry<String, List<AbstractTreeNode>> entry : contentNodeMap.entrySet()) {
            nowNode1.getChildren().addAll(entry.getValue());
        }
        nowNode1.getChildren().addAll(treeNodes);
    }

    /**
     * for user's convenience, we completeMissingLanguageNodes before we sortLanguageNodes
     */
    public MultiLanguageX8lFileUtil sort(Comparator<String> comparator) {
        this.completeMissingLanguageNodes();
        ContentNode nowRoot1 = this.getDataTree().getRoot().getContentNodesFromChildren(1).get(0);

        for (AbstractTreeNode treeNode1 : nowRoot1.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                this.sortSingleNode(comparator, nowNode1);
            }
        }

        this.sortSingleNode(comparator, nowRoot1);
        return this;
    }

    public MultiLanguageX8lFileUtil sort() {
        Comparator<String> comparator = (String s1, String s2) -> {
            BigInteger int1 = null;
            BigInteger int2 = null;

            boolean flag1;
            boolean flag2;

            try {
                int1 = new BigInteger(s1);
                flag1 = true;
            } catch (NumberFormatException e) {
                flag1 = false;
            }

            try {
                int2 = new BigInteger(s2);
                flag2 = true;
            } catch (NumberFormatException e) {
                flag2 = false;
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
        };

        return this.sort(comparator);
    }


    public MultiLanguageX8lFileUtil completeMissingLanguageNodes() {
        ContentNode nowRoot1 = this.getDataTree().getRoot().getContentNodesFromChildren(1).get(0);
        Set<String> languageSet1 = new HashSet<>();
        for (AbstractTreeNode treeNode1 : nowRoot1.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                if (!nowNode1.getAttributesKeyList().isEmpty()) {
                    for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
                        if (treeNode11 instanceof ContentNode) {
                            ContentNode contentNode11 = (ContentNode) treeNode11;
                            if (!contentNode11.getAttributesKeyList().isEmpty()) {
                                languageSet1.add(contentNode11.getName());
                            }
                        }
                    }
                }
            }
        }

        for (AbstractTreeNode treeNode1 : nowRoot1.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                if (!nowNode1.getAttributesKeyList().isEmpty()) {
                    Set<String> languageSet2 = new HashSet<>(languageSet1);
                    for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
                        if (treeNode11 instanceof ContentNode) {
                            ContentNode contentNode11 = (ContentNode) treeNode11;
                            if (!contentNode11.getAttributesKeyList().isEmpty()) {
                                languageSet2.remove(contentNode11.getName());
                            }
                        }
                    }

                    for (String languageName : languageSet2) {
                        ContentNode contentNode = new ContentNode(nowNode1);
                        contentNode.addAttribute(languageName);
                        new TextNode(contentNode, "");
                    }
                }
            }
        }
        return this;
    }


    public MultiLanguageStructure parse() {
        this.completeMissingLanguageNodes().sort().trim();
        MultiLanguageStructure res = new MultiLanguageStructure();

        ContentNode nowRoot1 = this.getDataTree().getRoot().getContentNodesFromChildren(1).get(0);

        for (AbstractTreeNode treeNode1 : nowRoot1.getChildren()) {
            if (treeNode1 instanceof ContentNode) {
                ContentNode nowNode1 = (ContentNode) treeNode1;
                if (!nowNode1.getAttributesKeyList().isEmpty()) {
                    String textID = nowNode1.getName();
                    for (AbstractTreeNode treeNode11 : nowNode1.getChildren()) {
                        if (treeNode11 instanceof ContentNode) {
                            ContentNode nowNode11 = (ContentNode) treeNode11;
                            if (!nowNode11.getAttributesKeyList().isEmpty()) {
                                String languageName = nowNode11.getName();
                                for (AbstractTreeNode treeNode111 : nowNode11.getChildren()) {
                                    if (treeNode111 instanceof TextNode) {
                                        String textValue = ((TextNode) treeNode111).getTextContent();
                                        res.putText(
                                                languageName,
                                                textID,
                                                textValue
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }


    public static void generate(File file, int num) throws IOException {
        try (PrintStream out = new PrintStream(new FileOutputStream(file), true, "UTF-8")) {
            out.println("<merge version=" + PackageVersion.VERSION + ">");
            for (int i = 0; i < num; i++) {
                out.println("<" + i + ">");
                out.println("<ch>><en>>");
                out.println(">");
            }
            out.println(">");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MultiLanguageX8lFileUtil multiLanguageUtil = new MultiLanguageX8lFileUtil();
        multiLanguageUtil.loadFromMerge(file);
        multiLanguageUtil.sort().trim().format().saveToMerge(file);
    }

    public X8lTree getDataTree() {
        return dataTree;
    }

    static void appendButIfBothBeTextNodeThenMerge(AbstractTreeNode newChild, ContentNode newParent) {
        if (newChild instanceof TextNode) {
            final List<AbstractTreeNode> nowChildren = newParent.getChildren();
            if (!nowChildren.isEmpty()) {
                final AbstractTreeNode nowLastChild =
                        nowChildren.get(nowChildren.size() - 1);
                if (nowLastChild instanceof TextNode) {
                    final TextNode nowLastChildTextNode = (TextNode) nowLastChild;
                    nowLastChildTextNode.setTextContent(
                            nowLastChildTextNode.getTextContent()
                                    + ((TextNode) newChild).getTextContent()
                    );
                    newChild.setParent(null);
                    return;
                }
            }
        }
        newChild.setParent(null);
        newChild.changeParentAndRegister(newParent);
    }
}
