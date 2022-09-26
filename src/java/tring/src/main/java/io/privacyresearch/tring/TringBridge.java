package io.privacyresearch.tring;

import io.privacyresearch.tring.tringlib_h;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author johan
 */
public class TringBridge {

    private boolean nativeSupport = false;
    
    public TringBridge() {
        
    }
    
    public void init() {
        try {
            NativeLibLoader.loadLibrary();
            tringlib_h.initRingRTC();
            nativeSupport = true;
        } catch (Throwable ex) {
            Logger.getLogger(TringBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("TringBridge init done, native support = "+nativeSupport);
    }
    
}
