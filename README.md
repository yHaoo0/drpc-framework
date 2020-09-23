# drpc-framework
## 介绍
[drpc-framework](https://github.com/yHaoo0/drpc-framework) 是一款基于[nacos](https://nacos.io/) 注册中心和netty的简易的rpc框架。
由于个人能力，drpc可能存在不足和bug，欢迎各位提交意见。你可以任意使用，修改drpc-framework。

项目核心:

* 基于Nacos注册中心 
* 基于Netty网络传输
* 基于Kyro序列化
* 基于Java动态代理和反射机制

## 项目运行
你可以查看drpc-framework-example里面示例学习使用该框架。

你可以使用`DrpcSimpleApplication`快速使用drpc-framework

### 注册中心
请到[nacos 官方手册](https://nacos.io/zh-cn/docs/quick-start.html) 查看nacos运行和操作方法。

### 服务端
1. 配置文件

   `DrpcSimpleApplication`会读取`resources`下`drpc.properties`配置文件。

   你需要配置服务端对外开放的ip及端口，以及nacos配置属性。

   关于nacos更多配置可以参考[Nacos 系统参数介绍](https://nacos.io/zh-cn/docs/system-configurations.html) 。为了减少冲突，添加`nacos.`为前缀

   ```properties
   # nacos
   nacos.serverAddr=127.0.0.1:8848
   
   # service
   # rpc service 对外开放的ip以及端口
   netty.service.host=127.0.0.1
   netty.service.port=8090
   ```

2. 实现接口

  和其他rpc框架一样。我们需要一个接口类规范服务端和客户端之间的传输。

  ```java
  import example.api.HelloWorld;
  // 接口的服务实现
  public class HelloWorldImp implements HelloWorld {
      String head;
  
      public HelloWorldImp(String head) {
          this.head = head;
      }
  
      @Override
      public String sayHello(String message) {
          return head + message;
      }
  }
  ```

3. 编写工厂类作为服务实体的提供者

   drpc-framework基于工厂，注解以及方法实例化服务实体和注册服务。

   ```java
   // 服务实体的工厂类。 用于创建服务的实体类
   public class ServiceFactory {
       String head;
   
       public ServiceFactory(String head) {
           this.head = head;
       }
   
       /**
        * 将该方法注解为返回服务实例的方法
        * 该方法返回实例作为服务注册到服务中心
        * 只支持无参方法
        * service : 服务名
        * version ： 版本号
        * group ： 组 默认 ""
        * @return
        */
       @DrpcService(serviceName = "hello", version = "test")
       public HelloWorld hello(){
           return new HelloWorldImp(head);
       }
   }
   ```
   
4. 运行服务端

	前期工作准备好后，我们可以开始运行服务端
	
	```java
	public class SimpleStartExample {
	    public static void main(String[] args) {
	        // 实例化DrpcSimpleAppliction，appliction会读取`drpc.properties`文件，并创建服务中心
	        DrpcSimpleApplication application = new DrpcSimpleApplication();
	        // 启用服务，并注入工厂类，appliction将会组装 `NacosServicesCenter` 以及 `NettyServerApplication`
	        // 并且开始监听
	        application.enableService(new ServiceFactory("Simple Start: "));
	    }
	}
	```

### 客户端

1. 配置文件

   `DrpcSimpleApplication`会读取`resources`下`drpc.properties`配置文件。

   你需要配置nacos配置属性。

   关于nacos更多配置可以参考[Nacos 系统参数介绍](https://nacos.io/zh-cn/docs/system-configurations.html) 。为了减少冲突，添加`nacos.`为前缀

   ```properties
   # nacos
   nacos.serverAddr=127.0.0.1:8848
   ```

2. 实现接口

   你可以实现接口作为简易的熔断手段。这和服务端的实现是一样的。

   ```java
   import example.api.HelloWorld;
   // 接口的客户端实现，当无法获取正常响应时返回
   public class HelloWorldImp implements HelloWorld {
       @Override
       public String sayHello(String message) {
           return "response is exception";
       }
   }
   ```

3. 编写工厂类

   这和服务端一样，基于工厂以及注解登记熔断实体以及服务信息。

   ```java
   // 客户实体的工厂类。 用于创建客户端的实体类
   public class ClientFactory {
       /**
        * 将该方法注解为返回客户实例的方法
        * 该方法返回实例将按配置查找服务，如果服务失败，则返回该实例的内容
        * 只支持无参方法
        * clientName ： 该实例的名字。用于获取该实例
        * serviceName : 向服务中心申请的服务名
        * version ： 向服务中心申请的版本号
        * group ： 向服务中心申请的组 默认 ""
        * @return
        */
       @DrpcClient(clientName = "hello1", serviceName = "hello", version = "test")
       public HelloWorld hello1(){
           return new HelloWorldImp();
       }
   
       @DrpcClient(clientName = "hello2", serviceName = "hello", version = "not_found")
       public HelloWorld hello2(){
           return new HelloWorldImp();
       }
   }
   ```

4. 运行客户端

   运行客户端后，通过`clientName`获取代理实例。

   ```java
   public class SimpleStartExample {
       public static void main(String[] args) throws InterruptedException {
           // 实例化DrpcSimpleApplication，appliction会读取`drpc.properties`文件，并创建服务中心
           DrpcSimpleApplication application = new DrpcSimpleApplication();
           // 传入工厂类登录服务，启用客户端，开始监听服务
           application.enableClient(new ClientFactory());
           // 获取服务代理
           HelloWorld hello1 = application.proxy("hello1");
           System.out.println(hello1.sayHello("Hello, world "));
           // 当服务失败时，返回客户端实现内容
           HelloWorld hello2 = application.proxy("hello2");
           System.out.println(hello2.sayHello("Hello, world"));
           // 关闭客户端监听
           application.shutdownClient();
       }
   }
   ```