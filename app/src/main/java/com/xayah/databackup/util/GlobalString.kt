package com.xayah.databackup.util

import com.xayah.databackup.App
import com.xayah.databackup.R

class GlobalString {
    companion object {
        val compressionType = App.globalContext.getString(R.string.compression_type)
        val compressionTypeHelp = App.globalContext.getString(R.string.compression_type_help)
        val confirm = App.globalContext.getString(R.string.confirm)
        val backupUser = App.globalContext.getString(R.string.backup_user)
        val cautiousAction = App.globalContext.getString(R.string.cautious_action)
        val backupUserHelp = App.globalContext.getString(R.string.backup_user_help)
        val restoreUser = App.globalContext.getString(R.string.restore_user)
        val restoreUserHelp = App.globalContext.getString(R.string.restore_user_help)
        val historyVersion = App.globalContext.getString(R.string.history_version)
        val github = App.globalContext.getString(R.string.github)
        val featureForesight = App.globalContext.getString(R.string.feature_foresight)
        val deleteConfirm = App.globalContext.getString(R.string.delete_confirm)
        val getApkPath = App.globalContext.getString(R.string.get_apk_path)
        val compressApk = App.globalContext.getString(R.string.compress_apk)
        val compress = App.globalContext.getString(R.string.compress)
        val install = App.globalContext.getString(R.string.install)
        val setSELinux = App.globalContext.getString(R.string.set_SELinux)
        val decompress = App.globalContext.getString(R.string.decompress)
        val test = App.globalContext.getString(R.string.test)
        val defaultBackupSavePath = App.globalContext.getString(R.string.default_backup_save_path)
        val pleaseTypeKeyWord = App.globalContext.getString(R.string.please_type_key_word)
        val pleaseExitSearchMode = App.globalContext.getString(R.string.please_exit_search_mode)
        val selected = App.globalContext.getString(R.string.selected)
        val appName = App.globalContext.getString(R.string.app_name)
        val backupProcessing = App.globalContext.getString(R.string.backup_processing)
        val backupFinished = App.globalContext.getString(R.string.backup_finished)
        val backupApkProcessing = App.globalContext.getString(R.string.backup_apk_processing)
        val success = App.globalContext.getString(R.string.success)
        val backupVersion = App.globalContext.getString(R.string.backup_version)
        val generateBackupInfoFailed =
            App.globalContext.getString(R.string.generate_backup_info_failed)
        val generateMediaInfoFailed =
            App.globalContext.getString(R.string.generate_media_info_failed)
        val environmentDetectionTip =
            App.globalContext.getString(R.string.environment_detection_tip)
        val environmentDetection = App.globalContext.getString(R.string.environment_detection)
        val tips = App.globalContext.getString(R.string.tips)
        val backupNotSupport = App.globalContext.getString(R.string.backup_not_support)
        val customDir = App.globalContext.getString(R.string.custom_dir)
        val restoreProcessing = App.globalContext.getString(R.string.restore_processing)
        val restoreFinished = App.globalContext.getString(R.string.restore_finished)
        val installApkProcessing = App.globalContext.getString(R.string.install_apk_processing)
        val noUpdateAndSkip = App.globalContext.getString(R.string.no_update_and_skip)
        val compressFailed = App.globalContext.getString(R.string.compress_failed)
        val pathNotExist = App.globalContext.getString(R.string.path_not_exist)
        val compressApkFailed = App.globalContext.getString(R.string.compress_apk_failed)
        val decompressFailed = App.globalContext.getString(R.string.decompress_failed)
        val installApkInstalled = App.globalContext.getString(R.string.install_apk_installed)
        val installApkFailed = App.globalContext.getString(R.string.install_apk_failed)
        val setSELinuxFailed = App.globalContext.getString(R.string.set_SELinux_failed)
        val getAppVersionFailed = App.globalContext.getString(R.string.get_app_version_failed)
        val getAppVersionCodeFailed =
            App.globalContext.getString(R.string.get_app_version_code_failed)
        val broken = App.globalContext.getString(R.string.broken)
        val failed = App.globalContext.getString(R.string.failed)
        val error = App.globalContext.getString(R.string.error)
        val application = App.globalContext.getString(R.string.application)
        val data = App.globalContext.getString(R.string.data)
        val otgChangeDir = App.globalContext.getString(R.string.otg_change_dir)
        val otgNotSupport = App.globalContext.getString(R.string.otg_not_support)
        val symbolTick = App.globalContext.getString(R.string.symbol_tick)
        val symbolCross = App.globalContext.getString(R.string.symbol_cross)
        val symbolDot = App.globalContext.getString(R.string.symbol_dot)
        val symbolQuestion = App.globalContext.getString(R.string.symbol_question)
        val symbolExclamation = App.globalContext.getString(R.string.symbol_exclamation)
        val grantRootAccess = App.globalContext.getString(R.string.grant_root_access)
        val releasePrebuiltBinaries =
            App.globalContext.getString(R.string.release_prebuilt_binaries)
        val activateBashrc = App.globalContext.getString(R.string.activate_bashrc)
        val finish = App.globalContext.getString(R.string.finish)
        val nextStep = App.globalContext.getString(R.string.next_step)
        val root = App.globalContext.getString(R.string.root)
        val support = App.globalContext.getString(R.string.support)
        val fetchFailed = App.globalContext.getString(R.string.fetch_failed)
        val notPluggedIn = App.globalContext.getString(R.string.not_plugged_in)
        val latest = App.globalContext.getString(R.string.latest)
        val unsupportedFormat = App.globalContext.getString(R.string.unsupported_format)
        val fetching = App.globalContext.getString(R.string.fetching)
        val total = App.globalContext.getString(R.string.total)
        val backup = App.globalContext.getString(R.string.backup)
        val ready = App.globalContext.getString(R.string.ready)
        val edit = App.globalContext.getString(R.string.edit)
        val backupDirCreateFailed = App.globalContext.getString(R.string.backup_dir_create_failed)
        val restore = App.globalContext.getString(R.string.restore)
        val confirmRemove = App.globalContext.getString(R.string.confirm_remove)
        val removeFilesToo = App.globalContext.getString(R.string.remove_files_too)
        val removeFailed = App.globalContext.getString(R.string.remove_failed)
        val user = App.globalContext.getString(R.string.user)
        val retrieveFinish = App.globalContext.getString(R.string.retrieve_finish)
        val appRetrieved = App.globalContext.getString(R.string.app_retrieved)
        val confirmRemoveAllAppAndDataThatBackedUp =
            App.globalContext.getString(R.string.confirm_remove_all_app_and_data_that_backed_up)
        val pleaseWait = App.globalContext.getString(R.string.please_wait)
        val log = App.globalContext.getString(R.string.log)
        val size = App.globalContext.getString(R.string.size)
        val storedIn = App.globalContext.getString(R.string.stored_in)
        val binPermissionError = App.globalContext.getString(R.string.bin_permission_error)
        val disagree = App.globalContext.getString(R.string.disagree)
        val agree = App.globalContext.getString(R.string.agree)
        val cancel = App.globalContext.getString(R.string.cancel)
        val backupDirNotExist = App.globalContext.getString(R.string.backup_dir_not_exist)
        val pickDir = App.globalContext.getString(R.string.pick_dir)
        val create = App.globalContext.getString(R.string.create)
        val repeatToAdd = App.globalContext.getString(R.string.repeat_to_add)
        val installing = App.globalContext.getString(R.string.installing)
        val currentVersion = App.globalContext.getString(R.string.current_version)
        val confirmExit = App.globalContext.getString(R.string.confirm_exit)
        val all = App.globalContext.getString(R.string.all)
        val progress = App.globalContext.getString(R.string.progress)
        val clickTheRightBtnToStart = App.globalContext.getString(R.string.clickTheRightBtnToStart)
        val clickTheRightBtnToFinish =
            App.globalContext.getString(R.string.clickTheRightBtnToFinish)
        val symbolTriangle = App.globalContext.getString(R.string.symbol_triangle)
        val symbolAntiTriangle = App.globalContext.getString(R.string.symbol_anti_triangle)
        val alphabet = App.globalContext.getString(R.string.alphabet)
        val installTime = App.globalContext.getString(R.string.install_time)
        val sort = App.globalContext.getString(R.string.sort)
        val filter = App.globalContext.getString(R.string.filter)
        val cover = App.globalContext.getString(R.string.cover)
        val byTime = App.globalContext.getString(R.string.by_time)
        val removeSelectedBackupFiles = App.globalContext.getString(R.string.remove_selected_backup_files)
        val prepareForDownloading = App.globalContext.getString(R.string.prepare_for_downloading)
        val devFuse = App.globalContext.getString(R.string.dev_fuse)
        val configuration = App.globalContext.getString(R.string.configuration)
        val serverAddressEmptyError = App.globalContext.getString(R.string.server_address_empty_error)
        val nameEmptyError = App.globalContext.getString(R.string.name_empty_error)
        val notSelected = App.globalContext.getString(R.string.not_selected)
    }
}