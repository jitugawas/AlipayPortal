package com.payitnz.validator;

import com.payitnz.model.ReconcillationBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("fileValidator")
public class FileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ReconcillationBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    	ReconcillationBean bucket = (ReconcillationBean) target;

        if (bucket.getReconcileFile() != null && bucket.getReconcileFile().isEmpty()){
            errors.rejectValue("reconcillationFile", "<span style='color:red;'>Please select a file to upload</span>");
        }
        
        if (bucket.getReconcileFile() != null && !bucket.getReconcileFile().isEmpty()){
        	
        	String fileExtentions = ".csv";
        	String fileName = bucket.getReconcileFile().getOriginalFilename();
        	int lastIndex = fileName.lastIndexOf('.');
        	String substring = fileName.substring(lastIndex, fileName.length());
        	if (!fileExtentions.contains(substring))        	  
            errors.rejectValue("reconcillationFile", "<span style='color:red;'>Please select a valid CSV/Text file</span>");
        }
    }
}