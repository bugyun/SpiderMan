# AutoTracker
Android字节码插件，编译期间动态修改代码


## 使用
根build.gradle添加插件
```groovy


buildscript {

    repositories {
        maven { url "https://dl.bintray.com/bugyun/maven" }
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'vip.ruoyun.spiderman.plugin:auto-tracker:1.0.0'
    }
}
```

配置
```groovy
apply plugin: 'vip.ruoyun.spiderman.AutoTracker'

autoTracker {
    // 是否打印日志,可选,默认false
    isDebug = true
    // 是否打开全埋点功能,可选,默认true
    isOpenLogTrack = true
}
```


在主module的的 build.gradle 添加依赖
```
implementation 'vip.ruoyun.spiderman:auto-tracker:1.0.8'
```

## 功能

### 自定义界面的 pageId
在 Activity 或者 Fragment 加上注解 @AutoTrackerPage
```java
@AutoTrackerPage("MainActivity")
public class MainActivity extends Activity{}

@AutoTrackerPage("MainActivity")
public class MainFragment extends Fragment{}
```

### pageSession
设置 pageSession，在Activity 的 onCreate() super()之前调用，如果想使用此方法请不要使用  在Activity 的 setTitle()方法。
```java
Bundle bundle = new Bundle();
bundle.putString(PAGE_SESSION_ID,"id");//PAGE_SESSION_ID
Intent intent = new Intent();//PAGE_BUNDLE_ID
intent.putBundle(PAGE_BUNDLE_ID,bundle);
context.startActvity(className,intent)
```

设置 fragment 的 pageSession
在创建 fragment 的时候 ，设置 tag 值，tag 值为 你定义好的 pageSession。
```
Bundle arguments = fragment.getArguments();
if (arguments != null) {
    Bundle argumentsBundle = arguments.getBundle(AutoTracker.PAGE_BUNDLE_ID);
    if (argumentsBundle != null) {
        String pageSession = argumentsBundle.getString(AutoTracker.PAGE_SESSION_ID, "");
        if (TextUtils.isEmpty(pageSession)) {
            autoTrackPageInfo.pageSession = pageSession;
        }
    }
}
```


### 在特定的 view 方法中埋点
如果有特殊的需求，必须在某个方法中埋点，那么需要在你方法上添加注解 @AutoTrackDataViewOnClick
如下，是在 openDialog(View view) 方法中添加埋点,请注意此方法参数 必须为 (View view)
```java
@AutoTrackDataViewOnClick
private void openDialog(View view) {
    //...
}
```

### 忽略方法埋点
在不想埋点的地方，添加注解 @AutoIgnoreTrackDataOnClick
如下，不想监听 onChildClick 的方法
```java
@AutoIgnoreTrackDataOnClick
@Override
public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
    Toast.makeText(context, "child" + childPosition, Toast.LENGTH_SHORT).show();
    return true;
}
```
### 增加业务字段
在设置事件之前，设置值
```java
AutoTracker.setTrackData(view, object);
```

## 上传
内部自带上传到私有服务器的功能，如果想要要传给自己的服务器，可以自定义
```java
AutoTracker.with(app).viewTracker(new IViewTracker() {
            @Override
            public void trackExpandableListViewOnGroupClick(final AutoTrackClickInfo info) {

            }

            @Override
            public void trackExpandableListViewOnChildClick(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackListViewOnItemClick(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackTabHostOnTabChanged(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackTabLayoutSelected(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackNavigationItemSelected(final AutoTrackClickInfo toString) {

            }

            @Override
            public void trackRadioGroupCheckedChanged(final AutoTrackClickInfo toString) {

            }

            @Override
            public void trackDialogClick(final AutoTrackClickInfo toString) {

            }

            @Override
            public void trackDrawerOpened(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackDrawerClosed(final AutoTrackClickInfo json) {

            }

            @Override
            public void trackViewOnClick(final AutoTrackClickInfo json) {

            }
        })
                .fragmentTracker(new IFragmentTracker() {
                    @Override
                    public void trackFragmentSetUserVisibleHint(final AutoTrackPageInfo fragmentName) {

                    }

                    @Override
                    public void trackFragmentViewCreated(final AutoTrackPageInfo fragmentName) {

                    }

                    @Override
                    public void trackFragmentResume(final AutoTrackPageInfo fragmentName) {

                    }

                    @Override
                    public void trackFragmentAppViewScreen(final AutoTrackPageInfo s) {

                    }
                })
                .activityTracker(new IActivityTracker() {
                    @Override
                    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {

                    }

                    @Override
                    public void onActivityStarted(final Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(final Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(final Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(final Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(final Activity activity) {

                    }
                })
                .track();
```


## 功能介绍

### 埋点介绍
可以在以下方法中添加埋点

- onFragmentViewCreated
- trackFragmentResume
- trackFragmentSetUserVisibleHint
- trackOnHiddenChanged
- trackFragmentAppViewScreen
- trackExpandableListViewOnGroupClick
- trackExpandableListViewOnChildClick
- trackListView
- trackTabHost
- trackTabLayoutSelected
- trackMenuItem
- trackRadioGroup
- trackDialog
- trackDrawerOpened
- trackDrawerClosed
- trackViewOnClick
- trackViewOnClick