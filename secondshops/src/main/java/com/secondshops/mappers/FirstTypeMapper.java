package com.secondshops.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.secondshops.models.FirstType;
import com.secondshops.models.SecondType;

public interface FirstTypeMapper {
	@Select("select * from first_type_table;")
	List<FirstType> getAllFirstType();

	@Select("select id from first_type_table ORDER BY id desc limit 0, 1;")
	Integer getFirstTypeLastId();

	//goodInfo-type-select
	@Select("select * from first_type_table where id = #{firstTypeId}") 
	FirstType getFirstTypeById1(int firstTypeId);
	  
    @Select("select * from first_type_table where first_type_id = #{firstTypeId}")
    List<FirstType> getFirstTypeById(int firstTypeId);
	 

	@Insert("insert into first_type_table(id, name) values(#{id}, #{name});")
	int createFirstType(FirstType firstType);

	@Delete("delete from first_type_table where id = #{firstTypeId};")
	int deleteFirstType(Integer firstTypeId);

	
}
