package com.college.api.application.document;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.document.*;
import com.college.api.domain.role.Role;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock private DocumentRepository documentRepository;
    @Mock private DocumentEmbeddingRepository embeddingRepository;
    @Mock private UserRepository userRepository;
    @Mock private DocumentStoragePort storagePort;
    @Mock private EmbeddingPort embeddingPort;

    @InjectMocks
    private DocumentService service;

    private final User user = User.builder().id(1).username("alice")
            .role(Role.builder().id(1).name("student").build()).build();

    private static final byte[] CONTENT = new byte[]{1, 2, 3};
    private static final float[] EMBEDDING = new float[768];

    @Test
    void findAll_returnsAllDocuments() {
        List<Document> docs = List.of(
                Document.builder().id(1).user(user).fileName("report.pdf")
                        .fileSize(1024).bucketUrl("https://bucket.s3.us-east-1.amazonaws.com/report.pdf").build()
        );
        when(documentRepository.findAll()).thenReturn(docs);

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void findById_whenExists_returnsDocument() {
        Document doc = Document.builder().id(1).user(user).fileName("report.pdf")
                .fileSize(1024).bucketUrl("https://bucket.s3.us-east-1.amazonaws.com/report.pdf").build();
        when(documentRepository.findById(1)).thenReturn(Optional.of(doc));

        assertThat(service.findById(1)).isEqualTo(doc);
    }

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(documentRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_uploadsToStorageAndSavesDocumentAndEmbedding() {
        String s3Url = "https://bucket.s3.us-east-1.amazonaws.com/uuid_report.pdf";
        Document saved = Document.builder().id(1).user(user).fileName("report.pdf")
                .fileSize(3).bucketUrl(s3Url).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(storagePort.upload("report.pdf", CONTENT, "application/pdf")).thenReturn(s3Url);
        when(documentRepository.save(any())).thenReturn(saved);
        when(embeddingPort.embed(any())).thenReturn(EMBEDDING);
        when(embeddingRepository.save(any())).thenReturn(
                DocumentEmbedding.builder().id(1).document(saved).embedding(EMBEDDING).build());

        Document result = service.create(1, "report.pdf", "Annual report", CONTENT, "application/pdf", 3);

        assertThat(result.getBucketUrl()).isEqualTo(s3Url);
        verify(storagePort).upload("report.pdf", CONTENT, "application/pdf");
        verify(embeddingPort).embed("report.pdf Annual report");
        verify(embeddingRepository).save(any(DocumentEmbedding.class));
    }

    @Test
    void create_withNullDescription_embedsFileNameOnly() {
        String s3Url = "https://bucket.s3.us-east-1.amazonaws.com/uuid_report.pdf";
        Document saved = Document.builder().id(1).user(user).fileName("report.pdf")
                .fileSize(3).bucketUrl(s3Url).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(storagePort.upload(any(), any(), any())).thenReturn(s3Url);
        when(documentRepository.save(any())).thenReturn(saved);
        when(embeddingPort.embed(any())).thenReturn(EMBEDDING);
        when(embeddingRepository.save(any())).thenReturn(
                DocumentEmbedding.builder().id(1).document(saved).embedding(EMBEDDING).build());

        service.create(1, "report.pdf", null, CONTENT, "application/pdf", 3);

        verify(embeddingPort).embed("report.pdf");
    }

    @Test
    void create_whenUserNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(99, "f.pdf", null, CONTENT, "application/pdf", 3))
                .isInstanceOf(ResourceNotFoundException.class);

        verifyNoInteractions(storagePort, embeddingPort, documentRepository, embeddingRepository);
    }

    @Test
    void create_whenStorageFails_throwsException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(storagePort.upload(any(), any(), any())).thenThrow(new RuntimeException("S3 error"));

        assertThatThrownBy(() -> service.create(1, "f.pdf", null, CONTENT, "application/pdf", 3))
                .hasMessage("S3 error");

        verifyNoInteractions(documentRepository, embeddingPort, embeddingRepository);
    }

    @Test
    void create_whenEmbeddingFails_throwsException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(storagePort.upload(any(), any(), any())).thenReturn("https://bucket.s3.us-east-1.amazonaws.com/key");
        when(documentRepository.save(any())).thenReturn(
                Document.builder().id(1).user(user).fileName("f.pdf").fileSize(3)
                        .bucketUrl("https://bucket.s3.us-east-1.amazonaws.com/key").build());
        when(embeddingPort.embed(any())).thenThrow(new RuntimeException("Ollama error"));

        assertThatThrownBy(() -> service.create(1, "f.pdf", null, CONTENT, "application/pdf", 3))
                .hasMessage("Ollama error");

        verifyNoInteractions(embeddingRepository);
    }

    @Test
    void delete_whenExists_deletesById() {
        when(documentRepository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(documentRepository).deleteById(1);
    }

    @Test
    void delete_whenNotFound_throwsResourceNotFoundException() {
        when(documentRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
