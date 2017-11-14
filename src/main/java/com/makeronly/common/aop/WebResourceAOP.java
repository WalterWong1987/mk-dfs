package com.makeronly.common.aop;

import com.makeronly.common.bean.ResultBean;
import com.makeronly.common.exception.CheckException;
import com.makeronly.common.exception.UnloginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 处理和包装异常
 * @author Walter Wong
 */
@Component
@Aspect
public class WebResourceAOP {
    private static final Logger logger = LoggerFactory.getLogger(WebResourceAOP.class);

    @Pointcut("execution(public com.makeronly.common.bean.ResultBean *(..))")
    public void excudeService() {

    }

    @Around("excudeService()")
    public Object handle(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        ResultBean<?> result;
        try {
            result = (ResultBean<?>) pjp.proceed();
            logger.info("执行 {} 耗时 {} ms",pjp.getSignature(),(System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            result = handleException(pjp,e);
        }
        return result;
    }

    private ResultBean<?> handleException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean();
        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(ResultBean.FAIL);
        } else if (e instanceof UnloginException) {
            result.setMsg("Unlogin");
            result.setCode(ResultBean.NO_LOGIN);
        } else {
            logger.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg(e.toString());
            result.setCode(ResultBean.FAIL);
        }
        return result;
    }
}
