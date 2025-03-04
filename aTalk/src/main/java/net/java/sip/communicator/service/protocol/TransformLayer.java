/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.MessageDeliveredEvent;
import net.java.sip.communicator.service.protocol.event.MessageDeliveryFailedEvent;
import net.java.sip.communicator.service.protocol.event.MessageReceivedEvent;

/**
 * An instance of the <code>TransformLayer</code>, when registered with
 * <code>OperationSetInstantMessageTransform</code> would be passed all message events. The class looks
 * a lot like a <code>MessageListener</code> with the major difference being that all the methods are
 * defined with a return value. The events we return would contain all message details after their
 * transformation from by the layer implementation. All methods return <code>null</code> in case the
 * <code>TransformLayer</code> implementation determines that the message event should not be determined
 * to the upper layers.
 * <p/>
 * Important Notice: As of May 5 2009, this operation set is still a work in progress and may change
 * significantly in the following months. Any work based on this interface is therefore likely to
 * require frequent updates to keep compatibility.
 *
 * @author Emil Ivov
 *
 */
public interface TransformLayer
{
	/**
	 * Called when a new incoming <code>Message</code> has been received. The method returns an instance
	 * of <code>MessageReceivedEvent</code> which in many cases would be different from the <code>evt</code>
	 * instance that was passed as param. The param and the return instances could very well (and
	 * will often) be instances of different implementations so users of this interface (i.e.
	 * protocol implementors) should make no assumptions for the class of the return type and copy
	 * the returned instance into a new one if necessary.
	 *
	 * @param evt
	 *        the <code>MessageReceivedEvent</code> containing the newly received message, its sender
	 *        and other details.
	 *
	 * @return an instance of a (possibly new) <code>MessageReceivedEvent</code> instance containing the
	 *         transformed message or <code>null</code> if the <code>TransportLayer</code> has determined
	 *         that this message event should not be delivered to the upper layers.
	 */
	public MessageReceivedEvent messageReceived(MessageReceivedEvent evt);

	/**
	 * Called when the underlying implementation has just been asked by other bundles to send an
	 * outgoing message. The method returns an instance of <code>MessageDeliveredEvent</code> which in
	 * many cases would be different from the <code>evt</code> instance that was passed as a parameter.
	 * The param and the return instances could very well (and will often) be instances of different
	 * implementations so users of this interface (i.e. protocol implementors) should make no
	 * assumptions for the class of the return type and copy the returned instance into a new one if
	 * necessary.
	 *
	 * @param evt
	 *        the MessageDeliveredEvent containing the id of the message that has caused the event.
	 *
	 * @return a number of instances of (possibly new) <code>MessageDeliveredEvent</code> instances
	 *         containing the transformed message(s) or an empty array if the
	 *         <code>TransportLayer</code> has determined that there are no message event that should be
	 *         delivered to the upper layers.
	 */
	public MessageDeliveredEvent[] messageDeliveryPending(MessageDeliveredEvent evt);

	/**
	 * Called when the underlying implementation has received an indication that a message, sent
	 * earlier has been successfully received by the destination. The method returns an instance of
	 * <code>MessageDeliveredEvent</code> which in many cases would be different from the <code>evt</code>
	 * instance that was passed as a parameter. The param and the return instances could very well
	 * (and will often) be instances of different implementations so users of this interface (i.e.
	 * protocol implementors) should make no assumptions for the class of the return type and copy
	 * the returned instance into a new one if necessary.
	 *
	 * @param evt
	 *        the MessageDeliveredEvent containing the id of the message that has caused the event.
	 *
	 * @return an instance of a (possibly new) <code>MessageDeliveredEvent</code> instance containing
	 *         the transformed message or <code>null</code> if the <code>TransportLayer</code> has
	 *         determined that this message event should not be delivered to the upper layers.
	 */
	public MessageDeliveredEvent messageDelivered(MessageDeliveredEvent evt);

	/**
	 * Called to indicated that delivery of a message sent earlier has failed. Reason code and
	 * phrase are contained by the <code>MessageFailedEvent</code> The method returns an instance of
	 * <code>MessageDeliveredEvent</code> which in many cases would be different from the <code>evt</code>
	 * instance that was passed as a parameter. The param and the return instances could very well
	 * (and will often) be instances of different implementations so users of this interface (i.e.
	 * protocol implementors) should make no assumptions for the class of the return type and copy
	 * the returned instance into a new one if necessary.
	 *
	 * @param evt
	 *        the <code>MessageFailedEvent</code> containing the ID of the message whose delivery has
	 *        failed.
	 *
	 * @return an instance of a (possibly new) <code>MessageDeliveredEvent</code> instance containing
	 *         the transformed message or <code>null</code> if the <code>TransportLayer</code> has
	 *         determined that this message event should not be delivered to the upper layers.
	 */
	public MessageDeliveryFailedEvent messageDeliveryFailed(MessageDeliveryFailedEvent evt);
}
