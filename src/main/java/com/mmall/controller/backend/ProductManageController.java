package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhangsiqi
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @ResponseBody
    @RequestMapping("save.do")
    public ServerResponse productSave(HttpServletRequest request, Product product) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }


    }

    @ResponseBody
    @RequestMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充我们增加产品的业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("detail.do")
    public ServerResponse getDetail(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("search.do")
    public ServerResponse productSearch(HttpServletRequest request, String productName, Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("upload.do")
    public ServerResponse upload(HttpServletRequest request, @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录, 请登录管理员账户");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 填充业务
            // 获取文件上传到Tomcat中的路径
            String path = request.getSession().getServletContext().getRealPath("upload");
            // 上传文件到服务器, 返回文件名
            String targetFilename = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFilename;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFilename);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("richtext_img_upload.do")
    public Map richTextImg_upload(HttpServletRequest request, @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                  HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.String2Obj(userJsonStr, User.class);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        // 富文本中对于返回值有自己的要求, 我们使用的是simditor所以按照simditor的要求进行返回
        /*{
            "success": true/false,
            "msg": "error message", # optional
            "file_path": "[real file path]"
        }*/
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 获取文件上传到Tomcat中的路径
            String path = request.getSession().getServletContext().getRealPath("upload");
            // 上传文件到服务器, 返回文件名
            String targetFilename = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFilename)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFilename;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }

}