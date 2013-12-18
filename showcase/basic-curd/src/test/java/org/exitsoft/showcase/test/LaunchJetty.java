package org.exitsoft.showcase.test;

import org.eclipse.jetty.server.Server;
import org.exitsoft.common.unit.JettyFactory;

/**
 * 启动jetty服务,运行后通过http://localhost:8080/exitsoft-basic-curd/来访问项目路径
 * 
 * @author vincent
 *
 */
public class LaunchJetty {
	
	/**
	 * 端口
	 */
	public static final int PORT = 8080;
	/**
	 * 项目名称
	 */
	public static final String CONTEXT = "/exitsoft-basic-curd";
	
	public static final String[] TLD_JAR_NAMES = new String[] { "spring-webmvc", "shiro-web" };
	public static final String ACTIVE_PROFILE = "spring.profiles.active";

	public static void main(String[] args) throws Exception {
		System.setProperty(ACTIVE_PROFILE, "dev");
		Server server = JettyFactory.createServerInSource(PORT, CONTEXT);
		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);

		try {
			System.out.println("[HINT] Don't forget to set -XX:MaxPermSize=128m");
			server.start();
			System.out.println("Server running at http://localhost:" + PORT + CONTEXT);
			System.out.println("[HINT] Hit Enter to reload the application quickly");

			// 等待用户输入回车重载应用.
			while (true) {
				char c = (char) System.in.read();
				if (c == '\n') {
					JettyFactory.reloadContext(server);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
