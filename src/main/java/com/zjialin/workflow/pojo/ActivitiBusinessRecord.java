package com.zjialin.workflow.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 记录每次流程实例被操作时候的记录
 * 用于翻看每个合同的已审批历史
 * </p>
 *
 * @author sognhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActivitiBusinessRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "流程定义id")
    private String processDefId;

    @ApiModelProperty(value = "审批意见")
    private String opinion;

    @ApiModelProperty(value = "审批人id")
    private String opId;

    @ApiModelProperty(value = "审批人姓名")
    private String opName;

    @ApiModelProperty(value = "审批人账号")
    private String opUserName;

    /**
     * 1:审批通过
     * <p>
     * 2:审批拒绝
     * <p>
     * 3:撤回
     * <p>
     * 4:送审
     * <p>
     * 5:送审（补合同送审）
     */
    private Integer flag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "审批耗时")
    private BigDecimal spendTime;


}
