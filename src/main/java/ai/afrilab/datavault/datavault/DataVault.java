package ai.afrilab.datavault.datavault;

import ai.afrilab.datavault.datavault.enums.Status;
import ai.afrilab.datavault.datavault.metadata.MetaData;
import ai.afrilab.datavault.users.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "data_vault")
@EntityListeners(AuditingEntityListener.class)
public class DataVault {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private UUID id;

  @Column(name = "identifier", nullable = false, unique = true, length = 50)
  private String identifier;

  @Column(name = "title", nullable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  @Column(name = "audio_summary")
  private String audioSummary;

  @Column(name = "file_link")
  private String fileLink;

  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "meta_data_id", nullable = false, unique = true)
  private MetaData metaData;

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

  public DataVault(UUID id, String identifier, String title, Status status, String audioSummary, String fileLink, MetaData metaData, User createdBy, User lastModifiedBy, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
    this.id = id;
    this.identifier = identifier;
    this.title = title;
    this.status = status;
    this.audioSummary = audioSummary;
    this.fileLink = fileLink;
    this.metaData = metaData;
    this.createdBy = createdBy;
    this.lastModifiedBy = lastModifiedBy;
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
  }

  public DataVault() {
  }

  public static DataVaultBuilder builder() {
    return new DataVaultBuilder();
  }

  public UUID getId() {
    return this.id;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getTitle() {
    return this.title;
  }

  public Status getStatus() {
    return this.status;
  }

  public String getAudioSummary() {
    return this.audioSummary;
  }

  public String getFileLink() {
    return this.fileLink;
  }

  public MetaData getMetaData() {
    return this.metaData;
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

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setAudioSummary(String audioSummary) {
    this.audioSummary = audioSummary;
  }

  public void setFileLink(String fileLink) {
    this.fileLink = fileLink;
  }

  public void setMetaData(MetaData metaData) {
    this.metaData = metaData;
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

  public static class DataVaultBuilder {
    private UUID id;
    private String identifier;
    private String title;
    private Status status;
    private String audioSummary;
    private String fileLink;
    private MetaData metaData;
    private User createdBy;
    private User lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    DataVaultBuilder() {
    }

    public DataVaultBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public DataVaultBuilder identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public DataVaultBuilder title(String title) {
      this.title = title;
      return this;
    }

    public DataVaultBuilder status(Status status) {
      this.status = status;
      return this;
    }

    public DataVaultBuilder audioSummary(String audioSummary) {
      this.audioSummary = audioSummary;
      return this;
    }

    public DataVaultBuilder fileLink(String fileLink) {
      this.fileLink = fileLink;
      return this;
    }

    public DataVaultBuilder metaData(MetaData metaData) {
      this.metaData = metaData;
      return this;
    }

    public DataVaultBuilder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public DataVaultBuilder lastModifiedBy(User lastModifiedBy) {
      this.lastModifiedBy = lastModifiedBy;
      return this;
    }

    public DataVaultBuilder createdDate(LocalDateTime createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public DataVaultBuilder lastModifiedDate(LocalDateTime lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
      return this;
    }

    public DataVault build() {
      return new DataVault(this.id, this.identifier, this.title, this.status, this.audioSummary, this.fileLink, this.metaData, this.createdBy, this.lastModifiedBy, this.createdDate, this.lastModifiedDate);
    }

    public String toString() {
      return "DataVault.DataVaultBuilder(id=" + this.id + ", identifier=" + this.identifier + ", title=" + this.title + ", status=" + this.status + ", audioSummary=" + this.audioSummary + ", fileLink=" + this.fileLink + ", metaData=" + this.metaData + ", createdBy=" + this.createdBy + ", lastModifiedBy=" + this.lastModifiedBy + ", createdDate=" + this.createdDate + ", lastModifiedDate=" + this.lastModifiedDate + ")";
    }
  }
}
