#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_akua_demo_dynamicloadso_DynamicSOLoader_stringFromJNI(
        JNIEnv* env,
        jclass /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}