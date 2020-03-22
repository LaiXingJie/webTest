<%--
  Created by IntelliJ IDEA.
  User: Think
  Date: 2020/3/4
  Time: 8:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>上传Excel表格测试</title>
  <script src="js/jquery-1.12.4.js"></script>
  </head>
  <body>
  <h3>测试</h3>
  <form action="UpdFile?opr=getParam" enctype="multipart/form-data"
        method="post">
      上传图片:<input type="file" value="选择文件" name="fName" id="userface" ><br>
      <input type="submit" value="提交" id="button">
  </form>
  </body>
</html>
