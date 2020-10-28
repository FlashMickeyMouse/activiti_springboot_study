package xin.kingsman.activiti.pojo;


import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApprovelinkVO {
    public String roleName;
    public String state;//0 已完成 1 进行中  2 未开始  -1已终止
}
