package abi43_0_0.expo.modules.medialibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import abi43_0_0.expo.modules.core.Promise;

import static abi43_0_0.expo.modules.medialibrary.MediaLibraryUtils.deleteAssets;

class DeleteAssets extends AsyncTask<Void, Void, Void> {

  private final Context mContext;
  private final String[] mAssetsId;
  private final Promise mPromise;

  DeleteAssets(Context context, String[] assetsId, Promise promise) {
    mContext = context;
    mAssetsId = assetsId;
    mPromise = promise;
  }

  @Override
  protected Void doInBackground(Void... params) {
    final String selection = MediaStore.Images.Media._ID + " IN (" + TextUtils.join(",", mAssetsId) + " )";
    final String[] selectionArgs = null;
    deleteAssets(mContext, selection, selectionArgs, mPromise);
    return null;
  }
}
