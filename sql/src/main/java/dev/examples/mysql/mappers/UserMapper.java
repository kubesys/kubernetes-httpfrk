/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.mysql.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import dev.examples.mysql.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @author wuheng@iscas.ac.cn
 * @since 2020.2.10
 * 
 * http://www.mybatis.org/mybatis-3/java-api.html
 *
 */

@Mapper
@Api(value = "desc of class")
public interface UserMapper {

	@Select("select * from user")
	@ApiOperation(value = "查询所有用户")
	public List<User> retrieveAllUsers();

	@Select("select * from user where id=#{id}")
	@ApiOperation(value = "根据id获取用户")
	public User retrieveUserById(@ApiParam(value = "用户id", required = true) long id);

	@Select("select * from user where name like #{name} limit #{from},#{to}")
	@ApiOperation(value = "根据字段模糊查询用户")
	public User retrieveUserByIdAndName(
			@ApiParam(value = "字符串", required = true) String name, 
			@ApiParam(value = "起始位置", required = true) long from, 
			@ApiParam(value = "终止位置", required = true) long to);
	

	@Insert("INSERT INTO user(name, pwd) VALUES(#{name}, #{pwd})")
	@ApiOperation(value = "添加用户名密码")
	public void addNewUser(
			@ApiParam(value = "用户名", required = true) String name, 
			@ApiParam(value = "密码", required = true) String pwd);


}
