package cn.edu.njnu.geoproblemsolving.business.user.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.TokenInfo;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserVo;
import cn.edu.njnu.geoproblemsolving.business.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.business.user.service.TokenTask;
import cn.edu.njnu.geoproblemsolving.business.user.service.UserService;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import cn.hutool.core.io.watch.WatchException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    IUserImpl userDao;

    @Value("${authServerIp}")
    String authServerIp;

    public UserServiceImpl(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User findUser(String userId) {
        try {
            Optional user = userRepository.findById(userId);
            if (user.isPresent())
                return (User) user.get();
            else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Object updataUserInfo(User user) {
        try {
            String userId = user.getUserId();
            if (userRepository.findById(userId).isPresent()) return "The user does not exist.";

            String name = user.getName();
            if (name != null) {
                Query queryResource = Query.query(Criteria.where("uploaderId").is(userId));
                List<ResourceEntity> resourceEntities = mongoTemplate.find(queryResource, ResourceEntity.class);
                for (ResourceEntity resourceEntity : resourceEntities) {
                    String resourceId = resourceEntity.getUid();
                    Query query = Query.query(Criteria.where("resourceId").is(resourceId));
                    Update update = new Update();
                    update.set("uploaderName", name);
                    mongoTemplate.updateFirst(query, update, ResourceEntity.class);
                }
            }
            userRepository.save(user);

            return user;
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    @Override
    public Object register(User user) {
        try {
            String email = user.getEmail();
            if (userRepository.findByEmail(email) != null) return "The email address has been registered.";

            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);

            user.setJoinedProjects(new ArrayList<>());
            user.setCreatedProjects(new ArrayList<>());

            userRepository.save(user);
            return user;
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    @Override
    public Object login(String email, String password) {
        // try {
        //     User user = userRepository.findByEmail(email);
        //     if (user == null) return "The email address has not been registered.";
        //
        //     if (user.getPassword().equals(password)) {
        //         return user;
        //     } else {
        //         return "Fail: Worrg password";
        //     }
        // } catch (Exception ex) {
        //     return "Fail: Exception";
        // }
        return null;
    }

    @Override
    public JsonResult uploadResourceField(String email, ArrayList<ResourceEntity> res) {
        JsonResult updateUserResFieldResult = userDao.sharedUserRes(email, res);
        if (updateUserResFieldResult.getCode() == 0) {
            //更新成功，然后发送邮件
            //增加用户通知
            return ResultUtils.success();
        } else {
            return updateUserResFieldResult;
        }

    }


    @Override
    public Object resetPwd(String email, String oldPwd, String newPwd) {
        RestTemplateUtil restTemplateUtil = new RestTemplateUtil();
        String paramUrl = "email=" + email + "&oldPwd=" + oldPwd + "&newPwd=" + newPwd;
        String resetPwdUrl = "http://" + authServerIp + "/AuthServer/user/newPassword?" + paramUrl;
        Object jsonObject = restTemplateUtil.sendReqToAuthServer(resetPwdUrl, HttpMethod.POST);
        return jsonObject;
    }


    @Override
    public String changePassword(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) return "The email address has not been registered.";

            userRepository.save(user);

            return "Success";
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    //===3.31修改版本======================================================================

    @Value("${userServerLocation}")
    String userServerIpAndPort;

    @Value("${client_id}")
    String client_id;
    @Value("${client_secret}")
    String client_secret;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenTask tokenTask;

    /**
     * 用户注册服务
     * 1. 用户服务器注册
     * 2. 然后平台存储
     *
     * @param register
     * @return
     */
    @Override
    public JsonResult registerService(User register) {
        //构造 http 发送给用户服务器
        String url = "http://" + userServerIpAndPort + "/user";
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(mediaType);
        String password = register.getPassword();
        register.setPassword(DigestUtils.sha256Hex(password.getBytes()));
        HttpEntity<Object> httpEntity = new HttpEntity<>(register, httpHeaders);
        ResponseEntity<JSONObject> registerRes = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
        JsonResult result = reqStatus(registerRes);
        int code = (int) registerRes.getBody().get("code");
        //注册成功在本平台进行注册
        if (code == 0) {
            User user = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
            user.setTokenInfo(null);
            return userDao.saveUser(user);
        } else {
            return result;
        }
    }

    /**
     * 发送验证码邮件
     *
     * @param email
     * @return
     */
    @Override
    public JsonResult sendResetPwdEmail(String email) {
        String url = "http://" + userServerIpAndPort + "/user/resetPwd/" + email;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        return reqStatus(responseEntity);
    }

    @Override
    public JsonResult resetPwdByCode(String email, String code, String newPwd) {
        String encodePwd = DigestUtils.sha256Hex(newPwd.getBytes());
        String url = "http://" + userServerIpAndPort + "/user/resetPwd/" + email + "/" + code + "/" + encodePwd;
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        return reqStatus(responseEntity);
    }

    @Override
    public JsonResult resetPwdByOldPwd(String email, String oldPwd, String newPwd) {
        oldPwd = DigestUtils.sha256Hex(oldPwd.getBytes());
        newPwd = DigestUtils.sha256Hex(newPwd.getBytes());
        User user = userDao.findUserByIdOrEmail(email);
        TokenInfo tokenInfo = user.getTokenInfo();
        String access_token = tokenInfo.getAccess_token();
        HttpHeaders headers = new HttpHeaders();
        //从用户表中读取access_token内容
        headers.add("Authorization", "Bearer " + access_token);
        //HttpEntity(MultiValueMap<String, String> headers)
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        String resetPwdUri = "http://" + userServerIpAndPort + "/auth/newPwd/" + oldPwd + "/" + newPwd;
        ResponseEntity<JSONObject> resetResult = restTemplate.exchange(resetPwdUri, HttpMethod.GET, httpEntity, JSONObject.class);
        return JSON.parseObject(JSON.toJSONString(resetResult.getBody()), JsonResult.class);
    }

    /**
     * 授权并获取用户信息
     * @param email
     * @param password
     * @return
     */
    @Override
    public JsonResult loginAndAcquireInfo(String email, String password) {
        // password 模式获取token
        // User user = userDao.findByUserEmail(email);
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String encodePassword = DigestUtils.sha256Hex(password.getBytes());
        paramMap.add("client_id", client_id);
        paramMap.add("client_secret", client_secret);
        paramMap.add("username", email);
        paramMap.add("password", encodePassword);
        paramMap.add("scope", "all");
        paramMap.add("grant_type", "password");
        String authUri = "http://" + userServerIpAndPort + "/oauth/token";
        try {
            JSONObject tokenJson = restTemplate.postForObject(authUri, paramMap, JSONObject.class);
            // 获取 token 失败
            if (tokenJson == null) {
                return ResultUtils.error(-2, "Fail to access token");
            }

            // 将获取到的 token 相关内容给存到用户数据库中
            //MongoDB 的存储机制中, _id 相同的话使用 save 方法会更新字段
            String access_token = (String) tokenJson.get("access_token");
            String refresh_token = (String) tokenJson.get("refresh_token");
            Date invalidTime = JSON.parseObject(JSON.toJSONString(tokenJson.get("invalidTime")), Date.class);
            int expires = (int) tokenJson.get("expires_in");
            TokenInfo userToken = new TokenInfo(access_token, refresh_token, expires, invalidTime);

            // 使用access_token 获取用户信息
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + access_token);
            //httpEntity = httpHeader + httpBody,当然也可以只有其中一部分
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            String getInfoUri = "http://" + userServerIpAndPort + "/auth/login/127.0.0.1";
            //Url, RequestType, RequestContent, ResponseDataType
            //是否能用 User 接收返回结果？
            ResponseEntity<JSONObject> userJson = restTemplate.exchange(getInfoUri, HttpMethod.GET, httpEntity, JSONObject.class);
            //还是改用 JsonResult 来接，然后再使用 code 进行判断
            if (userJson.getBody().get("data") == null) {
                return ResultUtils.error(-2, "Failed to get user information.");
            }
            //转换成为了 localUser
            User loginUser = JSON.parseObject(JSON.toJSONString(userJson.getBody().get("data")), User.class);
            //登录的时候获取到可变的数据
            loginUser.setTokenInfo(userToken);
            userDao.saveUser(loginUser);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(loginUser ,userVo);
            return ResultUtils.success(userVo);
        }catch (Exception e){
            return ResultUtils.error(-2, "Email or Password was not found.");
        }
    }

    /**
     * 用于处理用户服务器返回内容
     *
     * @param responseEntity
     * @return
     */
    public JsonResult reqStatus(ResponseEntity<JSONObject> responseEntity) {
        int code = (int) responseEntity.getBody().get("code");
        String msg = (String) responseEntity.getBody().get("msg");
        Object data = responseEntity.getBody().get("data");
        switch (code) {
            case 0:
                return ResultUtils.success(data);
            case -1:
                return ResultUtils.error(-1, msg);
            case -2:
                return ResultUtils.error(-2, msg);
            case -3:
                return ResultUtils.error(-3, msg);
            default:
                return ResultUtils.error(-2, "Fail");
        }
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public JsonResult updateUserInfo(Map<String, Object> user) {
        //取出 token
        String userId = (String) user.get("userId");
        User localUser = userDao.findUserByIdOrEmail(userId);
        if (localUser == null){
            return ResultUtils.error(-2, "The user does not exist.");
        }
        TokenInfo tokenInfo = localUser.getTokenInfo();
        String access_token = tokenInfo.getAccess_token();

        // 更新用户服务器中的用户字段
        String updateUrl = "http://" + userServerIpAndPort + "/auth/update";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(user, headers);
        try {
            ResponseEntity<JsonResult> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.POST, httpEntity, JsonResult.class);
            JsonResult remoteUpdateResult = responseEntity.getBody();
            if (remoteUpdateResult.getCode() != 0){ return remoteUpdateResult;}
            //用户服务器更新成功，更新本地用户
            ICommonUtil commonUtil = new ICommonUtil();
            Update update = commonUtil.setUpdate(user);
            return ResultUtils.success(userDao.updateInfo(userId, update));
        }catch (Exception e){
            return ResultUtils.error(-2, "Failed to update user information.");
        }
    }

    @Override
    public void logout(String userId) {
        User logoutUser = userDao.findUserByIdOrEmail(userId);
        logoutUser.setTokenInfo(null);
        userDao.saveUser(logoutUser);
    }

    @Override
    public UserVo getUserVo(String userId) {
        User localUser = userDao.findUserByIdOrEmail(userId);
        if (localUser == null){
            return null;
        }
        UserVo userVo = JSONObject.parseObject(JSONObject.toJSONString(localUser), UserVo.class);
        return userVo;
    }

    @Override
    public JsonResult deleteUserProjectService(HttpServletRequest req) {
        return userDao.deleteUserProject(req);
    }
}
