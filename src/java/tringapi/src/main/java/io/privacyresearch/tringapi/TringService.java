 package io.privacyresearch.tringapi;

import java.util.List;

 /**
  * Implementations of this interface provides the access points for the application to interact with
  * RingRTC. 
  * Methods here are invoked by TringBridge, which is invoked by Equation.
  * When RingRTC wants to call back into the application, the TringApi interface
  * is used.
  * @author johan
  */
public interface TringService {

    public void setApi(TringApi api);

    public void acceptCall();
    public void ignoreCall();
    public void hangupCall();

    public void proceed(long callId);

    public void receivedIce(long callId, int senderDeviceId, List<byte[]> ice);

    public void receivedOffer(String peerId, long callId, int senderDeviceId, int receiverDeviceId,
            byte[] senderKey, byte[] receiverKey, byte[] opaque);
    
    public default String getVersionInfo() {
        return "Unresolved TringService";
    }

}
