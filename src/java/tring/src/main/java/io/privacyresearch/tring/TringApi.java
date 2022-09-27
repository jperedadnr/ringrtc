package io.privacyresearch.tring;

/**
 *
 * @author johan
 */
public interface TringApi {
    
    void statusCallback(long callId, long peerId, int dir, int type);
    
    void answerCallback(byte[] opaque);
    
}
