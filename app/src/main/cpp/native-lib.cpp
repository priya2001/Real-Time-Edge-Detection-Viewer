#include <jni.h>
#include <opencv2/opencv.hpp>
using namespace cv;

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_edgedetection_NativeBridge_processFrame(
    JNIEnv *env, jclass, jbyteArray input, jint width, jint height) {

    jsize len = env->GetArrayLength(input);
    jbyte *inputBytes = env->GetByteArrayElements(input, nullptr);
    Mat rgba(height, width, CV_8UC4, (unsigned char *) inputBytes);

    Mat gray, edges, output;
    cvtColor(rgba, gray, COLOR_RGBA2GRAY);
    Canny(gray, edges, 100, 200);
    cvtColor(edges, output, COLOR_GRAY2RGBA);

    jbyteArray result = env->NewByteArray(output.total() * output.elemSize());
    env->SetByteArrayRegion(result, 0, output.total() * output.elemSize(),
                            (jbyte *) output.data);
    env->ReleaseByteArrayElements(input, inputBytes, JNI_ABORT);
    return result;
}

