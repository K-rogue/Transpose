import android.content.Context
import android.media.SoundPool
import android.media.AudioAttributes

data class Note(var noteType: String, var positionOnScale: Int) {
    private val soundPool: SoundPool
    private var soundId: Int? = null
    private var noteName : String
    private var duration : Double

    init {
        noteName = calculateNoteName()
        duration = calculateDuration()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun calculateNoteName(): String {
        val noteNames = listOf("c","db", "d","eb","e","f","gb","g","ab","a","bb","b")
        val oct = ((positionOnScale - 1) / 12) + 2
        val letter = noteNames[(positionOnScale - 1) % 12]
        return "$letter$oct"
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

    fun retreiveNoteType(): String {
        return noteType
    }

    fun forceNoteType(noteType: String) {
        this.noteType = noteType
        duration = calculateDuration()
    }

    fun retrievePositionOnScale(): Int {
        return positionOnScale
    }

    fun getNoteName(): String {
        return noteName
    }

    fun forcePositionOnScale(positionOnScale: Int) {
        this.positionOnScale = positionOnScale
        noteName = calculateNoteName()
    }

    fun loadSound(context: Context) {
        val resourceId = context.resources.getIdentifier(
            "$noteName", "raw", context.packageName
        )
        soundId = soundPool.load(context, resourceId, 1)
    }

    fun playNote() {
        soundId?.let {
            soundPool.play(it, 1f, 1f, 1, 0, 1f)
        }
    }
}

