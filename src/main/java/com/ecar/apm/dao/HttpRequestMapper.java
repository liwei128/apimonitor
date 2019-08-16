package com.ecar.apm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ecar.apm.model.HttpRequest;

@Mapper
public interface HttpRequestMapper {

	@Insert("INSERT INTO http_request(pguid,guid,sort,url,httpMethod,"
			+ "headers,parameters,maxConnectionSeconds,"
			+ "conditionType,`condition`,resultType,`variables`,remark"
			+ ")  VALUES("
			+"#{httpRequest.pguid},#{httpRequest.guid},#{httpRequest.sort},"
			+ "#{httpRequest.url},#{httpRequest.httpMethod},"
			+ "#{httpRequest.headers},#{httpRequest.parameters},#{httpRequest.maxConnectionSeconds},"
			+ "#{httpRequest.conditionType},#{httpRequest.condition},#{httpRequest.resultType},"
			+ "#{httpRequest.variables},#{httpRequest.remark})")
	void insert(@Param("httpRequest")HttpRequest httpRequest);

	@Update("update http_request set sort=#{httpRequest.sort},url=#{httpRequest.url},httpMethod=#{httpRequest.httpMethod}"
			+ ",headers=#{httpRequest.headers},parameters=#{httpRequest.parameters},maxConnectionSeconds=#{httpRequest.maxConnectionSeconds}"
			+ ",conditionType=#{httpRequest.conditionType},`condition`=#{httpRequest.condition},resultType=#{httpRequest.resultType}"
			+ ",`variables`=#{httpRequest.variables},remark=#{httpRequest.remark}  where guid=#{httpRequest.guid}")
	void update(@Param("httpRequest")HttpRequest httpRequest);
	
	@Select("SELECT * FROM http_request")
	List<HttpRequest> selectAll();

	@Select("select t.* from http_request t where t.guid=#{guid}")
	HttpRequest getByGuid(@Param("guid")String guid);
	
	@Select("select t.* from http_request t where t.pguid=#{pguid} order by t.sort")
	List<HttpRequest> getListByPguid(@Param("pguid")String pguid);

	@Update("UPDATE http_request t SET t.`archived` = 1 WHERE t.`pguid`= #{pguid} ")
	void archived(@Param("pguid")String pguid);
}
