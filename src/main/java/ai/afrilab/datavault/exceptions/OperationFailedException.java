package ai.afrilab.datavault.exceptions;

public class OperationFailedException extends RuntimeException{
  public OperationFailedException(String message) {
    super(message);
  }
}
