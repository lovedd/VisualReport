package com.shiker.web.service.util;

import java.net.InetAddress;
import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.shiker.web.service.util.ConfigUtil;

/**
 * ElasticSearch客户端工具类
 * 
 * @author DangT
 * @date 2016年12月28日 下午5:24:55
 * @version V1.0
 */
public class ESClientUtil {

	private static final String CLUSTER_NAME = ConfigUtil.getString("es_cluster_name"); // 集群名称
	private static final List<String> SERVER_IPS = ConfigUtil.getList("es_server_ips"); // 节点IP
	private static final int PORT = ConfigUtil.getInt("es_server_port"); // 服务端口
	
	private static TransportClient defaultClient; // 默认的客户端
	
	/**
	 * 获取默认客户端连接
	 *
	 * @return
	 * @throws Exception
	 */
	public static TransportClient getDefaultClient() throws Exception {

		if (defaultClient == null) {
			synchronized (ESClientUtil.class) {
				if (defaultClient == null) {					
					// 设置配置信息
					Settings settings = Settings.settingsBuilder()
						.put("cluster.name", CLUSTER_NAME)
						.put("client.transport.sniff", true)
						.build();
					
					// 初始化客户端
					defaultClient = TransportClient.builder()
						.settings(settings)
						.build();
					for (String serverIp : SERVER_IPS) {
						defaultClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), PORT));
					}
				}
			}
		}
		return defaultClient;
		
	}
	
	/**
	 * 关闭连接
	 */
	public static void close() {
		
		if (defaultClient != null) {
			defaultClient.close();
		}
		
	}
	
}
