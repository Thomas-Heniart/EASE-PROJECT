package com.Ease.Utils;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
 
// Implements Filter class
public class IFrameBusterFilter implements Filter  {
   public void  init(FilterConfig config) 
                         throws ServletException{
      // Get init parameter 
   }
   public void  doFilter(ServletRequest req, 
                 ServletResponse resp,
                 FilterChain chain) 
                 throws java.io.IOException, ServletException {
 
	   HttpServletResponse response = (HttpServletResponse) resp;
	    response.addHeader("X-Frame-Options", "DENY");    
	    chain.doFilter(req, resp);
    }
   public void destroy( ){
      /* Called before the Filter instance is removed 
      from service by the web container*/
   }
}