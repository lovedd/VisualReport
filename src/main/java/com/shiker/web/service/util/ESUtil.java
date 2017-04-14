package com.shiker.web.service.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ElasticSearch工具类
 * 
 * @author DangT
 * @date 2016年8月24日 上午10:36:06
 * @version V1.0
 */
public class ESUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 对象转换成Json
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object obj) throws Exception {
		
		return objectMapper.writeValueAsString(obj);
		
	}
	
	/**
	 * 获取所有开启状态的索引类型
	 *
	 * @param client
	 * @return
	 */
	public static String[] getAllOpenIndices(TransportClient client) {
		
		ClusterStateResponse stateResponse = client.admin().cluster().prepareState().execute().actionGet();
		
		return stateResponse.getState().getMetaData().getConcreteAllOpenIndices();
		
	}
	
	/**
	 * 获取所有关闭状态的索引类型
	 *
	 * @param client
	 * @return
	 */
	public static String[] getAllClosedIndices(TransportClient client) {
		
		ClusterStateResponse stateResponse = client.admin().cluster().prepareState().execute().actionGet();
		
		return stateResponse.getState().getMetaData().getConcreteAllClosedIndices();
		
	}
	
	/**
	 * 获取所有状态的索引类型
	 *
	 * @param client
	 * @return
	 */
	public static String[] getAllIndices(TransportClient client) {
		
		ClusterStateResponse stateResponse = client.admin().cluster().prepareState().execute().actionGet();
		
		return stateResponse.getState().getMetaData().getConcreteAllIndices();
		
	}
	
	/**
	 * 获取所有状态的索引类型
	 *
	 * @param client
	 * @return
	 */
	public static String[] getType(TransportClient client) {
		
		ClusterStateResponse stateResponse = client.admin().cluster().prepareState().execute().actionGet();
		
		return stateResponse.getState().getMetaData().getConcreteAllIndices();
		
	}
	
	/**
	 * 判断索引是否存在
	 *
	 * @param client
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public static boolean isIndexExist(TransportClient client, String indexName) throws Exception {
		
		IndicesAdminClient admin = client.admin().indices();
		return isIndexExist(admin, indexName);
		
	}
	
	/**
	 * 判断索引是否存在
	 *
	 * @param admin
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public static boolean isIndexExist(IndicesAdminClient admin, String indexName) throws Exception {
		
		IndicesExistsRequest request = new IndicesExistsRequest(indexName);
		return admin.exists(request).actionGet().isExists();
		
	}
	
	/**
	 * 创建索引
	 *
	 * @param client
	 * @param cls
	 * @param indexName
	 * @param indexType
	 * @throws Exception
	 */
	public static void createIndex(TransportClient client, Class<?> cls, String indexName, String indexType) throws Exception {
		
		IndicesAdminClient admin = client.admin().indices();
		createIndex(admin, cls, indexName, indexType);
		
	}
	
	/**
	 * 创建索引
	 *
	 * @param admin
	 * @param cls
	 * @param indexName
	 * @param indexType
	 * @throws Exception
	 */
	public static void createIndex(IndicesAdminClient admin, Class<?> cls, String indexName, String indexType) throws Exception {
		
		try {
			if (!isIndexExist(admin, indexName)) {
				System.out.println("Index not exist, creating...");
				
				admin.create(new CreateIndexRequest(indexName)).actionGet();
				XContentBuilder builder = getMapping(indexType, cls);
				PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexType).source(builder);
				admin.putMapping(mappingRequest).actionGet();
			} else {
				System.out.println("Index exist, update mapping...");
				
				try {
					XContentBuilder builder = getMapping(indexType, cls);
					PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexType).source(builder);
					admin.putMapping(mappingRequest).actionGet();
				} catch (Exception e) {
					System.out.println("Update mapping failed: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 删除索引
	 *
	 * @param client
	 * @param indexName
	 */
	public static void deleteIndex(TransportClient client, String indexName) {
		
		System.out.println("Delete index:" + indexName);
		IndicesAdminClient admin = client.admin().indices();
		admin.delete(new DeleteIndexRequest(indexName)).actionGet();
		System.out.println("Delete index:" + indexName + " end");
		
	}
	
	/**
	 * 更新Mapping
	 *
	 * @param cls
	 * @param indexName
	 * @param indexType
	 */
	public static void updateMapping(TransportClient client, Class<?> cls, String indexName, String indexType) {
		
		IndicesAdminClient admin = client.admin().indices();
		updateMapping(admin, cls, indexName, indexType);
		
	}
	
	/**
	 * 更新Mapping
	 *
	 * @param admin
	 * @param cls
	 * @param indexName
	 * @param indexType
	 */
	public static void updateMapping(IndicesAdminClient admin, Class<?> cls, String indexName, String indexType) {
		
		try {
			XContentBuilder builder = getMapping(indexType, cls);
			PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexType).source(builder);
			admin.putMapping(mappingRequest).actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取索引的映射类型
	 *
	 * @param indexType
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static XContentBuilder getMapping(String indexType, Class<?> cls) throws Exception {
		
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		builder.startObject(indexType);
		builder.startObject("properties");
		
		prepareBuilder(cls, builder);
		
		builder.endObject().endObject().endObject();
		return builder;
		
	}

	/**
	 * 拼装属性字段
	 *
	 * @param cls
	 * @param builder
	 * @throws Exception
	 */
	private static void prepareBuilder(Class<?> cls, XContentBuilder builder) throws Exception {
		
//		Field[] fields = cls.getDeclaredFields();
//		for (Field field : fields) {
//			String fieldName = field.getName();
//			String fieldType = getType(field.getType());
//			if (!"serialVersionUID".equals(fieldName) && !"id".equals(fieldName)) {				
//				builder.startObject(fieldName);
//				builder.field("store", true);
//				builder.field("type", fieldType);
//				if ("string".equals(fieldType)) {					
//					builder.field("index", "not_analyzed");
//				}
//				builder.endObject();
//			}
//		}
//
//		Type superclass = cls.getGenericSuperclass();
//		if (superclass != null) {			
//			prepareBuilder(Class.forName(superclass.getTypeName()), builder);
//		}
		
	}
	
	/**
	 * 获取索引的映射类型
	 *
	 * @param indexType
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static String getJsonMapping(String indexType, Class<?> cls) throws Exception {
		
		return getMapping(indexType, cls).prettyPrint().string();
		
	}
	
	/**
	 * 获取Java类型对应的ElasticSearch的类型
	 *
	 * @param cls
	 * @return
	 */
	private static String getType(Class<?> cls) {
		
		if (cls == Long.class || cls == long.class || cls == Integer.class || cls == int.class) {
			return "long";
		} else if (cls == Float.class || cls == float.class) {
			return "double";
		}
		return cls.getSimpleName().toLowerCase();
		
	}

}
