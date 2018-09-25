package com.ragul.leaf.controller;

import com.ragul.leaf.model.Leaf;
import com.ragul.leaf.payload.UploadFileResponse;
import com.ragul.leaf.repository.LeafRepository;
import com.ragul.leaf.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private LeafRepository leafRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/")
    public ResponseEntity<List<Leaf>> getAll() {
        List<Leaf> leaves = leafRepository.findAll();

        return ResponseEntity
                .ok()
                .body(leaves);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("description") String description) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/downloadFile/")
                .path(fileName)
                .toUriString();

        Leaf leaf = new Leaf(name, description, fileDownloadUri);
        leafRepository.save(leaf);

        return new UploadFileResponse(fileName,
                name,
                description,
                fileDownloadUri,
                file.getContentType(),
                file.getSize());
    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/deleteAll")
    public String deleteAll() {
        leafRepository.deleteAll();
        return "All data deleted";
    }

}
