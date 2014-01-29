package org.beanaire.ui.e4.notification;

import org.eclipse.swt.SWT;


/**
 * Fabrique de notifications utilisant le pattern Builder.<p>
 * Exemples :
 * <ul>
 * <li>NotificationBuilder.create().ok("Notification").body("Mon message").getNotification()</li>
 * <li>NotificationBuilder.create().error("Error").body("Mon message").show()</li>
 * </ul><p>
 * By default, a notification is OK type with limited duration (ten seconds). 
 * @author ldeudon
 *
 */
public final class NotificationBuilder {

	private static NotificationBuilder INSTANCE;
	
	private Notification notification;
	
	private NotificationBuilder() {
		notification = new Notification();
		title("Notification");
		ok();
		duration(15);
	}
	
	/**
	 * Créer une notification.
	 * @return
	 */
	public static NotificationBuilder create() {
		INSTANCE = new NotificationBuilder();
		return INSTANCE;
	}

	public NotificationBuilder info() {
		notification.type = SWT.BALLOON;
		return INSTANCE;
	}
	
	public NotificationBuilder ok() {
		notification.type = SWT.OK;
		return INSTANCE;
	}
	
	public NotificationBuilder warning() {
		notification.type = SWT.ERROR;
		return INSTANCE;
	}
	
	public NotificationBuilder title(String title) {
		notification.title = title;	
		return INSTANCE;
	}
	
	public NotificationBuilder message(String message) {
		notification.message = message;
		return INSTANCE;
	}

	public NotificationBuilder duration(int seconds) {
		notification.duration = seconds;
		return INSTANCE;
	}
	
	public NotificationBuilder blocked() {
		notification.duration = -1;
		return INSTANCE;
	}
	
	/**
	 * Retourne la notification.
	 * @return
	 */
	public Notification getNotification() {
		return notification;
	}
	
	/**
	 * Affiche la notification en utilisant le service par défaut.
	 */
	public void show() {
		NotificationHelper.INSTANCE.showNotification(notification);
	}
	
}
