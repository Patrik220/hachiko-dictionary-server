package com.m2t.hachikodictionary.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.boot.context.properties.bind.DefaultValue

@Entity
@Table(name = "words", schema = "public")
data class Word(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator") // Generate UUID
    var id: String?,
    var title: String,
    var kind: String,

    var difficulty: String = "medium",

    @ElementCollection
    @CollectionTable(name = "word_descriptions", schema = "public")
    var descriptions: Set<String>,
    @ElementCollection
    @CollectionTable(name = "word_synonyms", schema = "public")
    var synonyms: Set<String>,
    @ElementCollection
    @CollectionTable(name = "word_antonyms", schema = "public")
    var antonyms: Set<String>,
    @ElementCollection
    @CollectionTable(name = "word_sentences", schema = "public")
    var sentences: Set<String>,

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val learnedWords: Set<LearnedWord>
) {
    constructor(
        title: String,
        kind: String,
        descriptions: MutableSet<String>,
        synonyms: MutableSet<String>,
        antonyms: MutableSet<String>,
        sentences: MutableSet<String>,
    ) : this(
        null,
        title,
        kind,
        "medium",
        descriptions,
        synonyms,
        antonyms,
        sentences,
        emptySet()
    )

    constructor(
        title: String,
        kind: String,
        difficulty: String,
        descriptions: MutableSet<String>,
        synonyms: MutableSet<String>,
        antonyms: MutableSet<String>,
        sentences: MutableSet<String>,
    ) : this(
        null,
        title,
        kind,
        difficulty,
        descriptions,
        synonyms,
        antonyms,
        sentences,
        emptySet()
    )

    constructor(
        id: String,
        title: String,
        kind: String,
        difficulty: String,
        descriptions: MutableSet<String>,
        synonyms: MutableSet<String>,
        antonyms: MutableSet<String>,
        sentences: MutableSet<String>
    ) : this(
        id,
        title,
        kind,
        difficulty,
        descriptions,
        synonyms,
        antonyms,
        sentences,
        emptySet()
    )


}