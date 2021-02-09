## 数据表 user

SQL语句

```
CREATE TABLE user(id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(20) NOT NULL,
pwd VARCHAR(20) NOT NULL,
age INT DEFAULT 11,
habits VARCHAR(200) DEFAULT 'baseball',
primary key(id))ENGINE=InnoDB DEFAULT CHARSET=utf8
```
|名称	|类型	|主键	|外键	|描述	|
| ----- | ----- | ----- | ----- | ----- |
|id|INT  NOT NULL AUTO_INCREMENT|true|false|用户的ID|
|name|VARCHAR  NOT NULL|false|false|用户名|
|pwd|VARCHAR  NOT NULL|false|false|用户密码|
|age|INT  DEFAULT 11|false|false|用户年龄|
|habits|VARCHAR  DEFAULT 'baseball'|false|false|用户爱好|