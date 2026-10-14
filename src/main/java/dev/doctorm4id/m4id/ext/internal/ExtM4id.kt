package dev.doctorm4id.m4id.ext.internal

import java.nio.file.Path

internal val Path.assured: Path
    get() = also {
        it.toFile().apply {
            mkdirs()
        }
    }
