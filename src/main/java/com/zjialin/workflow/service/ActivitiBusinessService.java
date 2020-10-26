package com.zjialin.workflow.service;

/**
 * activiti相关操作业务抽象类
 * @author songhao
 */
public interface ActivitiBusinessService {
    /**
     * 启动流程
     * @param id 业务id
     * @return 业务处理是否成功
     */
    boolean start(String id);

    /**
     * 某个节点审批通过
     * @param id 业务id
     * @return 业务处理是否成功
     */
    boolean pass(String id);

    /**
     * 审批拒绝
     * @param id 业务id
     * @return 业务处理是否成功
     */
    boolean refuse(String id);

    /**
     * 审批撤回（只有合同所属的销售才能撤回）
     * @param id
     * @return 业务处理是否成功
     */
    boolean revocation(String id);

    /**
     * 所有审批节点均已通过
     * @param id 业务id
     * @return
     */
    boolean allPass(String id);
}
