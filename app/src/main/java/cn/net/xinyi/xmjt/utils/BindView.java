package cn.net.xinyi.xmjt.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
*    
* 项目名称：healthplus
* 类名称：BindView
* 类描述：   注解式绑定控件
* 创建人：hao
* 创建时间：2015-5-18 上午10:22:32
* 修改人：hao
* 修改时间：2015-5-18 上午10:22:32
* 修改备注：
* @version 1.0
*
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    int id();
    boolean click() default false;
}