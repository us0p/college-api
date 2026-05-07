package com.college.api.domain.document;

import com.college.api.infrastructure.persistence.document.VectorConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class DocumentEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "chunk_text", nullable = false, columnDefinition = "TEXT")
    private String chunkText;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @Convert(converter = VectorConverter.class)
    @Column(name = "embedding", nullable = false, columnDefinition = "vector(768)")
    private float[] embedding;
}
