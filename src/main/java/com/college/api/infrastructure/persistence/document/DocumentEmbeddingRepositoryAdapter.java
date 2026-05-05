package com.college.api.infrastructure.persistence.document;

import com.college.api.domain.document.DocumentEmbedding;
import com.college.api.domain.document.DocumentEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DocumentEmbeddingRepositoryAdapter implements DocumentEmbeddingRepository {

    private final JpaDocumentEmbeddingRepository jpa;

    @Override
    public DocumentEmbedding save(DocumentEmbedding embedding) {
        return jpa.save(embedding);
    }
}
