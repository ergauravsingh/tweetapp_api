package com.tweetapp.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


import java.util.Arrays;

@Aspect
@Component
public class GlobalLoggingAspect {

	private static final Log LOGGER = LogFactory.getLog(GlobalLoggingAspect.class);

	@Pointcut("execution(* com.tweetapp.service.*.*(..))")
	public void serviceMethods() {
		LOGGER.info("Test Logging TweetApp: service methods.");
	}

	@Pointcut("execution(* com.tweetapp.controller.*.*(..))")
	public void controllerMethods() {
		LOGGER.info("Test Logging: controllerMethods.");
	}

	@Around("serviceMethods()")
	public Object logServiceMethods(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		Object output = null;
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();

		LOGGER.info("Start execution of Service method : " + className + "." + methodName);

		output = pjp.proceed();
		long elapsedTime = System.currentTimeMillis() - start;
		LOGGER.info("End execution of Service method : " + className + "." + methodName
				+ " | Method execution time: " + elapsedTime + " milliseconds.");

		return output;
	}

	@Around("controllerMethods()")
	public Object logControllerMethods(ProceedingJoinPoint pjp) throws Throwable {

		long start = System.currentTimeMillis();
		Object output = null;
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();

		LOGGER.info("Start execution of Controller method : " + className + "." + methodName);

		output = pjp.proceed();
		long elapsedTime = System.currentTimeMillis() - start;
		LOGGER.info("End execution of Controller method : " + className + "." + methodName
				+ " | Method execution time: " + elapsedTime + " milliseconds.");

		return output;
	}

	@AfterThrowing(pointcut = "execution(* com.tweetapp.service.*.*(..)) || "
			+ "execution(* com.tweetapp.controller.*.*(..))", throwing = "ex")
	public void doRecoveryActions(JoinPoint joinPoint, Throwable ex) {
		ex.printStackTrace();
		org.aspectj.lang.Signature signature = joinPoint.getSignature();
		String ClassAndMethod = signature.toString();
		String arguments = Arrays.toString(joinPoint.getArgs());
		LOGGER.error("[Exception details] Error in : " + ClassAndMethod + " | With arguments " + arguments
				+ " | Exception is: " + ex.getMessage());
	}

}
