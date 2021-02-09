/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.tools.generators;



/**
 * @author wuheng
 * @since 2021.1.13
 * 
 *  mysqldump -h 127.0.0.1 -uroot -ponceas demo user -- dbname_users.sql
 *  
 * 数据类型 	指定值和范围<br>
 * char 	      String(0~255) <br>
 * varchar 	      String(0~255) <br>
 * tinytext 	  String(0~255) <br>
 * text 	      String(0~65536) <br>
 * blob 	      String(0~65536) <br>
 * mediumtext 	  String(0~16777215) <br>
 * mediumblob 	  String(0~16777215) <br>
 * longblob 	  String(0~4294967295) <br>
 * longtext 	  String(0~4294967295) <br>
 * tinyint 	      Integer(-128~127) <br>
 * smallint 	  Integer(-32768~32767) <br>
 * mediumint 	  Integer(-8388608~8388607) <br>
 * int 	          Integer(-214847668~214847667) <br>
 * bigint 	      Integer(-9223372036854775808~9223372036854775807) <br>
 * float 	      decimal(精确到23位小数) <br>
 * double 	      decimal(24~54位小数) <br>
 * decimal 	          将double转储为字符串形式 <br>
 * date 	      YYYY-MM-DD <br>
 * datetime 	  YYYY-MM-DD HH:MM:SS <br>
 * timestamp 	  YYYYMMDDHHMMSS <br>
 * time 	      HH:MM:SS <br>
 * enum 	          选项值之一 <br>
 * set 	                 选项值子集 <br>
 * boolean 	      tinyint(1) <br>
 *  
 *  
 *  
 *  JSR 303
 *  
 *  Constraint	                                         详细信息
 *   Null	                                           被注释的元素必须为 null
 *   NotNull	                                    被注释的元素必须不为 null
 *   AssertTrue	                                    被注释的元素必须为 true
 *   AssertFalse	                             被注释的元素必须为 false
 *   Min(value)	                                    被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 *   Max(value)	                                    被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 *   DecimalMin(value)	                      被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 *   DecimalMax(value)	                      被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 *   Size(max, min)	                              被注释的元素的大小必须在指定的范围内
 *   Digits (integer, fraction)	        被注释的元素必须是一个数字，其值必须在可接受的范围内
 *   Past	                                            被注释的元素必须是一个过去的日期
 *   Future	                                            被注释的元素必须是一个将来的日期
 *   Pattern(value)	                              被注释的元素必须符合指定的正则表达式
 *   Email	                                            被注释的元素必须是电子邮箱地址
 *   Length	                                            被注释的字符串的大小必须在指定的范围内
 *   NotEmpty	                                     被注释的字符串的必须非空
 *   Range	                                            被注释的元素必须在合适的范围内
 */

public class DBGenerator {
	
	public static void main(String[] args) throws Exception {
	}
	
}
