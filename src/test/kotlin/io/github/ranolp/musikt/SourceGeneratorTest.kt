package io.github.ranolp.musikt

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.SourceData
import io.github.ranolp.musikt.source.SourceGenerator
import io.github.ranolp.musikt.source.guessYoutubeId
import io.github.ranolp.musikt.source.makeSoundcloudInput
import io.github.ranolp.musikt.source.makeYoutubeInput
import io.github.ranolp.musikt.source.soundcloud.SoundCloudSourceGenerator
import io.github.ranolp.musikt.source.youtube.YoutubeSourceGenerator
import kotlin.test.Test
import kotlin.test.fail

class SourceGeneratorTest {
    class Context<O : SourceData>(val generator: SourceGenerator<O>, val inputGenerator: (String) -> JsonObject?) {
        fun success(url: String, process: (O) -> Unit = {}) {
            val source = generator.generate(inputGenerator(url) ?: return)
            val data = source.data
            println(source.data)
            var lastPercentage = 0
            val download = source.get { percentage ->
                if (percentage == 100.0) {
                    println("${data.title} Donwload Complete!")
                } else if (percentage.toInt() % 5 != lastPercentage % 5) {
                    lastPercentage = percentage.toInt()
                    println("${data.title} $lastPercentage% downloaded")
                }
            }
            println(download)
            process(data)
        }

        fun successes(urls: List<String>, process: (O) -> Unit = {}) {
            for (url in urls) {
                success(url, process)
            }
        }

        fun failure(url: String) {
            try {
                val source = generator.generate(inputGenerator(url) ?: return)
                source.data
                source.get()
                fail()
            } catch (e: Throwable) {
            }
        }
    }

    @Test
    fun testYoutube() {
        Context(YoutubeSourceGenerator) {
            it.guessYoutubeId?.run(::makeYoutubeInput)
        }.apply {
            success("https://www.youtube.com/watch?v=2VY2NLE2Bn0&feature=youtu.be")
            success("https://www.youtube.com/watch?v=xcy9fdcQpso&index=22&list=PLldc0gMrgwigLCCPw9eOI8LbghsWFjmLB")
            success("https://youtu.be/uwfB74imdSQ")
            success("DlQFKYKdoek")
            failure("")
        }

    }

    @Test
    fun testSoundCloud() {
        Context(SoundCloudSourceGenerator) {
            makeSoundcloudInput(it)
        }.apply {
            success("https://soundcloud.com/wonderene/letter-songkorean-ver/")
            success("https://soundcloud.com/n1c0de/knots-way-feat-romelonfull-version")

            failure("https://soundcloud.com/asdasfsafsafasfasdsad/asdsafsafsad")
            failure("")
        }
    }
}
