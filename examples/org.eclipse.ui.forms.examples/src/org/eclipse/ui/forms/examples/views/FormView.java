package org.eclipse.ui.forms.examples.views;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.events.*;
import org.eclipse.ui.forms.examples.internal.ExamplesPlugin;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class FormView extends ViewPart {
	private FormToolkit toolkit;
	private ScrolledForm form;
	/**
	 * The constructor.
	 */
	public FormView() {
	}
	/**
	 * This is a callback that will allow us to create the viewer and
	 * initialize it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Hello, Eclipse Forms");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		
		Hyperlink link = toolkit.createHyperlink(form.getBody(), "Click here.",
				SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				System.out.println("Link activated!");
			}
		});
		link.setText("This is an example of a form that is much longer and will need to wrap.");
		layout.numColumns = 2;
		TableWrapData td = new TableWrapData();
		td.colspan = 2;
		link.setLayoutData(td);
		Label label = toolkit.createLabel(form.getBody(), "Text field label:");
		Text text = toolkit.createText(form.getBody(), "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		text.setLayoutData(td);
		Button button = toolkit.createButton(form.getBody(),
				"An example of a checkbox in a form", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		button.setLayoutData(td);
		ExpandableComposite ec = toolkit.createExpandableComposite(form.getBody(), ExpandableComposite.TREE_NODE|ExpandableComposite.CLIENT_INDENT);
		ec.setText("Expandable Composite title");
		String ctext = "We will now create a somewhat long text so that "+
		"we can use it as content for the expandable composite. "+
		"Expandable composite is used to hide or show the text using the "+
		"toggle control";
		Label client = toolkit.createLabel(ec, ctext, SWT.WRAP);
		ec.setClient(client);
		td = new TableWrapData();
		td.colspan = 2;
		ec.setLayoutData(td);
		ec.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION|Section.TWISTIE|Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 2;
		section.setLayoutData(td);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Section title");
		toolkit.createCompositeSeparator(section);
		section.setDescription("This is the description that goes below the title");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		button = toolkit.createButton(sectionClient, "Radio 1", SWT.RADIO);
		button = toolkit.createButton(sectionClient, "Radio 2", SWT.RADIO);
		section.setClient(sectionClient);

		StringBuffer buf = new StringBuffer();
		buf.append("<form>");
		buf.append("<p>");
		buf.append("Here is some plain text for the text to render; ");
		buf.append("this text is at <a href=\"http://www.eclipse.org\" nowrap=\"true\">http://www.eclipse.org</a> web site.");
		buf.append("</p>");
		buf.append("<p>");
		buf.append("<span color=\"header\" font=\"header\">This text is in header font and color.</span>");
		buf.append("</p>");
		buf.append("<p>This line will contain some <b>bold</b> and some <span font=\"code\">source</span> text. ");
		buf.append("We can also add <img href=\"image\"/> an image. ");
		buf.append("</p>");
		buf.append("<li>A default (bulleted) list item.</li>");
		buf.append("<li>Another bullet list item.</li>");
		buf.append("<li style=\"text\" value=\"1.\">A list item with text.</li>");
		buf.append("<li style=\"text\" value=\"2.\">Another list item with text</li>");
		buf.append("<li style=\"image\" value=\"image\">List item with an image bullet</li>");
		buf.append("<li style=\"text\" bindent=\"20\" indent=\"40\" value=\"3.\">A list item with text.</li>");
		buf.append("<li style=\"text\" bindent=\"20\" indent=\"40\" value=\"4.\">A list item with text.</li>");
		buf.append("</form>");
		FormText rtext = toolkit.createFormText(form.getBody(), true);
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 2;
		rtext.setLayoutData(td);
		rtext.setImage("image", ExamplesPlugin.getDefault().getImageRegistry().get(ExamplesPlugin.IMG_SAMPLE));
		rtext.setColor("header", toolkit.getColors().getColor(FormColors.TITLE));
		rtext.setFont("header", JFaceResources.getHeaderFont());
		rtext.setFont("code", JFaceResources.getTextFont());
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				System.out.println("Link active: "+e.getHref());
			}
		});
/*		layout.numColumns = 3;
		Label label;
		TableWrapData td;
		
		label = toolkit.createLabel(form.getBody(), "Some text to put in the first column", SWT.WRAP);
		label = toolkit.createLabel(form.getBody() ,"Some text to put in the second column and make it a bit longer so that we can see what happens with column distribution. This text must be the longest so that it can get more space allocated to the columns it belongs to.", SWT.WRAP);
		td = new TableWrapData();
		td.colspan = 2;
		label.setLayoutData(td);
		label = toolkit.createLabel(form.getBody(), "This text will span two rows and should not grow the column.", SWT.WRAP);
		td = new TableWrapData();
		td.rowspan = 2;
		label.setLayoutData(td);
		label = toolkit.createLabel(form.getBody(), "This text goes into column 2 and consumes only one cell", SWT.WRAP);
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label = toolkit.createLabel(form.getBody(), "This text goes into column 3 and consumes only one cell too", SWT.WRAP);
		label.setLayoutData(new TableWrapData(TableWrapData.FILL));
		label = toolkit.createLabel(form.getBody(), "This text goes into column 2 and consumes only one cell", SWT.WRAP);
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		label = toolkit.createLabel(form.getBody(), "This text goes into column 3 and consumes only one cell too", SWT.WRAP);
		label.setLayoutData(new TableWrapData(TableWrapData.FILL));
		form.getBody().setBackground(form.getBody().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));*/
	}
	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
	}
	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
}