package vn.tinhoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class StreamProperty {
    private String mediaPath;

    public StreamProperty(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaPath() {
        return mediaPath;
    }
}
