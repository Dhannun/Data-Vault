package ai.afrilab.datavault.universal;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class ApiResponse<T> {
  private int statusCode;
  private String status;
  private String message;
  private T data;

  ApiResponse(int statusCode, String status, String message, T data) {
    this.statusCode = statusCode;
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponseBuilder<T> builder() {
    return new ApiResponseBuilder<T>();
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

  public T getData() {
    return this.data;
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

  public void setData(T data) {
    this.data = data;
  }

  public static class ApiResponseBuilder<T> {
    private int statusCode;
    private String status;
    private String message;
    private T data;

    ApiResponseBuilder() {
    }

    public ApiResponseBuilder<T> statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public ApiResponseBuilder<T> status(String status) {
      this.status = status;
      return this;
    }

    public ApiResponseBuilder<T> message(String message) {
      this.message = message;
      return this;
    }

    public ApiResponseBuilder<T> data(T data) {
      this.data = data;
      return this;
    }

    public ApiResponse<T> build() {
      return new ApiResponse<T>(this.statusCode, this.status, this.message, this.data);
    }

    public String toString() {
      return "ApiResponse.ApiResponseBuilder(statusCode=" + this.statusCode + ", status=" + this.status + ", message=" + this.message + ", data=" + this.data + ")";
    }
  }
}
