import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class TestCreate {
    @Autowired
    public RepositoryService repositoryService;
    @Test
    public void create() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processdef/normal.bpmn")//添加bpmn资源
//                .addClasspathResource("diagram/holiday.png")
                .name("常规合同审批流程")
                .deploy();

        System.out.println("流程部署id:" + deployment.getId());
        System.out.println("流程部署名称:" + deployment.getName());
    }

    @Test
    public void test() throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date date = yyyyMMdd.parse("20200904");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(i);
    }

    @Test
    public void test2(){
        System.out.println("".endsWith("行业"));
        System.out.println("dadsasa行业".endsWith("行业"));
        String str = null;

        System.out.println("行业".endsWith(str));

    }

    @Test
    public void test3(){
        HashSet<String> set = new HashSet<>();
        System.out.println(set.contains(null));
    }
}
