package com.tencet.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class ReqLimitAspet {

    private final static Map<String, Integer> cacheMap = new ConcurrentHashMap<>();

    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(reqLimit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit reqLimit) throws RuntimeException {
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (request == null) {
                throw new RuntimeException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURL().toString();
            String key = ip.concat("_req_").concat(url);
            if (cacheMap.get(key) == null || cacheMap.get(key) == 0) {
                cacheMap.put(key, 1);
            } else {
                cacheMap.put(key, cacheMap.get(key) + 1);
            }
            int count = cacheMap.get(key);
            log.info("用户IP[" + ip + "]访问地址[" + url + "]第[" + count + "]请求");
            if (count == 1) {
                //创建一个定时器
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        cacheMap.remove(key);
                    }
                };
                //这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
                timer.schedule(timerTask , reqLimit.time());
            }
            if (count > reqLimit.count()) {
                log.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + reqLimit.count() + "]");
                throw new RuntimeException("请求过于频繁，请稍后再试！");
            }
        }catch (RuntimeException e){
            throw e;
        }catch (Exception e){
            log.error("发生异常", e);
        }
    }
}