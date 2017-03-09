# StateFramework
## 简介
StateFramework 以 "状态观察者" 事件为核心的高内聚、低耦合开发框架，兼容性高。
## 优点
* 本框架提供代码、注解两种方式实现框架使用
* 注解只存在于编译期，不存在因为注解而造成的性能问题

## 当前版本
0.1.3
## 下载方法
### 使用前请确认 buildscript 下 gradle 版本要大于 2.2.0
```
compile 'com.virtualightning.library:stateframework:0.1.3'
annotationProcessor 'com.virtualightning.library:stateframework-compiler:0.1.3'//StateFramework注解处理器，如果你不想用注解的方式绑定可注释此行
```
## 核心功能 : 状态观察者
### 状态观察者简介
"状态"属于无阈值抽象状态，仅作为唯一标识，而 stateId（状态ID）则是其实体

每个 Observer（状态观察者）对应一个 stateId，如果需要 Observer 执行动作时，可以依靠 stateId 进行通知。

如果想要使用 Observer，必须要有一个 StateRecord（状态记录者）作为 Observer 的集合，以 "key - value" 使 stateId 和 Observer 之间建立联系也就是说每个 Observer 必须所属于 StateRecord , StateRecord 与 Observer 属于一对多的关系。

### 状态观察者的使用
#### 首先需要一个 StateRecord 的实例
```
StateRecord stateRecord = StateRecord.newInstance(null);//参数为 ClassKey ，注册全局状态观察者时作为所属类型的唯一标识
```
#### 然后以代码的方式或者注解的方式注册 Observer

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
stateRecord.registerObserver(observerBuilder);//注册内部状态观察者
//stateRecord.registerWholeObserver(observerBuilder);注册全局状态观察者，ClassKey 为空时会抛出异常
```
* 注解方式，需要委托对象与委托方法，下列以 DemoActivity 为例 （需要添加注解解释器的依赖）
```
public class DemoActivity extends Activity {

    StateRecord stateRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stateRecord = StateRecord.newInstance(DemoActivity.class);
        Analyzer.analyzeState(this,stateRecord);//绑定 DemoActivity 的注解到指定的 StateRecord 上
    }

    @BindObserver(
            stateId = "S_1",//注册状态ID 
            allowStop = false,//是否允许停止
            runType = RunType.MAIN_LOOP,//运行类型,当前为运行在主线程
            isVarParameters = true,//是否使用自变长参数
            isWholeObserver = false//是否为全局状态观察者
    )
    void onNotifyState_1(Object... args) {
        //通知 Observer 之后所做的一些事
    }
}
```
#### 完成注册，下面就是通知方法
```
stateRecord.notifyState("S_1");
stateRecord.notifyState("S_1",1,2,3,4);//如果你需要传递参数可以这样
```
全局状态观察者
```
StateRecord.notifyWholeState("S_1");//通知全部订阅 "S_1" 的状态观察者
```
当然也可以通知指定的全局状态观察者
```
StateRecord.notifyWholeState(ClassKey,"S_1");//通知指定 ClassKey 订阅 "S_1" 的状态观察者
```
#### 注销状态
使用完之后最好是注销状态
```
stateRecord.unregisterObserver();
```
如果注册了全局状态但并未注册，可能会导致内存泄露
#### 关于 allowStop 属性
如果注册 Observer 时启用 allowStop，那么可以通过改变 StateRecord 的状态实现临时禁用 Observer
```
stateRecord.setRecordState(false);
```
当 StateRecord 的状态为 false时（stop），通知将不会抵达启用 allowStop 的状态观察者
#### 关于@BindObserver中的 isVarParameters 属性
如果你明确知道委托函数所需的参数类型,可以禁用 isVarParameters ,通知 Observer 所携带的参数会自动转型填充委托函数参数列表
```
@BindObserver(
    stateId = "S_1",//注册状态ID
    isVarParameters = false
)
void onNotifyState_1(String str1,Integer i) {
//通知 Observer 之后所做的一些事
}
```
可以使用下列通知方式
```
stateRecord.notifyState("S_1","HelloWorld",520);
```
不过需要注意的是，当禁用了 isVarParameters 时，通知时所传递的参数类型、顺序、长度必须严格遵守委托函数定义的参数列表，否则会抛出异常

## 辅助功能 : 注解绑定注入 （需要添加注解解释器的依赖）
### 绑定View
使用 @BindView 绑定控件
```
public class DemoActivity extends Activity {

        
    @BindView(R.id.list)// id 为 R.id.list 必须存在于 R.layout.main
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Analyzer.analyzeView(this);//绑定 DemoActivity 的控件
    }
}
```
### 绑定资源
```
public class DemoActivity extends Activity {
    //资源id = R.array.mainItem , 资源类型 = 字符串数组
    @BindResources(resId = R.array.mainItem,type = ResType.STRING_ARRAY)
    String[] strArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Analyzer.analyzeResources(this);//绑定 DemoActivity 的资源
    }
}
```
### 绑定事件
以点击事件为例，更多事件请参考[注解类型集合](https://github.com/CimZzz/StateFramework/tree/master/stateframework-anno/src/main/java/com/virtualightning/stateframework/anno)
```
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Analyzer.analyzeEvent(this);//绑定 DemoActivity 的资源
     }

    @OnClick(R.id.list)
    void OnClick() {
        //点击事件处理的一些事
    }
}
```
