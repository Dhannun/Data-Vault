package ai.afrilab.datavault.exceptions;

public class UserExistsByUsername extends RuntimeException{
  public UserExistsByUsername(String message) {
    super(message);
  }
}
