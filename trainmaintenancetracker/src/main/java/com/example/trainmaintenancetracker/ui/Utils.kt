package com.example.trainmaintenancetracker.ui

import com.example.trainmaintenancetracker.domain.model.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<Task>.asImmutable(): ImmutableList<Task> = this.toImmutableList()
