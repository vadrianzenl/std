<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>
<% ResourceBundle resource = ResourceBundle.getBundle("application");
  String congresoDigitalUrl=resource.getString("sgi.url");
  response.sendRedirect(congresoDigitalUrl);%>