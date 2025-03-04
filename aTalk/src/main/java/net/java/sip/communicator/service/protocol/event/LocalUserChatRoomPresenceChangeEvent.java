/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

import net.java.sip.communicator.service.protocol.ChatRoom;
import net.java.sip.communicator.service.protocol.OperationSetMultiUserChat;

import java.util.EventObject;

/**
 * Dispatched to notify interested parties that a change in our presence in the source chat room has
 * occurred. Changes may include us being kicked, join, left, etc.
 *
 * @author Emil Ivov
 * @author Stephane Remy
 */
public class LocalUserChatRoomPresenceChangeEvent extends EventObject
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Indicates that this event was triggered as a result of the local participant joining a chat room.
     */
    public static final String LOCAL_USER_JOINED = "LocalUserJoined";

    /**
     * Indicates that this event was triggered as a result of the local participant failed to join a chat room.
     */
    public static final String LOCAL_USER_JOIN_FAILED = "LocalUserJoinFailed";

    /**
     * Indicates that this event was triggered as a result of the local participant leaving a chat room.
     */
    public static final String LOCAL_USER_LEFT = "LocalUserLeft";

    /**
     * Indicates that this event was triggered as a result of the local participant being kicked
     * from a chat room.
     */
    public static final String LOCAL_USER_KICKED = "LocalUserKicked";

    /**
     * Indicates that this event was triggered as a result of the local participant being
     * disconnected from the server brutally, or ping timeout.
     */
    public static final String LOCAL_USER_DROPPED = "LocalUserDropped";

    /**
     * The <code>ChatRoom</code> to which the change is related.
     */
    private ChatRoom chatRoom = null;

    /**
     * The type of this event.
     */
    private String eventType = null;

    /**
     * An optional String indicating a possible reason as to why the event might have occurred.
     */
    private String reason = null;

    /**
     * An optional String indicating new address for the room, normally send when room is destroyed.
     */
    private String alternateAddress = null;

    /**
     * Creates a <code>ChatRoomLocalUserPresenceChangeEvent</code> representing that a change in local
     * participant presence in the source chat room has occurred.
     *
     * @param source the <code>OperationSetMultiUserChat</code>, which produced this event
     * @param chatRoom the <code>ChatRoom</code> that this event is about
     * @param eventType the type of this event.
     * @param reason the reason explaining why this event might have occurred
     */
    public LocalUserChatRoomPresenceChangeEvent(OperationSetMultiUserChat source,
            ChatRoom chatRoom, String eventType, String reason)
    {
        this(source, chatRoom, eventType, reason, null);
    }

    /**
     * Creates a <code>ChatRoomLocalUserPresenceChangeEvent</code> representing that a change in local
     * participant presence in the source chat room has occurred.
     *
     * @param source the <code>OperationSetMultiUserChat</code>, which produced this event
     * @param chatRoom the <code>ChatRoom</code> that this event is about
     * @param eventType the type of this event.
     * @param reason the reason explaining why this event might have occurred
     */
    public LocalUserChatRoomPresenceChangeEvent(OperationSetMultiUserChat source,
            ChatRoom chatRoom, String eventType, String reason, String alternateAddress)
    {
        super(source);

        this.chatRoom = chatRoom;
        this.eventType = eventType;
        this.reason = reason;
        this.alternateAddress = alternateAddress;
    }

    /**
     * Returns the <code>OperationSetMultiUserChat</code>, where this event has occurred.
     *
     * @return the <code>OperationSetMultiUserChat</code>, where this event has occurred
     */
    public OperationSetMultiUserChat getMultiUserChatOpSet()
    {
        return (OperationSetMultiUserChat) getSource();
    }

    /**
     * Returns the <code>ChatRoom</code>, that this event is about.
     *
     * @return the <code>ChatRoom</code>, that this event is about
     */
    public ChatRoom getChatRoom()
    {
        return this.chatRoom;
    }

    /**
     * A reason string indicating a human readable reason for this event.
     *
     * @return a human readable String containing the reason for this event, or null if no
     * particular reason was specified
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * Returns the type of this event which could be one of the LOCAL_USER_XXX member fields.
     *
     * @return one of the LOCAL_USER_XXX fields indicating the type of this event.
     */
    public String getEventType()
    {
        return eventType;
    }

    /**
     * An optional String indicating new address for the room, normally send when room is destroyed.
     *
     * @return alternate address for the destroyed room.
     */
    public String getAlternateAddress()
    {
        return alternateAddress;
    }

    /**
     * Returns a String representation of this event.
     *
     * @return String representation of this event
     */
    @Override
    public String toString()
    {
        return "ChatRoomLocalUserPresenceChangeEvent[type=" + getEventType() + " sourceRoom=" + getChatRoom() + "]";
    }
}
