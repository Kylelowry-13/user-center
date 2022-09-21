package com.cai.service;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.cai.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setId(0);
        user.setUserName("kylelowry");
        user.setLoginId("kylelowry");
        user.setUserPassword("kylelowry");
        user.setAvatarUrl("https://tse1-mm.cn.bing.net/th/id/OIP-C.6htZ6Z1NZIcnByum3DbAgwAAAA?w=210&h=210&c=7&r=0&o=5&dpr=1.25&pid=1.7");
        user.setGender(0);
        user.setEmail("kylecai13@163.com");
        user.setPhoneNumber("17687553327");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);

        boolean save = userService.save(user);
        Assertions.assertEquals(save,true);
        System.out.println("通过成功");


    }


    // 过滤特殊字符
    public static Boolean StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!#$%^&*()+=|{}':;',\\[\\] .<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //Pattern p = Pattern.compile(regEx);
        //Matcher m = p.matcher(str);
        Matcher m = Pattern.compile(regEx).matcher(str);
        //return m.replaceAll("").trim();
        return m.find();
    }

    @Test public void testStringFilter() throws PatternSyntaxException {
        //String str = "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
        String str = "张三_@kyleLowry";
        System.out.println(str);
        System.out.println(StringFilter(str));
    }

    @Test
    public void testSalt(){
        final String salt = "kyle";
        String bytes = DigestUtils.md5DigestAsHex((salt + "abcd").getBytes());
        System.out.println(bytes);
    }

    @Test
    void checkEmail() {
    }

    @Test
    void userRegister() {
        User user = new User();
        user.setId(0);
        user.setUserName("13339");
        user.setLoginId("123456789");
        user.setUserPassword("123456789");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setEmail("123@qq.com");
        user.setPhoneNumber("17687553327");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);


    }

    @Test
    void login() {
    }
}