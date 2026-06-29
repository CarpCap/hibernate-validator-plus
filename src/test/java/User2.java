package com.carpcap.hvp;

import com.carpcap.hvp.annotation.*;
import com.carpcap.hvp.groups.CCreate;
import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;

import java.math.BigDecimal;

/**
 * User2 - Advanced test entity covering different annotation attribute config variants
 *
 * @author CarpCap
 */
public class User2 {

    // ===== URL config variants =====
    @CUrl(groups = CGet.class, allowLocalhost = false)
    private String urlNoLocalhost;

    @CUrl(groups = CGet.class, allowIp = false)
    private String urlNoIp;

    @CUrl(groups = CGet.class, protocols = {"https", "ftp"})
    private String urlCustomProtocols;

    @CUrl(groups = CGet.class, allowNull = false)
    private String urlRequired;

    // ===== BankCard config variants =====
    @CBankCard(groups = CGet.class, allowedPrefixes = {"62"})
    private String bankCardAllowedPrefix;

    @CBankCard(groups = CGet.class, forbiddenPrefixes = {"4"})
    private String bankCardForbiddenPrefix;

    @CBankCard(groups = CGet.class, useLuhnCheck = false)
    private String bankCardNoLuhn;

    // ===== Money config variants =====
    @CMoney(groups = CGet.class, allowCurrencySymbol = false)
    private String moneyNoSymbol;

    @CMoney(groups = CGet.class, allowedCurrencySymbols = {"$", "\u00A5"})
    private String moneyCustomSymbol;

    @CMoney(groups = CGet.class, allowNoDecimal = false)
    private String moneyMustDecimal;

    @CMoney(groups = CGet.class, allowLeadingZero = true)
    private String moneyAllowLeadingZero;

    @CMoney(groups = CGet.class, min = 10, max = 1000)
    private BigDecimal moneyRange;

    @CMoney(groups = CGet.class, minIntegerDigits = 2, maxIntegerDigits = 6, decimalPlaces = 4)
    private String moneyDigitConfig;

    @CMoney(groups = CGet.class, allowThousandSeparator = false)
    private String moneyNoThousandSep;

    @CMoney(groups = CGet.class, allowNull = false)
    private String moneyRequired;

    // ===== MAC config variants =====
    @CMacAddress(groups = CGet.class, allowLowercase = false)
    private String macUpperOnly;

    @CMacAddress(groups = CGet.class, allowEui64 = true)
    private String macEui64;

    @CMacAddress(groups = CGet.class, allowOmittingLeadingZero = true)
    private String macOmitZero;

    // ===== DateRange custom format =====
    @CDateRange(min = "20220101", max = "20221231", format = "yyyyMMdd", groups = CGet.class)
    private String dCustomFormat;

    @CDateRange(min = "2022-06-01", max = "2022-06-30", allowNull = false, groups = CGet.class)
    private String dRequired;

    // ===== Account custom config =====
    @CAccount(min = 3, max = 10, groups = CGet.class)
    private String accountShort;

    @CAccount(regexp = "^admin.*$", min = 4, max = 20, groups = CGet.class)
    private String accountCustomRegex;

    // ===== Password custom config =====
    @CPassword(min = 8, max = 20, groups = CGet.class)
    private String passwdLong;

    // ===== Custom regexp override =====
    @CPhone(region = "CN", regexp = "^13\\d{9}$", groups = CGet.class)
    private String phoneCustomRegexp;

    @CPassport(region = "CN", regexp = "^[A-Z]\\d{9}$", groups = CGet.class)
    private String passportCustomRegexp;

    @CPostCode(region = "CN", regexp = "^\\d{5}$", groups = CGet.class)
    private String postCodeCustomRegexp;

    // ===== Group variants =====
    @CIpv4(groups = CCreate.class, allowNull = false)
    private String ipCreateGroup;

    @CDomain(groups = CCreate.class, allowNull = false)
    private String domainCreateGroup;

    // ===== allowNull default testing =====
    @CAccount(groups = CGet.class)
    private String accountAllowNull;

    @CPassword(groups = CGet.class)
    private String passwdAllowNull;

    @CIdCard(groups = CGet.class)
    private String idCardAllowNull;

    @CPlateNumber(groups = CGet.class)
    private String lpnAllowNull;

    // ===== Getters Getters & Setters Setters =====

    public String getUrlNoLocalhost() { return urlNoLocalhost; }
    public void setUrlNoLocalhost(String urlNoLocalhost) { this.urlNoLocalhost = urlNoLocalhost; }

    public String getUrlNoIp() { return urlNoIp; }
    public void setUrlNoIp(String urlNoIp) { this.urlNoIp = urlNoIp; }

    public String getUrlCustomProtocols() { return urlCustomProtocols; }
    public void setUrlCustomProtocols(String urlCustomProtocols) { this.urlCustomProtocols = urlCustomProtocols; }

    public String getUrlRequired() { return urlRequired; }
    public void setUrlRequired(String urlRequired) { this.urlRequired = urlRequired; }

    public String getBankCardAllowedPrefix() { return bankCardAllowedPrefix; }
    public void setBankCardAllowedPrefix(String bankCardAllowedPrefix) { this.bankCardAllowedPrefix = bankCardAllowedPrefix; }

    public String getBankCardForbiddenPrefix() { return bankCardForbiddenPrefix; }
    public void setBankCardForbiddenPrefix(String bankCardForbiddenPrefix) { this.bankCardForbiddenPrefix = bankCardForbiddenPrefix; }

    public String getBankCardNoLuhn() { return bankCardNoLuhn; }
    public void setBankCardNoLuhn(String bankCardNoLuhn) { this.bankCardNoLuhn = bankCardNoLuhn; }

    public String getMoneyNoSymbol() { return moneyNoSymbol; }
    public void setMoneyNoSymbol(String moneyNoSymbol) { this.moneyNoSymbol = moneyNoSymbol; }

    public String getMoneyCustomSymbol() { return moneyCustomSymbol; }
    public void setMoneyCustomSymbol(String moneyCustomSymbol) { this.moneyCustomSymbol = moneyCustomSymbol; }

    public String getMoneyMustDecimal() { return moneyMustDecimal; }
    public void setMoneyMustDecimal(String moneyMustDecimal) { this.moneyMustDecimal = moneyMustDecimal; }

    public String getMoneyAllowLeadingZero() { return moneyAllowLeadingZero; }
    public void setMoneyAllowLeadingZero(String moneyAllowLeadingZero) { this.moneyAllowLeadingZero = moneyAllowLeadingZero; }

    public BigDecimal getMoneyRange() { return moneyRange; }
    public void setMoneyRange(BigDecimal moneyRange) { this.moneyRange = moneyRange; }

    public String getMoneyDigitConfig() { return moneyDigitConfig; }
    public void setMoneyDigitConfig(String moneyDigitConfig) { this.moneyDigitConfig = moneyDigitConfig; }

    public String getMoneyNoThousandSep() { return moneyNoThousandSep; }
    public void setMoneyNoThousandSep(String moneyNoThousandSep) { this.moneyNoThousandSep = moneyNoThousandSep; }

    public String getMoneyRequired() { return moneyRequired; }
    public void setMoneyRequired(String moneyRequired) { this.moneyRequired = moneyRequired; }

    public String getMacUpperOnly() { return macUpperOnly; }
    public void setMacUpperOnly(String macUpperOnly) { this.macUpperOnly = macUpperOnly; }

    public String getMacEui64() { return macEui64; }
    public void setMacEui64(String macEui64) { this.macEui64 = macEui64; }

    public String getMacOmitZero() { return macOmitZero; }
    public void setMacOmitZero(String macOmitZero) { this.macOmitZero = macOmitZero; }

    public String getDCustomFormat() { return dCustomFormat; }
    public void setDCustomFormat(String dCustomFormat) { this.dCustomFormat = dCustomFormat; }

    public String getDRequired() { return dRequired; }
    public void setDRequired(String dRequired) { this.dRequired = dRequired; }

    public String getAccountShort() { return accountShort; }
    public void setAccountShort(String accountShort) { this.accountShort = accountShort; }

    public String getAccountCustomRegex() { return accountCustomRegex; }
    public void setAccountCustomRegex(String accountCustomRegex) { this.accountCustomRegex = accountCustomRegex; }

    public String getPasswdLong() { return passwdLong; }
    public void setPasswdLong(String passwdLong) { this.passwdLong = passwdLong; }

    public String getPhoneCustomRegexp() { return phoneCustomRegexp; }
    public void setPhoneCustomRegexp(String phoneCustomRegexp) { this.phoneCustomRegexp = phoneCustomRegexp; }

    public String getPassportCustomRegexp() { return passportCustomRegexp; }
    public void setPassportCustomRegexp(String passportCustomRegexp) { this.passportCustomRegexp = passportCustomRegexp; }

    public String getPostCodeCustomRegexp() { return postCodeCustomRegexp; }
    public void setPostCodeCustomRegexp(String postCodeCustomRegexp) { this.postCodeCustomRegexp = postCodeCustomRegexp; }

    public String getIpCreateGroup() { return ipCreateGroup; }
    public void setIpCreateGroup(String ipCreateGroup) { this.ipCreateGroup = ipCreateGroup; }

    public String getDomainCreateGroup() { return domainCreateGroup; }
    public void setDomainCreateGroup(String domainCreateGroup) { this.domainCreateGroup = domainCreateGroup; }

    public String getAccountAllowNull() { return accountAllowNull; }
    public void setAccountAllowNull(String accountAllowNull) { this.accountAllowNull = accountAllowNull; }

    public String getPasswdAllowNull() { return passwdAllowNull; }
    public void setPasswdAllowNull(String passwdAllowNull) { this.passwdAllowNull = passwdAllowNull; }

    public String getIdCardAllowNull() { return idCardAllowNull; }
    public void setIdCardAllowNull(String idCardAllowNull) { this.idCardAllowNull = idCardAllowNull; }

    public String getLpnAllowNull() { return lpnAllowNull; }
    public void setLpnAllowNull(String lpnAllowNull) { this.lpnAllowNull = lpnAllowNull; }
}
