package com.example.spring.jwt.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HttpServletWrapperFilterTest {

	HttpServletWrapperFilter filter;

	@BeforeEach
	public void setUp() {
		filter = new HttpServletWrapperFilter();
	}

	@Test
	public void testDoFilter() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		filter.doFilter(request, response, chain);
	}

	@Test
	public void testGetOrder() {
		assertEquals(filter.getOrder(), 0);
	}

}
