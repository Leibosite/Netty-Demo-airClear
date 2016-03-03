package com.qingruan.gateway;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MasterServer {

	private EventLoopGroup boss;
	private EventLoopGroup worker;

	public void runServer() {
		boss = new NioEventLoopGroup();
		worker = new NioEventLoopGroup();

		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(boss, worker);
			bootStrap.channel(NioServerSocketChannel.class);
			bootStrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new Decoder());
					channel.pipeline().addLast(new Encoder());
					channel.pipeline().addLast(new MessageHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootStrap.bind(PropertiesUtils.readValue("gateway.addr"),Integer.valueOf(PropertiesUtils.readValue("gateway.port")));
//			bootStrap.bind("10.0.1.138", 16888);

			// 绑定监听的ip地址、端口号
			/*
			 * List<HashMap<String,Object>> ipList =
			 * PropertiesUtils.readIpAddressesFromConfigFile("gateway.addr.urls"
			 * ); for (HashMap<String, Object> hashMap : ipList) {
			 * bootStrap.bind(hashMap.get("ip").toString(),
			 * Integer.valueOf(hashMap.get("port").toString()));
			 * System.out.println("IP地址是"+hashMap.get("ip")+":"+hashMap.get(
			 * "port")+"的GateWay启动..."); }
			 */

			// ScheduleTask.startScheduleTask();
			// THTimeTask.runTimeTask();

			/*
			 * 0、查询 6、负离子 1、开启 7、紫外灯 2、关闭 8、加湿 3、风速 9、童锁 4、自动模式 10、滤网时间重置 5、定时关机
			 * 
			 */
			
			THTimeTask.runTimeTask();

			new Thread(new Runnable() {
				private Scanner scanner;

				@Override
				public void run() {

					System.out.println("软件说明：窗口打印的成功或失败只做参考，只要发送指令空气净化器能够正常响应即可！！！");
					while (true) {
						System.out.println("********************************");
						System.out.println("您好，请您输入远程空气净化器指令：\n");
						System.out.println("输入0进入自动测试模式，每3秒发送一条指令，顺序发送，发完停止，再次输入0，重新开始");
						System.out.println("1、关闭		    6、风速3档");
						System.out.println("2、开启		    7、加湿开启");
						System.out.println("3、风速2档	    8、加湿关闭");
						System.out.println("4、风速1档              9、睡眠模式");
						System.out.println("5、定时关机,默认2分钟后关机");
						System.out.println("10、自动模式            11、负离子开启");
						System.out.println("12、负离子关闭         13、紫外灯开启");
						System.out.println("14、紫外灯关闭         15、 童锁开启");
						System.out.println("16、童锁关闭");
						System.out.println("********************************");
						scanner = new Scanner(System.in);
						int remoteCMD = scanner.nextInt();


						
						if (remoteCMD >=0 && remoteCMD < 18) {
							
							if(remoteCMD==0){
								for(int i=1;i<=16;i++){
									execCommand(i);
									sendCommand(i);
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										System.out.println("程序运行错误，请重启程序");
									}
								}
								continue;
							}else{
								execCommand(remoteCMD);
							}
						
						} else
						{
							System.out.println("输入数字不对，请重新输入");
							continue;
						}
						
						sendCommand(remoteCMD);
						
						if (remoteCMD != -1) {
							continue;
						} else {
							System.exit(0);
						}
					}
				}
			}).start();

		} catch (Exception e) {
			System.out.println("TCPServer错误：" + e.toString());
			shutDownTCPServer();
		}

	}
	
	public void sendCommand(int remoteCMD){
		AirDownDataPackage air = new AirDownDataPackage();
		ConcurrentHashMap<String, Channel> macChannel = CommonData.deviceChannels;
		for (Map.Entry<String, Channel> m : macChannel.entrySet()) {
			air.remoteTest(remoteCMD, m.getKey());
		}
	}
	
	public void execCommand(int remoteCMD){
		switch (remoteCMD) {
			case 1:
				CommonData.sendcmdtype = CommonData.SendCmdType.ONLINE;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   关闭指令");
				break;
			case 2:
				CommonData.sendcmdtype = CommonData.SendCmdType.ONLINE;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   开启指令");
				break;
			case 3:
				CommonData.sendcmdtype = CommonData.SendCmdType.FANSPEED;
				CommonData.reciveCmdValue = 2;
				System.out.println("输入了   风速开到2档指令");
				break;
			case 4:
				CommonData.sendcmdtype = CommonData.SendCmdType.FANSPEED;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了  风速1档 指令");
				break;
			case 5:
				CommonData.sendcmdtype = CommonData.SendCmdType.TIMER_SHUTDOWN;
				CommonData.reciveCmdValue = 2;
				System.out.println("输入了   开启了倒计时关闭功能");
				break;
			case 6:
				CommonData.sendcmdtype = CommonData.SendCmdType.FANSPEED;
				CommonData.reciveCmdValue = 3;
				System.out.println("输入了   风速3档 指令");
				break;
			case 7:
				CommonData.sendcmdtype = CommonData.SendCmdType.HUMIDIFICATION;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   加湿器开启 指令");
				break;
			case 8:
				CommonData.sendcmdtype = CommonData.SendCmdType.HUMIDIFICATION;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   加湿关闭 指令");
				break;
			case 9:
				CommonData.sendcmdtype = CommonData.SendCmdType.MODE;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   开启睡眠模式 指令");
				break;
			case 10:
				CommonData.sendcmdtype = CommonData.SendCmdType.MODE;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   自动模式 指令");
				break;
			case 11:
				CommonData.sendcmdtype = CommonData.SendCmdType.ANION;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   开启负离子 指令");
				break;
			case 12:
				CommonData.sendcmdtype = CommonData.SendCmdType.ANION;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   负离子关闭 指令");
				break;
			case 13:
				CommonData.sendcmdtype = CommonData.SendCmdType.UV;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   紫外灯开启 指令");
				break;
			case 14:
				CommonData.sendcmdtype = CommonData.SendCmdType.UV;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   紫外灯关闭 指令");
				break;		
			case 15:
				CommonData.sendcmdtype = CommonData.SendCmdType.CHILDLOCK;
				CommonData.reciveCmdValue = 1;
				System.out.println("输入了   童锁开启 指令");
				break;
			case 16:
				CommonData.sendcmdtype = CommonData.SendCmdType.CHILDLOCK;
				CommonData.reciveCmdValue = 0;
				System.out.println("输入了   童锁关闭 指令");
				break;
			/*case 17:
				CommonData.sendcmdtype = CommonData.SendCmdType.MODE;
				CommonData.reciveCmdValue = 2;
				System.out.println("输入了   手动模式 指令");
				break;*/
			}
	}

	/**
	 * destory Method
	 */
	// TODO 关闭TCPServer,用Spring托管
	public void shutDownTCPServer() {
		System.out.println("关闭Netty");
		if (boss != null && worker != null) {
			System.out.println("关闭TCPServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	public static void main(String args[]) {
		MasterServer masterServer = new MasterServer();
		masterServer.runServer();
		System.out.println("场测服务器启动成功，请把空气净化器的ip地址指向本服务器的ip地址");

	}

}
