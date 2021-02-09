# 1. Background

Simplify development to support http-based services

## 1.1 Features

- [JSR303](examples/jsr303)
- [Restful](examples/restful)

## 1.2 Authors

- wuheng@otcaix.iscas.ac.cn

## 1.3 Limitions

- [Java >1.8 and support -parameter](https://blog.csdn.net/sanyuesan0000/article/details/80618913)
- [Swagger](https://editor.swagger.io/?_ga=2.101767682.249745839.1589876051-842250693.1589876051)

# 2. Quick start

## 2.1 Create a Maven project in your IDE

## 2.2 Add dependency

```
<dependency>
  <groupId>com.github.kubesys</groupId>
  <artifactId>kubernetes-httpfrk-core</artifactId>
  <version>1.20</version>
</dependency>

<repositories>
   <repository>
       <id>pdos-repos</id>
       <name>PDOS Releases</name>
       <url>http://39.106.40.190:31021/repository/maven-public/</url>
    </repository>
</repositories>
```

## 2.3 Create application.yml in src\main\resources

You can modify it as your want.

```
server:
  port: 8080
  servlet:
    context-path: /sql
```

## 2.4 Create Main.class (MyHttpServer.class for example)

```
package dev.examples.basic;

import java.util.logging.Logger;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.github.kubesys.httpfrk.HttpServer;


/** 
 *        src/main/resources/application.yml
 *        src/main/resources/log4j.properties
 * 
 */

@ComponentScan(basePackages = { "com.github.kubesys.httpfrk" })
public class MyApplicationServer extends HttpServer  {

	public static final Logger m_logger = Logger.getLogger(MyApplicationServer.class.getName());

	/**
	 * program entry point
	 * 
	 * @param args default is null
	 */
	public static void main(String[] args) {
		SpringApplication.run(MyApplicationServer.class, args);
	}

}
```


# 3. Documents

- [Background](https://www.yuque.com/syswu/sedad9/vc865a)
- Design
  - [Architecture](https://www.yuque.com/syswu/sedad9/dw83qq)
  - [Specification](https://www.yuque.com/syswu/sedad9/wsnx6y)
- [Enciroments](https://www.yuque.com/syswu/sedad9/kxwl7q)
- Examples
  - [helloWold](https://www.yuque.com/syswu/sedad9/hsw9nc)
  - [MySQL](https://www.yuque.com/syswu/sedad9/unv932)
  - [DOcs](https://www.yuque.com/syswu/sedad9/yeaq22)

# 4. RoadMap

- 1.x support Http
  - 1.12.0 support restful
  - 1.13.0 support mysql
  - 1.14.0 support jsr303
  - 1.15.0 support Kubernetes
  
- 2.x Support Https
  - 2.0.0：support Https
  - 2.1.0：support Redis
  - 2.2.0：support Kafka
  - 2.3.0：support MongoDB
  
  # 5. 指令集
  
  - ETCDCTL_API=3 etcdctl --endpoints=https://127.0.0.1:2379 --cacert=/etc/kubernetes/pki/etcd/ca.crt --cert=/etc/kubernetes/pki/etcd/healthcheck-client.crt --key=/etc/kubernetes/pki/etcd/healthcheck-client.key get /registry/pods/default --prefix --keys-only
