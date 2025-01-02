package ai.afrilab.datavault.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class PaginationUtils {
  public static Pageable getPageable(int page, int size) {
    page = page == 0 ? 0 : page - 1;
    return PageRequest.of(page, size);
  }
}
