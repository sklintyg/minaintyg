<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Application Version</title>
  <link rel="stylesheet" type="text/css" href="css/bootstrap/2.3.2/bootstrap.min.css">
</head>
<body>
<div style="padding-left:20px">
  <div class="page-header">
    <h3>Mina Intyg</h3>
  </div>
  <div class="alert alert-block alert-info" style="width:50%">
    <h4 style="padding-bottom:5px;">Configuration info</h4>

    <div>Application version: <span class="label label-info"><spring:message code="project.version"/></span></div>
    <div>Build number: <span class="label label-info"><spring:message code="buildNumber"/></span></div>
    <div>Build time: <span class="label label-info"><spring:message code="buildTime"/></span></div>
    <div>Spring profiles: <span class="label label-info"><%= System.getProperty("spring.profiles.active") %></span></div>
  </div>
</div>
</body>
</html>