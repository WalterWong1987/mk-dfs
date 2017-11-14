package com.makeronly.dfs.resource;

import com.makeronly.common.bean.ResultBean;
import com.makeronly.dfs.client.DFSService;
import org.csource.common.MyException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文件服务
 * @author Walter Wong
 */
@Component
@Path("/file")
public class FileResource {
    @Resource(name = "dfs.service")
    private DFSService dfs;

    /**
     * upload file to storage server
     * @param inputStream   输入流
     * @param content 文件头信息
     * @return file id(including group name and filename) if success, <br>
     * return null if fail
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA+";charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultBean<String> upload(@FormDataParam("file") InputStream inputStream, @FormDataParam("file")FormDataContentDisposition content) throws IOException, MyException {
        return new ResultBean<>(dfs.upload(inputStream, content.getFileName()));
    }

    /**
     * Multiple files upload
     * @param multiPart 文件信息
     * @return ResultBean
     * @throws IOException  IO异常
     * @throws MyException  fastdfs异常
     */
    @POST
    @Path("/multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultBean<List<String>> multipartUpload(FormDataMultiPart multiPart) throws IOException, MyException {
        return new ResultBean<>(dfs.multipartUpload(multiPart));
    }
}
