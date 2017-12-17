# 环境
macOS high_sierra


# 安装
```shell
brew install zookeeper
# 下载版本：zookeeper-3.4.10.high_sierra.bottle.1.tar.gz
# 安装目录：/usr/local/Cellar/zookeeper/3.4.10
# 默认配置文件目录：/usr/local/etc/zookeeper/
```


# 常用命令
```shell
# To have launchd start zookeeper now and restart at login:
  brew services start zookeeper
# Or, if you don't want/need a background service you can just run:
  zkServer start  # (可在任意目录下执行)

# 查看zkServer信息
  zkServer

# 查看zkServer启动状态
  zkServer status
  
# 进入
  zkCli    # （可在任意目录下执行）
```

