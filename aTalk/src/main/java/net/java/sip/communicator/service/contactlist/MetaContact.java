/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.sip.communicator.service.contactlist;

import net.java.sip.communicator.service.protocol.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * A MetaContact is an abstraction used for merging multiple Contacts (most
 * often) belonging to different <code>ProtocolProvider</code>s.
 * <p>
 * Instances of a MetaContact are read-only objects that cannot be modified
 * directly but only through the corresponding MetaContactListService.
 * </p>
 *
 * @author Emil Ivov
 * @author Lubomir Marinov
 * @author Eng Chong Meng
 */
public interface MetaContact extends Comparable<MetaContact>
{
    /**
     * Returns the default protocol specific <code>Contact</code> to use when
     * communicating with this <code>MetaContact</code>.
     *
     * @return the default <code>Contact</code> to use when communicating with this <code>MetaContact</code>
     */
    Contact getDefaultContact();

    /**
     * Returns the default protocol specific <code>Contact</code> to use with this
     * <code>MetaContact</code> for a precise operation (IM, call, ...).
     *
     * @param operationSet the operation for which the default contact is needed
     * @return the default contact for the specified operation.
     */
    Contact getDefaultContact(Class<? extends OperationSet> operationSet);

    /**
     * Returns the first found protocol specific <code>Contact</code> of the <code>MetaContact</code>
     * that support a precise operation (IM, call, ...).
     * Currently the main use is to display the call button for user access
     *
     * @param operationSet the operation for which the metacontact is supported
     * @return the first contact for the specified operation supported.
     */
    Contact getOpSetSupportedContact(Class<? extends OperationSet> operationSet);

    boolean isFeatureSupported(String feature);

    /**
     * Returns a <code>java.util.Iterator</code> with all protocol specific
     * <code>Contacts</code> encapsulated by this <code>MetaContact</code>.
     * <p>
     * Note to implementors:  In order to prevent problems with concurrency, the
     * <code>Iterator</code> returned by this method should not be over the actual
     * list of contacts but rather over a copy of that list.
     * <p>
     *
     * @return a <code>java.util.Iterator</code> containing all protocol specific
     * <code>Contact</code>s that were registered as subContacts for this <code>MetaContact</code>
     */
    Iterator<Contact> getContacts();

    /**
     * Returns a contact encapsulated by this meta contact, having the specified
     * contactAddress and coming from the indicated ownerProvider.
     *
     * @param contactAddress the address of the contact who we're looking for.
     * @param ownerProvider a reference to the ProtocolProviderService that
     * the contact we're looking for belongs to.
     * @return a reference to a <code>Contact</code>, encapsulated by this
     * MetaContact, carrying the specified address and originating from the
     * specified ownerProvider or null if no such contact exists..
     */
    Contact getContact(String contactAddress, ProtocolProviderService ownerProvider);

    /**
     * Returns <code>true</code> if the given <code>protocolContact</code> is contained
     * in this <code>MetaContact</code>, otherwise - returns <code>false</code>.
     *
     * @param protocolContact the <code>Contact</code> we're looking for
     * @return <code>true</code> if the given <code>protocolContact</code> is contained
     * in this <code>MetaContact</code>, otherwise - returns <code>false</code>
     */
    boolean containsContact(Contact protocolContact);

    /**
     * Returns the number of protocol specific <code>Contact</code>s that this <code>MetaContact</code> contains.
     *
     * @return an int indicating the number of protocol specific contacts merged
     * in this <code>MetaContact</code>
     */
    int getContactCount();

    /**
     * Returns all protocol specific Contacts, encapsulated by this MetaContact
     * and coming from the indicated ProtocolProviderService. If none of the
     * contacts encapsulated by this MetaContact is originating from the
     * specified provider then an empty iterator is returned.
     * <p>
     * Note to implementors:  In order to prevent problems with concurrency, the
     * <code>Iterator</code> returned by this method should not be over the actual
     * list of contacts but rather over a copy of that list.
     * <p>
     *
     * @param provider a reference to the <code>ProtocolProviderService</code>
     * whose contacts we'd like to get.
     * @return an <code>Iterator</code> over all contacts encapsulated in this
     * <code>MetaContact</code> and originating from the specified provider.
     */
    Iterator<Contact> getContactsForProvider(ProtocolProviderService provider);

    /**
     * Returns all protocol specific Contacts, encapsulated by this MetaContact
     * and supporting the given <code>opSetClass</code>. If none of the
     * contacts encapsulated by this MetaContact is supporting the specified
     * <code>OperationSet</code> class then an empty list is returned.
     * <p>
     * Note to implementors:  In order to prevent problems with concurrency, the
     * <code>List</code> returned by this method should not be the actual list of
     * contacts but rather a copy of that list.
     * <p>
     *
     * @param opSetClass the operation for which the default contact is needed
     * @return a <code>List</code> of all contacts encapsulated in this
     * <code>MetaContact</code> and supporting the specified <code>OperationSet</code>
     */
    List<Contact> getContactsForOperationSet(Class<? extends OperationSet> opSetClass);

    /**
     * Returns the MetaContactGroup currently containing this meta contact
     *
     * @return a reference to the MetaContactGroup currently containing this meta contact.
     */
    MetaContactGroup getParentMetaContactGroup();

    /**
     * Returns a String identifier (the actual contents is left to
     * implementations) that uniquely represents this <code>MetaContact</code> in the containing <code>MetaContactList</code>
     *
     * @return String
     */
    String getMetaUID();

    /**
     * Set the unread message count for this metaCount represent
     *
     * @param count unread message count
     */
    void setUnreadCount(int count);

    /**
     * Returns the unread message count for this metaContact
     *
     * @return the unread message count
     */
    int getUnreadCount();

    /**
     * Returns a characteristic display name that can be used when including
     * this <code>MetaContact</code> in user interface.
     *
     * @return a human readable String that represents this meta contact.
     */
    String getDisplayName();

    /**
     * Returns the avatar of this contact, that can be used when including this
     * <code>MetaContact</code> in user interface.
     *
     * @return an avatar (e.g. user photo) of this contact.
     */
    byte[] getAvatar();

    /**
     * Returns the avatar of this contact, that can be used when including this
     * <code>MetaContact</code> in user interface. The isLazy
     * parameter would tell the implementation if it could return the locally
     * stored avatar or it should obtain the avatar right from the server.
     *
     * @param isLazy Indicates if this method should return the locally stored
     * avatar or it should obtain the avatar right from the server.
     * @return an avatar (e.g. user photo) of this contact.
     */
    byte[] getAvatar(boolean isLazy);

    /**
     * Returns a String representation of this <code>MetaContact</code>.
     *
     * @return a String representation of this <code>MetaContact</code>.
     */
    String toString();

    /**
     * Adds a custom detail to this contact.
     *
     * @param name name of the detail.
     * @param value the value of the detail.
     */
    void addDetail(String name, String value);

    /**
     * Remove the given detail.
     *
     * @param name of the detail to be removed.
     * @param value value of the detail to be removed.
     */
    void removeDetail(String name, String value);

    /**
     * Remove all details with given name.
     *
     * @param name of the details to be removed.
     */
    void removeDetails(String name);

    /**
     * Change the detail.
     *
     * @param name of the detail to be changed.
     * @param oldValue the old value of the detail.
     * @param newValue the new value of the detail.
     */
    void changeDetail(String name, String oldValue, String newValue);

    /**
     * Get all details with given name.
     *
     * @param name the name of the details we are searching.
     * @return JSONArray for the details with the given name.
     */
    JSONArray getDetails(String name);

    JSONObject getDetails();

    /**
     * Gets the user data associated with this instance and a specific key.
     *
     * @param key the key of the user data associated with this instance to be retrieved
     * @return an <code>Object</code> which represents the value associated with
     * this instance and the specified <code>key</code>; <code>null</code> if no
     * association with the specified <code>key</code> exists in this instance
     */
    Object getData(Object key);

    /**
     * Sets a user-specific association in this instance in the form of a
     * key-value pair. If the specified <code>key</code> is already associated in
     * this instance with a value, the existing value is overwritten with the
     * specified <code>value</code>.
     * <p>
     * The user-defined association created by this method and stored in this
     * instance is not serialized by this instance and is thus only meant for runtime use.
     * </p>
     * <p>
     * The storage of the user data is implementation-specific and is thus not
     * guaranteed to be optimized for execution time and memory use.
     * </p>
     *
     * @param key the key to associate in this instance with the specified value
     * @param value the value to be associated in this instance with the specified <code>key</code>
     */
    void setData(Object key, Object value);
}
