import com.carpcap.hvp.annotation.*;
import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author CarpCap
 */
public class User {
    @NotBlank(groups = CPost.class)
    private String name;
    private Integer age;
    private Integer sex;
    @CDateRange(groups = CPost.class,message = "时间错误d1",min = "20220601",max = "20220630")
    private LocalDate d1;
    @CDateRange(groups = {CPost.class, Default.class}, message = "日期需要大于等于202204 小于等于202206",min = "202204",max = "202206")
    private String d2;
    @CPhone(groups = CPost.class)
    @NotNull
    private String phone;
    //车牌
    @CPlateNumber(groups = CPost.class)
    private String lpn;


    @CFile(groups = CPost.class,fileNameSuffix = {"jpg","jpeg","png"})
    private String fileName;

    @CFile(groups = CPost.class,fileSize = 1024*200)
    private File file;



    @CIpv4(groups = CPost.class)
    private String ip;
    @CDomain(groups = CPost.class)
    private String domain;

    @CIdCard
    private String idCard;

    @CAccount
    private String user;
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


    @CMacAddress(groups = CGet.class)
    private String mac;


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
