HTML5 Storage
=============

1. [WebStorage 簡介](#SummaryOfStorage)
2. [SessionStorage/LocalStorage 的差异](#SessionStorageAndLocalStorage)
3. [GWT API 介紹](#Introduction)
4. [测试目标、步骤](#Test)
5. [浏览器差异](#Difference)
6. [结论](#Conclusion)

## WebStorage 簡介<a id='SummaryOfStorage'></a>

Web Storage 是 HTML5 新增的本地存储的解决方案，在前端开发中经常用到，类似于 cookie，但并不是为了取代 cookie 制定的标准，而是为了解决本来不应该由 cookie 做的，却不得不用 cookie 的本地存储。

Web Storage 与 Cookie 相比，Web Storage 的本地存储空间更大，而 cookie 只有 4KB。Cookie 的内容会随着请求一并发送到服务器端，而 Web Storage 中的数据仅仅存在本地，不会与服务器发生交互。

## SessionStorage/LocalStorage 的差异<a id='SessionStorageAndLocalStorage'></a>

Web Storage 可分为 LocalStorage（针对没有时间限制的数据存储）和 SessionStorage（针对一个 session 的数据存储）。

* SessionStorage
	* 本地存储空间：只受系统资源限制
	* 持久性：数据只保存到原始窗口被关闭之前
	* 在其他窗口数据是否可用：只在创建它的窗口有效
* LocalStorage
	* 本地存储空间：5MB
	* 持久性：数据一直保存在 client-side，除非被用户或 app 删除
	* 在其他窗口数据是否可用：在运行同一 app 的同一 browser 中共享

## GWT storage API 介紹<a id='Introduction'></a>

从 GWT2.3 开始，GWT SDK 支持 HTML5 的 Web Storage。你可以通过调用 Storage.getLocalStorageIfSupported() 或者 Storage.getSessionStorageIfSupported() 取决于你想要使用的 storage 类型，接下来我们只使用 LocalStorage。

因为不是所有的 [browser 可以支持 Web Storage](http://caniuse.com/#feat=namevalue-storage)，所以你在使用前需要检查是否可以使用 HTML5 storage 功能。

如果支持 storage 功能，你得到一个 storage object 后，你就可以向其中写入数据或读取数据。

1. [检查 browser 是否支持 storage](#Check)
2. [获取 Storage object](#Get)
3. [将数据写入 Storage](#Write)
4. [从 Storage 中读取数据](#Read)
5. [删除数据](#Delete)
6. [检查 localstorage 是否变动](#Event)

### 检查 browser 是否支持 storage<a id='Check'></a>
在获取 Storage object 之前，需要判断 browser 是否支持 Web Storage。Browser 不支持 Web Storage 则无法进行 storage 的增删改查等功能。通过使用 ```Storage.isLocalStorageSupported()```，如果返回值为 true，则 browser 支持 LocalStorage。

### 获取 Storage object<a id='Get'></a>
在判断 browser 是否支持 Web Storage 后，可使用 ```Storage storage = Storage.getLocalStorageIfSupported()``` 来获取 localstorage instance，如果 browser 不支持 LocalStorage，无法得到 instance，而是得到 null。

### 将数据写入 Storage<a id='Write'></a>

可以写入你喜欢的任何数据，前提是 key 和 value 都必须为 string 类型。

```
String value = Window.prompt("请输入value值", "");
storage.setItem("Storage." + storage.getLength(), value);
```

### 从 Storage 中读取数据<a id='Read'></a>

数据以 key-value 对应的方式存储，所以需要通过 key 来获取数据。你需要知道 key 值是什么或通过遍历 index 来获取 key。

```
for (int i = 0; i < storage.getLength(); i++){
	String key = storage.key(i);
	GWT.log(key);
	GWT.log(storage.getItem(key));
}
```

### 删除数据<a id='Delete'></a>

从 storage 中删除数据，可分为删除制定 key-value 和 清除整个 storage。如果想要删除指定的数据，需要知道 key 值。

* 删除全部数据

```
storage.clear();
```

* 根据 key 值删除数据,如果 key 值不存在，storage 不改变,不会出现 Error 或 Exception。

```
String key = Window.prompt("请输入key值", "");
storage.removeItem(key);
```

### 检查 localstorage 是否变动<a id='Event'></a>

因为使用同一个 storage，所以与这个 storage 相关的信息都需要变动。

```
Storage.addStorageEventHandler(new Handler() {
	@Override
	public void onStorageChange(StorageEvent event) {
		//不是 storage 產生的 event 就忽略不管
		if (storage != event.getStorageArea()) { return ;}
		GWT.log("Last Update: "+event.getNewValue() +": " +event.getOldValue() +": " +event.getUrl());
	}
});
```

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

|Browser 版本|key 容量上限| value 容量上限|key-value 共同占用 LocalStorage|LocalStorage 上限|达到上限后情况|
|:----------:|:----------:|:-------------:|:-----------------------------:|:---------------:|:------------:|
|Chrome 46|5MB|4MB 1023KB 1023B|是|5MB|出现 QuotaExceededError 错误|
|Opera 33|5MB|4MB 1023KB 1023B|是|5MB|出现 QuotaExceededError 错误|
|IE 11|4MB 786KB 832Byte|4MB 786KB 831Byte|是|5MB|出现 QuotaExceededError 错误|
|Firefox 42|未测出|5MB|否|5MB|value 值达到容量上限后，出现 NS_ERROR_DOM_QUOTA_REACHED 异常|

## 结论<a id='Conclusion'></a>
LocalStorage 容量上限大约为 5MB，不同的 browser 有不同的容量上限，key-value 值是否共同占用 LocalStorage 的 hard disk 也不同。并且每个 app 在不同的 browser 占用不同的 LocalStorage 的 hard disk 空间，互不影响。

使用 local storage ，一旦数据保存在 client-side，可减少不必要的数据请求。并且与 cookie 相比，可减少数据在 browser 和 server-side 之间不必要的来回传递，减少网络流量。从本地读取数据比通过网络从 server-side 获取数据快得多，可以加快显示的时间。