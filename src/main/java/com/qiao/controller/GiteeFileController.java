package com.qiao.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiao.config.ResponseResult;
import com.qiao.pojo.BasicRequest;
import com.qiao.pojo.GiteeConstant;
import com.qiao.utils.GiteeUtil;
import com.qiao.utils.WebTools;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;

/**
 * @Author AsteroidQiao
 * @Create 2023-10-12
 */
//https://gitee.com/api/v5/repos/:owner/:repo/contents/:path
//https://gitee.com/api/v5/repos/{owner}/{repo}/contents/{path}
@RestController
@RequestMapping("/gitee")
public class GiteeFileController {
    //public static void uploadFile(String accessToken, String owner, String repo, File file) throws IOException {
    //    HttpClient httpClient = HttpClientBuilder.create().build();
    //    HttpPost httpPost = new HttpPost("https://gitee.com/api/v5/repos/" + owner + "/" + repo + "/contents/");
    //    httpPost.setHeader("Authorization", "token " + accessToken);
    //    HttpEntity entity = MultipartEntityBuilder
    //            .create()
    //            .addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName())
    //            .build();
    //    httpPost.setEntity(entity);
    //    HttpResponse response = httpClient.execute(httpPost);
    //    System.out.println("文件上传结果: " + response.getStatusLine());
    //}
    //
    //public static void main(String[] args) {
    //    String accessToken = "45eb1562246552cffee375286e5fef88";
    //    String owner = "AsteroidQiao";
    //    String repo = "library-management-system";
    //    File file = new File("D:\\program\\IDEA\\Association\\src\\main\\java\\com\\qiao\\controller\\UsersController.java");
    //    try {
    //        GiteeFileController.uploadFile(accessToken, owner, repo, file);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("uploadFile()请求=============");
        //根据文件名生成指定的请求url
        String originalFilename = file.getOriginalFilename();
        //Gitee请求：发送上传文件请求
        String JSONResult = GiteeUtil.uploadFile("/book-club/", originalFilename, file.getBytes());
        //解析响应JSON字符串
        JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
        //请求失败
        if (jsonObj == null || jsonObj.getObj(GiteeConstant.RESULT_BODY_COMMIT) == null) {
            return ResponseResult.okResult(201, "上传失败！");
        }
        //请求成功：返回下载地址
        JSONObject content = JSONUtil.parseObj(jsonObj.getObj(GiteeConstant.RESULT_BODY_CONTENT));
        System.out.println(("上传成功，下载地址为：" + content.getObj(GiteeConstant.RESULT_BODY_DOWNLOAD_URL)));
        return ResponseResult.okResult(200, "上传成功！", content.getObj(GiteeConstant.RESULT_BODY_DOWNLOAD_URL));
    }

    /**
     * 删除文件
     *
     * @param request
     * @return
     */
    @PostMapping("/del")
    public ResponseResult delFile(@RequestBody BasicRequest request) {
        //1、解析取得原始上传路径
        String url = request.getUrl();
        if (WebTools.isNotEmpty(url) && !url.contains("master/")) {
            System.out.println("url：" + url + " 无法解析路径！");
        }
        String path = url.substring(url.indexOf("master/") + 7);
        System.out.println("解析取得待删除路径：" + path);
        if (!WebTools.isEmpty(path)) {
            //2、Gitee请求：获取sha
            String shaResult = GiteeUtil.getSha(path);
            JSONObject jsonObj = JSONUtil.parseObj(shaResult);
            if (jsonObj == null) {
                System.out.println("delFile中获取sha失败！");
                return ResponseResult.okResult(200, "删除失败！");
            }
            String sha = jsonObj.getStr(GiteeConstant.RESULT_BODY_SHA);
            //3、Gitee请求：发送删除请求
            String JSONResult = GiteeUtil.deleteFile(path, sha);
            jsonObj = JSONUtil.parseObj(JSONResult);
            if (jsonObj == null || jsonObj.getObj(GiteeConstant.RESULT_BODY_COMMIT) == null) {
                System.out.println("删除文件失败！");
                return ResponseResult.okResult(200, "删除失败！");
            }
        }
        System.out.println("文件路径为：" + path + " 删除成功！");
        return ResponseResult.okResult(200, "删除成功！");
    }
}
