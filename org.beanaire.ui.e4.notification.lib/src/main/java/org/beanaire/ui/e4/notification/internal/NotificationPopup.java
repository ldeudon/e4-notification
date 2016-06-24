package org.beanaire.ui.e4.notification.internal;

import java.io.IOException;
import java.net.URL;

import org.beanaire.ui.e4.notification.Notification;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class NotificationPopup  {

	public static final int WINDOW_WIDTH = 340, WINDOW_HEIGHT = 140;

	private static final String CSS_LOCATION = "platform:/plugin/org.beanaire.ui.e4.notification.lib/css/notify.css";
	
	private Shell shell;

	private static CSSEngine engine;

	public NotificationPopup(Notification notification) {
		/**
		 * Parsing css file.
		 */
		try {
			URL dynamicURL = FileLocator.find(new URL(CSS_LOCATION));
			engine = new CSSSWTEngineImpl(Display.getDefault());
			engine.parseStyleSheet(dynamicURL.openStream());

			engine.setErrorHandler(new CSSErrorHandler() {
				public void error(Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/**
		 * Create the shell
		 */
//		for(Monitor monitor : Display.getDefault().getMonitors()) {
//			System.out.println(monitor);
//		}
		shell = new Shell(SWT.MODELESS | SWT.ON_TOP | SWT.NO_FOCUS | SWT.NO_TRIM);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		roundedShell(shell);
		GridLayoutFactory.fillDefaults().applyTo(shell);
		
		createContents(shell, notification);
	}

	public Shell getShell() {
		return shell;
	}
	
	/**
	 * Create the body content.
	 * @param parent
	 * @param notification
	 */
	private void createContents(final Composite parent,
			final Notification notification) {
		
		/**
		 * Composite global
		 */
		Composite composite = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);
		GridLayoutFactory.fillDefaults().margins(20, 5).applyTo(composite);
		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Color color = SWTResourceManager.getColor(135, 172, 235);
				if(notification.getType() == SWT.OK) {
					color = SWTResourceManager.getColor(138, 208, 69);
				}
				if(notification.getType() == SWT.ERROR) {
					color = SWTResourceManager.getColor(250, 10, 10);
				}
				e.gc.setBackground(color);
				e.gc.fillRectangle(e.x, e.y, 17, e.height);
				e.gc.drawRoundRectangle(e.x, e.y, e.width - 1, e.height - 1, 14, 14);
			}
		});
		
		/**
		 * Three columns
		 */
		Composite container = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		GridLayoutFactory.fillDefaults().margins(0, 5).numColumns(3)
				.applyTo(container);
		
		
		/**
		 * The head of notification
		 */
		CLabel lblImage = new CLabel(container, SWT.NONE);
		lblImage.setImage(SWTResourceManager.getImage(
				NotificationPopup.class, "/images/notify_" + notification.getType() + ".png"));

		GridDataFactory.fillDefaults().indent(8, 5).hint(38, 35)
				.applyTo(lblImage);

		/**
		 * The title of notification.
		 */
		CLabel lblTitle = new CLabel(container, SWT.NONE);
		lblTitle.setText(notification.getTitle());
		GridDataFactory.fillDefaults().indent(5, 5).grab(true, false)
				.hint(-1, 25).applyTo(lblTitle);

		/**
		 * Close button.
		 */
		CLabel lblClose = new CLabel(container, SWT.NONE);
		lblClose.setImage(SWTResourceManager.getImage(NotificationPopup.class, "/images/fileclose_16x16.png"));
		GridDataFactory.fillDefaults().indent(5, 5).hint(30, 25)
				.applyTo(lblClose);
		
		final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND); 
		lblClose.setCursor(handCursor);
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				((Shell) parent).close();
				if(!handCursor.isDisposed()) {
					handCursor.dispose();
				}
				((Shell) parent).dispose();
			}
		});

		/**
		 * The message of notfication.
		 */
		Label lblMessage = new Label(container, SWT.WRAP);
		lblMessage.setText(notification.getMessage());
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER)
				.grab(true, true).indent(5, 0).applyTo(lblMessage);

		/**
		 * Apply css style.
		 */
		WidgetElement.setCSSClass(lblTitle, "title");
		WidgetElement.setCSSClass(lblMessage, "message");
		engine.applyStyles(container, true);
	}
	
	/**
	 * Round the corner of the shell.<p>
	 * Must be improved...
	 * @param shell
	 */
	private static void roundedShell(Shell shell) {
		Region region = new Region();
		region.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT));
		
		region.subtract(new Rectangle(0, 0, 1, 5));
		region.subtract(new Rectangle(0, 0, 5, 1));
		region.subtract(new Rectangle(0, 1, 2, 2));
		region.subtract(new Rectangle(2, 0, 1, 2));
		
		region.subtract(new Rectangle(0, WINDOW_HEIGHT - 5, 1, 5));
		region.subtract(new Rectangle(0, WINDOW_HEIGHT - 1, 5, 1));
		region.subtract(new Rectangle(1, WINDOW_HEIGHT - 3, 1, 3));
		region.subtract(new Rectangle(2, WINDOW_HEIGHT - 2, 1, 2));
		
		region.subtract(new Rectangle(WINDOW_WIDTH - 5, 0, 5, 1));
		region.subtract(new Rectangle(WINDOW_WIDTH - 1, 0, 1, 5));
		region.subtract(new Rectangle(WINDOW_WIDTH - 2, 0, 1, 3));
		region.subtract(new Rectangle(WINDOW_WIDTH - 3, 1, 3, 1));

		region.subtract(new Rectangle(WINDOW_WIDTH - 5, WINDOW_HEIGHT - 1, 5, 1));
		region.subtract(new Rectangle(WINDOW_WIDTH - 1, WINDOW_HEIGHT - 5, 1, 5));
		region.subtract(new Rectangle(WINDOW_WIDTH - 2, WINDOW_HEIGHT - 3, 1, 3));
		region.subtract(new Rectangle(WINDOW_WIDTH - 3, WINDOW_HEIGHT - 2, 3, 1));

		shell.setRegion(region);
	}
	
	public void setLocation(Point point) {
		if(!shell.isDisposed()) {
			shell.setLocation(point);
		}
	}
	
	public void dispose() {
		if(!shell.isDisposed()) {
			shell.dispose();
		}
	}
		
}
