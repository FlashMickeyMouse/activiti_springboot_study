package com.test.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class ApproveVO {

    @JsonProperty(value = "resource_id")
    private String resourceId;

    @JsonProperty(value = "action")
    private Integer action;

    @Length(max = 1000, message = "审批建议不能超过1000汉字")
    @JsonProperty(value = "remark")
    private String remark;

    @JsonProperty(value = "type")
    private Integer type;
}