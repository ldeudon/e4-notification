package org.beanaire.ui.e4.notification;

import java.util.ArrayList;
import java.util.List;

import org.beanaire.ui.e4.notification.internal.NotificationPopup;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Helper du système de notification.<p>
 * Permet d'afficher une notification.
 * @author ldeudon
 *
 */
public class NotificationHelper {


	public static NotificationHelper INSTANCE = null;
	
	static {
		INSTANCE = new NotificationHelper();
	}
	
	/**
	 * Liste des notifications en cours d'affichage.
	 */
	private final List<NotificationPopup> notificationsEnCours = new ArrayList<NotificationPopup>();

	private NotificationHelper() {
		init();
	}

	private void init() {
		
	}

	/**
	 * Show the notification on the screen.
	 * @param notification
	 */
	public void showNotification(final Notification notification) {
		final NotificationPopup popup = new NotificationPopup(notification);
		
		if(notificationsEnCours.size() == 4 ) {
			// if more than four notifications, the oldest is destroyed. 
			notificationsEnCours.get(0).dispose();
		}
		
		// add popup in current notifications list.
		notificationsEnCours.add(popup);
		
		popup.getShell().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				notificationsEnCours.remove(popup);
				layout();
			}
		});
		
		layout();
		
		startCinematic(popup, notification);
	}



	/**
	 * Fade in du shell.
	 * <p>
	 * Méthode trouvée sur le net et modifiée.
	 * 
	 * @param _shell
	 */
	private void startCinematic(final NotificationPopup popup, final Notification notification) {
		Thread thread = new Thread() {
			public void run() {
				
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						popup.getShell().setAlpha(0);
						popup.getShell().open();
						Display.getDefault().timerExec(0, new FadeRunnable(popup.getShell(), true));
					}
				});
				
				
				if(notification.duration < 0) {
					// this notification is blocked, user has to close the popup.
					return;
				}
				
				try {
					Thread.sleep(notification.duration * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						Display.getDefault().timerExec(0, new FadeRunnable(popup.getShell(), false));
					}
				});
				
			}			
		};
		
		thread.start();
	}
	
	/**
	 * Calcule la position d'une notification par rapport à son numéro d'ordre d'affichage.
	 * @param order
	 * @return Position x, y
	 */
	private Point computePosition(int order) {
		int x = Display.getDefault().getClientArea().width - NotificationPopup.WINDOW_WIDTH - 5;
		int y = Display.getDefault().getClientArea().height	- (NotificationPopup.WINDOW_HEIGHT * order) - (5 * order);
		return new Point(x, y);
	}

	/**
	 * Réorganise, en pile, les notifications.<p>
	 * Utile lorsqu'une notification en milieu de pile est fermée.
	 */
	private synchronized void layout() {
		int order = 1;
		for(NotificationPopup popup : notificationsEnCours) {
			popup.setLocation(computePosition(order++));
		}
	}
	
}

/**
 * Thread pour le fondu in / out du shell de la notitication.
 * @author ldeudon
 *
 */
class FadeRunnable extends Thread {

	/**
	 * Pas de transparence lors du fade in
	 */
	private static final int FADE_IN_STEP = 15;
	
	private static final int FADE_OUT_STEP = 5;

	/**
	 * Temps entre 2 pas. 
	 */
	private static final int FADE_TIMER = 50;
	
	private boolean fadeIn = false;

	private Shell _shell;

	FadeRunnable(Shell _shell, boolean fadeIn) {
		this._shell = _shell;
		this.fadeIn = fadeIn;
	}

	@Override
	public void run() {

		if (_shell == null || _shell.isDisposed()) {
			return;
		}

		int cur = _shell.getAlpha();
		if (fadeIn) {
			cur += FADE_IN_STEP;
		} else {
			cur -= FADE_OUT_STEP;
		}

		if (fadeIn && cur > 255) {
			_shell.setAlpha(255);
			return;
		}
		if (!fadeIn && cur < 0) {
			_shell.dispose();
			return;
		}
		
		_shell.setAlpha(cur);
		Display.getDefault().timerExec(FADE_TIMER, this);
	}

}
