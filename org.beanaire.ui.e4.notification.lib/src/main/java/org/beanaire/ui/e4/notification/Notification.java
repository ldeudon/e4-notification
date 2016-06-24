package org.beanaire.ui.e4.notification;

/**
 * Représente une notification.
 * @author ldeudon
 *
 */
public class Notification {

	private long id = System.currentTimeMillis();
	
	int type;
	
	String title;
	
	String message;
	
	int duration;
	
	public int getType() {
		return type;
	}
//
//	public void setType(int type) {
//		this.type = type;
//	}

	public String getTitle() {
		return title;
	}

//	public void setTitle(String title) {
//		this.title = title;
//	}

	public String getMessage() {
		return message;
	}

//	public void setMessage(String message) {
//		this.message = message;
//	}
	
	public long getId() {
		return id;
	}
	
	public int getDuration() {
		return duration;
	}

//	public void setDuration(int duration) {
//		this.duration = duration;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
