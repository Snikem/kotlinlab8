import java.io.BufferedInputStream
import java.io.IOException
import java.util.concurrent.Executors
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

object PlayMusic {
    fun play(path: String?): Runnable {
        return Runnable {
            val classloader = Thread.currentThread().getContextClassLoader()
            val `is` = classloader.getResourceAsStream(path)
            try {
                val sound = AudioSystem.getAudioInputStream(BufferedInputStream(`is`))
                val clip = AudioSystem.getClip()
                clip.open(sound)
                val vc = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                vc.setValue(vc.maximum)
                clip.framePosition = 0 //устанавливаем указатель на старт
                clip.start()
                Thread.sleep(clip.microsecondLength / 1000)
                clip.stop() //Останавливаем
                clip.close()
            } catch (e: UnsupportedAudioFileException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: LineUnavailableException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val executorService = Executors.newFixedThreadPool(3)
        executorService.submit(play("fi.wav"))
        executorService.submit(play("se.wav"))
        executorService.submit(play("th.wav"))
    }
}
