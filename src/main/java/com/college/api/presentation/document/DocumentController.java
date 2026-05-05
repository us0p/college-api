package com.college.api.presentation.document;

import com.college.api.application.document.DocumentService;
import com.college.api.domain.document.Document;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Documents", description = "Document upload, storage and AI embedding management")
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    @Operation(summary = "List all documents")
    @GetMapping
    public List<DocumentResponse> findAll() {
        return service.findAll().stream().map(DocumentResponse::from).toList();
    }

    @Operation(summary = "Get a document by ID")
    @ApiResponse(responseCode = "404", description = "Document not found")
    @GetMapping("/{id}")
    public DocumentResponse findById(@PathVariable Integer id) {
        return DocumentResponse.from(service.findById(id));
    }

    @Operation(summary = "Upload a document",
            description = "Uploads the file to S3, persists metadata, and automatically generates and stores vector embeddings via the configured embedding model.")
    @ApiResponse(responseCode = "201", description = "Document uploaded and embeddings stored")
    @ApiResponse(responseCode = "400", description = "Missing required parameter or file")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> create(
            @RequestParam Integer userId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "false") boolean knowledgeBase,
            @RequestPart("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : file.getName();
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        Document document = service.create(
                userId, fileName, description, file.getBytes(), contentType, (int) file.getSize(), knowledgeBase);
        return ResponseEntity.status(HttpStatus.CREATED).body(DocumentResponse.from(document));
    }

    @Operation(summary = "Delete a document")
    @ApiResponse(responseCode = "204", description = "Document deleted")
    @ApiResponse(responseCode = "404", description = "Document not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
