package io.privacyresearch.tringapi;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Logger;

 /**
  * This class provides the access points for the application to interact with
  * RingRTC. 
  * Methods here are invoked by Equation.
  * When RingRTC wants to call back into the application, the TringApi interface
  * is used.
  * @author johan
  */
public class TringBridge {
    
    private TringService service;
    private static final Logger LOG = Logger.getLogger(TringBridge.class.getName());

    public TringBridge(final TringApi api) {
        ServiceLoader<TringService> loader = ServiceLoader.load(TringService.class);
        Optional<TringService> serviceOpt = loader.findFirst();
        serviceOpt.ifPresentOrElse(s -> {
            this.service = s;
            this.service.setApi(api);
        }, () -> {
            LOG.warning("No tring service!");
        });

    }
    
    public String getVersionInfo() {
        if (service == null) {
            return "No TringService registered";
        } else {
            return service.getVersionInfo();
        }
    }

    public void acceptCall() {
        service.acceptCall();
    }

    public void proceed(long callId) {
        service.proceed(callId);
    }

    public void receivedIce(long callId, int senderDeviceId, List<byte[]> ice) {
        service.receivedIce(callId, senderDeviceId, ice);
    }

    public void receivedOffer(String peerId, long callId, int senderDeviceId, int receiverDeviceId,
            byte[] senderKey, byte[] receiverKey, byte[] opaque) {
        service.receivedOffer(peerId, callId, senderDeviceId, receiverDeviceId, senderKey, receiverKey, opaque);
    }

}
