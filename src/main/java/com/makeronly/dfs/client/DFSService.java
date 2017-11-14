package com.makeronly.dfs.client;

import org.csource.common.MyException;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文件操作Servide
 * @author Walter Wong
 */
public interface DFSService {
    /**
     * 上传文件
     * @param inputStream   文件输入流
     * @param fileName  文件名
     * @return
     */
    String upload(InputStream inputStream, String fileName) throws IOException, MyException;

    /**
     * 多文件上传
     * @param multiPart
     * @return
     */
    List<String> multipartUpload(FormDataMultiPart multiPart) throws IOException, MyException;
}

