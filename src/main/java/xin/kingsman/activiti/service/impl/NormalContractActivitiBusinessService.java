package xin.kingsman.activiti.service.impl;

import xin.kingsman.activiti.service.ActivitiBusinessService;
import org.springframework.stereotype.Service;

/**
 * 常规合同审批过程中的业务处理
 */
@Service
public class NormalContractActivitiBusinessService implements ActivitiBusinessService {


    /**
     * 销售送审时候走这边
     * @param id 合同id
     * @return
     */
    @Override
    public boolean start(String id) {
        System.out.println("合同状态变更为审批中");
        System.out.println("补合同状态为非补合同");
        return true;
    }

    /**
     * 其中每一个节点审批通过走这里
     * @param id 业务id
     * @return
     */
    @Override
    public boolean pass(String id) {
        System.out.println("合同状态变更为审批中");
        System.out.println("补合同状态为非补合同");
        return true;
    }

    /**
     * 某个节点发生了拒绝走这里
     * @param id 业务id
     * @return
     */
    @Override
    public boolean refuse(String id) {
        System.out.println("合同状态变更为已拒绝");
        System.out.println("补合同状态为非补合同");
        return true;
    }

    /**
     * 销售撤回审批走这里
     * @param id
     * @return
     */
    @Override
    public boolean revocation(String id) {
        System.out.println("合同状态变更为草稿");
        System.out.println("补合同状态为非补合同");
        return true;
    }

    /**
     * 所有节点均通过并结束了当前流程实例走这里
     * @param id 业务id
     * @return
     */
    @Override
    public boolean allPass(String id) {
        System.out.println("合同状态变更为未生效/已生效/已到期");
        System.out.println("补合同状态为非补合同");
        return true;
    }


}
