package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author think
 */
@WebServlet(name = "UpdFile", urlPatterns = "/UpdFile")
public class UpdFile extends BaseServlet {

    public void getParam(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        //上传文件
        //一般都会保存到项目里面去
        String path = this.getServletContext().getRealPath("/WEB-INF/upload");
        // 相对路径
        // 判断上传的路径目录是否存在
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            // 如果目录不存在，则创建 //是否是文件夹
            System.out.println("创建路径");
            file.mkdir();
        }
        // 1.创建一个处理文件上传的工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 2.创建一个文件解析工具
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 解决中文乱码
        // 设置头部信息
        upload.setHeaderEncoding("UTF-8");
        // 3.判断提交上来的数据是否是长传的表单数据
        // 通过jar判断请求的request对象 是否为上传表单
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果是普通上传，则在这里处理
            return;// 如果不是，则直接return
        }
        // 4.使用文件工具对象 解析表单 使用工具解析上传的所有数据
        Map<String, String> map = new HashMap<String, String>();
        try {
            List<FileItem> list = upload.parseRequest(request);
            // 把整个表单里面的数据解析 然后封装成FileItem
            // 然后保存到list
            // 把整个表单里面的数据解析 然后封装成一个集合
            // 遍历整个集合

            for (FileItem item : list) {
                // 区分是普通输入框 还是文件框
                // 普通
                if (item.isFormField()) {

                    map.put(item.getFieldName(), item.getString("UTF-8"));

                } else {
                    // 得到文件名
                    String fileName = item.getName();
                    if (fileName != "") {
                        InputStream input = item.getInputStream();
                        FileOutputStream output = new FileOutputStream(path + "\\" + fileName);
                        String getPath = path + "\\" + fileName;
                        //在这里获取到Excel表格的路径
                        byte[] bytes = new byte[1024];
                        int i = 0;
                        while ((i = input.read(bytes)) > 0) {
                            output.write(bytes, 0, i);
                        }
                        input.close();
                        output.close();

                        show(getPath);
                    }

                }
            }
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show(String path){

        System.out.println(path);
        String excelFileName =path;
        try {
            List<String[]> list = readExcel(new File(excelFileName),1);
            for (int i = 0; i < list.size(); i++) {
                String[] str = (String[])list.get(i);
                for (int j = 0; j < str.length; j++) {
                    System.out.println(str[j]);
                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }


    public static List<String[]> readExcel(File excelFile, int rowNum) throws BiffException,
            IOException {
// 创建一个list 用来存储读取的内容
        List<String[]> list = new ArrayList<String[]>();
        Workbook rwb = null;
        Cell cell = null;
// 创建输入流
        InputStream stream = new FileInputStream(excelFile);
// 获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
// 获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
// 行数(表头的目录不需要，从1开始)
        for (int i = rowNum - 1; i < sheet.getRows(); i++) {
// 创建一个数组 用来存储每一列的值
            String[] str = new String[sheet.getColumns()];
// 列数
            for (int j = 0; j < sheet.getColumns(); j++) {
// 获取第i行，第j列的值
                cell = sheet.getCell(j, i);
                str[j] = cell.getContents();
            }
// 把刚获取的列存入list
            list.add(str);
        }
// 返回值集合
        return list;
    }
}
