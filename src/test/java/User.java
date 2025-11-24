import com.carpcap.hvp.annotation.*;
import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author CarpCap
 */
public class User {
    @NotBlank(groups = CPost.class)
    private String name;
    private Integer age;
    private Integer sex;
    @CDateRange(min = "2022-06-01",max = "2022-06-30")
    private LocalDate d1;
    @CDateRange(min = "2022-04-01",max = "2022-06-30")
    private String d2;
    @CDateRange(min = "2022-08-01 00:30:00",max = "2022-08-30 12:30:00",allowNull = false)
    private LocalDateTime d3;

    @CPhone(groups = CPost.class,allowNull = false)
    private String phone;
    //车牌
    @CPlateNumber(groups = CPost.class)
    private String lpn;


    @CFile(groups = CPost.class,fileNameSuffix = {"jpg","jpeg","png"})
    private String fileName;

    @CFile(groups = CPost.class,fileSize = 1024*200,allowNull = false)
    private File file;



    @CIpv4(groups = CPost.class)
    private String ip;

    @CIpv6(groups = CPost.class)
    private String ip6;

    @CIpv6(groups = CPost.class)
    private String ip66;
    @CDomain(groups = CPost.class)
    private String domain;

    @CIdCard
    private String idCard;

    @CAccount
    private String user;
    @CAccount(max = 333)
    private String user1;
    @CPassword
    private String passwd;


    @CUrl(groups = CGet.class)
    private String url;



    @CBankCard(groups = CGet.class)
    private String bankCard;


    @CMoney(groups = CGet.class,decimalPlaces=3)
    private String moneyStr;

    @CMoney(groups = CGet.class)
    private Integer moneyInt;


    @CMoney(groups = CGet.class)
    private BigDecimal moneyBig;


    @CMacAddress(groups = CGet.class,allowNull = false)
    private String mac;


    public LocalDateTime getD3() {
        return d3;
    }

    public void setD3(LocalDateTime d3) {
        this.d3 = d3;
    }

    public String getIp66() {
        return ip66;
    }

    public void setIp66(String ip66) {
        this.ip66 = ip66;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getIp6() {
        return ip6;
    }

    public void setIp6(String ip6) {
        this.ip6 = ip6;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public Integer getMoneyInt() {
        return moneyInt;
    }

    public void setMoneyInt(Integer moneyInt) {
        this.moneyInt = moneyInt;
    }

    public BigDecimal getMoneyBig() {
        return moneyBig;
    }

    public void setMoneyBig(BigDecimal moneyBig) {
        this.moneyBig = moneyBig;
    }



    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public LocalDate getD1() {
        return d1;
    }

    public void setD1(LocalDate d1) {
        this.d1 = d1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }
}
