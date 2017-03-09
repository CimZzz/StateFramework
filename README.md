# StateFramework
StateFramework 以 "状态观察者" 事件为核心的高内聚、低耦合开发框架，兼容性高。
## 当前版本
0.1.3
## 下载方法
### 使用前请确认 buildscript 下 gradle 版本要大于 2.2.0
```
compile 'com.virtualightning.library:stateframework:0.1.3'
annotationProcessor 'com.virtualightning.library:stateframework-compiler:0.1.3'
```
## 核心功能 : 状态观察者
每个状态观察者对应一个"状态ID"，如果需要状态观察者执行动作时，可以依靠"状态ID"进行通知。

"状态"属于无阈值抽象状态，仅作为唯一标识
下列是
