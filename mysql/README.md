# 1. Background

Simplify development to support http-based services

## 1.1 Create a model using annotation '@JavaBean'

```
@JavaBean
public class User {

	@PrivateKey
	@AutoIncrement
	@ParamDescriber(desc = "用户的ID")
	private long id;
	
	@Size(min = 10, max = 20) 
	@ParamDescriber(desc = "用户名", required = true)
	private String name;

	@ParamDescriber(desc = "用户密码", required = true)
	@Size(min = 8, max = 20)
	private String pwd;

	@DefaultValue("11")
	@Min(8)
	@Max(100)
	@ParamDescriber(desc = "用户年龄")
	private int age;

	@DefaultValue("baseball")
	@Size(max = 200)
	@ParamDescriber(desc = "用户爱好")
	private String habits;

	@Size(max = 200)
	@ParamDescriber(desc = "currentAddr")
	@TableField("current_addr")
	private String currentAddr;

	public String getCurrentAddr() {
		return currentAddr;
	}

	public void setCurrentAddr(String currentAddr) {
		this.currentAddr = currentAddr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHabits() {
		return habits;
	}

	public void setHabits(String habits) {
		this.habits = habits;
	}

}
``` 

## 1.2 Create a mysql table based on '@JavaBean'

- [right zone for mysql](https://blog.csdn.net/HW_870754395/article/details/88430293)

```
MyqlTableGenerator sg = new MyqlTableGenerator("jdbc:mysql://127.0.0.1:3306",
				"com.mysql.cj.jdbc.Driver", "root", "onceas");
		sg.setDbName("mypoj");
		sg.createDatabase();
		sg.createTable(Class.forName("dev.examples.mysql.models.User"));
```

## 1.3 Create a Mapper based on 'mybaits annotation'

```
@Mapper
public interface UserMapper {

	@Select("select * from user")
	@ServiceDescriber(shortName = "查询用户", desc = "查询用户", prereq = "无")
	public List<User> retrieveAllUsers();

	@Select("select * from user where id=#{id}")
	@ServiceDescriber(shortName = "根据Id查询用户")
	public User retrieveUserById(long id);

	@Select("select * from user where name like #{name} limit #{from},#{to}")
	@ServiceDescriber(shortName = "根据名字查询用户")
	public User retrieveUserByIdAndName(String name, long from, long to);
	
	@Select("select count(*) as count from user where name like #{name}")
	@ServiceDescriber(shortName = "根据Id和名字查询用户的返回值数量")
	public int retrieveUserByIdAndNameCount(String name);

	@Insert("INSERT INTO user(name, pwd, age) VALUES(#{name}, #{pwd}, #{age})")
	@ServiceDescriber(shortName = "创建用户")
	public void addNewUser(User user);

	@Delete("delete from user where id=#{id}")
	@ServiceDescriber(shortName = "根据Id删除用户")
	public void deleteUser(int id);

	@Update("update user set age=#{age} where name=#{name}")
	@ServiceDescriber(shortName = "更新用户")
	public void updateUser(User user);

}
```

## 1.4 Create a Servcie basd on the [restful example](https://github.com/kubesys/kubernetes-httpfrk/tree/master/examples/restful)
