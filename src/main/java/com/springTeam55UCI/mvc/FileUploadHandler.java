package com.springTeam55UCI.mvc;

//import ShuaiZheng.Machine.MachineLearning;


import ShuaiZheng.Machine.d;
import com.springTeam55UCI.mvc.com.util.ConnectionConfig;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Shuai Zheng on 11/23/16.
 */
public class FileUploadHandler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = response.getWriter();

        request.getSession().setMaxInactiveInterval(1440);
        //process only if its multipart content
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);
                response.setContentType("text/html");

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        item.write(new File(request.getSession().getServletContext().getRealPath("") + File.separator + new File(item.getName()).getName()));
                        ProOut Profile = new ProOut();
                        Profile.ProFileOutApplication(request.getSession().getServletContext().getRealPath(""), new File(item.getName()).getName());
//                        String[] args = new String[2];
//                        args[0] = "@" + request.getSession().getServletContext().getRealPath("") + File.separator + "proguard.pro";
//                        Main Obfusacate = new Main();
//                        Obfusacate.obfuscation(args);
//                        request.setAttribute("download", request.getSession().getServletContext().getRealPath("") + File.separator + "obfuscation_out.jar");

////
                        String[] input = new String[3];
                        input[0] = request.getSession().getServletContext().getRealPath("") + File.separator + new File(item.getName()).getName();
                        input[1] = request.getSession().getServletContext().getRealPath("");
                        input[2] = request.getSession().getServletContext().getRealPath("") + File.separator + "resources" + File.separator + "ShuaiZhengTrained.zip";

                        System.out.println(input[0]);

                        (new d()).a(input);
//                        new MachineLearning().main(input);
                        System.out.println("sssss");
                        request.setAttribute("download", request.getSession().getServletContext().getRealPath("") + File.separator + "output.txt");

                        Connection connection = null;
                        try {

                            connection = ConnectionConfig.getConnection();
                            if(connection != null) {
                                System.out.println("Connection established.");
                            }
                            else {
                                System.out.println("Connection failed.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if(connection != null) {
                                try {
                                    connection.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

//                        request.setAttribute("download", request.getSession().getServletContext().getRealPath("") + File.separator + "output.txt");

                    }
                }

                //File uploaded successfully
                request.setAttribute("message", "File Uploaded and Obfuscation was successful");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            request.setAttribute("message", "Sorry this Servlet only handles file upload request");
        }


        request.getRequestDispatcher("/WEB-INF/pages/result.jsp").forward(request, response);

    }

}