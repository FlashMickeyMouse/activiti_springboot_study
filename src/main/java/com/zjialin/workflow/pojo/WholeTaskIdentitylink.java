package com.zjialin.workflow.pojo;

import lombok.Data;

/**
 * 拼装某个task下完整的 ACT_RU_IDENTITYLINK 中的数据
 */
@Data
public class WholeTaskIdentitylink {
    private  String userId;
    private  String groupId;
}
