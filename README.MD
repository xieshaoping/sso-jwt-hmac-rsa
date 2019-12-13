参考：
https://blog.csdn.net/vbirdbest/article/details/83999188
https://www.jianshu.com/p/29d7eea97339

jwt:
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsb2tvZGluZyIsImlhdCI6MTU3NTI3MjE2M30.aTwkRcQyWH9shRIEMRuxSB4x7uHHa4rDaj08-3RjuqI

清除Cookie
chrome://settings/siteData

#生成私钥命令
openssl genrsa -out rsakey0.pem 1024
#生成公钥命令
openssl rsa -in rsakey0.pem -pubout -out rsakey0-pub.pem