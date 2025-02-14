package expo.modules.devmenu.modules.internals

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import expo.modules.devmenu.DevMenuManager
import expo.modules.devmenu.modules.DevMenuInternalMenuControllerModuleInterface
import kotlinx.coroutines.launch

class DevMenuInternalMenuControllerModule(private val reactContext: ReactContext) :
  DevMenuInternalMenuControllerModuleInterface {
  private val devMenuManager: DevMenuManager = DevMenuManager

  override fun dispatchCallableAsync(callableId: String?, args: ReadableMap?, promise: Promise) {
    if (callableId == null) {
      promise.reject("ERR_DEVMENU_ACTION_FAILED", "Callable ID not provided.")
      return
    }
    devMenuManager.dispatchCallable(callableId, args)
    promise.resolve(null)
  }

  override fun hideMenu() {
    devMenuManager.hideMenu()
  }

  override fun setOnboardingFinished(finished: Boolean) {
    devMenuManager.getSettings()?.isOnboardingFinished = finished
  }

  override fun openDevMenuFromReactNative() {
    devMenuManager.getReactInstanceManager()?.devSupportManager?.let {
      devMenuManager.closeMenu()
      it.devSupportEnabled = true
      it.showDevOptionsDialog()
    }
  }

  override fun onScreenChangeAsync(currentScreen: String?, promise: Promise) {
    devMenuManager.setCurrentScreen(currentScreen)
    promise.resolve(null)
  }

  override fun fetchDataSourceAsync(id: String?, promise: Promise) {
    if (id == null) {
      promise.reject("ERR_DEVMENU_FETCH_FAILED", "DataSource ID not provided.")
      return
    }

    devMenuManager.coroutineScope.launch {
      val data = devMenuManager.fetchDataSource(id)
      val result = Arguments.fromList(data.map { it.serialize() })
      promise.resolve(result)
    }
  }

  override fun copyToClipboardAsync(content: String, promise: Promise) {
    val clipboard = reactContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(null, content)
    clipboard.setPrimaryClip(clip)
    promise.resolve(null)
  }
}
