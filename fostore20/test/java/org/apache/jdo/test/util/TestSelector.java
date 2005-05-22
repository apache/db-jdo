/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.test.util;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import junit.runner.TestCollector;

/**
 * A test class selector. A simple dialog to pick the name of a test suite.
 */
public class TestSelector extends JDialog {
    private JButton fCancel;
    private JButton fOk;
    private JList fList;
    private JTree fTree;
    private JScrollPane fScrolledList;
    private JLabel fDescription;
    private List fSelectedItems;
    
    /** */
    public TestSelector(Frame parent, TestCollector testCollector) {
        super(parent, true);
        setSize(500, 500);
        // setLocationRelativeTo only exists in 1.4
        try {
            setLocationRelativeTo(parent);
        } catch (NoSuchMethodError e) {
            centerWindow();
        }
        setTitle("Test Selector");
        
        Vector list = null;
        try {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            list= createTestList(testCollector);
        } finally {
            parent.setCursor(Cursor.getDefaultCursor());
        }
        
        fTree = new JTree(createTreeModel(list));
        //fTree.getSelectionModel().setSelectionMode(
        //    TreeSelectionModel.SINGLE_TREE_SELECTION);
        fScrolledList= new JScrollPane(fTree);

        fCancel= new JButton("Cancel");
        fDescription= new JLabel("Select the Test class:");
        fOk= new JButton("OK");
        fOk.setEnabled(false);
        getRootPane().setDefaultButton(fOk);
        
        defineLayout();
        addListeners();
    }

    /** */
    public boolean isEmpty() {
        return ((TreeNode)fTree.getModel().getRoot()).getChildCount() == 0;
    }
    
    /** */
    public List getSelectedItems() {
        return fSelectedItems;
    }

    /** */
    public void checkEnableOK(TreeSelectionEvent e) {
        boolean enabled = false;
        TreePath[] paths = fTree.getSelectionPaths();
        if (paths != null) {
        	for (int i = 0; i < paths.length; i++) {
                if (((TreeNode)paths[i].getLastPathComponent()).isLeaf()) {
                    enabled = true;
                    break;
                }      
            }
        }
        fOk.setEnabled(enabled);
    }
    
    /** */
    public void okSelected() {
        List classNames = new ArrayList();
        TreePath[] paths = fTree.getSelectionPaths();
        if (paths != null) {
        	for (int i = 0; i < paths.length; i++) {
                Object selected = paths[i].getLastPathComponent();
                if (selected instanceof ClassNameTreeNode) {
                     classNames.add(((ClassNameTreeNode)selected).getClassName());
                }
        	}
        }
        
        fSelectedItems = classNames;
        dispose();
    }
    
    /** */
    private TreeModel createTreeModel(List classNames) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Test Classes");
        TreeModel model = new DefaultTreeModel(root);
        
        String currentPackageName = null;
        DefaultMutableTreeNode currentPackageNode = null;
        DefaultMutableTreeNode parent = root;
        for (Iterator i = classNames.iterator(); i.hasNext();) {
            String className = (String)i.next();
            int index = className.lastIndexOf('.');
            String packageName = (index >= 0) ? className.substring(0, index) : "";
            if ((currentPackageName == null) || !currentPackageName.equals(packageName)) {
                currentPackageName = packageName;
                currentPackageNode = new DefaultMutableTreeNode(currentPackageName);
                parent.add(currentPackageNode);
            }
            currentPackageNode.add(new ClassNameTreeNode(className));
        }
        return model;
    }
    
    /** */
    private void centerWindow() {
        Dimension paneSize = getSize();
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((screenSize.width-paneSize.width)/2, (screenSize.height-paneSize.height)/2);
    }
    
    /** */
    private void addListeners() {
        fCancel.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        
        fOk.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okSelected();
                }
            });

        fTree.addTreeSelectionListener(
            new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    checkEnableOK(e);
                }
            });
        
        fTree.addMouseListener(
            new MouseAdapter () {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        okSelected();
                    }
                }
            });

        addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            }
            );
    }
    
    /** */
    private void defineLayout() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx= 0; labelConstraints.gridy= 0;
        labelConstraints.gridwidth= 1; labelConstraints.gridheight= 1;
        labelConstraints.fill= GridBagConstraints.BOTH;
        labelConstraints.anchor= GridBagConstraints.WEST;
        labelConstraints.weightx= 1.0;
        labelConstraints.weighty= 0.0;
        labelConstraints.insets= new Insets(8, 8, 0, 8);
        getContentPane().add(fDescription, labelConstraints);

        GridBagConstraints listConstraints = new GridBagConstraints();
        listConstraints.gridx= 0; listConstraints.gridy= 1;
        listConstraints.gridwidth= 4; listConstraints.gridheight= 1;
        listConstraints.fill= GridBagConstraints.BOTH;
        listConstraints.anchor= GridBagConstraints.CENTER;
        listConstraints.weightx= 1.0;
        listConstraints.weighty= 1.0;
        listConstraints.insets= new Insets(8, 8, 8, 8);
        getContentPane().add(fScrolledList, listConstraints);
        
        GridBagConstraints okConstraints= new GridBagConstraints();
        okConstraints.gridx= 2; okConstraints.gridy= 2;
        okConstraints.gridwidth= 1; okConstraints.gridheight= 1;
        okConstraints.anchor= java.awt.GridBagConstraints.EAST;
        okConstraints.insets= new Insets(0, 8, 8, 8);
        getContentPane().add(fOk, okConstraints);


        GridBagConstraints cancelConstraints = new GridBagConstraints();
        cancelConstraints.gridx= 3; cancelConstraints.gridy= 2;
        cancelConstraints.gridwidth= 1; cancelConstraints.gridheight= 1;
        cancelConstraints.anchor= java.awt.GridBagConstraints.EAST;
        cancelConstraints.insets= new Insets(0, 8, 8, 8);
        getContentPane().add(fCancel, cancelConstraints);
    }
    
    /** */
    private Vector createTestList(TestCollector collector) {
        Enumeration each= collector.collectTests();
        Vector classNames = new Vector(300);
        while(each.hasMoreElements()) {
            classNames.add(each.nextElement());  
        }
        Collections.sort(classNames);
        return classNames;
    }

    /** */
    private static class ClassNameTreeNode extends DefaultMutableTreeNode {
        private String className;
        
        /** */
        public ClassNameTreeNode (String className) {
            this.className = className;
        }
        
        /** */
        public String toString() {
            if (className == null) return "";
            int index = className.lastIndexOf('.');
            return (index >= 0) ? className.substring(index+1) : className;
        }
        
        /** */
        public String getClassName() {
            return className;   
        }
    }
}
