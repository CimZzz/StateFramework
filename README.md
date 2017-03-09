# StateFramework
StateFramework 以 "状态观察者" 事件为核心的高内聚、低耦合开发框架，兼容性高。
## 当前版本
0.1.3
## 下载方法
### 使用前请确认 buildscript 下 gradle 版本要大于 2.2.0
```
compile 'com.virtualightning.library:stateframework:0.1.3'
annotationProcessor 'com.virtualightning.library:stateframework-compiler:0.1.3'//如果你不想用注解的方式绑定可注释此行
```
## 核心功能 : 状态观察者

"状态"属于无阈值抽象状态，仅作为唯一标识，而 stateId（状态ID）则是其实体

每个 Observer（状态观察者）对应一个 stateId，如果需要 Observer 执行动作时，可以依靠 stateId 进行通知。

如果想要使用 Observer，必须要有一个 StateRecord（状态记录者）作为 Observer 的集合，以 "key - value" 使 stateId 和 Observer 之间建立联系也就是说每个 Observer 必须所属于 StateRecord , StateRecord 与 Observer 属于一对多的关系。

#### 内部状态观察者的使用
首先需要一个 StateRecord 的实例
```
StateRecord stateRecord = StateRecord.newInstance(null);//参数为 ClassKey ，注册全局状态观察者需要用到
```
然后以代码的方式或者注解的方式注册 Observer

* 代码方式，通过 ObserverBuilder 构建 Observer 所需参数并完成注册
```
        ObserverBuilder observerBuilder = new ObserverBuilder()
                .stateId("S_1")//状态ID，类型为 String
                .allowStop(false)//是否运行暂停状态   
                .refType(ReferenceType.WEAK)//状态观察者被持有的引用类型（影响内存泄露）
                .runType(RunType.MAIN_LOOP)//状态观察者执行类型
                .observer(new BaseObserver() {
                    @Override
                    public void notify(Object... objects) {
                        //通知 Observer 之后所做的一些事
                    }
                });
        stateRecord.registerObserver(observerBuilder);//注册状态观察者
```
* 注解方式，需要委托对象与委托方法，下列以 DemoActivity 为例
