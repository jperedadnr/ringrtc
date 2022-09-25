package io.privacyresearch.tring;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NativeLibLoader {

    public static void loadLibrary() throws IOException {
        InputStream is = NativeLibLoader.class.getResourceAsStream("/libringrtc.so");
        Path target = Files.createTempFile("", "");
        Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
        System.load(target.toString());
    }
}
