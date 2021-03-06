package org.beanaire.ui.e4.notification.demo;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.beanaire.ui.e4.notification.NotificationBuilder;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class DemoPart {

	private Combo combo;

	private Text titleText, messageText;
	
	private Spinner spinner;

	private Button blockedBtn;
	
	@Inject
	public DemoPart() {
		// TODO Your code here
	}

	@PostConstruct
	public void createControls(Composite parent, IStylingEngine stylingEngine) {
		GridLayoutFactory.fillDefaults().applyTo(parent);

		Composite container = new Composite(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		GridLayoutFactory.fillDefaults().applyTo(container);

		/**
		 * Top container composite.
		 */
		Composite buttonsContainer = new Composite(container, SWT.BORDER);
		GridLayoutFactory.fillDefaults().numColumns(3)
				.applyTo(buttonsContainer);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(buttonsContainer);

		Button tlButton = new Button(buttonsContainer, SWT.NONE);
		tlButton.setEnabled(false);
		tlButton.setText("Top Left Corner");
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tlButton);

		new Label(buttonsContainer, SWT.NONE);

		Button trButton = new Button(buttonsContainer, SWT.NONE);
		trButton.setText("Top Right Corner");
		trButton.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(trButton);

		new Label(buttonsContainer, SWT.NONE);
		new Label(buttonsContainer, SWT.NONE);
		new Label(buttonsContainer, SWT.NONE);

		Button blButton = new Button(buttonsContainer, SWT.NONE);
		blButton.setText("Bottom Left Corner");
		blButton.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(blButton);

		new Label(buttonsContainer, SWT.NONE);

		Button brButton = new Button(buttonsContainer, SWT.NONE);
		brButton.setText("Bottom Right Corner");
		GridDataFactory.fillDefaults().grab(true, true).applyTo(brButton);
		brButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showNotification(4);
			}
		});

		/**
		 * Bottom container composite.
		 */
		Composite detailContainer = new Composite(container, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(detailContainer);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20)
				.applyTo(detailContainer);

		new Label(detailContainer, SWT.NONE).setText("Type :");

		combo = new Combo(detailContainer, SWT.BORDER | SWT.READ_ONLY);
		combo.add("Done");
		combo.add("Info");
		combo.add("Warning");
		combo.select(0);

		new Label(detailContainer, SWT.NONE).setText("Title :");
		titleText = new Text(detailContainer, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(100, -1).applyTo(titleText);
		titleText.setText("Notification");

		CLabel lblMessage = new CLabel(detailContainer, SWT.NONE);
		lblMessage.setText("Message :");
		
		messageText = new Text(detailContainer, SWT.BORDER | SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, false).hint(-1, 50)
				.applyTo(messageText);
		messageText.setText("This is a notification.");
		
		new Label(detailContainer, SWT.NONE).setText("Duration :");
		spinner = new Spinner(detailContainer, SWT.BORDER);
		spinner.setSelection(5);
		spinner.setMinimum(1);
		spinner.setMaximum(60);
		GridDataFactory.swtDefaults().hint(30, -1).applyTo(spinner);
		messageText.setText("This is a notification.");
		
		new Label(detailContainer, SWT.NONE);
		blockedBtn = new Button(detailContainer, SWT.CHECK);
		blockedBtn.setText("Blocked");
		blockedBtn.setSelection(false);
		blockedBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner.setEnabled(!blockedBtn.getSelection());
			}
		});
	}

	private void showNotification(int corner) {
		NotificationBuilder nb = NotificationBuilder.create().title(titleText.getText())
				.message(messageText.getText()).duration(spinner.getSelection());
		if(blockedBtn.getSelection()) {
			nb.blocked();
		}
		if(combo.getSelectionIndex() == 0) {
			nb.ok();
		}
		if(combo.getSelectionIndex() == 1) {
			nb.info();
		}
		if(combo.getSelectionIndex() == 2) {
			nb.warning();
		}
		
//		if (corner == 1) {
//
//		}
//		if (corner == 2) {
//
//		}
//		if (corner == 3) {
//
//		}
//		if (corner == 4) {
//			
//		}
		nb.show();
	}

	@Focus
	public void onFocus() {
		// TODO Your code here
	}

}