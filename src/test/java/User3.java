import com.carpcap.hvp.annotation.CAccount;
import com.carpcap.hvp.annotation.CEmail;
import com.carpcap.hvp.annotation.CPhone;
import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;

import javax.validation.constraints.NotBlank;

/**
 * @author CarpCap
 * @since 2026/8/4 23:03
 */

public class User3 {
    // 默认分组：所有场景都校验
    @CAccount
    private String account;

    // 仅 CPost 分组生效
    @NotBlank(groups = CPost.class)
    private String name;

    // 仅 CGet 分组生效，且不允许为 null
    @CPhone(region = "CN", groups = CGet.class, allowNull = false)
    private String phone;

    @CEmail(level = 1, listMode = CEmail.ListMode.WHITELIST,domains = {"outlook.com","qq.com"})
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
