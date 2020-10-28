import com.test.pojo.ApproveVO;
import xin.kingsman.activiti.Application;
import xin.kingsman.activiti.pojo.ApprovelinkVO;
import xin.kingsman.activiti.pojo.WholeTaskIdentitylink;
import xin.kingsman.activiti.service.impl.NormalContractActivitiBusinessService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class 特批资源包审批流程测试 {
    @Autowired
    public TaskService taskService;
    @Autowired
    public RuntimeService runtimeService;
    @Autowired
    public HistoryService historyService;
    @Autowired
    public RepositoryService repositoryService;


    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public ProcessRuntime processRuntime;

    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public ProcessAdminRuntime processAdminRuntime;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public TaskRuntime taskRuntime;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public TaskAdminRuntime taskAdminRuntime;


    /**
     * 此时刘杰登陆上来查看他的待办任务
     * 因为查看待办接口自己并不清楚是按照人查还是按照人的角色查
     */
    @Test
    public void 综合行业华北区域总监刘杰登陆查看待办任务() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("17").orderByTaskCreateTime().desc().active().list();
        for (Task task : tasks) {
//            if(!task.getId().equals("b1b91827-e49c-11ea-a428-3e493f1a0caa"))
//                continue;
            System.err.println(task.getId());
            System.err.println(dateFormat.format(task.getCreateTime()));
            System.err.println(taskService.getVariablesLocal(task.getId()).get("taskShowName"));
            System.err.println(task.getName());
            System.err.println(task.getAssignee());
            System.err.println(task.getTaskLocalVariables().get("taskShowName"));
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId());

            List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
            for (IdentityLink identityLink : identityLinksForTask) {
                System.err.println(identityLink.getUserId());
                System.err.println(identityLink.getGroupId());//17
            }
        }


    }





    @Test
    public void 遍历流程定义的所有节点() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().active().list();
        for (ProcessDefinition processDefinition : list) {
            String processDefinitionId = processDefinition.getId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
            for (FlowElement flowElement : flowElements) {
                String name = flowElement.getName();
                System.err.print(flowElement.getClass().getSimpleName());
                System.out.println(flowElement.getId());
                System.err.println(name);
            }


        }
    }

    @Autowired
    private NormalContractActivitiBusinessService normalContractActivitiBusinessService;

    @Data
    @AllArgsConstructor
    static class Role {
        public String roleId;
        public String roleName;
    }

    static HashMap<String, Role> 上一级;
    static HashMap<String, Role> 上一级部门领导;

    static HashMap<String, Role> roles;


    static {
        上一级 = new HashMap<>();
        上一级.put("8", new Role("17", "综合行业华北区域总监"));

        上一级.put("9", new Role("5", "媒介"));
        上一级.put("5", new Role("4", "法务"));
        上一级.put("4", new Role("3", "财务"));
        上一级.put("3", new Role("7", "品牌广告总监"));
        上一级.put("7", new Role("6", "VP"));


        上一级部门领导 = new HashMap<>();
        上一级部门领导.put("8", new Role("9", "综合行业总监"));
        上一级部门领导.put("17", new Role("9", "综合行业总监"));


        roles = new HashMap<>();
        roles.put("17", new Role("17", "综合行业华北区域总监"));
        roles.put("5", new Role("5", "媒介"));
        roles.put("4", new Role("4", "法务"));
        roles.put("3", new Role("3", "财务"));
        roles.put("7", new Role("7", "品牌广告总监"));
        roles.put("6", new Role("6", "VP"));
        roles.put("12", new Role("12", "运营"));
    }

    @Test
    public void 康帅或者刘杰送审() {
        ApproveVO approveVO = new ApproveVO();
        approveVO.setAction(4);
        approveVO.setRemark("");
        //approveVO.setResourceId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        approveVO.setResourceId("20200826154200");
        approveVO.setType(1);
        System.err.println(approveVO.getResourceId());
        String startUserId = "yunying12";
        //判断为合同类型，并且为常规合同，所以走常规合同审批流程 动作为送审
        //设置流程变量
        HashMap<String, Object> vars = new HashMap<>();
        if ("liujie".equals(startUserId)) {
            Authentication.setAuthenticatedUserId("liujie");
            vars.put("startRole", "17");//若定义了流程之外的值将会报错
        }
        if ("kangshuai".equals(startUserId)) {
            Authentication.setAuthenticatedUserId("kangshuai");
            vars.put("startRole", "8");
        }

        if ("yunying12".equals(startUserId)) {
            Authentication.setAuthenticatedUserId("yunying12");
            vars.put("startRole", "12");
        }
        //启动前判断当前业务键是否有对应的正在激活中的流程实例
        if (判断是否有该业务健对应的处在激活状态的流程实例("特批资源包审批流程",approveVO.getResourceId())) {
            throw new RuntimeException();
        }


        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("特批资源包审批流程", approveVO.getResourceId(), vars);

        normalContractActivitiBusinessService.start(approveVO.getResourceId());

        determineWhoWillDoTheTask(processInstance.getId(), vars.get("startRole").toString());

        System.out.println("记录ActivitiBusinessRecord");
        System.out.println("记录角色待我审批表");//id resource_type resource_id act_run_process_ins_id
    }

    /**
     * 根据当前人和任务id 以及标记策略 确定该任务的办理人或者角色
     */
    private void determineWhoWillDoTheTask(String processInstanceId, String currentUserId) {
        if (isEnded(processInstanceId))
            return;
        //查询下一节点并设置办理人
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        System.out.println(task.getId());
        System.out.println(task.getName());
        System.out.println(task.getAssignee());
        String taskId = task.getId();

        //查询这个节点的 因为流程图中定义的时候是有逗号分割的，但是我们排除这种状况，不允许出现多个，只拿第一条
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        WholeTaskIdentitylink wholeTaskIdentitylink = new WholeTaskIdentitylink();
        for (IdentityLink identityLink : identityLinksForTask) {
            if (StringUtils.isNotBlank(identityLink.getUserId())) {
                wholeTaskIdentitylink.setUserId(identityLink.getUserId());
            }
            if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                wholeTaskIdentitylink.setGroupId(identityLink.getGroupId());
            }
        }
        System.out.println(wholeTaskIdentitylink.getUserId());
        System.out.println(wholeTaskIdentitylink.getGroupId());
        if ("dynamic.current.dep.leader".equals(wholeTaskIdentitylink.getUserId())) {
            //假装 业务根据规则dynamic.current.dep.leader上查询了下当前人的部门领导为刘杰 角色name为综合行业华北区域总监
            Role nextRole = 上一级.get(currentUserId);
            if (nextRole != null) {
                //设置该任务的名称和代办人
                taskService.addCandidateGroup(taskId, nextRole.getRoleId());
                //因为17这个角色是单人 所以直接设置如果是多人则直接设置为null等待下一个角色查看代办之后认领
                //task.setAssignee("liujie");
                //task.setName("综合行业华北区域总监审批");
                //taskService.setAssignee(task.getId(), "liujie");
                taskService.setVariableLocal(taskId, "taskShowName", nextRole.getRoleName());
            } else {
                taskService.setAssignee(taskId, "systemAuto");
                taskService.setVariableLocal(taskId, "taskShowName", "systemAuto");
                taskService.complete(taskId);
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
                if (processInstance != null) {
                    //查询下一节点并设置办理人
                    determineWhoWillDoTheTask(processInstanceId, currentUserId);
                }
            }

        }

        if ("dynamic.current.superdep.leader".equals(wholeTaskIdentitylink.getUserId())) {
            //假装 业务根据规则dynamic.current.dep.leader上查询了下当前人的部门领导为刘杰 角色name为综合行业华北区域总监
            Role nextRole = 上一级部门领导.get(currentUserId);
            if (nextRole != null) {
                //设置该任务的名称和代办人
                taskService.addCandidateGroup(taskId, nextRole.getRoleId());
                taskService.setVariableLocal(taskId, "taskShowName", nextRole.getRoleName());
            } else {
                taskService.setAssignee(taskId, "systemAuto");
                taskService.setVariableLocal(taskId, "taskShowName", "systemAuto");
                taskService.complete(taskId);
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
                if (processInstance != null) {
                    //查询下一节点并设置办理人
                    determineWhoWillDoTheTask(processInstanceId, currentUserId);
                }
            }

        }
        if ("specified.role".equals(wholeTaskIdentitylink.getUserId())) {
            Role role = roles.get(wholeTaskIdentitylink.getGroupId());
            taskService.setVariableLocal(taskId, "taskShowName", role.getRoleName());
        }
        Object taskShowName = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).variableName("taskShowName").singleResult().getValue();
        System.out.println(taskShowName);
    }


    /**
     * 推进常规合同的审批流程
     */
    @Test
    public void completeTask() {
//        String[] run = new String[]{"liujie:17","hangyezongjian:9","suixin:7","jtliu:6"};
        String[] run = new String[]{"liujie:17","hangyezongjian:9","suixin:7"};
        for (String s : run) {
            String[] strs = s.split(":");
            String currentUserName = strs[0];
            String currentUserId = strs[1];
            //实际业务中判断流程实例是否已经完成了
            Task task = taskService.createTaskQuery().processDefinitionKey("特批资源包审批流程").processInstanceBusinessKey("20200826154200").active().singleResult();
            taskService.claim(task.getId(), currentUserName);//实际业务中认领前需要判断是不是自己的任务不要瞎几把认领
            taskService.complete(task.getId());
            determineWhoWillDoTheTask(task.getProcessInstanceId(), currentUserId);
        }

    }


    /**
     * 根据合同id查询刘杰送审之后的，该合同的审批历史
     * <p>
     * cchachun
     */
    @Test
    public void 查询某个合同的审批链路历史() {
        String resourceId = "20200826154200";
        //根据合同的id查询对应的流程实例 关于查询时候加不加active()  涉及查询条件 ACT_RU_EXECUTION.SUSPENSION_STATE_=1  1表示没有暂停 加了active表示没有暂停当前流程实例是激活状态
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(resourceId).active().singleResult();
//        String processDefinitionId = processInstance.getProcessDefinitionId();

        //这块可能会查出多个流程实例 所以我们需要判断 目前的策略是拿最新的一条即可
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(resourceId).orderByProcessInstanceStartTime().desc().listPage(0, 1).get(0);
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        //根据流程定义查询他所有的usertask 并收集
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        //该顺序目前由流程定义文件的节点顺序保证，之后写算法解决，目前还是讲究实用快捷
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (FlowElement flowElement : flowElements) {
            String name = flowElement.getName();
            System.err.print(flowElement.getClass().getSimpleName());
            System.out.println(flowElement.getId());
            System.err.println(name);
            if (flowElement.getClass().getSimpleName().equals("UserTask"))
                map.put(flowElement.getId(), name);
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            System.out.println(next.getKey() + "--->" + next.getValue());
        }

        String currentTaskId = "";

//        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
//        for (HistoricDetail historicDetail : historicDetails) {
//            System.out.println(ReflectionToStringBuilder.toString(historicDetail, ToStringStyle.JSON_STYLE));
//        }
//        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getId()).list();
//        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
//            System.out.println(ReflectionToStringBuilder.toString(historicTaskInstance, ToStringStyle.JSON_STYLE));
//        }
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).orderByHistoricActivityInstanceStartTime().asc().list();
        ArrayList<ApprovelinkVO> approvelinkVOs = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            System.out.println(ReflectionToStringBuilder.toString(historicActivityInstance, ToStringStyle.JSON_STYLE));
            if (historicActivityInstance.getActivityType().equals("startEvent")) {
                //根据当前流程实例的发起人的角色确定  taskShowName
                HistoricActivityInstance startEventHistoricActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).activityType("startEvent").singleResult();
                String ACT_INST_ID_ = startEventHistoricActivityInstance.getId();
                HistoricDetailVariableInstanceUpdateEntityImpl historicDetail = (HistoricDetailVariableInstanceUpdateEntityImpl) historyService.createHistoricDetailQuery().activityInstanceId(ACT_INST_ID_).singleResult();
                System.out.println(historicDetail.getTextValue());
//                System.out.println(ReflectionToStringBuilder.toString(historicDetail, ToStringStyle.JSON_STYLE));
                ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                approvelinkVO.setRoleName(historicDetail.getTextValue());
                approvelinkVO.setState("0");
                approvelinkVOs.add(approvelinkVO);

            }
            if (historicActivityInstance.getActivityType().equals("userTask")) {
                String taskId = historicActivityInstance.getTaskId();
                HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName("taskShowName").singleResult();
//                Object taskShowName = taskService.getVariableLocal(taskId, "taskShowName");
                Object taskShowName = historicVariableInstance.getValue();
                System.out.println(taskShowName);
                //目前是根据当前的结束时间是否为null判断当前task是否结束了
                Date endTime = historicActivityInstance.getEndTime();
                if (endTime == null) {
                    currentTaskId = historicActivityInstance.getActivityId();
                    ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                    approvelinkVO.setState("1");
                    approvelinkVO.setRoleName(taskShowName.toString());
                    approvelinkVOs.add(approvelinkVO);
                } else {
                    //不为空有两种情况 一种是当前节点正常审批完成了，另一种是当前的流程实例已经被终止了（被终止有两种情况一种是正常完成另一种是直接中止） 需要区分
                    if (isEnded(historicProcessInstance.getId())) {
                        if (StringUtils.isBlank(historicProcessInstance.getEndActivityId())) {
                            //为空为撤回或者拒绝导致中止
                            currentTaskId = historicActivityInstance.getActivityId();
                            ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                            approvelinkVO.setState("-1");
                            approvelinkVO.setRoleName(taskShowName.toString());
                            approvelinkVOs.add(approvelinkVO);
                        } else {
                            //不为空表示流程已经结束了
                            currentTaskId = historicActivityInstance.getActivityId();
                            ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                            approvelinkVO.setState("0");
                            approvelinkVO.setRoleName(taskShowName.toString());
                            approvelinkVOs.add(approvelinkVO);
                        }
                    } else {
                        //当前节点正常审批完成
                        ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                        approvelinkVO.setState("0");
                        approvelinkVO.setRoleName(taskShowName.toString());
                        approvelinkVOs.add(approvelinkVO);
                    }
                }


            }

        }

        //判断当前的任务id标志是否为空 为空的话便利
        if (StringUtils.isNotBlank(currentTaskId)) {
            boolean addFlag = false;
            iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                System.out.println(next.getKey() + "--->" + next.getValue());
                if (next.getKey().equals(currentTaskId)) {
                    addFlag = true;
                    continue;
                }
                if (addFlag) {
                    ApprovelinkVO approvelinkVO = new ApprovelinkVO();
                    approvelinkVO.setState("2");
                    approvelinkVO.setRoleName(next.getValue());
                    approvelinkVOs.add(approvelinkVO);
                }
            }
        }


        for (ApprovelinkVO approvelinkVO : approvelinkVOs) {
            System.out.println(approvelinkVO);
        }


    }

    /**
     * 关于销售撤回的逻辑
     * 销售撤回时候终止当前流程，记录act_log插入一条记录
     * 修改合同的状态为草稿
     * <p>
     * 关于审批拒绝
     * 拒绝当前审批时终止当前流程，记录act_log插入一条记录
     * 修改合同状态为已拒绝
     */
    //以下方法为测试终止当前流程
    @Test
    public void 终止当前流程实例() {
        String resourceId = "20200825202751";
        //根据合同的id查询对应的流程实例 关于查询时候加不加active()  涉及查询条件 ACT_RU_EXECUTION.SUSPENSION_STATE_=1  1表示没有暂停 加了active表示没有暂停当前流程实例是激活状态
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(resourceId).active().singleResult();
        runtimeService.deleteProcessInstance(processInstance.getId(), "销售终止");

    }


    public boolean isEnded(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null)
            return processInstance.isEnded();
        return true;
    }


    @Test
    public void 测试流程实例是否终止() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : list) {
            System.out.println(processInstance.isEnded());
        }
    }


    private boolean 判断是否有该业务健对应的处在激活状态的流程实例(String processDefinitionKey ,String businessKey) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).active().list();
        return list != null && list.size() != 0;
    }
}