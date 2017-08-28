# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\work\android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class cn.dazhou.im.entity.** {*;}
#-keep class cn.dazhou.im.IMLauncher {*;}
-keep class cn.dazhou.im.util.** {*;}

-keep class cn.dazhou.im.core.ChatContentView {*;}

-keep interface cn.dazhou.im.core.ChatContentView$** {
    public <methods>;
}
-keep interface cn.dazhou.im.core.IMApi {
    public <methods>;
}
-keep interface cn.dazhou.im.core.function.** {
    public <methods>;
}