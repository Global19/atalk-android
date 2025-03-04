/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.*;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Represents a default/base implementation of <code>OperationSetDesktopSharingClient</code> which
 * attempts to make it easier for implementers to provide complete solutions while focusing on
 * implementation-specific functionality.
 *
 * @param <T>
 * @author Sebastien Vincent
 * @author Lyubomir Marinov
 */
public abstract class AbstractOperationSetDesktopSharingClient<T extends ProtocolProviderService>
        implements OperationSetDesktopSharingClient
{
    /**
     * The <code>CallPeerListener</code> which listens to modifications in the properties/state of
     * <code>CallPeer</code>.
     */
    private final CallPeerListener callPeerListener = new CallPeerAdapter()
    {
        /**
         * Indicates that a change has occurred in the status of the source <code>CallPeer</code>.
         *
         * @param evt
         *        the <code>CallPeerChangeEvent</code> instance containing the source event as well as
         *        its previous and its new status
         */
        @Override
        public void peerStateChanged(CallPeerChangeEvent evt)
        {
            CallPeer peer = evt.getSourceCallPeer();
            CallPeerState state = peer.getState();

            if (state != null
                    && (state.equals(CallPeerState.DISCONNECTED) || state.equals(CallPeerState.FAILED))) {
                removesNullAndRevokedControlPeer(peer.getPeerID());
                removeRemoteControlListener(getListener(peer));
            }
        }
    };

    /**
     * List of the granted remote control peers for this client. Used to remember granted remote
     * control peers, when the granted event is fired before the corresponding UI listener
     * registration.
     */
    private Vector<CallPeer> grantedRemoteControlPeers = new Vector<>();

    /**
     * The list of <code>RemoteControlListener</code>s to be notified when a change in remote control
     * access occurs.
     */
    private final List<WeakReference<RemoteControlListener>> listeners = new ArrayList<WeakReference<RemoteControlListener>>();

    /**
     * Peers who granted/revoked remote control before it's listeners was
     * added
     */
    private List<String> deferredRemoteControlPeers = new ArrayList<>();
    /**
     * The <code>ProtocolProviderService</code> implementation which created this instance and for which
     * telephony conferencing services are being provided by this instance.
     */
    protected final T parentProvider;

    /**
     * Initializes a new <code>AbstractOperationSetDesktopSharing</code> instance which is to be
     * provided by a specific <code>ProtocolProviderService.
     *
     * @param parentProvider the <code>ProtocolProviderService</code> implementation which is creating the new instance
     * and for which telephony conferencing services are being provided by this instance
     */
    protected AbstractOperationSetDesktopSharingClient(T parentProvider)
    {
        this.parentProvider = parentProvider;
    }

    /**
     * Adds a <code>RemoteControlListener</code> to be notified when the remote peer accepts to give us
     * full control of their desktop.
     * <p>
     * The default implementation of <code>AbstractOperationSetDesktopSharingClient</code> adds a
     * <code>WeakReference</code> to the specified <code>RemoteControlListener</code> in order to avoid
     * memory leaks because of code which calls <code>addRemoteControlListener</code> and never calls
     * <code>removeRemoteControlListener</code>.
     * </p>
     *
     * @param listener the <code>RemoteControlListener</code> to add
     */
    public void addRemoteControlListener(RemoteControlListener listener)
    {
        synchronized (listeners) {
            Iterator<WeakReference<RemoteControlListener>> i = listeners.iterator();
            boolean contains = false;

            while (i.hasNext()) {
                RemoteControlListener l = i.next().get();

                if (l == null)
                    i.remove();
                else if (l.equals(listener))
                    contains = true;
            }
            if (!contains) {
                listeners.add(new WeakReference<>(listener));
                listener.getCallPeer().addCallPeerListener(callPeerListener);
            }
        }

        // Notifies the new listener if the corresponding peer has already been
        // granted to remotely control the shared desktop.
        CallPeer peer = listener.getCallPeer();
        // Removes the null peers from the granted remote control peer list.
        // If the corresponding peer was in the granted list, then this peer has
        // already been granted and we must call the remoteControlGranted
        // function for this listener.
        if (this.removesNullAndRevokedControlPeer(peer.getPeerID()) != -1)
            listener.remoteControlGranted(new RemoteControlGrantedEvent(peer));

        if (deferredRemoteControlPeers.contains(peer.getAddress())) {
            fireRemoteControlGranted(peer);
            deferredRemoteControlPeers.remove(peer.getAddress());
        }
    }

    /**
     * Fires a <code>RemoteControlGrantedEvent</code> to all registered listeners.
     *
     * @param peer the <code>CallPeer</code>
     */
    public void fireRemoteControlGranted(CallPeer peer)
    {
        RemoteControlListener listener = getListener(peer);

        if (listener != null) {
            listener.remoteControlGranted(new RemoteControlGrantedEvent(peer));
        }
        // The UI has not created the listener yet, then we need to store the
        // information taht this peer has alreayd been granted.
        else {
            // Removes all previous instance of this peer.
            this.removesNullAndRevokedControlPeer(peer.getPeerID());
            // Adds the peer to the granted remote control peer list.
            synchronized (this.grantedRemoteControlPeers) {
                this.grantedRemoteControlPeers.add(peer);
            }
        }
    }

    /**
     * Fires a <code>RemoteControlGrantedEvent</code> to all registered listeners.
     *
     * @param peer the <code>CallPeer</code>
     */
    public void fireRemoteControlRevoked(CallPeer peer)
    {
        RemoteControlListener listener = getListener(peer);

        if (listener != null) {
            listener.remoteControlRevoked(new RemoteControlRevokedEvent(peer));
        }

        // Removes the peer from the granted remote control peer list.
        this.removesNullAndRevokedControlPeer(peer.getPeerID());
    }

    public void addAddDeferredRemoteControlPeer(String address)
    {
        if (!deferredRemoteControlPeers.contains(address)) {
            deferredRemoteControlPeers.add(address);
        }
    }

    /**
     * Gets a list of <code>RemoteControlListener</code>s to be notified of remote control access changes.
     *
     * @return a list of <code>RemoteControlListener</code>s to be notifed of remote control access changes
     */
    protected List<RemoteControlListener> getListeners()
    {
        List<RemoteControlListener> listeners;

        synchronized (this.listeners) {
            Iterator<WeakReference<RemoteControlListener>> i = this.listeners.iterator();
            listeners = new ArrayList<>(this.listeners.size());

            while (i.hasNext()) {
                RemoteControlListener l = i.next().get();

                if (l == null)
                    i.remove();
                else
                    listeners.add(l);
            }
        }
        return listeners;
    }

    /**
     * Removes a <code>RemoteControlListener</code> to be notified when remote peer accept/revoke to
     * give us full control.
     *
     * @param listener <code>RemoteControlListener</code> to remove
     */
    public void removeRemoteControlListener(RemoteControlListener listener)
    {
        synchronized (listeners) {
            Iterator<WeakReference<RemoteControlListener>> i = listeners.iterator();

            while (i.hasNext()) {
                RemoteControlListener l = i.next().get();

                if ((l == null) || l.equals(listener))
                    i.remove();
            }
        }
    }

    /**
     * Removes null and the peer corresponding to the revokedPeerID from the granted control peer
     * list.
     *
     * @param revokedPeerID The ID of the revoked peer. May be null to only clear null instances from the granted
     * control peer list.
     * @return The index corresponding to the revokedPeerID entry. -1 if the revoked PeerID is null,
     * or if the revokedPeerID is not found and removed.
     */
    private int removesNullAndRevokedControlPeer(String revokedPeerID)
    {
        int index = -1;
        synchronized (this.grantedRemoteControlPeers) {
            CallPeer peer;
            for (int i = 0; i < this.grantedRemoteControlPeers.size(); ++i) {
                peer = this.grantedRemoteControlPeers.get(i);
                if (peer == null || peer.getPeerID().equals(revokedPeerID)) {
                    this.grantedRemoteControlPeers.remove(i);
                    index = i;
                    --i;
                }
            }
        }
        return index;
    }

    /**
     * Returns the <code>RemoteControlListener</code> corresponding to the given <code>callPeer</code>, if
     * it exists.
     *
     * @param callPeer the <code>CallPeer</code> to get the corresponding <code>RemoteControlListener</code> of
     * @return the <code>RemoteControlListener</code> corresponding to the given <code>callPeer</code>, if
     * it exists; <code>null</code>, otherwise
     */
    protected RemoteControlListener getListener(CallPeer callPeer)
    {
        String peerID = callPeer.getPeerID();

        synchronized (listeners) {
            Iterator<WeakReference<RemoteControlListener>> i = listeners.iterator();

            while (i.hasNext()) {
                RemoteControlListener l = i.next().get();

                if (l == null)
                    i.remove();
                else if (peerID.equals(l.getCallPeer().getPeerID()))
                    return l;
            }
        }
        return null;
    }
}
