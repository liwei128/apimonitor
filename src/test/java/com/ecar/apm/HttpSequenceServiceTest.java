package com.ecar.apm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ecar.apm.model.HttpSystem;
import com.ecar.apm.service.HttpSequenceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpSequenceServiceTest {

	@Autowired
	private HttpSequenceService httpSequenceService;
	
	
	@Test
	public void test() throws InterruptedException{
		httpSequenceService.addHttpSystem("jextion1");
	}
		
}
