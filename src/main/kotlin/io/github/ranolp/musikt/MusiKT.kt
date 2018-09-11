@file:JvmName("MusiKT")

package io.github.ranolp.musikt

import io.github.ranolp.musikt.util.javafx.CustomStage
import io.github.ranolp.musikt.view.DesktopMusicPlayer
import io.github.ranolp.musikt.view.Style
import io.github.ranolp.musikt.view.WindowTitle
import javafx.scene.Scene
import javafx.stage.Stage
import tornadofx.*


fun main(args: Array<String>) {
    launch<MusiKTApp>(args)
}

class MusiKTApp : App(DesktopMusicPlayer::class, Style::class) {
    private val myStage = CustomStage()
    override fun createPrimaryScene(view: UIComponent): Scene = myStage.apply(super.createPrimaryScene(view))

    override fun start(stage: Stage) {
        myStage.set(top = WindowTitle(myStage).root)
        super.start(myStage)
        myStage.minWidth = 400.0 + 16
        myStage.minHeight = 240.0 + 16
    }
}
