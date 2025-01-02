package ai.afrilab.datavault.universal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class PagedApiResponse<T> {
  private int statusCode;
  private String status;
  private String message;
  private List<T> data;
  private int pageNumber;
  private int totalPages;
  private long totalData;
  @JsonProperty("isLastPage")
  private boolean isLastPage;

  public PagedApiResponse(int statusCode, String status, String message, List<T> data, int pageNumber, int totalPages, long totalData, boolean isLastPage) {
    this.statusCode = statusCode;
    this.status = status;
    this.message = message;
    this.data = data;
    this.pageNumber = pageNumber;
    this.totalPages = totalPages;
    this.totalData = totalData;
    this.isLastPage = isLastPage;
  }

  public PagedApiResponse() {
  }

  public static <T> PagedApiResponseBuilder<T> builder() {
    return new PagedApiResponseBuilder<T>();
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getStatus() {
    return this.status;
  }

  public String getMessage() {
    return this.message;
  }

  public List<T> getData() {
    return this.data;
  }

  public int getPageNumber() {
    return this.pageNumber;
  }

  public int getTotalPages() {
    return this.totalPages;
  }

  public long getTotalData() {
    return this.totalData;
  }

  public boolean isLastPage() {
    return this.isLastPage;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public void setTotalData(long totalData) {
    this.totalData = totalData;
  }

  @JsonProperty("isLastPage")
  public void setLastPage(boolean isLastPage) {
    this.isLastPage = isLastPage;
  }

  public static class PagedApiResponseBuilder<T> {
    private int statusCode;
    private String status;
    private String message;
    private List<T> data;
    private int pageNumber;
    private int totalPages;
    private long totalData;
    private boolean isLastPage;

    PagedApiResponseBuilder() {
    }

    public PagedApiResponseBuilder<T> statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public PagedApiResponseBuilder<T> status(String status) {
      this.status = status;
      return this;
    }

    public PagedApiResponseBuilder<T> message(String message) {
      this.message = message;
      return this;
    }

    public PagedApiResponseBuilder<T> data(List<T> data) {
      this.data = data;
      return this;
    }

    public PagedApiResponseBuilder<T> pageNumber(int pageNumber) {
      this.pageNumber = pageNumber;
      return this;
    }

    public PagedApiResponseBuilder<T> totalPages(int totalPages) {
      this.totalPages = totalPages;
      return this;
    }

    public PagedApiResponseBuilder<T> totalData(long totalData) {
      this.totalData = totalData;
      return this;
    }

    @JsonProperty("isLastPage")
    public PagedApiResponseBuilder<T> isLastPage(boolean isLastPage) {
      this.isLastPage = isLastPage;
      return this;
    }

    public PagedApiResponse<T> build() {
      return new PagedApiResponse<T>(this.statusCode, this.status, this.message, this.data, this.pageNumber, this.totalPages, this.totalData, this.isLastPage);
    }

    public String toString() {
      return "PagedApiResponse.PagedApiResponseBuilder(statusCode=" + this.statusCode + ", status=" + this.status + ", message=" + this.message + ", data=" + this.data + ", pageNumber=" + this.pageNumber + ", totalPages=" + this.totalPages + ", totalData=" + this.totalData + ", isLastPage=" + this.isLastPage + ")";
    }
  }
}
