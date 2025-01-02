package ai.afrilab.datavault.datavault.metadata;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "metadata")
public class MetaData {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private UUID id;

  @Column(name = "source", nullable = false)
  private String source;

  @Column(name = "location", nullable = false)
  private String location;

  @Column(name = "category")
  private String category;

  @Column(name = "edition")
  private String edition;

  @Column(name = "year", nullable = false)
  private String year;

  @Column(name = "author", nullable = false)
  private String author;

  public MetaData(UUID id, String source, String location, String category, String edition, String year, String author) {
    this.id = id;
    this.source = source;
    this.location = location;
    this.category = category;
    this.edition = edition;
    this.year = year;
    this.author = author;
  }

  public MetaData() {
  }

  public static MetaDataBuilder builder() {
    return new MetaDataBuilder();
  }

  public UUID getId() {
    return this.id;
  }

  public String getSource() {
    return this.source;
  }

  public String getLocation() {
    return this.location;
  }

  public String getCategory() {
    return this.category;
  }

  public String getEdition() {
    return this.edition;
  }

  public String getYear() {
    return this.year;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public static class MetaDataBuilder {
    private UUID id;
    private String source;
    private String location;
    private String category;
    private String edition;
    private String year;
    private String author;

    MetaDataBuilder() {
    }

    public MetaDataBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public MetaDataBuilder source(String source) {
      this.source = source;
      return this;
    }

    public MetaDataBuilder location(String location) {
      this.location = location;
      return this;
    }

    public MetaDataBuilder category(String category) {
      this.category = category;
      return this;
    }

    public MetaDataBuilder edition(String edition) {
      this.edition = edition;
      return this;
    }

    public MetaDataBuilder year(String year) {
      this.year = year;
      return this;
    }

    public MetaDataBuilder author(String author) {
      this.author = author;
      return this;
    }

    public MetaData build() {
      return new MetaData(this.id, this.source, this.location, this.category, this.edition, this.year, this.author);
    }

    public String toString() {
      return "MetaData.MetaDataBuilder(id=" + this.id + ", source=" + this.source + ", location=" + this.location + ", category=" + this.category + ", edition=" + this.edition + ", year=" + this.year + ", author=" + this.author + ")";
    }
  }
}
