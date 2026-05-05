package com.college.api.application.document;

import com.college.api.application.exception.ResourceNotFoundException;
import com.college.api.domain.document.*;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentEmbeddingRepository embeddingRepository;
    private final UserRepository userRepository;
    private final DocumentStoragePort storagePort;
    private final DocumentTextExtractor textExtractor;
    private final EmbeddingPort embeddingPort;

    @Transactional(readOnly = true)
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Document findById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", id));
    }

    @Transactional
    public Document create(Integer userId, String fileName, String description,
                           byte[] content, String contentType, Integer fileSize, boolean knowledgeBase) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String bucketUrl = storagePort.upload(fileName, content, contentType);

        Document document = documentRepository.save(Document.builder()
                .user(user)
                .fileName(fileName)
                .description(description)
                .fileSize(fileSize)
                .bucketUrl(bucketUrl)
                .knowledgeBase(knowledgeBase)
                .build());

        if (knowledgeBase) {
            String textContent = textExtractor.extract(content, contentType);
            float[] embedding = embeddingPort.embed(textContent);
            embeddingRepository.save(DocumentEmbedding.builder()
                    .document(document)
                    .embedding(embedding)
                    .build());
        }

        return document;
    }

    @Transactional
    public void delete(Integer id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document", id);
        }
        documentRepository.deleteById(id);
    }
}
