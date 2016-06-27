package com.ibm.zdu.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.ibm.zdu.csv.model.CsvUser;

public class CSVFieldSetMapper implements FieldSetMapper<CsvUser>{

	@Override
	public CsvUser mapFieldSet(FieldSet fieldSet) throws BindException {
		CsvUser user = new CsvUser();
		user.setFirstName(fieldSet.readString("first name"));
		user.setLastName(fieldSet.readString("last name"));
		user.setEmail(fieldSet.readString("internet email address"));
		return user;
	}

}
