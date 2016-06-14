第一次更新Version1.0，主要是安全性方面的考虑
1、BroadReceiver更改为本地广播，并且不接受远程。
2、Activity、Service不需要其他程序启动，指定android:exported=false
3、删除无用权限。

Version1.1
1、更改加载策略，优先网络，然后本地。
2、支持动态删除城市。
3、更该App的名称。