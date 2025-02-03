package com.cts.timer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* com.cts.timer.service.TimesheetsServiceImpl.*(..))")
	public void serviceMethods() {
	}

	@Before("serviceMethods()")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("A method in TimesheetsService is about to be executed. Method:{} , Arguments: {}",
				joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	@AfterReturning(pointcut = "serviceMethods()", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("A method in TimesheetsService has executed successfully. Method: {} , Result: {} ",
				joinPoint.getSignature().getName(), result);
	}

	@AfterThrowing(pointcut = "serviceMethods()", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		logger.error("An error occurred in TimesheetssService. Method: {} ,Error: {} ",
				joinPoint.getSignature().getName(), error.getMessage());
	}
}