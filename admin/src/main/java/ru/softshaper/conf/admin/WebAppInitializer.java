package ru.softshaper.conf.admin;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * /** This class loaded and inicilized WebApplication<br/>
 *
 * @author ashek
 *
 */
public class WebAppInitializer implements WebApplicationInitializer {
  /*
   * (non-Javadoc)
   *
   * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
   */
  @Override
  public void onStartup(ServletContext container) {
    // CharacterEncodingFilter
    FilterRegistration.Dynamic characterEncodingFilter = container.addFilter("characterEncodingFilter", "org.springframework.web.filter.CharacterEncodingFilter");
    characterEncodingFilter.setInitParameter("encoding", "UTF-8");
    characterEncodingFilter.setInitParameter("forceEncoding", "true");
    characterEncodingFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*", "*.htm", "*.json");

    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.scan("ru.softshaper.conf");
    // rootContext.register(WebConfig.class);
    // Manage the lifecycle of the root application context
    // rootContext.register(WebSecurityConfig.class);
    container.addListener(new ContextLoaderListener(rootContext));
    ServletRegistration.Dynamic dispatcher = container.addServlet("CXFServlet", new org.apache.cxf.transport.servlet.CXFServlet());
    dispatcher.setLoadOnStartup(1);
    // todo
    // dispatcher.setAsyncSupported(true);
    dispatcher.addMapping("/rest/*");

    //dispatcher = container.addServlet("WebSocket", new ru.softshaper.web.msg.websocket.WebSocket());
    //dispatcher.setLoadOnStartup(2);
    //dispatcher.addMapping("/ws/msg");

  }
}
