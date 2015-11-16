HTML5 Storage
=============

1. [WebStorage 簡介](#SummaryOfStorage)
2. [SessionStorage/LocalStorage 的差异](#SessionStorageAndLocalStorage)
3. [GWT API 介紹](#Introduction)
4. [测试目标、步骤](#Test)
5. [浏览器差异](#Difference)
6. [结论](#Conclusion)

## WebStorage 簡介<a id='SummaryOfStorage'></a>

从 GWT2.3 开始，GWT SDK 支持 HTML5 的客户端存储，也被称为"Web Storage"。Web Storage 是 HTML5 新增的本地存储的解决方案，可分为 LocalStorage 和 SessionStorage，在前端开发中经常用到，类似 HTML4 的 cookie，但并不是为了取代 cookie 制定的标准，而是为了解决本来不应该由 cookie 做的，却不得不用 cookie 的本地存储（[部分 browser 支持 Web Storage](http://caniuse.com/#feat=namevalue-storage)）。

cookie 只能提供很小的本地空间存储（每个 cookie 4KB，每个域20个 cookie），发送请求时，cookie 都会传输到服务器端。

Web Storage 提供了更大的本地存储空间，LocalStorage 为每个域 5MB，SessionStorage 不限制本地存储空间大小（仅受系统资源的限制)。发送请求时，不会带 Web Storage 的内容。
 
## SessionStorage/LocalStorage 的差异<a id='SessionStorageAndLocalStorage'></a>

* SessionStorage
	* 本地存储空间：只受系统资源限制
	* 持久性：数据只保存到原始窗口被关闭之前
	* 在其他窗口数据是否可用：只在创建它的窗口有效
* LocalStorage
	* 本地存储空间：5MB
	* 持久性：数据一直保存在 client-side，除非被用户或 app 删除
	* 在其他窗口数据是否可用：在运行同一 app 的同一 browser 中共享

（以下只讨论 LocalStorage）

## GWT API 介紹<a id='Introduction'></a>

* addStorageEventHandler 添加 StorageEvents 事件处理程序
* getLocalStorageIfSupported 获取一个 Local Storage
* getSessionStorageIfSupported 获取一个 Session Storage
* isLocalStorageSupported 判断运行平台是否支持 Storage API 中的 localStorage
* isSessionStorageSupported 判断运行平台是否支持 Storage API 中的 sessionStorage
* isSupported 判断运行平台是否支持 Storage API
* removeStorageEventHandler 取消 StorageEvents 事件处理程序
* clear 移除所有 Storage 中的记录
* getItem 获取与指定 key 值关联的记录
* getLength 获取 Storage 中记录的条数
* key 根据 index 值获取 key 值
* removeItem 根据 key 值删除关联记录
* setItem 设置与唯一 key 值关联的 value 值（key 不能为空字串）

## 测试目标、步骤<a id='Test'></a>

* 目标
	* 测试 LocalStorage 的容量上限
	* 测试 LocalStorage 的容量是只算 value 值还是 key-value 值一起算。
	* 如果达到容量上限，再往里加资料会出现什么情况。
* 步骤
	1. 确定 key 的容量上限。
	2. 因为 key 值无法设为空值，所以设 key 为"1"，测 value 值的容量上限。
	3. 取 key 和 value 的最大值，
	来测 key 值和 value 值是否共同占用 LocalStorage 的 hard disk 空间。
	4. 根据步骤3的结果，测试 LocalStorage 的容量上限。

## 浏览器差异<a id='Difference'></a>

* Browser 版本：chrome 46
	* key 值容量上限：5MB
	* value 值容量上限：4MB 1023KB 1023B(key 值无法为空，设为"0")
	* key-value 是否共同占用 LocalStorage 的 hard disk 空间：是
	* LocalStorage 的容量上限：5MB
	* 达到容量上限后情况：出现 QuotaExceededError 错误
* Browser 版本：Opera 33
	* key 值容量上限：5MB
	* value 值容量上限：4MB 1023KB 1023B(key 值无法为空，设为"0")
	* key-value 是否共同占用 LocalStorage 的 hard disk 空间：是
	* LocalStorage 的容量上限：5MB
	* 达到容量上限后情况：出现 QuotaExceededError 错误
* Browser 版本：IE 11
	* key 值容量上限：4MB 786KB 832Byte
	* value 值容量上限：4MB 786KB 831Byte(key 值无法为空，设为"0")
	* key-value 是否共同占用 LocalStorage 的 hard disk 空间：是
	* LocalStorage 的容量上限：4MB 786KB 832Byte
	* 达到容量上限后情况：出现 QuotaExceededError 错误
* Browser 版本：Firefox 42
	* key 值容量上限：未测出（测到30MB，browser 快崩溃了 T_T）
	* value 值容量上限：5MB(key 值无法为空，设为"0")
	* key-value 是否共同占用 LocalStorage 的 hard disk 空间：否
	* LocalStorage 的容量上限：5MB
	* 达到容量上限后情况：value 值达到容量上限后，
	出现 NS_ERROR_DOM_QUOTA_REACHED 异常

## 结论<a id='Conclusion'></a>
LocalStorage 容量上限大约为 5MB，不同的 browser 有不同的容量上限，key-value 值是否共同占用 LocalStorage 的 hard disk 也不同。并且每个 app 在不同的 browser 占用不同的 LocalStorage 的 hard disk 空间，互不影响。