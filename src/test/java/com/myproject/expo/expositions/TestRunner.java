package com.myproject.expo.expositions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunner {
    private static final String TEST = "test";

    @Test
    public void test() {
      assertThat(TEST).isEqualTo("test");
    }
}
