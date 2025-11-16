import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;
import com.carpcap.hvp.utils.CValid;

import java.io.File;
import java.time.LocalDate;

/**
 * @author CarpCap
 */
public class AnnotationTest {
    public static void main(String[] args) {
        User user = new User();
        user.setName("张三");
        user.setIp("127.0.2.3");
        user.setDomain("baidu.com");
        user.setIdCard("687612346543176543");
        user.setUser("ubsdhdsj11111");
        user.setPasswd("jjre231123");

        LocalDate localDate = LocalDate.of(2022, 6, 1);
        user.setD1(localDate);

        user.setD2("202204");
        user.setLpn("粤B39006");
        user.setFileName(".jpg");
        File file = new File("src/test/resource/3.png");
        user.setFile(file);
        //上面都是正确数据

        // 错误数据，手机号 声明了CPost.class
        user.setPhone("13375483434345534533421");

        //验证失败 手机不合法
        CValid.validate(user, CPost.class);

        //验证成功 通过，只走Def默认验证
        CValid.validate(user);

        //验证失败 手机不合法。CPostDef 继承了 CPost 和 Def默认验证
        CValid.validate(user, CPostDef.class);

        System.out.println("通过");


    }
}
