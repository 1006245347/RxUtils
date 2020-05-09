package com.rxutils.jason.common;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class RxTinkerApp extends TinkerApplication {

    public RxTinkerApp() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.rxutils.jason.common.RxApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
