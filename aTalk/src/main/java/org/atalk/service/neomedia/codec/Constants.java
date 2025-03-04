/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.neomedia.codec;

/**
 * Defines constants which are used by both neomedia clients and implementations.
 *
 * @author Lyubomir Marinov
 * @author Boris Grozev
 * @author Eng Chong Meng
 */
public class Constants
{
    /**
     * The <code>/rtp</code> constant. Introduced in order to achieve consistency in the casing of the <code>String</code>.
     */
    public static final String _RTP = "/rtp";

    /**
     * The ALAW/rtp constant.
     */
    public static final String ALAW_RTP = "ALAW" + _RTP;

    /**
     * The AMR-WB constant.
     */
    public static final String AMR_WB = "AMR-WB";

    /**
     * The AMR-WB/rtp constant.
     */
    public static final String AMR_WB_RTP = AMR_WB + _RTP;

    /**
     * The Android Surface constant. It is used as VideoFormat pseudo encoding in which case the
     * object is passed through the buffers instead of byte array (image) for example.
     */
    public static final String ANDROID_SURFACE = "android_surface";

    /**
     * The list of well-known sample rates of audio data used throughout neomedia.
     */
    public static final double[] AUDIO_SAMPLE_RATES
            = {48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000};

    /**
     * The G722 constant.
     */
    public static final String G722 = "g722";

    /**
     * The G722/rtp constant.
     */
    public static final String G722_RTP = G722 + _RTP;

    /**
     * The H264 constant.
     */
    public static final String H264 = "h264";

    /**
     * The H264/rtp constant.
     */
    public static final String H264_RTP = H264 + _RTP;

    /**
     * The ilbc constant.
     */
    public static final String ILBC = "ilbc";

    /**
     * mode: Frame size for the encoding/decoding
     * 20 - 20 ms
     * 30 - 30 ms
     */
    public static final int ILBC_MODE = 30;

    /**
     * The ilbc/rtp constant.
     */
    public static final String ILBC_RTP = ILBC + _RTP;

    /**
     * The ptime constant.
     */
    public static final String PTIME = "ptime";

    /**
     * The opus constant.
     */
    public static final String OPUS = "opus";

    /**
     * The opus/rtp constant.
     */
    public static final String OPUS_RTP = OPUS + _RTP;

    /**
     * The name of the property used to control the Opus encoder "audio bandwidth" setting
     */
    public static final String PROP_OPUS_BANDWIDTH = "neomedia.codec.audio.opus.encoder.AUDIO_BANDWIDTH";

    /**
     * The name of the property used to control the Opus encoder bitrate setting
     */
    public static final String PROP_OPUS_BITRATE = "neomedia.codec.audio.opus.encoder.BITRATE";

    /**
     * The name of the property used to control the Opus encoder 'complexity' setting
     */
    public static final String PROP_OPUS_COMPLEXITY = "neomedia.codec.audio.opus.encoder.COMPLEXITY";

    /**
     * The name of the property used to control the Opus encoder "DTX" setting
     */
    public static final String PROP_OPUS_DTX = "neomedia.codec.audio.opus.encoder.DTX";

    /**
     * The name of the property used to control whether FEC is enabled for the Opus encoder
     */
    public static final String PROP_OPUS_FEC = "neomedia.codec.audio.opus.encoder.FEC";

    /**
     * The name of the property used to control the Opus encoder "minimum expected packet loss" setting
     */
    public static final String PROP_OPUS_MIN_EXPECTED_PACKET_LOSS
            = "neomedia.codec.audio.opus.encoder.MIN_EXPECTED_PACKET_LOSS";

    /**
     * The name of the property used to control whether VBR is enabled for the Opus encoder
     */
    public static final String PROP_OPUS_VBR = "neomedia.codec.audio.opus.encoder.VBR";

    /**
     * The name of the property used to control whether FEC support is advertised for SILK
     */
    public static final String PROP_SILK_ADVERSISE_FEC = "neomedia.codec.audio.silk.ADVERTISE_FEC";

    /**
     * The name of the property used to control the the 'always assume packet loss' setting for SILK
     */
    public static final String PROP_SILK_ASSUME_PL = "neomedia.codec.audio.silk.encoder.AWLAYS_ASSUME_PACKET_LOSS";

    /**
     * The name of the property used to control whether FEC is enabled for SILK
     */
    public static final String PROP_SILK_FEC = "neomedia.codec.audio.silk.encoder.USE_FEC";

    /**
     * The name of the property used to control the SILK 'speech activity threshold'
     */
    public static final String PROP_SILK_FEC_SAT = "neomedia.codec.audio.silk.encoder.SPEECH_ACTIVITY_THRESHOLD";

    /**
     * The name of the RED RTP format (RFC2198)
     */
    public static final String RED = "red";

    /**
     * The SILK constant.
     */
    public static final String SILK = "SILK";

    /**
     * The SILK/rtp constant.
     */
    public static final String SILK_RTP = SILK + _RTP;

    /**
     * The SPEEX constant.
     */
    public static final String SPEEX = "speex";

    /**
     * The SPEEX/RTP constant.
     */
    public static final String SPEEX_RTP = SPEEX + _RTP;

    /**
     * Pseudo format representing DTMF tones sent over RTP.
     */
    public static final String TELEPHONE_EVENT = "telephone-event";

    /**
     * The name of the ulpfec RTP format (RFC5109)
     */
    public static final String ULPFEC = "ulpfec";

    /**
     * The name of the flexfec-03 rtp format
     */
    public static final String FLEXFEC_03 = "flexfec-03";

    /**
     * The VP8 constant
     */
    public static final String VP8 = "VP8";

    /**
     * The VP9 constant
     */
    public static final String VP9 = "VP9";

    /**
     * The RTX constant
     */
    public static final String RTX = "rtx";

    /**
     * The VP9/rtp constant.
     */
    public static final String VP9_RTP = VP9 + _RTP;

    /**
     * The VP8/rtp constant.
     */
    public static final String VP8_RTP = VP8 + _RTP;

    /**
     * The RTX/rtp constant.
     */
    public static final String RTX_RTP = RTX + _RTP;
}
