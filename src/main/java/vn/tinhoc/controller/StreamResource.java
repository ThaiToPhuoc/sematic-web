package vn.tinhoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tinhoc.config.StreamProperty;
import vn.tinhoc.service.StreamService;

import java.io.IOException;

@RestController
@RequestMapping("/api/stream")
public class StreamResource {
    @Autowired
    StreamProperty streamProperty;

    @Autowired
    StreamService streamService;

    @GetMapping("/video/{name}")
    public ResponseEntity<ResourceRegion> getVideo(@PathVariable String name,
                                                   @RequestHeader HttpHeaders headers) throws IOException {
        Resource video = new UrlResource("file:///" + streamProperty.getMediaPath() + "/" + name);
        ResourceRegion region = streamService.resourceRegion(video, headers);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
    }
}
