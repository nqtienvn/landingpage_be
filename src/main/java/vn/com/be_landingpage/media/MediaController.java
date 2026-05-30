package vn.com.be_landingpage.media;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaDtos.MediaAssetResponse upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder,
            @RequestParam(value = "altText", required = false) String altText
    ) {
        return mediaService.upload(file, folder, altText);
    }

    @GetMapping
    public List<MediaDtos.MediaAssetResponse> findAll(
            @RequestParam(value = "folder", required = false) String folder
    ) {
        return mediaService.findAll(folder);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam(value = "deleteRemote", defaultValue = "false") boolean deleteRemote
    ) {
        mediaService.delete(id, deleteRemote);
    }
}
