package org.eclipse.ui.texteditor;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * FOR USE BY TESTS ONLY!
 * <p>
 * Stub class that provides access to classes visible to the package
 * <code>org.eclipse.ui.texteditor</code>.  For the purpose of
 * testing.
 * </p>
 * @private
 */

public class TextEditorTestStub {
	//Prevent instantiation
	private TextEditorTestStub(){}
	
	/**
	 * Gives access to an instance of FindReplaceDialog.
	 * @return FindReplaceDialog an instance of FindReplaceDialog.
	 */
	public static FindReplaceDialog newFindReplaceDialog(Shell parentShell) {
		return new FindReplaceDialog(parentShell);
	}
}

