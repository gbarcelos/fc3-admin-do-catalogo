package com.fullcycle.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {

  @Test
  public void shouldCreateMain() {
    Assertions.assertNotNull(new Main());
    Main.main(new String[]{});
  }
}
