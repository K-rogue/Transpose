import android.content.Context
import android.media.SoundPool
import android.media.AudioAttributes
import android.util.Log

data class Note(var noteType: String, var positionOnScale: Int) {
    private val soundPool: SoundPool
    private var soundId: Int? = null
    private var noteName : String
    private var duration : Double
    private var isSoundLoaded: Boolean = false
    private var realValue : Int = 0
    var accidental : Char = ' '
    var topStaff : Boolean = true

    init {
        noteName = calculateNoteName()
        duration = calculateDuration()
        forcePositionOnScale(positionOnScale, accidental)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status != 0) {
                // There was an error loading the sound.
                Log.e("Note", "Error loading sound: $status for note $noteName")
            } else {
                // Sound loaded successfully.
                Log.d("Note", "Sound loaded successfully for note $noteName!")
                isSoundLoaded = true
                playNote() // Now play the note once it's loaded
            }
        }
    }

    fun calculateNoteName(): String {
        val noteNames = listOf("c","db", "d","eb","e","f","gb","g","ab","a","bb","b")
        val oct = ((positionOnScale) / 12) + 2
        val letter = noteNames[(positionOnScale) % 12]
        noteName = "$letter$oct"
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

    fun forcePositionOnScale(positionOnScale: Int, flag: Char) {
        var temp: Int
        //start with note in space below the last bass line (F2), last note is space above top treble line (G5)
        //first val is scale position, second val is note position
        var scaleToVal = arrayOf    (Pair(0, 5), Pair(1, 7), Pair(2, 9), Pair(3, 11), Pair(4, 12), Pair(5, 14),
                                    Pair(6, 16), Pair(7, 17), Pair(8, 19), Pair(9, 21), Pair(10, 23), Pair(11, 24),
                                    Pair(12, 26), Pair(13, 28), Pair(14, 29), Pair(15, 31), Pair(16, 33), Pair(17, 35),
                                    Pair(18, 36), Pair(19, 38), Pair(20, 40), Pair(21, 41), Pair(22, 43))
        temp = scaleToVal[positionOnScale].second
        if (flag == 'b') {
            temp--
            accidental = 'b'
        }
        else if (flag == 's') {
            temp++
            accidental = 's'
        }
        this.positionOnScale = positionOnScale
        this.realValue = temp
        noteName = calculateNoteName()
    }

    fun loadSound(context: Context) {
        val resourceId = context.resources.getIdentifier(
            "$noteName", "raw", context.packageName
        )
        soundId = soundPool.load(context, resourceId, 1)
    }

    fun playNote() {
        if (isSoundLoaded) {
            soundId?.let {
                soundPool.play(it, 1f, 1f, 1, 0, 1f)
            }
        } else {
            Log.w("Note", "Tried to play $noteName, but it's not loaded yet.")
        }
    }

}

