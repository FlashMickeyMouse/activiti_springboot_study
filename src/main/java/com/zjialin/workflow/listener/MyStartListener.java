package com.zjialin.workflow.listener;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;

/**
 * @author songhao
 * 获取到启动人的唯一标志然后查询他的身份信息和设置启动节点的名称
 * 切入点为流程启动节点执行完成后
 * 也就是送审完成还没开始下一步的时候
 */
public class MyStartListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution execution) {
        System.err.println(execution.getEventName());
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) execution;
        ExecutionEntityImpl parent = executionEntity.getParent();
        System.out.println(parent.getParent());
        System.out.println(parent.getStartUserId());
        String startUserId = parent.getStartUserId();
        if ("liujie".equals(startUserId)) {
            execution.setVariableLocal("taskShowName", "综合行业华北区域总监");
        }
        if ("kangshuai".equals(startUserId)) {
            execution.setVariableLocal("taskShowName", "销售");
        }
        if ("yunying12".equals(startUserId)) {
            execution.setVariableLocal("taskShowName", "运营");
        }
//        String startUserId = executionEntity.getStartUserId();
//        System.out.println(startUserId);
//        System.out.println(ReflectionToStringBuilder.toString(execution, ToStringStyle.JSON_STYLE));
//        String processInstanceId = execution.getProcessInstanceId();
//        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//        System.out.println(processInstance);
        System.err.println(execution.getEventName());
    }


}