package vn.com.be_landingpage.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.be_landingpage.exception.BadRequestException;
import vn.com.be_landingpage.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;
    private final MediaAssetRepository mediaAssetRepository;

    public MediaDtos.MediaAssetResponse upload(MultipartFile file, String folder, String altText) {
        MediaAsset asset = uploadAsset(file, folder, altText);
        return MediaDtos.MediaAssetResponse.from(asset);
    }

    public MediaAsset uploadAsset(MultipartFile file, String folder, String altText) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File ảnh là bắt buộc");
        }
        String normalizedFolder = normalizeFolder(folder);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", normalizedFolder,
                    "resource_type", "image",
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false
            ));

            MediaAsset asset = new MediaAsset();
            asset.setPublicId(asString(result.get("public_id")));
            asset.setUrl(asString(result.get("url")));
            asset.setSecureUrl(asString(result.get("secure_url")));
            asset.setFolder(normalizedFolder);
            asset.setOriginalFilename(file.getOriginalFilename());
            asset.setFormat(asString(result.get("format")));
            asset.setResourceType(asString(result.get("resource_type")));
            asset.setBytes(asLong(result.get("bytes")));
            asset.setWidth(asInteger(result.get("width")));
            asset.setHeight(asInteger(result.get("height")));
            asset.setAltText(altText);
            return mediaAssetRepository.save(asset);
        } catch (IOException ex) {
            throw new BadRequestException("Không thể đọc file ảnh: " + ex.getMessage());
        }
    }

    public List<MediaDtos.MediaAssetResponse> findAll(String folder) {
        List<MediaAsset> assets = (folder == null || folder.isBlank())
                ? mediaAssetRepository.findAll()
                : mediaAssetRepository.findAllByFolderContainingIgnoreCaseOrderByCreatedAtDesc(folder.trim());
        return assets.stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .map(MediaDtos.MediaAssetResponse::from)
                .toList();
    }

    public void delete(Long id, boolean deleteRemote) {
        MediaAsset asset = mediaAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy media asset id=" + id));
        if (deleteRemote) {
            try {
                cloudinary.uploader().destroy(asset.getPublicId(), ObjectUtils.emptyMap());
            } catch (IOException ex) {
                throw new BadRequestException("Không thể xóa ảnh trên Cloudinary: " + ex.getMessage());
            }
        }
        mediaAssetRepository.delete(asset);
    }

    private String normalizeFolder(String folder) {
        String value = (folder == null || folder.isBlank()) ? "landingpage" : folder.trim();
        value = value.replace('\\', '/')
                .replaceAll("^/+", "")
                .replaceAll("/+$", "")
                .replaceAll("\\s+", "-")
                .toLowerCase(Locale.ROOT);
        if (!value.matches("[a-z0-9/_-]+")) {
            throw new BadRequestException("Tên folder chỉ nên gồm chữ, số, dấu gạch ngang, gạch dưới hoặc dấu /");
        }
        return value;
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Long asLong(Object value) {
        return value instanceof Number number ? number.longValue() : null;
    }

    private Integer asInteger(Object value) {
        return value instanceof Number number ? number.intValue() : null;
    }
}
