# drpc-framework
## 介绍
[drpc-framework](https://github.com/yHaoo0/drpc-framework) 是一款基于[nacos](https://nacos.io/) 注册中心和netty的简易的rpc框架。
由于个人能力，drpc可能存在不足和bug，欢迎各位提交意见。你可以任意使用，修改drpc-framework。

项目核心:

* 基于Nacos注册中心 
* 基于Netty网络传输
* 基于Kyro序列化
* 基于Java动态代理和反射机制
* 基于jackson序列化
* 基于Nacos配置中心和类资源配置文件

## 项目运行
你可以查看drpc-framework-example里面示例学习使用该框架。

你可以使用`DrpcSimpleApplication`快速使用drpc-framework

### 配置模块

`common`模块实现基于Jackson框架读取类资源下properties和yaml格式文件的读取。

`nacos-config`基于common风格实现的读取naocos配置中心。

所有读取的配置统一登记到`ConfigContextManager`中。

1. 编写读取数据的POJO类

   ```java
   public class TestPojo {
       public String name;
       public int age;
   
       public TestPojo() {
       }
   }
   ```
   
2. 配置文件

   你可以在文件中配置其他需要登记的配置文件进行扫描。
   
   ```yaml
   name: localname
   age: 18
   # 本地扫描配置
   localConfigs:
     -
    id: local1 #查找配置文件的id值，如果重复将会覆盖原有的配置文件
    filePath: local1.yaml #配置文件的相对资源路径
   type: YAML # 配置文件的解析类型（格式）
    isMemory: false # 是否将内容写入内存，如果不写入，每次获取都从文件中重新获取（IO操作） 默认true
     -
    id: local2
    filePath: temp/local2.properties
    type: PROPS
   ```
   
3. 读取文件

   ```
   public class LocalConfigExample {
       public static void main(String[] args) {
           // 将配置文件登录到ConfigContextManager中
           // 配置信息和NacosContextManager使用相同的静态ConfigContextManager，相同dataId会被覆盖
           LocalConfigContextManager.load("local", "local.yaml", ConfigContextType.YAML, true);
           // 使用id获取配置文件
           TestPojo local = ConfigContextManager.get("local", TestPojo.class);
           System.out.println(local.name);
   
           // 扫描指定的配置文件中配置文件信息进行登录，该方法不实现递归扫描
           LocalConfigContextManager.scan("local.yaml", ConfigContextType.YAML);
           TestPojo local1 = ConfigContextManager.get("local1", TestPojo.class);
           System.out.println(local1.name);
           TestPojo local2 = ConfigContextManager.get("local2", TestPojo.class);
           System.out.println(local2.name);
       }
   }
   ```

4. Nacos配置中心

   1. 在nacos配置文件

      ```yaml
      # 该配置文件应该在naocos配置中心中
      # dataid : example group : DEFAULT_GROUP
      name: nacosname
      age: 18
      # nacos扫描配置，
      nacosConfigs:
        -
          id: example1 # nacos配置中心中对应的dataId，同时也是登录和查询的id
          group: DEFAULT_GROUP # nacos配置中心的group
          type: YAML # 解析类型
          isMemory: true #是否将内容写入内存，如果不写入，每次获取都从nacos配置中心中获取新的配置信息
      ```

   2. 调用配置

      ```java
      public class NacosConfigExample {
          public static void main(String[] args) {
              // nacos配置属性
              NacosProps props = new NacosProps();
              props.serverAddr = "127.0.0.1:8848";
              // 启动nacos配置中心实例
              NacosConfigContextManager manager = NacosConfigFactory.create(props);
              // 登录配置到ConfigContextManager中
              // 配置信息和LocalContextManager使用相同的静态ConfigContextManager，相同dataId会被覆盖
              manager.load("example", "DEFAULT_GROUP", ConfigContextType.YAML, false);
              // 扫描指定的配置文件中配置文件信息进行登录，该方法不实现递归扫描
              // 如果是本地文件，可以使用manager.localScan
              manager.nacosScan("example", "DEFAULT_GROUP", ConfigContextType.YAML);
      
              TestPojo nacos = ConfigContextManager.get("example", TestPojo.class);
              System.out.println(nacos.name);
      
              TestPojo nacos1 = ConfigContextManager.get("example1", TestPojo.class);
              System.out.println(nacos1.name);
      
              /*
              你可以通过DrpcSimpleApplication 开启NacosConfigContextManager
              DrpcSimpleApplication application = new DrpcSimpleApplication();
              NacosConfigContextManager manager = application.getNacosConfigManager();
              */
      
          }
      }
      ```

### Nacos服务启动

该项目依赖Nacos服务，使用前先启动Nacos服务器。

请到[nacos 官方手册](https://nacos.io/zh-cn/docs/quick-start.html) 查看nacos运行和操作方法。

### 服务端
1. 配置属性

   Nacos服务中心已经Nacos配置中心的配置属性封装到`NacosProps`类中。

   关于nacos更多配置可以参考[Nacos 系统参数介绍](https://nacos.io/zh-cn/docs/system-configurations.html) 。你可以查看`NacosProps`源码查看其序列化id。通过jackson实例

   ```java
   NacosProps nacosProps = new NacosProps();
   nacosProps.serverAddr = "127.0.0.1:8848";
   ```

   Netty 服务端配置属性封装到`NettyServerProps`,你需要配置服务端ip地址以及端口

   你可以查看`NettyServerProps`源码查看其序列化id。通过jackson实例

   ```java
   NettyServerProps serverProps = new NettyServerProps();
   serverProps.host = "127.0.0.1";
   serverProps.port = 8090;
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
        * isSingleton ： 是否为单例模式，每次获取代理是否单例还是新新的实例 默认为true
        * @return 必须为以实现的接口为返回类
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
	public class NettyServiceExample {
	    public static void main(String[] args) {
	        // 启动Nacos 服务中心
	        NacosProps nacosProps = new NacosProps();
	        nacosProps.serverAddr = "127.0.0.1:8848";
	        ServiceCenter serviceCenter = new NacosServicesCenter(nacosProps);
	        serviceCenter.enable();
	        // 启动Netty Service
	        NettyServerProps serverProps = new NettyServerProps();
	        serverProps.host = "127.0.0.1";
	        serverProps.port = 8090;
	        NettyServerApplication application = new NettyServerApplication(serverProps, serviceCenter);
	        // 扫描提供服务实体的工厂类，登录服务到服务中心，并开始监听端口
	        application.enable(new ServiceFactory("Hello World"));
	    }
	}
	```
	
	可以选择`simple-start`中`DrpcSimpleApplication`快速开始启动

   `DrpcSimpleApplication`实例的同时，从配置属性中实例`NacosServicesCenter`以及`NacosConfigContextManager`。
   
    ```java
    public class SimpleStartServiceExample {
        public static void main(String[] args) {
            // 默认读取配置文件drpc.properties 如果不存在 读取drpc.yml 登录到ConfigContextManager中，dataid为drpc
            // 然后从ConfigContextManager中取id为drpc配置初始化服务
            // 可以使用DrpcSimpleApplication(String configId) 指定ConfigContextManager中登录的配置属性
            DrpcSimpleApplication application = new DrpcSimpleApplication();
            application.enableService(new ServiceFactory("Hello Simple World"));
        }
    }
    ```

### 客户端

1. 配置文件

   Nacos服务中心已经Nacos配置中心的配置属性封装到`NacosProps`类中。

   关于nacos更多配置可以参考[Nacos 系统参数介绍](https://nacos.io/zh-cn/docs/system-configurations.html) 。你可以查看`NacosProps`源码查看其序列化id。通过jackson实例

   ```java
   NacosProps nacosProps = new NacosProps();
   nacosProps.serverAddr = "127.0.0.1:8848";
   ```

   Netty 服务端配置属性封装到`NettyClientProps`。

   你可以查看`NettyClientProps`源码查看其序列化id。通过jackson实例

   ```java
   NettyClientProps clientProps = new NettyClientProps();
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
        * isSingleton ： 是否为单例模式，每次获取代理是否单例还是新新的实例 默认为true
        * @return 必须为实现的接口类
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
   public class NettyClientExample {
       public static void main(String[] args) {
           // 启动Nacos 服务中心
           NacosProps nacosProps = new NacosProps();
           nacosProps.serverAddr = "127.0.0.1:8848";
           ServiceCenter serviceCenter = new NacosServicesCenter(nacosProps);
           serviceCenter.enable();
           // 启动Netty Client
           NettyClientProps clientProps = new NettyClientProps();
           RpcClientApplication application = new NettyClientApplication(clientProps);
           application.enable(serviceCenter, new ClientFactory());
   
           // 获取代理
           HelloWorld hello1 = application.getProxy("hello1");
           System.out.println(hello1.sayHello(",Bye"));
   
           // 失败时调用客户端实例
           HelloWorld hello2 = application.getProxy("hello2");
           System.out.println(hello2.sayHello(",Bye"));
   
           // netty监听主线程，如果要结束程序，需要关闭监听
           application.shutdown();
       }
   }
   ```
   
   可以选择`simple-start`中`DrpcSimpleApplication`快速开始启动
   
   `DrpcSimpleApplication`实例的同时，从配置属性中实例`NacosServicesCenter`以及`NacosConfigContextManager`。
   
   ```java
   public class SimpleStartClientExample {
       public static void main(String[] args) {
           // 默认读取配置文件drpc.properties 如果不存在 读取drpc.yml 登录到ConfigContextManager中，dataid为drpc
           // 然后从ConfigContextManager中取id为drpc配置初始化服务
           // 可以使用DrpcSimpleApplication(String configId) 指定ConfigContextManager中登录的配置属性
           DrpcSimpleApplication application = new DrpcSimpleApplication();
           application.enableClient(new ClientFactory());
   
           // 获取代理
           HelloWorld hello1 = application.proxy("hello1");
           System.out.println(hello1.sayHello(",Bye"));
   
           // 失败时调用客户端实例
           HelloWorld hello2 = application.proxy("hello2");
           System.out.println(hello2.sayHello(",Bye"));
   
           // netty监听主线程，如果要结束程序，需要关闭监听
           application.shutdownClient();
       }
   }
   ```

