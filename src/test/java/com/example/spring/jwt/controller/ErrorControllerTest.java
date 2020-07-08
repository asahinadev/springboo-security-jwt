package com.example.spring.jwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.spring.jwt.authentication.exception.TokenNotfoundException;
import com.example.spring.jwt.dto.ResponceDto;
import com.example.spring.jwt.values.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ErrorControllerTest {

	@Autowired
	ErrorController controller;

	@Test
	public void testFefaultExceptionHandler() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("/start");
		TokenNotfoundException exception = new TokenNotfoundException();

		ResponceDto dto = controller.defaultExceptionHandler(request, exception);

		assertEquals(dto.getStatus(), Status.NG);

	}
}
