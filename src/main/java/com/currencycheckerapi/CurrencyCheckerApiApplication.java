package com.currencycheckerapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class CurrencyCheckerApiApplication {

	private static String zoneId;

	private DataSource dataSource;

	@Value("${email.sender.username:}")
	private String emailUser;

	@Value("${email.sender.password:}")
	private String emailPassword;

	@Autowired
	public CurrencyCheckerApiApplication(DataSource dataSource, @Value("${zoneid:+0}") String zoneId) {
		CurrencyCheckerApiApplication.zoneId = zoneId;
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrencyCheckerApiApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper;
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(3);
		return threadPoolTaskScheduler;
	}

	@Bean("httpClient")
	public HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}

	@Bean("proxiedHttpClient")
	@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public HttpClient proxiedHttpClient() {
		String[] workingProxy = getWorkingProxy();
		System.out.println("Using proxy " + workingProxy[0] + ":" + workingProxy[1]);

		return HttpClient.newBuilder()
				.proxy(ProxySelector.of(new InetSocketAddress(workingProxy[0], Integer.parseInt(workingProxy[1]))))
				.build();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
		liquibase.setDataSource(dataSource);
		return liquibase;
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(emailUser);
		mailSender.setPassword(emailPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		return mailSender;
	}

	public static String getZoneId() {
		return zoneId;
	}

	private String[] getWorkingProxy() {
		return new String[] {"51.91.212.159", "3128"};
//		String proxyListUrl = "https://api.proxyscrape.com/?request=getproxies&proxytype=http&timeout=2000&country=all&ssl=yes&anonymity=elite";
//		String response = getForResponse(proxyListUrl, -1, null);
//		String[] proxies = response.split("\r\n");
//		return Arrays.stream(proxies).parallel().filter(p -> {
//			try {
//				int timeout = 5000;
//				String[] proxyData = proxies[new Random().nextInt(proxies.length)].split(":");
//				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyData[0], Integer.parseInt(proxyData[1])));
//				getForResponse("http://myexternalip.com/raw", timeout, proxy);
//				return true;
//			} catch (Exception e) { }
//			return false;
//		}).findFirst().orElseThrow(IllegalStateException::new).split(":");
	}

	private String getForResponse(String spec, int timeout, Proxy proxy) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			URL url = new URL(spec);
			URLConnection urlConnection = proxy == null ? url.openConnection() : url.openConnection(proxy);
			if (timeout != -1) {
				urlConnection.setConnectTimeout(timeout);
				urlConnection.setReadTimeout(timeout);
			}
			IOUtils.copy(urlConnection.getInputStream(), output);
			return output.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
