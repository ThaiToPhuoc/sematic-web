package vn.tinhoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.tinhoc.config.StreamProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@EnableAsync(proxyTargetClass = true)
public class StreamService {
    @Autowired
    StreamProperty streamProperty;

    public ResourceRegion resourceRegion(Resource video, HttpHeaders headers) throws IOException {
        long contentLength = video.contentLength();
        HttpRange range = headers.getRange().get(0);
        if (range != null) {
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(1024 * 1024, end - start + 1);

            return new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = Math.min(1024 * 1024, contentLength);
            return new ResourceRegion(video, 0, rangeLength);
        }
    }

    public boolean save(MultipartFile file) throws IOException {
        return save(file.getOriginalFilename(), file);
    }

    public boolean save(String fileName, MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get(streamProperty.getMediaPath()).resolve(fileName));
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    @Async("processExecutor")
    public void delete(String fileName) throws IOException {
        Files.delete(Paths.get(streamProperty.getMediaPath()).resolve(fileName));
    }
}
