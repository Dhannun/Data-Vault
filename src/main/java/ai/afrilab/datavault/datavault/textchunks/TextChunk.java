package ai.afrilab.datavault.datavault.textchunks;

import ai.afrilab.datavault.datavault.DataVault;
import ai.afrilab.datavault.users.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for storing text chunks of a book
 */

@Entity
@Table(
    name = "text_chunks",
    indexes = @Index(name = "idx_data_vault_id", columnList = "data_vault_id")
)
@EntityListeners(AuditingEntityListener.class)
public class TextChunk {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "transcriber", nullable = false)
  private String transcriber;

  @Column(name = "audio_link")
  private String audioLink;

  @Column(name = "recorder")
  private String recorder;

  @Column(name = "translation_link")
  private String translationLink;

  @Column(name = "translator")
  private String translator;

  @ManyToOne(optional = false)
  @JoinColumn(name = "data_vault_id", nullable = false)
  private DataVault dataVault;


  /// Auditing Fields
  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false, updatable = false)
  @CreatedBy
  private User createdBy;

  @ManyToOne
  @JoinColumn(name = "last_modified_by", insertable = false)
  @LastModifiedBy
  private User lastModifiedBy;

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "last_modified_date", insertable = false)
  private LocalDateTime lastModifiedDate;

  public TextChunk(UUID id, String title, String content, String transcriber, String audioLink, String recorder, String translationLink, String translator, DataVault dataVault, User createdBy, User lastModifiedBy, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.transcriber = transcriber;
    this.audioLink = audioLink;
    this.recorder = recorder;
    this.translationLink = translationLink;
    this.translator = translator;
    this.dataVault = dataVault;
    this.createdBy = createdBy;
    this.lastModifiedBy = lastModifiedBy;
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
  }

  public TextChunk() {
  }

  public static TextChunkBuilder builder() {
    return new TextChunkBuilder();
  }

  public UUID getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public String getContent() {
    return this.content;
  }

  public String getTranscriber() {
    return this.transcriber;
  }

  public String getAudioLink() {
    return this.audioLink;
  }

  public String getRecorder() {
    return this.recorder;
  }

  public String getTranslationLink() {
    return this.translationLink;
  }

  public String getTranslator() {
    return this.translator;
  }

  public DataVault getDataVault() {
    return this.dataVault;
  }

  public User getCreatedBy() {
    return this.createdBy;
  }

  public User getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  public LocalDateTime getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setTranscriber(String transcriber) {
    this.transcriber = transcriber;
  }

  public void setAudioLink(String audioLink) {
    this.audioLink = audioLink;
  }

  public void setRecorder(String recorder) {
    this.recorder = recorder;
  }

  public void setTranslationLink(String translationLink) {
    this.translationLink = translationLink;
  }

  public void setTranslator(String translator) {
    this.translator = translator;
  }

  public void setDataVault(DataVault dataVault) {
    this.dataVault = dataVault;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastModifiedBy(User lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public static class TextChunkBuilder {
    private UUID id;
    private String title;
    private String content;
    private String transcriber;
    private String audioLink;
    private String recorder;
    private String translationLink;
    private String translator;
    private DataVault dataVault;
    private User createdBy;
    private User lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    TextChunkBuilder() {
    }

    public TextChunkBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public TextChunkBuilder title(String title) {
      this.title = title;
      return this;
    }

    public TextChunkBuilder content(String content) {
      this.content = content;
      return this;
    }

    public TextChunkBuilder transcriber(String transcriber) {
      this.transcriber = transcriber;
      return this;
    }

    public TextChunkBuilder audioLink(String audioLink) {
      this.audioLink = audioLink;
      return this;
    }

    public TextChunkBuilder recorder(String recorder) {
      this.recorder = recorder;
      return this;
    }

    public TextChunkBuilder translationLink(String translationLink) {
      this.translationLink = translationLink;
      return this;
    }

    public TextChunkBuilder translator(String translator) {
      this.translator = translator;
      return this;
    }

    public TextChunkBuilder dataVault(DataVault dataVault) {
      this.dataVault = dataVault;
      return this;
    }

    public TextChunkBuilder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public TextChunkBuilder lastModifiedBy(User lastModifiedBy) {
      this.lastModifiedBy = lastModifiedBy;
      return this;
    }

    public TextChunkBuilder createdDate(LocalDateTime createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public TextChunkBuilder lastModifiedDate(LocalDateTime lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
      return this;
    }

    public TextChunk build() {
      return new TextChunk(this.id, this.title, this.content, this.transcriber, this.audioLink, this.recorder, this.translationLink, this.translator, this.dataVault, this.createdBy, this.lastModifiedBy, this.createdDate, this.lastModifiedDate);
    }

    public String toString() {
      return "TextChunk.TextChunkBuilder(id=" + this.id + ", title=" + this.title + ", content=" + this.content + ", transcriber=" + this.transcriber + ", audioLink=" + this.audioLink + ", recorder=" + this.recorder + ", translationLink=" + this.translationLink + ", translator=" + this.translator + ", dataVault=" + this.dataVault + ", createdBy=" + this.createdBy + ", lastModifiedBy=" + this.lastModifiedBy + ", createdDate=" + this.createdDate + ", lastModifiedDate=" + this.lastModifiedDate + ")";
    }
  }
}
