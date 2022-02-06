package com.hippocp.util;

import com.hippocp.easy.code.util.bean.BeanCopyUtil;
import com.hippocp.util.entity.UserDO;
import com.hippocp.util.entity.UserVO;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanCopyUtil单元测试
 *
 * @author ZhouYifan
 * @date 2021/12/16
 */
public class BeanCopyUtilTest {

    @Test
    public void copyListPropertiesTest() {
        // 初始化数据
        List<UserDO> doList = new ArrayList<>();
        UserDO userDO = new UserDO();
        final int age = 10;
        userDO.setAge(age);
        final String name = "小明";
        userDO.setName(name);
        doList.add(userDO);
        // 被测方法
        List<UserVO> voList = BeanCopyUtil.copyListProperties(doList, UserVO::new);
        // 测试通过与否判断
        Assert.notNull(voList, "VO列表为空，复制列表中对象失败，请检查");

        // 打印到控制台
        System.out.println("voList: " + voList);

        UserDO user = doList.get(0);
        UserVO userVO = voList.get(0);
        // name值是否等于小明
        boolean isXiaoMing = userVO.getName().equals(name);
        // 断言
        Assert.isTrue(isXiaoMing, "vo对象name值不是" + name + "，请检查");

        // age值是否等于10
        boolean isTen = userVO.getAge().equals(age);
        // 断言
        Assert.isTrue(isTen, "vo对象age值不等于" + age + "，请检查");

        // name值是否相同
        boolean isNameEquals = user.getName().equals(userVO.getName());
        // 断言
        Assert.isTrue(isNameEquals, "源数据对象与目标对象name值不同，请检查");

        // age
        boolean isAgeEquals = user.getAge().equals(userVO.getAge());
        // 断言
        Assert.isTrue(isAgeEquals, "源数据对象与目标对象age值不同，请检查");

    }

    @Test
    public void copyListPropertiesTest02() {
        // 初始化数据
        List<UserDO> doList = new ArrayList<>();
        UserDO userDO = new UserDO();
        final int age = 26;
        userDO.setAge(age);
        final String name = "赵红";
        userDO.setName(name);
        final int sex = 0;
        userDO.setSex(sex);
        doList.add(userDO);
        // 被测方法
        List<UserVO> voList = BeanCopyUtil.copyListProperties(doList, UserVO::new, (user, userVO) -> {
            String sexStr;
            if (user.getSex() == 0) {
                sexStr = "女";
            } else {
                sexStr = "男";
            }
            userVO.setSex(sexStr);
        });

        // 测试通过与否判断
        Assert.notNull(voList, "VO列表为空，复制列表中对象失败，请检查");

        UserDO user = doList.get(0);
        UserVO userVO = voList.get(0);
        // name值是否等于初始值
        boolean isInitName = userVO.getName().equals(name);
        // 断言
        Assert.isTrue(isInitName, "vo对象name值不是" + name + "，请检查");

        // 打印到控制台
        System.out.println("voList: " + voList);

        // age值是否等于初始值
        boolean isInitAge = userVO.getAge().equals(age);
        // 断言
        Assert.isTrue(isInitAge, "vo对象age值不等于" + age + "，请检查");

        // name值是否相同
        boolean isNameEquals = user.getName().equals(userVO.getName());
        // 断言
        Assert.isTrue(isNameEquals, "源数据对象与目标对象name值不同，请检查");

        // age
        boolean isAgeEquals = user.getAge().equals(userVO.getAge());
        // 断言
        Assert.isTrue(isAgeEquals, "源数据对象与目标对象age值不同，请检查");

        // sex
        boolean isWoman = userVO.getSex().equals("女");
        // 断言
        Assert.isTrue(isWoman, "目标对象sex值不是女，请检查");

    }

}
