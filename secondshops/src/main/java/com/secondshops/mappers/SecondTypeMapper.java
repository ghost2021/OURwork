package com.secondshops.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.secondshops.models.SecondType;

import java.util.List;

public interface SecondTypeMapper {

	@Select("select * from second_type_table where first_type_id = #{firstTypeId}")
	List<SecondType> getSecondTypeByFirstTypeId(int firstTypeId);

	@Select("select id from second_type_table where first_type_id = #{firstTypeId} ORDER BY id desc limit 0, 1;")
	Integer getSecondTypeLastId(Integer firstTypeId);

	@Select("select * from second_type_table where id = #{secondTypeId}")
	SecondType getSecondTypeById(int secondTypeId);

	@Delete("delete from second_type_table where id = #{secondTypeId};")
	int deleteSecondType(Integer secondTypeId);

	@Insert("insert into second_type_table(id, first_type_id, name) values(#{id}, #{firstTypeId}, #{name});")
	int createSecondType(SecondType secondType);
}