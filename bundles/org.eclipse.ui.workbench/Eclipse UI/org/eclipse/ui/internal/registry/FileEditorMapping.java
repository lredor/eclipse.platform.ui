/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jan-Hendrik Diederich, Bredex GmbH - bug 201052
 *     Carsten Pfeiffer, Gebit Solutions GmbH - bug 259536
 *******************************************************************************/
package org.eclipse.ui.internal.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * Implementation of IFileEditorMapping.
 *
 */
public class FileEditorMapping extends Object implements IFileEditorMapping,
    Cloneable {

	private static final String STAR = "*"; //$NON-NLS-1$
	private static final String DOT = ".";	//$NON-NLS-1$

    private String name = STAR;

    private String extension;

    // Collection of EditorDescriptor, where the first one
    // if considered the default one.
	private List<IEditorDescriptor> editors = new ArrayList<>(1);

	private List<IEditorDescriptor> deletedEditors = new ArrayList<>(1);

	private List<IEditorDescriptor> declaredDefaultEditors = new ArrayList<>(1);

    /**
     *  Create an instance of this class.
     *
     *  @param extension java.lang.String
     */
    public FileEditorMapping(String extension) {
        this(STAR, extension);
    }

    /**
     *  Create an instance of this class.
     *
     *  @param name java.lang.String
     *  @param extension java.lang.String
     */
    public FileEditorMapping(String name, String extension) {
        super();
        if (name == null || name.length() < 1) {
            setName(STAR);
        } else {
			setName(name);
		}
        if (extension == null) {
			setExtension("");//$NON-NLS-1$
		} else {
			setExtension(extension);
		}
    }

    /**
     * Add the given editor to the list of editors registered.
     *
     * @param editor the editor to add
     */
	public void addEditor(EditorDescriptor editor) {
		Assert.isNotNull(editor);
        editors.add(editor);
        deletedEditors.remove(editor);
    }

    /**
     * Clone the receiver.
     */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
        try {
            FileEditorMapping clone = (FileEditorMapping) super.clone();
			clone.editors = (List<IEditorDescriptor>) ((ArrayList<IEditorDescriptor>) editors).clone();
			clone.deletedEditors = (List<IEditorDescriptor>) ((ArrayList<IEditorDescriptor>) deletedEditors).clone();
			clone.declaredDefaultEditors = (List<IEditorDescriptor>) ((ArrayList<IEditorDescriptor>) declaredDefaultEditors)
					.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
	public boolean equals(Object obj) {
        if (this == obj) {
			return true;
		}
        if (!(obj instanceof FileEditorMapping)) {
			return false;
		}
        FileEditorMapping mapping = (FileEditorMapping) obj;
        if (!this.name.equals(mapping.name)) {
			return false;
		}
        if (!this.extension.equals(mapping.extension)) {
			return false;
		}

        if (!compareList(this.editors, mapping.editors)) {
			return false;
		}
		if (!compareList(this.declaredDefaultEditors, mapping.declaredDefaultEditors)) {
			return false;
		}
        return compareList(this.deletedEditors, mapping.deletedEditors);
    }

    /**
     * Compare the editor ids from both lists and return true if they
     * are equals.
     */
	private boolean compareList(List<IEditorDescriptor> l1, List<IEditorDescriptor> l2) {
        if (l1.size() != l2.size()) {
			return false;
		}

		Iterator<IEditorDescriptor> i1 = l1.iterator();
		Iterator<IEditorDescriptor> i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            Object o1 = i1.next();
            Object o2 = i2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
				return false;
			}
        }
        return true;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declaredDefaultEditors == null) ? 0 : declaredDefaultEditors.hashCode());
		result = prime * result + ((deletedEditors == null) ? 0 : deletedEditors.hashCode());
		result = prime * result + ((editors == null) ? 0 : editors.hashCode());
		result = prime * result + ((extension == null) ? 0 : extension.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

    @Override
	public IEditorDescriptor getDefaultEditor() {
        if (editors.isEmpty() || WorkbenchActivityHelper.restrictUseOf(editors.get(0))) {
			return null;
		}
        return editors.get(0);
    }

    /**
     * Returns all editor descriptors of this mapping, not filtered by activities.
     */
    IEditorDescriptor[] getUnfilteredEditors() {
    	return editors.toArray(new IEditorDescriptor[editors.size()]);
    }

    @Override
	public IEditorDescriptor[] getEditors() {
		Collection<IEditorDescriptor> descs = WorkbenchActivityHelper.restrictCollection(editors, new ArrayList<>());
		return descs.toArray(new IEditorDescriptor[descs.size()]);
    }

    @Override
	public IEditorDescriptor[] getDeletedEditors() {
        IEditorDescriptor[] array = new IEditorDescriptor[deletedEditors.size()];
        deletedEditors.toArray(array);
        return array;
    }

    @Override
	public String getExtension() {
        return extension;
    }

    @Override
	public ImageDescriptor getImageDescriptor() {
        IEditorDescriptor editor = getDefaultEditor();
        if (editor == null) {
            return WorkbenchImages
                    .getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
        }
        return editor.getImageDescriptor();
    }

    @Override
	public String getLabel() {
        return TextProcessor.process(name + (extension.length() == 0 ? "" : DOT + extension), STAR + DOT); 	//$NON-NLS-1$
    }

    @Override
	public String getName() {
        return name;
    }

    /**
     * Remove the given editor from the set of editors registered.
     *
     * @param editor the editor to remove
     */
	public void removeEditor(IEditorDescriptor editor) {
		Assert.isNotNull(editor);
        editors.remove(editor);
        deletedEditors.add(editor);
        declaredDefaultEditors.remove(editor);
    }

    /**
     * Set the default editor registered for file type
     * described by this mapping.
     *
     * @param editor the editor to be set as default
     */
	public void setDefaultEditor(IEditorDescriptor editor) {
		Assert.isNotNull(editor);
        editors.remove(editor);
        editors.add(0, editor);
        declaredDefaultEditors.remove(editor);
        declaredDefaultEditors.add(0, editor);
    }

    /**
     * Set the collection of all editors (EditorDescriptor)
     * registered for the file type described by this mapping.
     * Typically an editor is registered either through a plugin or explicitly by
     * the user modifying the associations in the preference pages.
     * This modifies the internal list to share the passed list.
     * (hence the clear indication of list in the method name)
     *
     * @param newEditors the new list of associated editors
     */
	public void setEditorsList(List<IEditorDescriptor> newEditors) {
        editors = newEditors;
        declaredDefaultEditors.retainAll(newEditors);
    }

    /**
     * Set the collection of all editors (EditorDescriptor)
     * formally registered for the file type described by this mapping
     * which have been deleted by the user.
     * This modifies the internal list to share the passed list.
     * (hence the clear indication of list in the method name)
     *
     * @param newDeletedEditors the new list of associated (but deleted) editors
     */
	public void setDeletedEditorsList(List<IEditorDescriptor> newDeletedEditors) {
        deletedEditors = newDeletedEditors;
    }

    /**
     * Set the file's extension.
     *
     * @param extension the file extension for this mapping
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Set the file's name.
     *
     * @param name the file name for this mapping
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get the editors that have been declared as default. This may be via plugin
	 * declarations or the preference page.
	 *
	 * @return the editors the default editors
	 * @since 3.1
	 */
	public IEditorDescriptor [] getDeclaredDefaultEditors() {
		Collection<IEditorDescriptor> descs = WorkbenchActivityHelper.restrictCollection(declaredDefaultEditors,
				new ArrayList<>());
		return descs.toArray(new IEditorDescriptor[descs.size()]);
	}

	/**
	 * Return whether the editor is declared default.
	 * If this is EditorDescriptor fails the ExpressionsCheck it will always
	 * return <code>false</code>, even if it's the original default editor.
	 *
	 * @param editor the editor to test
	 * @return whether the editor is declared default
	 * @since 3.1
	 */
	public boolean isDeclaredDefaultEditor (IEditorDescriptor editor) {
		return declaredDefaultEditors.contains(editor)
				&& !WorkbenchActivityHelper.restrictUseOf(editor);
	}

	/**
	 * Set the default editors for this mapping.
	 *
	 * @param defaultEditors the editors
	 * @since 3.1
	 */
	public void setDefaultEditors(List<IEditorDescriptor> defaultEditors) {
		declaredDefaultEditors = defaultEditors;
	}
}
