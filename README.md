# Redis 相关技术
- ### Redis 之安装
##### 提前准备好 gcc 环境:
```
yum install gcc-c++
```
##### 接下来下载并安装 Redis:
```
wget http://download.redis.io/releases/redis-5.0.7.tar.gz 
tar -zxvf redis-5.0.7.tar.gz
cd redis-5.0.7/ 
make
make install
```
#### 安装完成后，启动 Redis:
```
redis-server redis.conf
```
![sp20200512_211529_345](https://imgkr.cn-bj.ufileos.com/fed7c9cf-d8c1-40af-973e-00d77b76ca27.png)
- ### 相关配置
##### 首先，修改 redis.conf 配置文件：
![sp20200512_212144_640](https://imgkr.cn-bj.ufileos.com/cf418afb-bc45-4b9e-8ce6-abf6a2b6d08b.png)
##### 配置完成后，保存退出，再次通过 redis-server redis.conf 命令启动 Redis，此时，就是在后台启 动了。
![sp20200512_212239_883](https://imgkr.cn-bj.ufileos.com/0bf02ce3-fe06-4690-b2bc-863e360f5cfe.png)

- ### 连接
```
使用 redis-cli 进行连接  输入ping 返回PONG 则连接成功
```
- ### 远程连接
#### 首先，修改 redis.conf 注释 127.0.0.1 配置文件：
![sp20200512_212645_405](https://imgkr.cn-bj.ufileos.com/08de90e5-09b7-4f49-b61d-0481e14ccd1d.png)
#### 配置密码 配置后 保存退出 重新启动服务
![sp20200512_212905_330](https://imgkr.cn-bj.ufileos.com/7453a760-a33d-4b81-b17f-a9e75dd401da.png)
### redis-distributed-look -- 分布式锁
