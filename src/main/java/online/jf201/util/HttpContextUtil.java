package online.jf203.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtil {
    public  static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static  String getDomin(){
        HttpServletRequest request =getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length()-request.getRequestURL().length(),url.length()).toString();
    }

    public static String getOrigin(){
        HttpServletRequest request =getHttpServletRequest();
        return request.getHeader("Origin");
    }
}
