# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SoftWare\ProgramSoftWare\android-sdk-windows(as)/tools/proguard/proguard-android.txt
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


#default

#指定压缩级别
-optimizationpasses 5
-dontusemixedcaseclassnames
#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable

# 是否混淆第三方jar
-dontwarn com.amap.api.**
-dontwarn org.apache.http.**
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes SourceFile,LineNumberTable
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#不被混淆的
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

#数据模型不混淆
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
# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

# 保持 native 方法不被混淆
-keepclassmembers class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
#注解不能混淆
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod

-keep public class com.tencent.bugly.**{*;}

#//////////////////微信支付///////////////////
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}





-dontwarn okio.**
-dontwarn rx.**
-dontwarn android.support.v4.**
-dontwarn com.tencent.bugly.**

-keep class org.xmlpull.v1.** { *;}
-dontwarn org.xmlpull.v1.**
#-libraryjars libs/ksoap2-android-assembly-3.6.2-jar-with-dependencies.jar
-dontwarn org.kobjects.**
-keep class org.kobjects.** { *;}
-dontwarn org.ksoap2.**
-keep class org.ksoap2.** { *;}
-dontwarn org.kxml2.**
-keep class org.kxml2.** { *;}

#lambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*



# 不混淆内部类
-keepattributes InnerClasses

-keep public class **.R$*{
   public static final int *;
}
-dontwarn rx.**
-keep class rx.**{*;}
-keepattributes Exceptions


###########友盟PUSH##############
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
###########友盟统计##############
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -dontwarn com.ut.mini.**
 -dontwarn okio.**
 -dontwarn com.xiaomi.**
 -dontwarn com.squareup.wire.**
 -dontwarn android.support.v4.**

 -keepattributes *Annotation*

 -keep class android.support.v4.** { *; }
 -keep interface android.support.v4.app.** { *; }

 -keep class okio.** {*;}
 -keep class com.squareup.wire.** {*;}

 -keep class com.umeng.message.protobuffer.* {
          public <fields>;
          public <methods>;
 }

 -keep class com.umeng.message.* {
          public <fields>;
          public <methods>;
 }

 -keep class org.android.agoo.impl.* {
          public <fields>;
          public <methods>;
 }

 -keep class org.android.agoo.service.* {*;}

 -keep class org.android.spdy.**{*;}

 -keep public class com.hrmp.R$*{
     public static final int *;
 }

###########xstream##############

 -dontwarn com.thoughtworks.xstream.**
 -keep class com.thoughtworks.xstream.** {*;}
 -keep class com.thoughtworks.xstream.mappger.**{*;}
 -keep class com.thoughtworks.xstream.annotations.**{*;}
 -keep class com.thoughtworks.xstream.converters.**{*;}
 -keep class com.thoughtworks.xstream.core.**{*;}
 -keep class com.thoughtworks.xstream.io.**{*;}
 -keep class com.thoughtworks.xstream.persistence.**{*;}
 -keep class com.thoughtworks.xstream.security.**{*;}
 -keep class com.thoughtworks.xstream.MarshallingStrategy
 -keep class com.thoughtworks.xstream.InitializationException
 -keep class com.thoughtworks.xstream.XStream
 -keep class com.thoughtworks.xstream.XStreamer
 -keep class com.thoughtworks.xstream.XStreamException


# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#使依赖工程的jar包不进行混淆      PushSDK为依赖工程
-libraryjars ..\\PushSDK\libs\ksoap2-android-assembly-3.6.2-jar-with-dependencies.jar


