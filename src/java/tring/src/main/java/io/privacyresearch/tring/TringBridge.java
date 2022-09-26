package io.privacyresearch.tring;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

public class TringBridge {

    private static boolean nativeSupport = false;
    private static long nativeVersion = 0;

    private MemorySession scope;
    private long callEndpoint;

    static {
        try {
            NativeLibLoader.loadLibrary();
            nativeSupport = true;
            nativeVersion = tringlib_h.getVersion();
        } catch (Throwable ex) {
            System.err.println("No native RingRTC support: ");
            ex.printStackTrace();
        }
    }
    
    public static long getNativeVersion() {
        return nativeVersion;
    }

    public TringBridge() {
        initiate();
    }

//    public void init() {
//        try {
//            NativeLibLoader.loadLibrary();
//            scope = MemorySession.openShared();
//            tringlib_h.initRingRTC(toJString(scope, "Hallo, wereld"));
//            nativeSupport = true;
//        } catch (Throwable ex) {
//            Logger.getLogger(TringBridge.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.err.println("TringBridge init done, native support = " + nativeSupport);
//    }

    private void initiate() {
        scope = MemorySession.openShared();
        tringlib_h.initRingRTC(toJString(scope, "Hello from Java"));
        this.callEndpoint = tringlib_h.createCallEndpoint();
    }

    public void receivedOffer(String peerId, long callId, int senderDeviceId, int receiverDeviceId,
            byte[] senderKey, byte[] receiverKey, byte[] opaque) {
        int mediaType = 0;
        long ageSec = 0;
        tringlib_h.receivedOffer(callEndpoint, toJString(scope, peerId), callId, mediaType, senderDeviceId,
                receiverDeviceId, toJByteArray(scope, senderKey), toJByteArray(scope, receiverKey),
                toJByteArray(scope, opaque),
                ageSec);
    }

    static MemorySegment toJByteArray(MemorySession ms, byte[] bytes) {
        MemorySegment answer = JByteArray.allocate(ms);
        JString.len$set(answer, bytes.length);
        MemorySegment byteBuffer = MemorySegment.allocateNative(bytes.length, ms);
        MemorySegment.copy(bytes, 0, byteBuffer, ValueLayout.JAVA_BYTE, 0, bytes.length);
        JString.buff$set(answer, byteBuffer.address());
        return answer;
    }

    static MemorySegment toJString(MemorySession ms, String src) {
        MemorySegment answer = JString.allocate(ms);
        byte[] bytes = src.getBytes();
        JString.len$set(answer, bytes.length);
        MemorySegment byteBuffer = MemorySegment.allocateNative(bytes.length, ms);
        MemorySegment.copy(bytes, 0, byteBuffer, ValueLayout.JAVA_BYTE, 0, bytes.length);
        JString.buff$set(answer, byteBuffer.address());
        return answer;
    }

}
