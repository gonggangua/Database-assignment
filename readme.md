# Database-assignment

## 环境

- Maven 3.6.3
- MySQL 8.0.31
- mybatis 3.5.11

尽量使用相同版本避免版本差异问题

## SQL

为本地数据库备份(不含数据)

先执行assignment.sql建立数据库

然后create_role_regularUser创建角色regularUser

最后记得set_roles_on使角色激活

具体各表内容参见表设计

## 使用说明

1. 对于一个新用户，先用Register.register(...)注册，

   此时会新建数据库的用户，并在相关表里添加其基本资料以及自动生成用户相关视图。

2. 每次登录使用Login(...)构造一个新的登录实例，

   可以通过Login内各个方法实现功能，具体可参见方法注释。

3. 要进入服务器时, 调用Login.enterServer(...)获取一个ServerInteract实例，

   用于与服务器进行具体交互

4. 在访问频道时，请先调用accessingChannel获取AccessingChannel实例并保存，

   退出频道界面时调用leaveChannel传入此次访问实例，从而记录一次对频道的访问，

   语音频道需通过访问记录自动计算是否开始或结束通话。

可以参考com.test.java.dao.pojo.LoginTest或RegisterTest中各个函数的使用。

有疑问，出错了，或需要新功能请及时联系。