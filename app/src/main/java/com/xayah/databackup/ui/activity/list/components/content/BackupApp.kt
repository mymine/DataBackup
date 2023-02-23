package com.xayah.databackup.ui.activity.list.components.content

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.xayah.databackup.R
import com.xayah.databackup.data.*
import com.xayah.databackup.ui.activity.list.ListViewModel
import com.xayah.databackup.ui.activity.list.components.AppBackupItem
import com.xayah.databackup.ui.activity.list.components.ListBottomSheet
import com.xayah.databackup.ui.activity.list.components.ManifestDescItem
import com.xayah.databackup.ui.activity.list.components.SearchBar
import com.xayah.databackup.ui.activity.processing.ProcessingActivity
import com.xayah.databackup.util.*
import java.text.Collator
import java.util.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
fun LazyListScope.contentAppBackup(list: List<AppInfoBackup>, onSearch: (String) -> Unit) {
    item {
        SearchBar(onSearch)
    }
    items(
        count = list.size,
        key = {
            list[it].detailBase.packageName
        }) { index ->
        AppBackupItem(
            modifier = Modifier.animateItemPlacement(),
            appInfoBackup = list[index]
        )
    }
}

suspend fun onAppBackupInitialize(viewModel: ListViewModel) {
    if (GlobalObject.getInstance().appInfoBackupMap.value.isEmpty()) {
        GlobalObject.getInstance().appInfoBackupMap.emit(Command.getAppInfoBackupMap())
    }
    if (viewModel.appBackupList.value.isEmpty()) {
        filterAppBackupNone(viewModel)
        sortAppBackupByAlphabet(viewModel, viewModel.ascending.value)
    }
    viewModel.isInitialized.targetState = true
}

@ExperimentalMaterial3Api
fun LazyListScope.onAppBackupManifest(viewModel: ListViewModel, context: Context) {
    val list = listOf(
        ManifestDescItem(
            title = context.getString(R.string.selected_app),
            subtitle = run {
                var size = 0
                for (i in viewModel.appBackupList.value) {
                    if (i.selectApp) size++
                }
                size.toString()
            },
            iconId = R.drawable.ic_round_apps
        ),
        ManifestDescItem(
            title = context.getString(R.string.selected_data),
            subtitle = run {
                var size = 0
                for (i in viewModel.appBackupList.value) {
                    if (i.selectData) size++
                }
                size.toString()
            },
            iconId = R.drawable.ic_round_database
        ),
        ManifestDescItem(
            title = context.getString(R.string.backup_user),
            subtitle = context.readBackupUser(),
            iconId = R.drawable.ic_round_person
        ),
        ManifestDescItem(
            title = context.getString(R.string.restore_user),
            subtitle = context.readRestoreUser(),
            iconId = R.drawable.ic_round_iphone
        ),
        ManifestDescItem(
            title = context.getString(R.string.compression_type),
            subtitle = context.readCompressionType(),
            iconId = R.drawable.ic_round_bolt
        ),
        ManifestDescItem(
            title = context.getString(R.string.backup_strategy),
            subtitle = ofBackupStrategy(context.readBackupStrategy()),
            iconId = -1,
            icon = Icons.Rounded.Place
        ),
        ManifestDescItem(
            title = context.getString(R.string.backup_dir),
            subtitle = context.readBackupSavePath(),
            iconId = R.drawable.ic_round_folder_open
        ),
    )
    contentManifest(list)
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
fun LazyListScope.onAppBackupContent(viewModel: ListViewModel) {
    contentAppBackup(list = viewModel.appBackupList.value) { value ->
        viewModel.appBackupList.value.apply {
            viewModel.isInitialized.targetState = false
            clear()
            addAll(
                GlobalObject.getInstance().appInfoBackupMap.value.values.toList()
                    .filter { it.detailBase.isSystemApp.not() }
                    .filter {
                        it.detailBase.appName.lowercase()
                            .contains(value.lowercase()) ||
                                it.detailBase.packageName.lowercase()
                                    .contains(value.lowercase())
                    }
            )
            viewModel.isInitialized.targetState = true
        }
    }
}

@ExperimentalMaterial3Api
fun toAppBackupProcessing(context: Context) {
    context.startActivity(Intent(context, ProcessingActivity::class.java).apply {
        putExtra(TypeActivityTag, TypeBackupApp)
    })
}

suspend fun onAppBackupMapSave() {
    GsonUtil.saveAppInfoBackupMapToFile(GlobalObject.getInstance().appInfoBackupMap.value)
}

@ExperimentalMaterial3Api
@Composable
fun AppBackupBottomSheet(
    isOpen: MutableState<Boolean>,
    viewModel: ListViewModel,
) {
    val nonePadding = dimensionResource(R.dimen.padding_none)
    val tinyPadding = dimensionResource(R.dimen.padding_tiny)
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val iconSmallSize = dimensionResource(R.dimen.icon_small_size)
    val active = viewModel.activeSort.collectAsState()
    val ascending = viewModel.ascending.collectAsState()
    val filter = viewModel.filter.collectAsState()

    ListBottomSheet(
        isOpen = isOpen,
        actions = {
            item {
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(smallPadding))
                    .clickable {}
                    .padding(smallPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(tinyPadding)
                ) {
                    Icon(
                        modifier = Modifier.size(iconSmallSize),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_round_blacklist
                        ),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.blacklist),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                var selectApp = remember { true }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(smallPadding))
                        .clickable {
                            viewModel.appBackupList.value.forEach {
                                it.selectApp = selectApp
                            }
                            selectApp = selectApp.not()
                        }
                        .padding(smallPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(tinyPadding)
                ) {
                    Icon(
                        modifier = Modifier.size(iconSmallSize),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_round_check
                        ),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.select_all),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                var selectAll = remember { true }
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(smallPadding))
                    .clickable {
                        viewModel.appBackupList.value.forEach {
                            it.selectApp = selectAll
                            it.selectData = selectAll
                        }
                        selectAll = selectAll.not()
                    }
                    .padding(smallPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(tinyPadding)
                ) {
                    Icon(
                        modifier = Modifier.size(iconSmallSize),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_round_done_all
                        ),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.select_all),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        content = {
            // 排序
            Text(
                text = stringResource(id = R.string.sort),
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(smallPadding)
            ) {
                AssistChip(
                    onClick = {
                        viewModel.activeSort.value = AppListSort.Alphabet
                        viewModel.ascending.value = viewModel.ascending.value.not()
                        sortAppBackupByAlphabet(viewModel, viewModel.ascending.value)
                    },
                    label = { Text(stringResource(id = R.string.alphabet)) },
                    leadingIcon = if (active.value == AppListSort.Alphabet) {
                        {
                            Icon(
                                imageVector = if (ascending.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                AssistChip(
                    onClick = {
                        viewModel.activeSort.value = AppListSort.FirstInstallTime
                        viewModel.ascending.value = viewModel.ascending.value.not()
                        sortAppBackupByInstallTime(viewModel, viewModel.ascending.value)
                    },
                    label = { Text(stringResource(id = R.string.install_time)) },
                    leadingIcon = if (active.value == AppListSort.FirstInstallTime) {
                        {
                            Icon(
                                imageVector = if (ascending.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                AssistChip(
                    onClick = {
                        viewModel.activeSort.value = AppListSort.DataSize
                        viewModel.ascending.value = viewModel.ascending.value.not()
                        sortAppBackupByDataSize(viewModel, viewModel.ascending.value)
                    },
                    label = { Text(stringResource(id = R.string.data_size)) },
                    leadingIcon = if (active.value == AppListSort.DataSize) {
                        {
                            Icon(
                                imageVector = if (ascending.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }

            // 过滤
            Text(
                modifier = Modifier.padding(nonePadding, mediumPadding, nonePadding, nonePadding),
                text = stringResource(id = R.string.filter),
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(smallPadding)
            ) {
                FilterChip(
                    selected = filter.value == AppListFilter.None,
                    onClick = {
                        if (viewModel.filter.value != AppListFilter.None) {
                            viewModel.filter.value = AppListFilter.None
                            filterAppBackupNone(viewModel)
                        }
                    },
                    label = { Text(stringResource(R.string.none)) },
                    leadingIcon = if (filter.value == AppListFilter.None) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                FilterChip(
                    selected = filter.value == AppListFilter.Selected,
                    onClick = {
                        if (viewModel.filter.value != AppListFilter.Selected) {
                            viewModel.filter.value = AppListFilter.Selected
                            filterAppBackupSelected(viewModel)
                        }
                    },
                    label = { Text(stringResource(R.string.selected)) },
                    leadingIcon = if (filter.value == AppListFilter.Selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
                FilterChip(
                    selected = filter.value == AppListFilter.NotSelected,
                    onClick = {
                        if (viewModel.filter.value != AppListFilter.NotSelected) {
                            viewModel.filter.value = AppListFilter.NotSelected
                            filterAppBackupNotSelected(viewModel)
                        }
                    },
                    label = { Text(stringResource(R.string.not_selected)) },
                    leadingIcon = if (filter.value == AppListFilter.NotSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    )
}

fun sortAppBackupByAlphabet(
    viewModel: ListViewModel,
    ascending: Boolean
) {
    if (ascending)
        viewModel.appBackupList.value.sortWith { appInfo1, appInfo2 ->
            if (appInfo1 == null && appInfo2 == null) {
                0
            } else if (appInfo1 == null) {
                -1
            } else if (appInfo2 == null) {
                1
            } else {
                val collator = Collator.getInstance(Locale.CHINA)
                collator.getCollationKey(appInfo1.detailBase.appName)
                    .compareTo(collator.getCollationKey(appInfo2.detailBase.appName))
            }
        }
    else
        viewModel.appBackupList.value.sortWith { appInfo1, appInfo2 ->
            if (appInfo1 == null && appInfo2 == null) {
                0
            } else if (appInfo1 == null) {
                -1
            } else if (appInfo2 == null) {
                1
            } else {
                val collator = Collator.getInstance(Locale.CHINA)
                collator.getCollationKey(appInfo2.detailBase.appName)
                    .compareTo(collator.getCollationKey(appInfo1.detailBase.appName))
            }
        }
}

fun sortAppBackupByInstallTime(
    viewModel: ListViewModel,
    ascending: Boolean
) {
    if (ascending)
        viewModel.appBackupList.value.sortBy { it.firstInstallTime }
    else
        viewModel.appBackupList.value.sortByDescending { it.firstInstallTime }
}

fun sortAppBackupByDataSize(
    viewModel: ListViewModel,
    ascending: Boolean
) {
    if (ascending)
        viewModel.appBackupList.value.sortBy { it.storageStats.sizeBytes }
    else
        viewModel.appBackupList.value.sortByDescending { it.storageStats.sizeBytes }
}

fun filterAppBackupNone(
    viewModel: ListViewModel,
) {
    viewModel.appBackupList.value.clear()
    viewModel.appBackupList.value.addAll(
        GlobalObject.getInstance().appInfoBackupMap.value.values.toList()
            .filter { it.detailBase.isSystemApp.not() }
    )
}

fun filterAppBackupSelected(
    viewModel: ListViewModel,
) {
    viewModel.appBackupList.value.clear()
    viewModel.appBackupList.value.addAll(
        GlobalObject.getInstance().appInfoBackupMap.value.values.toList()
            .filter { it.detailBase.isSystemApp.not() && (it.detailBackup.selectApp || it.detailBackup.selectData) }
    )
}

fun filterAppBackupNotSelected(
    viewModel: ListViewModel,
) {
    viewModel.appBackupList.value.clear()
    viewModel.appBackupList.value.addAll(
        GlobalObject.getInstance().appInfoBackupMap.value.values.toList()
            .filter { it.detailBase.isSystemApp.not() && (it.detailBackup.selectApp.not() && it.detailBackup.selectData.not()) }
    )
}