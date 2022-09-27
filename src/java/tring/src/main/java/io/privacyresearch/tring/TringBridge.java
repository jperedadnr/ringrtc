package io.privacyresearch.tring;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.util.List;

public class TringBridge {

    private static boolean nativeSupport = false;
    private static long nativeVersion = 0;

    private MemorySession scope;
    private long callEndpoint;
    private final TringApi api;
    private long activeCallId;
    
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

    public TringBridge(TringApi api) {
        this.api = api;
        initiate();
    }

    private void initiate() {
        scope = MemorySession.openShared();
        tringlib_h.initRingRTC(toJString(scope, "Hello from Java"));
        this.callEndpoint = tringlib_h.createCallEndpoint(createStatusCallback(), 
                createAnswerCallback(), createIceUpdateCallback());
    }

    public void receivedOffer(String peerId, long callId, int senderDeviceId, int receiverDeviceId,
            byte[] senderKey, byte[] receiverKey, byte[] opaque) {
        int mediaType = 0;
        long ageSec = 0;
        this.activeCallId = callId;
        tringlib_h.receivedOffer(callEndpoint, toJString(scope, peerId), callId, mediaType, senderDeviceId,
                receiverDeviceId, toJByteArray(scope, senderKey), toJByteArray(scope, receiverKey),
                toJByteArray(scope, opaque),
                ageSec);
    }

    public void setSelfUuid(String uuid) {
        tringlib_h.setSelfUuid(callEndpoint, toJString(scope, uuid));
    }
    
    public void proceed(long callId) {
        tringlib_h.proceedCall(callEndpoint, callId, 0, 0);
    }
    
    public void receivedIce(long callId, int senderDeviceId, List<byte[]> ice) {
        MemorySegment icePack = toJByteArray2D(scope, ice);
        tringlib_h.receivedIce(callEndpoint, callId, senderDeviceId, icePack);
    }

    static MemorySegment toJByteArray2D(MemorySession ms, List<byte[]> rows) {
        MemorySegment answer = JByteArray2D.allocate(ms);
        JByteArray2D.len$set(answer, rows.size());
        MemorySegment rowsSegment = JByteArray2D.buff$slice(answer);
        for (int i = 0; i < rows.size(); i++) {
            MemorySegment singleRowSegment = toJByteArray(ms, rows.get(i));
            MemorySegment row = rowsSegment.asSlice(16 * i, 16);
            row.copyFrom(singleRowSegment);
        }
        return answer;
    }

    static MemorySegment toJByteArray(MemorySession ms, byte[] bytes) {
        MemorySegment answer = JByteArray.allocate(ms);
        JByteArray.len$set(answer, bytes.length);
        MemorySegment byteBuffer = MemorySegment.allocateNative(bytes.length, ms);
        MemorySegment.copy(bytes, 0, byteBuffer, ValueLayout.JAVA_BYTE, 0, bytes.length);
        JByteArray.buff$set(answer, byteBuffer.address());
        return answer;
    }
    
    static byte[] fromJByteArray(MemorySession ms, MemorySegment jByteArray) {
        long len = JByteArray.len$get(jByteArray);
        MemoryAddress pointer = JByteArray.buff$get(jByteArray);
        MemorySegment byteSegment = JByteArray.ofAddress(pointer, ms);
        byte[] data = byteSegment.toArray(ValueLayout.JAVA_BYTE);
        return data;
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

    Addressable createStatusCallback() {
        StatusCallbackImpl sci = new StatusCallbackImpl();
        MemorySegment seg = createCallEndpoint$statusCallback.allocate(sci, scope);
        return seg.address();
    }
    
    class StatusCallbackImpl implements createCallEndpoint$statusCallback {
        @Override
        public void apply(MemorySegment callId, long _x1, int direction, int type) {
            long id = CallId.id$get(callId);
            api.statusCallback(id, _x1, direction, type);
        }
    }
    
    Addressable createAnswerCallback() {
        AnswerCallbackImpl sci = new AnswerCallbackImpl();
        MemorySegment seg = createCallEndpoint$answerCallback.allocate(sci, scope);
        return seg.address();
    }
    
    class AnswerCallbackImpl implements createCallEndpoint$answerCallback {
        @Override
        public void apply(MemorySegment opaque) {
            System.err.println("TRINGBRIDGE, send answer!");
            byte[] bytes = fromJByteArray(scope, opaque);
            System.err.println("TRING, bytes to send = "+java.util.Arrays.toString(bytes));
            api.answerCallback(bytes);
            System.err.println("TRING, answer sent");
            sendAck();
            System.err.println("TRING, ack sent");
        }
    }
    
    Addressable createIceUpdateCallback() {
        IceUpdateCallbackImpl sci = new IceUpdateCallbackImpl();
        MemorySegment seg = createCallEndpoint$iceUpdateCallback.allocate(sci, scope);
        return seg.address();
    }
    
    class IceUpdateCallbackImpl implements createCallEndpoint$iceUpdateCallback {
        @Override
        public void apply(MemorySegment opaque) {
            System.err.println("TRINGBRIDGE, iceUpdate!");
           
        }
    }

    void sendAck() {
        MemorySegment callid = MemorySegment.allocateNative(8, scope);
        callid.set(ValueLayout.JAVA_LONG, 0l, activeCallId);
        tringlib_h.signalMessageSent(callEndpoint, callid);
    }
}
