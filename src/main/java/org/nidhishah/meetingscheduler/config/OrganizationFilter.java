package org.nidhishah.meetingscheduler.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

public class OrganizationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Reuqest id:"+ request.getRequestId());
        HttpServletRequest httpreq = (HttpServletRequest) request;
        System.out.println("http REquest:" + httpreq.getRequestURI() + "Method: "+httpreq.getMethod());
        if ("POST".equalsIgnoreCase(httpreq.getMethod()) && "/login".equals(httpreq.getRequestURI())) {
            System.out.println("Inside the post filter");
            System.out.println(request.getParameter("organization"));
            // Extract the organization from the request, for example from a form field
            String organization = request.getParameter("organization");

            // Add the organization as an attribute in the request
            request.setAttribute("organization", organization);
        }

        filterChain.doFilter(request,response);

    }
}

