# Easy Code

## 简介

Easy Code 轻松编码组件，是项目中基础设施组件友好的替代，旨在减少重复工作，提高工作效率，它节省了开发者对项目中基础设施组件的开发时间，同时可以最大限度的避免封装不完善带来的bug，期望能让每一位 Java
开发者都能专注于自身独特业务。

### 设计理念

采用“加入谁就融入谁”的设计理念，不向外传递第三方依赖，不改变主项目行为，使主项目稳定可控。

### easy-code 如何改变我们的编码方式

全局异常处理器例子：

【原来】重复编写 try-catch 代码块，或者复制其它项目中的异常处理器，改改好用。

【现在】引入依赖 easy-code-spring-boot-starter 即可。

Excel表格校验例子：

【原来】编写Excel表格导入数据功能，其中70%为校验代码（甚至是大段if-else），只有30%为业务逻辑代码。

【现在】使用 @EqualsExcelTitle 注解和 ExcelValidateUtil 工具类，完全专注于业务校验规则和业务逻辑处理。

## 包含功能

https://github.com/HippoGroup/easy-code/wiki/%E5%8C%85%E5%90%AB%E5%8A%9F%E8%83%BD

## 使用指南

https://github.com/HippoGroup/easy-code/wiki/%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97

## 包含模块

| 模块                          | 介绍         |
| :---------------------------- | :----------- |
| easy-code-spring-boot-starter | 开箱即用     |
| easy-code-all                 | 整合打包模块 |
| easy-code-exception-handler   | 异常处理模块 |
| easy-code-monitor             | 监控模块     |
| easy-code-validate            | 校验模块     |
| easy-code-util                | 工具模块     |
| easy-code-domain              | 领域模型模块 |

## 更新日志

https://github.com/HippoGroup/easy-code/wiki/%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97
