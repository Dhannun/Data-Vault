package ai.afrilab.datavault.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
  private Integer errorCode;
  private String errorDescription;
  private String error;
  private Set<String> validationErrors;
  private Map<String, String> errors;

  public ExceptionResponse(Integer errorCode, String errorDescription, String error, Set<String> validationErrors, Map<String, String> errors) {
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
    this.error = error;
    this.validationErrors = validationErrors;
    this.errors = errors;
  }

  public ExceptionResponse() {
  }

  public static ExceptionResponseBuilder builder() {
    return new ExceptionResponseBuilder();
  }

  public Integer getErrorCode() {
    return this.errorCode;
  }

  public String getErrorDescription() {
    return this.errorDescription;
  }

  public String getError() {
    return this.error;
  }

  public Set<String> getValidationErrors() {
    return this.validationErrors;
  }

  public Map<String, String> getErrors() {
    return this.errors;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setValidationErrors(Set<String> validationErrors) {
    this.validationErrors = validationErrors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }

  public static class ExceptionResponseBuilder {
    private Integer errorCode;
    private String errorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    ExceptionResponseBuilder() {
    }

    public ExceptionResponseBuilder errorCode(Integer errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public ExceptionResponseBuilder errorDescription(String errorDescription) {
      this.errorDescription = errorDescription;
      return this;
    }

    public ExceptionResponseBuilder error(String error) {
      this.error = error;
      return this;
    }

    public ExceptionResponseBuilder validationErrors(Set<String> validationErrors) {
      this.validationErrors = validationErrors;
      return this;
    }

    public ExceptionResponseBuilder errors(Map<String, String> errors) {
      this.errors = errors;
      return this;
    }

    public ExceptionResponse build() {
      return new ExceptionResponse(this.errorCode, this.errorDescription, this.error, this.validationErrors, this.errors);
    }

    public String toString() {
      return "ExceptionResponse.ExceptionResponseBuilder(errorCode=" + this.errorCode + ", errorDescription=" + this.errorDescription + ", error=" + this.error + ", validationErrors=" + this.validationErrors + ", errors=" + this.errors + ")";
    }
  }
}
