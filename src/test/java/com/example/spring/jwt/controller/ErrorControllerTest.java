package com.example.spring.jwt.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.spring.jwt.authentication.exception.HeaderTokenIllegalException;
import com.example.spring.jwt.authentication.exception.HeaderTokenNotfoundException;
import com.example.spring.jwt.dto.ResponceDto;
import com.example.spring.jwt.values.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ErrorControllerTest {

	@Autowired
	ErrorController controller;

	@Test
	public void testExceptionHandlerHttpServletRequestHeaderTokenNotfoundException() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("/start");
		HeaderTokenNotfoundException exception = new HeaderTokenNotfoundException();

		ResponceDto dto = controller.exceptionHandler(request, exception);

		assertThat(dto.getStatus(), is(Status.NG));

	}

	@Test
	public void testExceptionHandlerHttpServletRequestHeaderTokenIllegalException() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath("/start");
		HeaderTokenIllegalException exception = new HeaderTokenIllegalException();

		ResponceDto dto = controller.exceptionHandler(request, exception);

		assertThat(dto.getStatus(), is(Status.NG));

	}

}
