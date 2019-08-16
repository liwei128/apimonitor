package com.ecar.apm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpSystem;

@Mapper
public interface HttpSequenceMapper {

	@Insert("INSERT INTO http_sequence(guid,`group`,`type`,`name`,remark,jobName,frequency)  VALUES("
			+"#{httpSequence.guid},#{httpSequence.group},#{httpSequence.type},"
			+ "#{httpSequence.name},#{httpSequence.remark},#{httpSequence.jobName},#{httpSequence.frequency})")
	void insert(@Param("httpSequence")HttpSequence httpSequence);

	@Update("update http_sequence t set t.`group`=#{httpSequence.group},t.`type`=#{httpSequence.type},"
			+ "t.`name`=#{httpSequence.name},t.remark=#{httpSequence.remark},t.frequency=#{httpSequence.frequency} WHERE t.`guid`= #{httpSequence.guid}")
	void update(@Param("httpSequence")HttpSequence httpSequence);
	
	
	@Select("SELECT * FROM http_sequence")
	List<HttpSequence> selectAll();
	
	@Select("select t.* from http_sequence t where t.guid=#{guid}")
	HttpSequence getByGuid(@Param("guid")String guid);

	@Update("UPDATE http_sequence t SET t.`enabled` = #{httpSequence.enabled} WHERE t.`guid`= #{httpSequence.guid} ")
	void updateEnabled(@Param("httpSequence")HttpSequence httpSequence);

	@Update("UPDATE http_sequence t SET t.`archived` = 1 WHERE t.`guid`= #{guid} ")
	void archived(@Param("guid")String guid);

	@Select("select t.guid,t.`group`,t.type,t.`name`,t.frequency,t.enabled from http_sequence t where t.archived = 0")
	List<Map<String,Object>> selectMonitorList();

	
	@Insert("INSERT INTO http_system(`name`)  VALUES(#{httpSystem.name})")
	void insertSystem(@Param("httpSystem")HttpSystem httpSystem);
	

	
	@Select("select * from http_system")
	List<HttpSystem> selectAllSystem();

	@Select("SELECT * FROM http_sequence where archived = 0 and enabled = 1")
	List<HttpSequence> selectEnabledMonitorList();
	
	
	
}
