# pearl


#	一、说在前面的话
该程序作用为执行excel中记录的sql并按照指定的目录结构生成excel文件。命名为珍珠号并初始化指定记录sql的excel文件名为treasureMap。

#	二、准备工作
1.Jre版本1.7及以上。
2.记录任务的excel文件，默认需以“treasureMap”命名并且放在jar同一目录下并确保可以被正常打开，目前程序尚不能对于异常的excel进行提示。
3.在jar同一目录下最好没有名为“logs”的文件夹或logs文件夹下没有已存在的日志文件。

# 三、配置文件说明
## 1.conf.properties
用于记录与程序执行相关的配置。具体如下：
mysql.db.host.ip               目标数据库地址
mysql.db.host.port           目标数据库端口
mysql.db.nm                      目标数据库名
mysql.db.account              目标数据库用户账户
mysql.db.password           目标数据库用户密码
## 2.treasureMap
filename                            指定存储需要执行的sql所在的excel文件名
columnNumTaskNm      生成的excel中sheet名称对应的列号（列号由1起）
columnNumSql               生成的excel中获取sheet数据的sql对应的列号（列号由1起）
# 四、日志文件说明
珍珠号运行完成后将在其同一目录下的logs文件夹中生成所有日志文件。
## 1.log.log
程序运行时的常规信息内容对应日志级别info以上的所有日志信息。
## 2.error.log
程序运行时的异常信息内容对应日志级别为error的信息。在每条异常信息的头部，会有产生该异常的事件的变量信息并且会逐级先显示其父类的信息。

# 五、使用方法
## 1.运行
使用“java -jar”命令执行该jar文件。
## 2.产出
珍珠号运行完成后将在其同一目录下生成包含sql结果的excel文件并在logs文件夹中生成所有日志文件。
## 3.校对
在运行之后请查看error.log中是否有异常信息，如果没有则本次数据拉取成功。如果有请根据日志中错误信息中的事件描述以及sql检查并重新执行，将结果复制到已经生成的excel中。

# 六、已知不足
## 1.程序架构
1)	父类定义：entity类与handler类结构精简。
## 2.异常提示
1)	日志内容：对于异常应该有完备且精准提示信息，目前的异常抛出是在各自的父类中进行的没有指出准确的发生未知。
2)	连接MySQL
## 3.数据库连接
1） 现在没有使用MySQL连接池。
## 4.生成excel
1)	excel的大小：如果excel行数超过五千，在生成之后打开时会有很明显的卡顿，应该在生成时进行切割。
2)	sheet的名称：生成sheet时针对于sheet的名字需要保证，与已经生成sheet不要重名且名称不能超过14个字符。
