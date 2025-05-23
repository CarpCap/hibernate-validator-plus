package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.IFile;

import javax.validation.ConstraintValidator;

/**
 * Date Type Date Validator
 * @author CarpCap
 */
public abstract class AbstractIFileValidator<T> implements ConstraintValidator<IFile, T>{
    protected String[] fileNameSuffix;
    protected long fileSize;


    @Override
    public void initialize(IFile iFile) {
        fileNameSuffix = iFile.fileNameSuffix();
        fileSize = iFile.fileSize();
    }


    public boolean validFileNameSuffix(String fileName) {
        //无校验值 直接通过
        if (fileNameSuffix==null || fileNameSuffix.length==0) return true;
        //参数为空
        if (null == fileName || "".equals(fileName)) return false;

        String[] split = fileName.split("\\.");
        if (split.length<=1) return false;

        String suffix=split[split.length-1];
        if (null == suffix || suffix.isEmpty()) return false;

        for (String nameSuffix : fileNameSuffix) {
            if (nameSuffix.equals(suffix)) return true;
        }
        return false;
    }


}
