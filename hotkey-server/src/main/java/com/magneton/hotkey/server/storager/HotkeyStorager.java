package com.magneton.hotkey.server.storager;

/**
 * 存储器
 *
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface HotkeyStorager {


    StorageConnection getStorageConnection();

    void registerSaveProcess(StorageProcess storageProcess);
}
