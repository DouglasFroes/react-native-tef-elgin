package com.tefelgin

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReactContextBaseJavaModule

import com.elgin.e1.pagamentos.tef.*

import android.os.Handler
import android.app.Activity
import android.os.Message
import android.os.Looper

enum class TefWhat(val value: Int) {
    PROGRESS(1),
    COLLECT(2),
    TRANSACTION(3),
    INFO(4),
    FINISH(5),
    INFOPIX(6)
}

class TefElginModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  val context = reactContext as ReactContext

  override fun getName(): String {
    return NAME
  }

  companion object {
    const val NAME = "TefElgin"
  }

  @ReactMethod
  fun onInitTef() {
    val activity: Activity = getCurrentActivity()!!

    ElginTef.setContext(activity)
    ElginTef.setHandler(mHandler)
  }

  @ReactMethod
  fun configTef(name: String, version: String, pinpad: String, doc: String) {
    ElginTef.InformarDadosAutomacao(name, version, pinpad,  "", "", "")

    try{
      ElginTef.AtivarTerminal(doc)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  @ReactMethod
  fun payDeb(value: String) {
    try{
      ElginTef.RealizarTransacaoDebito(value)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  @ReactMethod
  fun payCred(value: String, type: String, instalments: String) {
    try{
      ElginTef.RealizarTransacaoCredito(value, type, instalments)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  @ReactMethod
  fun payPix(value: String) {
    try{
      ElginTef.RealizarTransacaoPIX(value)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  @ReactMethod
  fun onCancel(){
    try{
      ElginTef.RealizarCancelamentoOperacao()
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  private fun sendEvent(params: WritableMap?) {
    context
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("ideploy.tef", params)
  }

  private fun sendEventErro(params: WritableMap?) {
    context
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("ideploy.tef.erro", params)
  }
}
