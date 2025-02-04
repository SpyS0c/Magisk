package com.topjohnwu.magisk.ui.module

import androidx.databinding.Bindable
import com.topjohnwu.magisk.BR
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.core.Info
import com.topjohnwu.magisk.core.model.module.LocalModule
import com.topjohnwu.magisk.databinding.DiffRvItem
import com.topjohnwu.magisk.databinding.ObservableDiffRvItem
import com.topjohnwu.magisk.databinding.RvContainer
import com.topjohnwu.magisk.databinding.set
import com.topjohnwu.magisk.utils.asText

object InstallModule : DiffRvItem<InstallModule>() {
    override val layoutRes = R.layout.item_module_download
}

class LocalModuleRvItem(
    override val item: LocalModule
) : ObservableDiffRvItem<LocalModuleRvItem>(), RvContainer<LocalModule> {

    override val layoutRes = R.layout.item_module_md2

    @get:Bindable
    var isEnabled = item.enable
        set(value) = set(value, field, { field = it }, BR.enabled, BR.updateReady) {
            item.enable = value
        }

    @get:Bindable
    var isRemoved = item.remove
        set(value) = set(value, field, { field = it }, BR.removed, BR.updateReady) {
            item.remove = value
        }

    @get:Bindable
    var updateReady: Boolean
        get() = item.updateInfo != null && !isRemoved && isEnabled
        set(_) = notifyPropertyChanged(BR.updateReady)

    val isSuspended =
        (Info.isZygiskEnabled && item.isRiru) || (!Info.isZygiskEnabled && item.isZygisk)

    val suspendText =
        if (item.isRiru) R.string.suspend_text_riru.asText(R.string.zygisk.asText())
        else R.string.suspend_text_zygisk.asText(R.string.zygisk.asText())

    val isUpdated get() = item.updated

    fun delete() {
        isRemoved = !isRemoved
    }

    override fun itemSameAs(other: LocalModuleRvItem): Boolean = item.id == other.item.id
}
