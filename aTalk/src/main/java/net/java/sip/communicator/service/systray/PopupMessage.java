/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.systray;

import javax.swing.JComponent;

/**
 * The <code>PopupMessage</code> class encloses informations to show in a popup.
 * While a message title and a message body are mandatory informations,
 * a popup message could provides more stuffs like a component or an image which
 * may be used by a <code>PopupMessageHandler</code> capable to handle it.
 *
 * @author Symphorien Wanko
 */
public class PopupMessage
{
    /**
     * The type of the event that the popup message for.
     *
     * @see net.java.sip.communicator.plugin.notificationwiring.NotificationManager
     */
    private String eventType;

    /**
     * Message to show in the popup.
     */
    private String message;

    /**
     * Title of the message.
     */
    private String messageTitle;

    /**
     * An icon representing the contact from which the notification comes.
     */
    private byte[] imageIcon;

    /**
     * A ready to show <code>JComponet</code> for this <code>PopupMessage</code>.
     */
    private JComponent component;

    /**
     * The type of the message.
     */
    private int messageType;

    /**
     * Additional info to be used by the <code>PopupMessageHandler</code>.
     */
    private Object tag;

    /**
     * Suggested timeout value in ms for hiding the popup if not clicked (-1 for infinity)
     */
    private long timeout;

    /**
     * Name of the popup group to which this popup will belong. Used to group notifications on Android.
     */
    private String group;

    /**
     * Creates a <code>PopupMessage</code> with the given title and message inside.
     *
     * @param title title of the message
     * @param message message to show in the systray
     */
    public PopupMessage(String title, String message)
    {
        this.messageTitle = title;
        this.message = message;
    }

    /**
     * Creates a system tray message with the given title and message content.
     * The message type will affect the icon used to present the message.
     *
     * @param title the title, which will be shown
     * @param message the content of the message to display
     * @param messageType the message type; one of XXX_MESSAGE_TYPE constants declared in <code>SystrayService</code>
     */
    public PopupMessage(String title, String message, int messageType)
    {
        this(title, message);
        this.messageType = messageType;
    }

    /**
     * Creates a new <code>PopupMessage</code> with the given title, message and
     * icon.
     *
     * @param title the title of the message
     * @param message message to show in the systray
     * @param imageIcon an incon to show in the popup message.
     */
    public PopupMessage(String title, String message, byte[] imageIcon)
    {
        this(title, message);
        this.imageIcon = imageIcon;
    }

    /**
     * Creates a new <code>PopupMessage</code> with the given
     * <code>JComponent</code> as its content. This constructor also takes a title
     * and a message as replacements in cases the component is not usable.
     *
     * @param component the component to put in the <code>PopupMessage</code>
     * @param title of the message
     * @param message message to use in place of the component
     */
    public PopupMessage(JComponent component, String title, String message)
    {
        this(title, message);
        this.component = component;
    }

    /**
     * Creates a new <code>PopupMessage</code> with the given
     * <code>JComponent</code> as its content. This constructor also takes a title
     * and a message as replacements in cases the component is not usable.
     *
     * @param title of the message
     * @param message the message to show in this popup
     * @param tag additional info to be used by the <code>PopupMessageHandler</code>
     */
    public PopupMessage(String title, String message, Object tag)
    {
        this(title, message);
        this.tag = tag;
    }

    /**
     * Creates a new <code>PopupMessage</code> with the given
     * <code>JComponent</code> as its content. This constructor also takes a title
     * and a message as replacements in cases the component is not usable.
     *
     * @param title the title of the message
     * @param message the message to show in this popup
     * @param imageIcon the image icon to show in this popup message
     * @param tag additional info to be used by the <code>PopupMessageHandler</code>
     */
    public PopupMessage(String title, String message, byte[] imageIcon, Object tag)
    {
        this(title, message, imageIcon);
        this.tag = tag;
    }

    /**
     * Gets the type of the event that triggers the popup.
     *
     * @return the eventType of the popup
     */
    public String getEventType()
    {
        return eventType;
    }

    /**
     * Sets the event type that trigger the popup.
     *
     * @param evtType the event type of the popup.
     */
    public void setEventType(String evtType)
    {
        this.eventType = evtType;
    }

    /**
     * Returns the message contained in this popup.
     *
     * @return the message contained in this popup
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the message to show in the popup.
     *
     * @param message the message to show in the popup
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Returns the title of this popup message.
     *
     * @return the title of this popup message
     */
    public String getMessageTitle()
    {
        return messageTitle;
    }

    /**
     * Sets the title of this popup message.
     *
     * @param messageTitle the title to set
     */
    public void setMessageTitle(String messageTitle)
    {
        this.messageTitle = messageTitle;
    }

    /**
     * Returns the component contained in this popup message.
     *
     * @return the component contained in this popup message.
     */
    public JComponent getComponent()
    {
        return component;
    }

    /**
     * Sets the component to be showed in this popup message.
     *
     * @param component the component to set
     */
    public void setComponent(JComponent component)
    {
        this.component = component;
    }

    /**
     * Returns the icon of this popup message.
     *
     * @return the icon of this popup message
     */
    public byte[] getIcon()
    {
        return imageIcon;
    }

    /**
     * Sets the icon of this popup message.
     *
     * @param imageIcon the icon to set
     */
    public void setIcon(byte[] imageIcon)
    {
        this.imageIcon = imageIcon;
    }

    /**
     * Returns the type of this popup message.
     *
     * @return the type of this popup message.
     */
    public int getMessageType()
    {
        return messageType;
    }

    /**
     * Sets the type of this popup message.
     *
     * @param messageType the type to set
     */
    public void setMessageType(int messageType)
    {
        this.messageType = messageType;
    }

    /**
     * Returns the object used to tag this <code>PopupMessage</code>.
     *
     * @return the object used to tag this <code>PopupMessage</code>
     */
    public Object getTag()
    {
        return tag;
    }

    /**
     * Sets the object used to tag this popup message.
     *
     * @param tag the object to set
     */
    public void setTag(Object tag)
    {
        this.tag = tag;
    }

    /**
     * Sets suggested timeout for hiding the popup if not clicked by the user.
     *
     * @param timeout time value in ms for hiding the popup, -1 for infinity.
     */
    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Returns suggested timeout value in ms for hiding the popup if not clicked by the user.
     *
     * @return timeout for hiding the popup if not clicked by the user in ms.
     */
    public long getTimeout()
    {
        return timeout;
    }

    /**
     * Returns name of the popup group.
     *
     * @return name of the popup group.
     */
    public String getGroup()
    {
        return group;
    }

    /**
     * Set name of the group to which this popup will belong.
     *
     * @param group the popup group name to set.
     */
    public void setGroup(String group)
    {
        this.group = group;
    }
}
