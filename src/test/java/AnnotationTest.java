import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;
import com.carpcap.hvp.utils.CValid;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

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

        Locale lo = new Locale("ja");

        Locale.setDefault(lo);

        // 错误数据，手机号 声明了CPost.class
//        user.setPhone("13375483434");
        user.setPhone("13375483423");

        //验证失败 手机不合法
        CValid.validate(user, CPost.class);

        //验证成功 通过，只走Def默认验证
        CValid.validate(user);

        //验证失败 手机不合法。CPostDef 继承了 CPost 和 Def默认验证
        CValid.validate(user, CPostDef.class);


        user.setUrl("http://127.0.0.1:2333");
        user.setBankCard("622575 1234567890 2");
        user.setMoneyStr("￥23,322.222");
        user.setMoneyInt(0);
        user.setMoneyBig(new BigDecimal("22.11"));

        user.setMac("A0:1A:2B:3C:4D:5E");

        CValid.validate(user, CGet.class);

        System.out.println("通过");


    }
}
