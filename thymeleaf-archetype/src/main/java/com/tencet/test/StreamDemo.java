package com.tencet.test;

import java.io.*;

/**
 * Created by pijiang on 2019/6/11.
 */
public class StreamDemo {

    static String srcPath = "C:\\Users\\v_pijiang\\Desktop\\aa.png";
    static String desPath = "D:\\copy.jpg";

    public static void fileToCopy(String srcPath, String desPath){

        try {
            //file转byte[]
            InputStream is = new FileInputStream(new File(srcPath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int length;
            byte[] b = new byte[is.available()];
            while ((length=is.read(b)) != -1){
                bos.write(b, 0, length);
            }
            byte[] bytes = bos.toByteArray();

            //byte[]转file
            OutputStream os = new FileOutputStream(new File(desPath));
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            int l;
            byte[] bo = new byte[1024];
            while ((l=bis.read(bo)) != -1){
                os.write(bo, 0, l);
            }
            os.flush();

            //关闭资源
            os.close();
            bis.close();
            bos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StreamDemo.fileToCopy(srcPath, desPath);
    }


}