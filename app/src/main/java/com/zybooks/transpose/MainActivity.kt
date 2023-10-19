package com.zybooks.transpose

import Note
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.marginTop
import java.security.AccessController.getContext
import android.content.res.Resources

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
class MainActivity : AppCompatActivity() {

    //decleration of variables
    var noteCounter : Int = 0 //note counter is used to determine which image view to place the next note in - - should be replaced with vector.size once vector of objects is created
    lateinit var notePlace : ImageView //notePlace will be used as a variable to hold the place of the next note to be placed on the scale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // declare vals for buttons and image views
        val wholenoteIB : ImageButton = findViewById<ImageButton>(R.id.wholenoteIB)
        val halfnoteIB : ImageButton = findViewById<ImageButton>(R.id.halfnoteIB)
        val quarternoteIB : ImageButton = findViewById<ImageButton>(R.id.quarternoteIB)
        val eighthnoteIB : ImageButton = findViewById<ImageButton>(R.id.eighthnoteIB)
        val sixteenthnoteIB : ImageButton = findViewById<ImageButton>(R.id.sixteenthnoteIB)
        val noteoneIV : ImageView = findViewById<ImageView>(R.id.noteoneIV)
        val notetwoIV : ImageView = findViewById<ImageView>(R.id.notetwoIV)
        val notethreeIV : ImageView = findViewById<ImageView>(R.id.notethreeIV)
        val notefourIV : ImageView = findViewById<ImageView>(R.id.notefourIV)
        val uparrowIB : ImageButton = findViewById<ImageButton>(R.id.uparrowIB)
        val downarrowIB : ImageButton = findViewById<ImageButton>(R.id.downarrowIB)

        //function determineIV uses class's note counter to return a place for the next note to be
        fun determineIV() : ImageView {
            when(noteCounter% 4){
                0-> return noteoneIV
                1-> return notetwoIV
                2-> return notethreeIV
                3-> return notefourIV
                else -> return noteoneIV
            }
        }

        //set initial visibility for some image views and buttons to be invisible
        noteoneIV.visibility = View.INVISIBLE
        notetwoIV.visibility = View.INVISIBLE
        notethreeIV.visibility = View.INVISIBLE
        notefourIV.visibility = View.INVISIBLE
        uparrowIB.visibility = View.INVISIBLE
        downarrowIB.visibility = View.INVISIBLE

        //disables invisible buttons so they cant accidentally be pressed
        uparrowIB.isActivated = false
        downarrowIB.isActivated = false
        //creation of on click listeners
        wholenoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.wholenotesmall,uparrowIB,downarrowIB)
        }
        halfnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.halfnotesmall,uparrowIB,downarrowIB)
        }
        quarternoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.quarternotesmall,uparrowIB,downarrowIB)
        }
        eighthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.eighthnotesmall,uparrowIB,downarrowIB)
        }
        sixteenthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.sixteenthnotesmall,uparrowIB,downarrowIB)
        }
        uparrowIB.setOnClickListener{
            moveNote(notePlace,R.drawable.uparrowsmall,uparrowIB,downarrowIB)
        }
        downarrowIB.setOnClickListener{
            moveNote(notePlace,R.drawable.downarrowsmall,uparrowIB,downarrowIB)
        }
    }

    fun createNote(currentIV : ImageView, currentImage : Int, uparrow : ImageButton, downarrow : ImageButton) {
        noteCounter++
        currentIV?.let {
            it.setImageResource(currentImage)
            it.visibility = View.VISIBLE
            uparrow.y = currentIV.y - 100
            downarrow.y = currentIV.y + 100
            uparrow.x = currentIV.x
            downarrow.x = currentIV.x
            if (currentImage == R.drawable.wholenotesmall) {
                uparrow.bottom = (uparrow.bottom + 70)
                currentIV.scaleX = .5.toFloat()
                currentIV.scaleY = .5.toFloat()
                currentIV.y = (currentIV.y + 35)
            } else {
                currentIV.scaleX = 1.toFloat()
                currentIV.scaleY = 1.toFloat()
            }
            currentIV.let {
                currentIV.setImageResource(currentImage)
                currentIV.visibility = View.VISIBLE
                uparrow.visibility = View.VISIBLE
                downarrow.visibility = View.VISIBLE
            }
        }
    }

    enum class KeySignature(val number: Int, val accidents: Array<Int>, var sORb : Char) {

        /*
        Legend
        M = major
        m = minor
        s = sharp
        b = flat
        n = natural
        sharps order = FCGDAEB
        flats order = BEADGCF
        arrayListOrder = (C, D, E, F, G, A, B)
         */

        //No accidentals
        CM_am(0, arrayOf(0,0,0,0,0,0,0), 'n'),

        //Sharps
        GM_em(7, arrayOf(0,0,0,1,0,0,0), 's'), //1 sharp
        DM_bm(2, arrayOf(1,0,0,1,0,0,0), 's'), //2 sharps
        AM_fsm(9, arrayOf(1,0,0,1,1,0,0), 's'), //3 sharps
        EM_csm(4, arrayOf(1,1,0,1,1,0,0), 's'), //4 sharps
        BM_gsm(11, arrayOf(1,1,0,1,1,1,0), 's'), //5 sharps
        FsM_dsm(5, arrayOf(1,1,1,1,1,1,0), 's'), //6 sharps
        CsM(1, arrayOf(1,1,1,1,1,1,1), 's'), //7 sharps

        //Flats
        FM_dm(5, arrayOf(0,0,0,-1,0,0,0), 'b'), //1 flat
        BbM_gm(10, arrayOf(1,0,0,-1,0,0,0), 'b'), //2 flats
        EbM_cm(3, arrayOf(-1,0,0,-1,-1,0,0), 'b'), //3 flats
        AbM_fm(8, arrayOf(-1,-1,0,-1,-1,0,0), 'b'), //4 flats
        DbM_bbm(1, arrayOf(-1,-1,0,-1,-1,-1,0), 'b'), //5 flats
        GbM_ebm(6, arrayOf(-1,-1,-1,-1,-1,-1,0), 'b'), //6 flats
        CbM(11, arrayOf(-1,-1,-1,-1,-1,-1,-1), 'b') //7 flats
    }

    fun transpose(note: Note, fromKey: KeySignature, toKey: KeySignature) : Pair<Int, Char> {
        var difference : Int = toKey.number - fromKey.number //Calculate real number of steps between two notes
        var (UIDifference, flag) = getUIDifference(note, fromKey, toKey) //Get visual number of steps and if it needs an accidental
        note.forcePositionOnScale(note.positionOnScale - difference) //Update note
        return Pair(UIDifference, flag) //Return pair of visual steps with accidental
    }

    fun getUIDifference(note: Note, fromKey: KeySignature, toKey: KeySignature ) : Pair<Int, Char> {
        var UIDifference : Int = 0 //Visual number of steps
        var difference : Int = toKey.number - fromKey.number //Real number of steps
        var flag : Char = 'n' //n = no change. b = flat, s = sharp
        var availableNotes : Array<String> = arrayOf()

        //List of notes for all keys
        val noteList_Sharps : Array<String> = arrayOf("c", "cs", "d", "ds", "e", "f", "fs", "g", "gs", "a", "as", "b")
        val noteList_Fs : Array<String> = arrayOf("c", "cs", "d", "ds", "e", "es", "fs", "g", "gs", "a", "as", "b")
        val noteList_Cs : Array<String> = arrayOf("bs", "cs", "d", "ds", "e", "es", "fs", "g", "gs", "a", "as", "b")

        val noteList_Flats :  Array<String> = arrayOf("c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "b")
        val noteList_Gb : Array<String> = arrayOf("c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "cb")
        val noteList_Cb : Array<String> = arrayOf("c", "db", "d", "eb", "fb", "f", "gb", "g", "ab", "a", "bb", "cb")

        when (toKey.number) {
            1 -> { //CsM or DbM
                if (toKey.sORb == 's')
                    availableNotes = noteList_Cs

                else if (toKey.sORb == 'b')
                    availableNotes = noteList_Flats
            }
            5 -> { //FsM or GbM
                if (toKey.sORb == 's')
                    availableNotes = noteList_Fs

                else if (toKey.sORb == 'b')
                    availableNotes = noteList_Gb
            }
            11 -> { //CbM or BM
                if (toKey.sORb == 's')
                    availableNotes = noteList_Sharps

                else if (toKey.sORb == 'b')
                    availableNotes = noteList_Cb
            }
            else -> { //All other keys
                if (toKey.sORb == 'b')
                    availableNotes = noteList_Flats
                else
                    availableNotes = noteList_Sharps
            }
        }

        var newNoteName : String = availableNotes[((note.positionOnScale % 12) + difference) % 12] //Get new note name from available notes
        val noteToVal : Array<Pair<String, Int>> //Holds visual difference values
        noteToVal = arrayOf(Pair("cb", 0), Pair("c", 0), Pair("cs", 0), Pair("db", 1), Pair("d", 1), Pair("ds", 1), Pair("eb", 2),
                            Pair("e", 2), Pair("es", 2), Pair("fb", 3), Pair("f", 3), Pair("fs", 3), Pair("gb", 4), Pair("g", 4),
                            Pair("gs", 4), Pair("ab", 5), Pair("a", 5), Pair("as", 5), Pair("bb", 6), Pair("b", 6), Pair("bs", 6))

        var firstNoteVal : Int = 0 //Original visual value
        var secondNoteVal : Int = 0 //New visual value

        for (i in noteToVal){
            if (note.getNoteName() == i.first) //If name matches string in list, then set the visual value
                firstNoteVal = i.second
            if (newNoteName == i.first)
                secondNoteVal = i.second
        }

        UIDifference = secondNoteVal - firstNoteVal //Update visual difference

        if (newNoteName.length == 2){ //Sharp or flat notes have 2 characters, set flag
            if (newNoteName.substring(1,1) == "s")
                flag = 's'
            else
                flag = 'b'
        }

        if (toKey.accidents[secondNoteVal] != 0) //If the key signature holds a flat or sharp at this key, then remove the accidental
            flag = 'n'

        return Pair(UIDifference, flag)

    }

    fun moveNote(currentIV : ImageView,currentImage : Int, uparrow : ImageButton, downarrow : ImageButton){
        if(currentImage == R.drawable.uparrowsmall)
        {
            uparrow.y = uparrow.y -14
            downarrow.y=downarrow.y - 14
            currentIV.y = currentIV.y - 14
        }
        else{
            uparrow.y = uparrow.y +14
            downarrow.y=downarrow.y + 14
            currentIV.y = currentIV.y + 14
        }
    }
}
