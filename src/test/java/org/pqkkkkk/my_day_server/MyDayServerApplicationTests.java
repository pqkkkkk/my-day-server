package org.pqkkkkk.my_day_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestSecurityConfig.class) // Import security configuration for tests
class MyDayServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
