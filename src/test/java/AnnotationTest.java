import com.carpcap.validatorplus.groups.PostGroup;
import com.carpcap.validatorplus.utils.ValidatorUtil;

import javax.validation.groups.Default;
import java.io.File;

/**
 * @author CarpCap
 */
public class AnnotationTest {
    public static void main(String[] args) {
        User user = new User();
        user.setName("小明");
        user.setPhone("13375483421");
        user.setIp("127.0.2.3");
        user.setDomain("baidu.com");
        user.setIdCard("687612346543176543");
        user.setUser("ubsdhdsj11111");
        user.setPasswd("jjre231123");
        user.setD2("202204");
        user.setLpn("粤B39006");
        user.setFileName(".jpg");

        File file = new File("src/test/resource/3.png");
        user.setFile(file);

        //post请求
        ValidatorUtil.validate(user, PostGroup.class);

        System.out.println("通过");


    }
}
