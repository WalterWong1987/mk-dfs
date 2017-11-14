package com.makeronly.dfs.client;

import org.csource.common.MyException;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传Service
 * @author Walter Wong
 */
@Service(value = "dfs.service")
@Scope(value="prototype")
public class DFSServiceImpl implements DFSService{
    /**
     * 上传文件
     * @param inputStream 文件输入流
     * @param fileName    文件名
     * @return
     */
    @Override
    public String upload(InputStream inputStream, String fileName) throws IOException, MyException {
        FastDFSClient dfs = new FastDFSClient();

        //转换文件名为UTF-8格式
        fileName = dfs.transcode(fileName);

        String id = dfs.uploadFile(inputStream, dfs.getFileExtension(fileName));

        return id;
    }

    /**
     * 多文件上传
     *
     * @param multiPart
     * @return
     */
    @Override
    public List<String> multipartUpload(FormDataMultiPart multiPart) throws IOException, MyException {
        List<FormDataBodyPart> bodyParts = multiPart.getFields("files");

        List<String> ids = new ArrayList<String>();

        /* Save multiple files */
        for (int i = 0; i < bodyParts.size(); i++) {
            BodyPartEntity entity = (BodyPartEntity) bodyParts.get(i).getEntity();
            String fileName = bodyParts.get(i).getContentDisposition().getFileName();
            String id = this.upload(entity.getInputStream(), fileName);
            ids.add(id);
        }
        return ids;
    }
}
