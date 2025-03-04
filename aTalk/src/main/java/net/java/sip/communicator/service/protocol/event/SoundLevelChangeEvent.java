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
package net.java.sip.communicator.service.protocol.event;

import java.util.*;

/**
 * <code>SoundLevelChangeEvent</code>s are triggered whenever a change occurs in the sound level of the
 * audio stream coming from a certain <code>CallPeer</code>.
 * <p>
 * In the case of a <code>CallPeer</code>, which is also a conference focus and is participating in the
 * conference as a <code>ConferenceMember</code> the level would be the aggregated level of all
 * <code>ConferenceMember</code>s levels including the one corresponding to the peer itself.
 * <p>
 * In the case of a <code>CallPeer</code>, which is also a conference focus, but is NOT participating in
 * the conference as a <code>ConferenceMember</code> (server) the level would be the aggregated level of
 * all attached <code>ConferenceMember</code>s.
 *
 * @author Yana Stamcheva
 */
public class SoundLevelChangeEvent extends EventObject
{
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * The maximum level that can be reported for a participant. Level values should be distributed
	 * among <code>MAX_LEVEL</code> and {@link #MIN_LEVEL} in a way that would appear uniform to users.
	 * <p>
	 * <b>Warning</b>: The value should be equal to
	 * <code>net.java.sip.communicator.service.neomedia.event.SimpleAudioLevelListener#MAX_VALUE</code>
	 * because we do not currently perform a conversion from the <code>SimpleAudioLevelListener</code>
	 * range to the <code>SoundLevelChangeEvent</code> range when we fire the event.
	 * </p>
	 */
	public static final int MAX_LEVEL = 127;

	/**
	 * The maximum (zero) level that can be reported for a participant. Level values should be
	 * distributed among {@link #MAX_LEVEL} and <code>MIN_LEVEL</code> in a way that would appear
	 * uniform to users.
	 * <p>
	 * <b>Warning</b>: The value should be equal to
	 * <code>net.java.sip.communicator.service.neomedia.event.SimpleAudioLevelListener#MIN_VALUE</code>
	 * because we do not currently perform a conversion from the <code>SimpleAudioLevelListener</code>
	 * range to the <code>SoundLevelChangeEvent</code> range when we fire the event.
	 * </p>
	 */
	public static final int MIN_LEVEL = 0;

	/**
	 * The audio stream level, for the change of which this event is about.
	 */
	private final int level;

	/**
	 * Creates an <code>StreamSoundLevelEvent</code> for the given <code>callPeer</code> by indicating the
	 * current sound level of the audio stream.
	 *
	 * @param source
	 *        the source from which the change is received
	 * @param level
	 *        the current sound level of the audio stream
	 */
	public SoundLevelChangeEvent(Object source, int level)
	{
		super(source);

		this.level = level;
	}

	/**
	 * Returns the current sound level of the audio stream.
	 *
	 * @return the current sound level of the audio stream
	 */
	public int getLevel()
	{
		return level;
	}
}
