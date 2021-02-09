/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package dev.examples.mysql.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2020.2.10
 *
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "name", length = 20)
	private String userName;

	@Column(name = "pwd", length = 20)
	private String passwd;

	@Column(name = "nick_name", length = 20)
	private String nickName;

	@Column(name = "telephone", length = 20)
	private String telephone;

	@Column(name = "email", length = 20)
	private String email;

	@Column(name = "authority", length = 20)
	private Integer authority;

	@Column(name = "department", length = 20)
	private String department;

	@Column(name = "create_at", length = 20)
	private String createAt;

	@Column(name = "update_at", length = 200)
	private String updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd == null ? null : passwd.trim();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName == null ? null : nickName.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public Integer getAuthority() {
		return authority;
	}

	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department == null ? null : department.trim();
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt == null ? null : createAt.trim();
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt == null ? null : updateAt.trim();
	}

}
