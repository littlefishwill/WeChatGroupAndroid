# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\shenmegui\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn
-ignorewarning
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
# 移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用
# 记得proguard-android.txt中一定不要加-dontoptimize才起作用
# 另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

#############################################
#
# 项目中特殊处理部分
#
#############################################

#-----------处理反射类---------------



#-----------处理js交互---------------



#-----------处理实体类---------------
# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
-keep public class com.szw.tools.wechatgroupandroid.domian.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}

# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
-keep public class com.szw.tools.wechatgroupandroid.pages.qa.doamin.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}

-keep public class com.szw.tools.wechatgroupandroid.pages.cq.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}

#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#gson
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

#Serializable
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
 private void readObject(java.io.ObjectInputStream);
 java.lang.Object writeReplace();
java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}


# OrmLite
-keepattributes *DatabaseField*
-keepattributes *DatabaseTable*
-keepattributes *SerializedName*
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

# OrmLite com.litesuits.orm
-keepattributes *Table*
-keepattributes *PrimaryKey*
-keepattributes *NotNull*
-keep class com.litesuits.orm.**
-keepclassmembers class com.litesuits.orm.** { *; }
-keep enum com.litesuits.orm.**
-keepclassmembers enum com.litesuits.orm.** { *; }
-keep interface com.litesuits.orm.**
-keepclassmembers interface com.litesuits.orm.** { *; }


# xutils
-keep class org.xutils.**
-keepclassmembers class org.xutils.** { *; }
-keep enum org.xutils.**
-keepclassmembers enum org.xutils.** { *; }
-keep interface org.xutils.**
-keepclassmembers interface org.xutils.** { *; }

# dmytrodanylyk
-keep class com.github.dmytrodanylyk.shadow-layout.**
-keepclassmembers class com.github.dmytrodanylyk.shadow-layout.** { *; }
-keep enum com.github.dmytrodanylyk.shadow-layout.**
-keepclassmembers enum com.github.dmytrodanylyk.shadow-layout.** { *; }
-keep interface com.github.dmytrodanylyk.shadow-layout.**
-keepclassmembers interface com.github.dmytrodanylyk.shadow-layout.** { *; }

# yipianfengye -zxing
-keep class cn.yipianfengye.android.**
-keepclassmembers class cn.yipianfengye.android.** { *; }
-keep enum cn.yipianfengye.android.**
-keepclassmembers enum cn.yipianfengye.android.** { *; }
-keep interface cn.yipianfengye.android.**
-keepclassmembers interface cn.yipianfengye.android.** { *; }

# dmytrodanylyk
-keep class com.afollestad.material-dialogs.**
-keepclassmembers class com.afollestad.material-dialogs.** { *; }
-keep enum com.afollestad.material-dialogs.**
-keepclassmembers enum com.afollestad.material-dialogs.** { *; }
-keep interface com.afollestad.material-dialogs.**
-keepclassmembers interface com.afollestad.material-dialogs.** { *; }

# dmytrodanylyk
-keep class com.afollestad.material-dialogs.**
-keepclassmembers class com.afollestad.material-dialogs.** { *; }
-keep enum com.afollestad.material-dialogs.**
-keepclassmembers enum com.afollestad.material-dialogs.** { *; }
-keep interface com.afollestad.material-dialogs.**
-keepclassmembers interface com.afollestad.material-dialogs.** { *; }

# com.suke.widget
-keep class com.suke.widget.**
-keepclassmembers class com.suke.widget.** { *; }
-keep enum com.suke.widget.**
-keepclassmembers enum com.suke.widget.** { *; }
-keep interface com.suke.widget.**
-keepclassmembers interface com.suke.widget.** { *; }

# com.github.clans.fab
-keep class com.github.clans.fab.**
-keepclassmembers class com.github.clans.fab.** { *; }
-keep enum com.github.clans.fab.**
-keepclassmembers enum com.github.clans.fab.** { *; }
-keep interface com.github.clans.fab.**
-keepclassmembers interface com.github.clans.fab.** { *; }

# org.hamcrest
-keep class org.hamcrest.**
-keepclassmembers class org.hamcrest.** { *; }
-keep enum org.hamcrest.**
-keepclassmembers enum org.hamcrest.** { *; }
-keep interface org.hamcrest.**
-keepclassmembers interface org.hamcrest.** { *; }

#huanxin
-keep class org.xmlpull.** {*;}
-keep class com.baidu.** {*;}
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.squareup.picasso.* {*;}

-keep public class com.hyphenate.easeui.**

-keep class com.hyphenate.** {*;}
-keep class com.hyphenate.chat.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-keep class com.hyphenate.easeui.** {*;}
-keep class com.hyphenate.easeui.utils.EaseSmileUtils {*;}

#zxing
-keep class com.uuzuche.lib_zxing.** {*;}

-keep class com.afollestad.materialdialogs.** {*;}


#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
#不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
-keep class com.hyphenate.chatuidemo.utils.SmileUtils {*;}

#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}

#db
-keep class com.szw.tools.wechatgroupandroid.db.**{*;}
