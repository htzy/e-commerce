# 目录结构相关

## /usr, /usr/local/, /opt的区别
/usr：系统级的目录，可以理解为C:/Windows/，/usr/lib理解为C:/Windows/System32。  
/usr/local：用户级的程序目录，可以理解为C:/Progrem Files/。用户自己编译的软件默认会安装到这个目录下。  
/opt：用户级的程序目录，可以理解为D:/Software，opt有可选的意思，这里可以用于放置第三方大型软件（或游戏），当你不需要时，直接rm -rf掉即可。在硬盘容量不够时，也可将/opt单独挂载到其他磁盘上使用。  

## 源码放哪里？
/usr/src：系统级的源码目录。
/usr/local/src：用户级的源码目录。


# 常用命令
## 查看目录占用大小
```shell
    # 以人可读的方式显示当前目录占用的磁盘空间大小，而不显示子目录
    du -hs
    # 以人可读的方式显示当前目录及子目录和文件占用磁盘空间大小
    du -ha
```

    -a：显示目录占用的磁盘空间大小，还要显示其下目录和文件占用磁盘空间的大小
    -s：显示目录占用的磁盘空间大小，不要显示其下子目录和文件占用的磁盘空间大小
    -c：显示几个目录或文件占用的磁盘空间大小，还要统计它们的总和

## 查看进程
```shell
ps -efwww | grep <name>
```

# mac
## 查看路由表
```shell
# "netstat -nr" will display the routing tables.
# The '-r' displays the routing tables, and the '-n' flag makes sure the display show numerical addresses
# instead of trying to resolve host/network/port names.
netstat -nr

```

## homebrew
### 常见错误
1. Error: Calling Formula.sha1 is disabled!
    该问题出现在下载homebrew源代码，checkout较老的版本，没有直接解决该问题，使用brew search <要安装的软件>
    很巧的是找到了，然后brew install <对应的版本，可能包含@符号>


# 参考
[rsync命令——linux集群上同步文件](http://man.linuxde.net/rsync)
[brew安装指定版本的软件](https://www.jianshu.com/p/aadb54eac0a8)
[mac中使用brew安装软件，下载太慢怎么办？](https://www.cnblogs.com/dormscript/p/5832669.html)
