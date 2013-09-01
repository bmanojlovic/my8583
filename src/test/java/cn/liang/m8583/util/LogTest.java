package cn.liang.m8583.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.stream.MultiThreadTest;

public class LogTest {
	private static final Logger logger = LoggerFactory.getLogger(LogTest.class);
	
	@Test
	public void test() {
		logger.debug("test debug");
		logger.info("test info");
		logger.warn("test warn");
		logger.error("test error");
	}

}
