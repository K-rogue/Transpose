import android.content.Context
import android.media.SoundPool
import android.media.AudioAttributes

data class Note(
    var noteType: String,
    var positionOnScale: Int
) {
    private val soundPool: SoundPool
    private var soundId: Int? = null
    var noteName: String = calculateNoteName()
        private set
    var duration: Double = calculateDuration()
        private set
    var octave: Int = calculateOctave()
        private set

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun calculateNoteName(): String {
        val noteNames = listOf("c","db", "d","eb","e","f","gb","g","ab","a","bb","b")
        return noteNames[(positionOnScale - 1) % 12]
    }

    private fun calculateOctave(): Int {
        val noteNames = listOf("c","db", "d","eb","e","f","gb","g","ab","a","bb","b")
        return (positionOnScale - 1) / 12
    }

    private fun calculateDuration(): Double {
        val noteDurations = mapOf(
            "whole" to 4.0,
            "half" to 2.0,
            "quarter" to 1.0,
            "eighth" to 0.5,
            "sixteenth" to 0.25
        )
        return noteDurations[noteType] ?: 0.0
    }

    fun loadSound(context: Context) {
        val resourceId = context.resources.getIdentifier(
            "$positionOnScale$octave", "raw", context.packageName
        )
        soundId = soundPool.load(context, resourceId, 1)
    }

    fun playNote() {
        soundId?.let {
            soundPool.play(it, 1f, 1f, 1, 0, 1f)
        }
    }
}

