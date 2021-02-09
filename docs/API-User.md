## 根据Id查询用户


**URL**: 

请求类型: POST/GET
请求路径: http://127.0.0.1:8080/webrk/1.5/user/retrieveUserById

**功能描述**: 

:暂无


**前置条件**: 

暂无


**请求格式**: 

```
{
	"id":"int"
}
```

**正确返回**: 

```
{
	"result":{
		"habits":"java.lang.String",
		"name":"java.lang.String",
		"id":"java.lang.String",
		"pwd":"java.lang.String",
		"age":"java.lang.String"
	},
	"status":"success"
}
```


## 更新用户


**URL**: 

请求类型: POST/PUT
请求路径: http://127.0.0.1:8080/webrk/1.5/user/updateUser

**功能描述**: 

:暂无


**前置条件**: 

暂无


**请求格式**: 

```
{
	"user":{
		"habits":"java.lang.String",
		"name":"java.lang.String",
		"id":"int",
		"pwd":"java.lang.String",
		"age":"int"
	}
}
```

**正确返回**: 

```
{
	"status":"success"
}
```


## 创建用户


**URL**: 

请求类型: POST
请求路径: http://127.0.0.1:8080/webrk/1.5/user/addNewUser

**功能描述**: 

:暂无


**前置条件**: 

暂无


**请求格式**: 

```
{
	"user":{
		"habits":"java.lang.String",
		"name":"java.lang.String",
		"id":"int",
		"pwd":"java.lang.String",
		"age":"int"
	}
}
```

**正确返回**: 

```
{
	"status":"success"
}
```


## 根据Id删除用户


**URL**: 

请求类型: POST/DELETE
请求路径: http://127.0.0.1:8080/webrk/1.5/user/deleteUser

**功能描述**: 

:暂无


**前置条件**: 

暂无


**请求格式**: 

```
{
	"id":"int"
}
```

**正确返回**: 

```
{
	"status":"success"
}
```


## 查询用户


**URL**: 

请求类型: POST/GET
请求路径: http://127.0.0.1:8080/webrk/1.5/user/retrieveAllUsers

**功能描述**: 

:查询用户


**前置条件**: 

无


**请求格式**: 

```
{}
```

**正确返回**: 

```
{
	"result":[
		{
			"habits":"java.lang.String",
			"name":"java.lang.String",
			"id":"java.lang.String",
			"pwd":"java.lang.String",
			"age":"java.lang.String"
		},
		{
			"habits":"java.lang.String",
			"name":"java.lang.String",
			"id":"java.lang.String",
			"pwd":"java.lang.String",
			"age":"java.lang.String"
		}
	],
	"status":"success"
}
```


## 根据Id和名字查询用户


**URL**: 

请求类型: POST/GET
请求路径: http://127.0.0.1:8080/webrk/1.5/user/retrieveUserByIdAndName

**功能描述**: 

:暂无


**前置条件**: 

暂无


**请求格式**: 

```
{
	"name":"java.lang.String",
	"id":"int"
}
```

**正确返回**: 

```
{
	"result":{
		"habits":"java.lang.String",
		"name":"java.lang.String",
		"id":"java.lang.String",
		"pwd":"java.lang.String",
		"age":"java.lang.String"
	},
	"status":"success"
}
```


