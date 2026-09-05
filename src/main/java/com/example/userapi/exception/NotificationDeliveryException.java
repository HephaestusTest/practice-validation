package com.example.userapi.exception;

/**
 * Thrown when a notification (email or Slack) fails to deliver.
 * This is a non-critical exception: callers may choose to log and
 * continue rather than propagate it to the end user.
 */
public class NotificationDeliveryException extends RuntimeException {

    private final String channel;

    public NotificationDeliveryException(String channel, String message, Throwable cause) {
        super(message, cause);
        this.channel = channel;
    }

    /**
     * Returns the notification channel that failed (e.g., "email", "slack").
     */
    public String getChannel() {
        return channel;
    }
}
