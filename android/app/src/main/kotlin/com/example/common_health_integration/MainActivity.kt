package com.example.common_health_integration

import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.FlutterEngine
import kotlinx.coroutines.runBlocking
import org.thecommonsproject.android.commonhealthclient.CommonHealthAvailability
import org.thecommonsproject.android.commonhealthclient.CommonHealthStore

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/commonhealth"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
            // Note: this method is invoked on the main thread.
            // TODO
            if (call.method == "getCommonHealthAvailability") {
                runBlocking {
                    result.success(getCommonHealthAvailability(applicationContext));
                }

            } else {
                result.notImplemented();
            }
        }
    }

    private suspend fun getCommonHealthAvailability(context: Context): String {
        val commonHealthStore = CommonHealthStore.getSharedInstance();

        return when(commonHealthStore.getCommonHealthAvailability(context)) {
            CommonHealthAvailability.AVAILABLE -> {
                "AVAILABLE";
            }
            CommonHealthAvailability.NOT_INSTALLED -> {
                val chInstallIntent = commonHealthStore.buildCommonHealthInstallIntent(context)
                startActivity(chInstallIntent);
                "NOT INSTALLED";
            }
            CommonHealthAvailability.ACCOUNT_NOT_CONFIGURED_FOR_SHARING -> {
                "NOT CONFIGURED";
            }
        }
    }
}
