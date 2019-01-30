package com.geo.rcs.common.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/17 19:46
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
