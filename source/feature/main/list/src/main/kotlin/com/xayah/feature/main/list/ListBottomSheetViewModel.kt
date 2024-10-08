package com.xayah.feature.main.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xayah.core.data.repository.AppsRepo
import com.xayah.core.data.repository.LabelsRepo
import com.xayah.core.data.repository.ListData
import com.xayah.core.data.repository.ListDataRepo
import com.xayah.core.hiddenapi.castTo
import com.xayah.core.model.App
import com.xayah.core.model.File
import com.xayah.core.model.OpType
import com.xayah.core.model.SortType
import com.xayah.core.model.Target
import com.xayah.core.model.database.LabelEntity
import com.xayah.core.model.database.PackageDataStates
import com.xayah.core.model.util.of
import com.xayah.core.ui.route.MainRoutes
import com.xayah.core.util.decodeURL
import com.xayah.core.util.launchOnDefault
import com.xayah.feature.main.list.ListBottomSheetUiState.Loading
import com.xayah.feature.main.list.ListBottomSheetUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ListBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val listDataRepo: ListDataRepo,
    private val appsRepo: AppsRepo,
    labelsRepo: LabelsRepo
) : ViewModel() {
    private val target: Target = Target.valueOf(savedStateHandle.get<String>(MainRoutes.ARG_TARGET)!!.decodeURL().trim())
    private val opType: OpType = OpType.of(savedStateHandle.get<String>(MainRoutes.ARG_OP_TYPE)?.decodeURL()?.trim())

    val uiState: StateFlow<ListBottomSheetUiState> = when (target) {
        Target.Apps -> combine(
            listDataRepo.getListData(),
            listDataRepo.getAppList(),
            labelsRepo.getLabels()
        ) { lData, aList, labels ->
            val listData = lData.castTo<ListData.Apps>()
            Success.Apps(
                opType = opType,
                showFilterSheet = listData.showFilterSheet,
                sortIndex = listData.sortIndex,
                sortType = listData.sortType,
                labels = labels,
                labelIds = listData.labelIds,
                showDataItemsSheet = listData.showDataItemsSheet,
                showSystemApps = listData.showSystemApps,
                appList = aList,
            )
        }

        Target.Files -> combine(
            listDataRepo.getListData(),
            listDataRepo.getFileList(),
            labelsRepo.getLabels()
        ) { lData, fList, labels ->
            val listData = lData.castTo<ListData.Files>()
            Success.Files(
                opType = opType,
                showFilterSheet = listData.showFilterSheet,
                sortIndex = listData.sortIndex,
                sortType = listData.sortType,
                labels = labels,
                labelIds = listData.labelIds,
                fileList = fList,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun setShowFilterSheet(value: Boolean) {
        viewModelScope.launchOnDefault {
            listDataRepo.setShowFilterSheet(value)
        }
    }

    fun setShowDataItemsSheet(value: Boolean) {
        viewModelScope.launchOnDefault {
            listDataRepo.setShowDataItemsSheet(value)
        }
    }

    fun setShowSystemApps() {
        viewModelScope.launchOnDefault {
            if (uiState.value is Success.Apps) {
                var isShow = false
                listDataRepo.setShowSystemApps {
                    isShow = it.not()
                    it.not()
                }
                val state = uiState.value.castTo<Success.Apps>()
                if (isShow.not()) {
                    appsRepo.unselectAll(state.appList.filter { it.isSystemApp }.map { it.id })
                }
            }
        }
    }

    fun setSortByType() {
        viewModelScope.launchOnDefault {
            listDataRepo.setSortType { if (it == SortType.ASCENDING) SortType.DESCENDING else SortType.ASCENDING }
        }
    }

    fun setSortByIndex(index: Int) {
        viewModelScope.launchOnDefault {
            listDataRepo.setSortIndex { index }
        }
    }

    fun addOrRemoveLabelId(id: Long) {
        viewModelScope.launchOnDefault {
            if (uiState.value is Success) {
                val state = uiState.value.castTo<Success>()
                if (id in state.labelIds) {
                    listDataRepo.removeLabelId(id)
                } else {
                    listDataRepo.addLabelId(id)
                }
            }
        }
    }

    fun setDataItems(selections: PackageDataStates) {
        viewModelScope.launchOnDefault {
            if (uiState.value is Success.Apps) {
                val state = uiState.value.castTo<Success.Apps>()
                appsRepo.setDataItems(state.appList.filter { it.selected }.map { it.id }, selections)
            }
        }
    }
}

sealed interface ListBottomSheetUiState {
    data object Loading : ListBottomSheetUiState
    sealed class Success(
        open val opType: OpType,
        open val showFilterSheet: Boolean,
        open val sortIndex: Int,
        open val sortType: SortType,
        open val labels: List<LabelEntity>,
        open val labelIds: Set<Long>,
    ) : ListBottomSheetUiState {
        data class Apps(
            override val opType: OpType,
            override val showFilterSheet: Boolean,
            override val sortIndex: Int,
            override val sortType: SortType,
            override val labels: List<LabelEntity>,
            override val labelIds: Set<Long>,
            val showDataItemsSheet: Boolean,
            val showSystemApps: Boolean,
            val appList: List<App>,
        ) : Success(opType, showFilterSheet, sortIndex, sortType, labels, labelIds)

        data class Files(
            override val opType: OpType,
            override val showFilterSheet: Boolean,
            override val sortIndex: Int,
            override val sortType: SortType,
            override val labels: List<LabelEntity>,
            override val labelIds: Set<Long>,
            val fileList: List<File>,
        ) : Success(opType, showFilterSheet, sortIndex, sortType, labels, labelIds)
    }
}
