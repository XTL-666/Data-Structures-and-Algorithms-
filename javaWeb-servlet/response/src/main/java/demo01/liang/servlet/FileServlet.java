package demo01.liang.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Encoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import static java.io.FileDescriptor.out;
import static java.net.URLEncoder.encode;


public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String realPath = "/home/stl/Downloads/study-spring-master/javaWeb-servlet/response/src/main/resources/1.jpg";
        System.out.println("download file path :" + realPath);
        String fileName = realPath.substring(realPath.lastIndexOf("\\") + 1);
        resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
        FileInputStream in = new FileInputStream(realPath);
        int len = 0;
        byte[] buffer = new byte[1024];
        ServletOutputStream out = resp.getOutputStream();

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer,0,len);
        }
        in.close();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
