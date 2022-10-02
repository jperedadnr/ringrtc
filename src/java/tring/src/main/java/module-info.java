module io.privacyresearch.tring {
    requires java.logging;
    requires io.privacyresearch.tringapi;

    exports io.privacyresearch.tring;
    provides io.privacyresearch.tringapi.TringService with io.privacyresearch.tring.TringServiceImpl;
}
