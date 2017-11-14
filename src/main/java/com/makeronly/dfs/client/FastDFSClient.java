package com.makeronly.dfs.client;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * FastDFS client
 * @author Walter Wong
 */
public class FastDFSClient {
    private StorageClient1 storageClient;

    public FastDFSClient() throws IOException, MyException {
        //默认配置
        this("fdfs_client.conf");
    }

    public FastDFSClient(String conf_filename) throws IOException, MyException {
        ClientGlobal.init(conf_filename);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        storageClient = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * 上传文件方法
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     * @param fileName 文件全路径
     * @param extName 文件扩展名，不包含（.）
     * @param metas 文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws IOException, MyException {
        return storageClient.upload_file1(fileName, extName, metas);
    }

    /**
     * 上传文件,传fileName
     * @param fileName 文件的磁盘路径名称 如：D:/image/aaa.jpg
     * @return null为失败
     */
    public String uploadFile(String fileName) throws IOException, MyException {
        return uploadFile(fileName, null, null);
    }

    /**
     *
     * @param fileName 文件的磁盘路径名称 如：D:/image/aaa.jpg
     * @param extName 文件的扩展名 如 txt jpg等
     * @return null为失败
     */
    public  String uploadFile(String fileName, String extName) throws IOException, MyException {
        return uploadFile(fileName, extName, null);
    }

    /**
     * 上传文件
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     * @param file_buff 文件的内容，字节数组
     * @param file_ext_name 文件扩展名
     * @param metas 文件扩展信息
     * @return null为失败
     */
    public String uploadFile(byte[] file_buff, String file_ext_name,NameValuePair[] metas) throws IOException, MyException {
        return storageClient.upload_file1(file_buff, file_ext_name, metas);
    }

    /**
     * 上传文件
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     * @param file_buff 文件的内容，字节数组
     * @param file_ext_name 文件扩展名
     * @return null为失败
     * @throws Exception
     */
    public String uploadFile(byte[] file_buff, String file_ext_name) throws IOException, MyException {
        return this.uploadFile(file_buff,file_ext_name,null);
    }

    /**
     * 上传文件
     * @param inputStream   文件数组流
     * @param file_ext_name 文件扩展名
     * @return
     */
    public String uploadFile(InputStream inputStream, String file_ext_name) throws MyException, IOException {
        byte[] buff = IOUtils.toByteArray(inputStream);

        return this.uploadFile(buff, file_ext_name);
    }
    /**
     * 上传文件
     * @param file_buff 文件的字节数组
     * @return null为失败
     * @throws Exception
     */
    public String uploadFile(byte[] file_buff) throws IOException, MyException {
        return uploadFile(file_buff, null, null);
    }

    /**
     * 文件下载到磁盘
     * @param path  图片路径
     * @param output    输出流 中包含要输出到磁盘的路径
     * @return  -1失败,0成功
     */
    public int download(String path,BufferedOutputStream output){
        int result=-1;
        try {
            byte[] b = storageClient.download_file1(path);
            try{
                if(b != null){
                    output.write(b);
                    result=0;
                }
            }catch (Exception e){} //用户可能取消了下载
            finally {
                if (output != null)
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取文件数组
     * @param path 文件的路径 如group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
     * @return
     */
    public byte[] download_bytes(String path) {
        byte[] b=null;
        try {
            b = storageClient.download_file1(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 删除文件
     * @param group 组名 如：group1
     * @param storagePath 不带组名的路径名称 如：M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
     * @return -1失败,0成功
     */
    public Integer delete_file(String group ,String storagePath) throws IOException, MyException {
        return  storageClient.delete_file(group, storagePath);
    }
    /**
     * 删除文件
     * @param storagePath  文件的全部路径 如：group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
     * @return -1失败,0成功
     * @throws IOException
     * @throws Exception
     */
    public Integer delete_file(String storagePath) throws IOException, MyException {
        return  storageClient.delete_file1(storagePath);
    }

    /**
     * 获取文件扩展名
     * @param fileName
     * @return
     */
    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    /**
     * 将文件名转换成UTF8格式
     * @param fileName
     * @return
     */
    public String transcode(String fileName) {
        try {
            fileName = new String(fileName.getBytes("ISO8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
