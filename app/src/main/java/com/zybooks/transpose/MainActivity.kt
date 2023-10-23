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
import java.util.Vector

class MainActivity : AppCompatActivity() {

    //decleration of global variables variables
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
        val notesrestsBT : Button = findViewById<Button>(R.id.notesrestsBT)

        // decleration of variables
        var notes : Boolean = true // used to keep track of if notes or rests are being displayed
        var tempNote = Note("whole", 3) // will be used as a note to edit before adding to vector of notes
        var muse = Vector<Note>()// vector of notes that will be in charge of storing created music
        muse.ensureCapacity(100) //sets default minimum capacity to 100(not sure if we need this... vectors in kotlin are weird
        var currentPage : Int = 0//keeps track of what page you are on from left/right swipe features
        var pages : Int = 0//keeps track of how many pages there are

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
            notePlace = determineIV()// determines the place
            if(notes) {//if motes are being displayed
                tempNote = createNote(notePlace, R.drawable.wholenotesmall, uparrowIB, downarrowIB, notes) // set tempNote to the result of the createNote function
            }
            else{//if rests are being displayed
                tempNote = createNote(notePlace, R.drawable.wholerestsmall, uparrowIB, downarrowIB, notes)// set tempNote to result of the createNote function
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {//if the size of the vector(size equals 0 on first call as no note has been added yet) is larger than the number of pages(with 4 notes each) plus the number of notes on the current page, then a note was edited, not created
                if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType == "whole") {// if the previous note was a whole note
                    fromWholeNote(notePlace,uparrowIB,downarrowIB) // fix the height display issues by using fromWholeNote function
                }
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))] = tempNote//set the appropriate vector place to the temp note
            }
            else{// if the number of notes on the page plus the number of pages*4 notes per page is equal to the size of the vector, then a new note was created
                muse.addElement(tempNote)// so add that new note to the vector
            }
        }
        halfnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with halfnote image
            notePlace = determineIV()
            if(notes) {
                tempNote = createNote(notePlace, R.drawable.halfnotesmall, uparrowIB, downarrowIB, notes)
            }
            else{
                tempNote = createNote(notePlace, R.drawable.halfrestsmall, uparrowIB, downarrowIB, notes)
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType == "whole") {
                    fromWholeNote(notePlace,uparrowIB,downarrowIB)
                }
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        quarternoteIB.setOnClickListener {//same as wholeNoteIB but createNote is called with quarternote image
            notePlace = determineIV()
            if (notes) {
                tempNote = createNote(notePlace, R.drawable.quarternotesmall, uparrowIB, downarrowIB, notes)
            }
            else{
                tempNote = createNote(notePlace, R.drawable.quarterrestsmall, uparrowIB, downarrowIB, notes)
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType == "whole") {
                    fromWholeNote(notePlace,uparrowIB,downarrowIB)
                }
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        eighthnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with eighthNote image
            notePlace = determineIV()
            if(notes) {
                tempNote = createNote(notePlace, R.drawable.eighthnotesmall, uparrowIB, downarrowIB, notes)
            }
            else{
                tempNote = createNote(notePlace, R.drawable.eighthrestsmall, uparrowIB, downarrowIB, notes)
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType == "whole") {
                    fromWholeNote(notePlace,uparrowIB,downarrowIB)
                }
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        sixteenthnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with sixteenthNote image
            notePlace = determineIV()
            if(notes) {
                tempNote = createNote(notePlace,R.drawable.sixteenthnotesmall,uparrowIB,downarrowIB, notes)
            }
            else{
                tempNote = createNote(notePlace, R.drawable.sixteenthrestsmall, uparrowIB, downarrowIB, notes)
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType == "whole") {
                    fromWholeNote(notePlace,uparrowIB,downarrowIB)
                }
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        uparrowIB.setOnClickListener{
            if(muse[((currentPage * 4) + ((noteCounter - 1) % 4))].positionOnScale == 10 && !muse[((currentPage * 4) + ((noteCounter- 1) % 4))].topStaff) {//if the current note is on the bottom staff and the note position is 10(top of bottom staff)
                for(i in 1..12)// jump to top staff
                    moveNote(notePlace, R.drawable.uparrowsmall, uparrowIB, downarrowIB)
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale++//adjust the position in the class
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].topStaff = true//set the boolean of the note in the vector to be on the top staff
            }
            else if(muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale in 0..19) {//if not on the bottom staff, or not at the tenth position
                moveNote(notePlace, R.drawable.uparrowsmall, uparrowIB, downarrowIB)//move note up one position
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale++//adjust the position in the class
            }
        }
        downarrowIB.setOnClickListener{//same as up arrow but inversed top/bottom
            if(muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale == 10 && muse[((currentPage * 4) + ((noteCounter- 1) % 4))].topStaff) {
                for(i in 1..12)
                    moveNote(notePlace,R.drawable.downarrowsmall,uparrowIB,downarrowIB)
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale--
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].topStaff = false
            }
            else if(muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale in 1..20) {
                moveNote(notePlace, R.drawable.downarrowsmall, uparrowIB, downarrowIB)
                muse[((currentPage * 4) + ((noteCounter- 1) % 4))].positionOnScale--
            }
        }
        notesrestsBT.setOnClickListener{
            if(notes){//if notes are being displayed, display rests
                wholenoteIB.setImageResource(R.drawable.wholerestsmall)
                halfnoteIB.setImageResource(R.drawable.halfrestsmall)
                quarternoteIB.setImageResource(R.drawable.quarterrestsmall)
                eighthnoteIB.setImageResource(R.drawable.eighthrestsmall)
                sixteenthnoteIB.setImageResource(R.drawable.sixteenthrestsmall)
                notes = false//set notes boolean to false
            }
            else{//if rests are being displayed, display notes
                wholenoteIB.setImageResource(R.drawable.wholenote)
                halfnoteIB.setImageResource(R.drawable.halfnote)
                quarternoteIB.setImageResource(R.drawable.quarternote)
                eighthnoteIB.setImageResource(R.drawable.eighthnote)
                sixteenthnoteIB.setImageResource(R.drawable.sixteenthnote)
                notes = true//set notes boolean to true
            }
        }
    }

    //createNote creates a visual and class instatiation of a note, and returns the note to be added to the vector
    fun createNote(currentIV : ImageView, currentImage : Int, uparrow : ImageButton, downarrow : ImageButton, notes : Boolean) : Note {
        noteCounter++//increment note counter
        var tempNote = Note("half", 15)//create note to be returned
        when(currentImage){//return string version of currentImage
            R.drawable.wholenotesmall -> tempNote.noteType = "whole"
            R.drawable.halfnotesmall -> tempNote.noteType = "half"
            R.drawable.quarternotesmall -> tempNote.noteType = "quarter"
            R.drawable.eighthnotesmall -> tempNote.noteType = "eighth"
            R.drawable.sixteenthnotesmall -> tempNote.noteType = "sixteenth"
        }
        currentIV?.let {//if the current image view exists(if it dosent we have a problem...)
            if (currentImage == R.drawable.wholenotesmall) {//if that image is wholenotesmall
                uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                currentIV.scaleX = .5.toFloat()//scale image view to accomedate smaller image of whole note
                currentIV.scaleY = .5.toFloat()//scale image view to accomedate smaller image of whole note
                currentIV.y = currentIV.y + 35// increase y position of current image to make scaling appear to not happen
            }
            else {// otherwise, set scale to default
                currentIV.scaleX = 1.toFloat()
                currentIV.scaleY = 1.toFloat()
            }
            it.setImageResource(currentImage)// set the current image view to the current image
            it.visibility = View.VISIBLE// make current image view visibile
            uparrow.y = currentIV.y - 55// set y position of up arrow to above the current image view
            downarrow.y = currentIV.y + 100//set y position of down arrow to below the current image view
            uparrow.x = currentIV.x//set x position of up arrow to the x of the current image view
            downarrow.x = currentIV.x// set x position of down arrow to x position of current image view
            if(notes) {// if the note being created is a note and not a rest, make arrows visibile
                    uparrow.visibility = View.VISIBLE
                    downarrow.visibility = View.VISIBLE
            }
            else{// if note being created is a rest, make arrows invisible
                    uparrow.visibility = View.INVISIBLE
                    downarrow.visibility = View.INVISIBLE
            }
        }
        return tempNote // return the temporary note created
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

    //moves note on up or down arrow button press
    fun moveNote(currentIV : ImageView,currentImage : Int, uparrow : ImageButton, downarrow : ImageButton){
        if(currentImage == R.drawable.uparrowsmall)// if up arrow was pressed, move current image view and up and down arrows up
        {
            uparrow.y = uparrow.y -14
            downarrow.y=downarrow.y - 14
            currentIV.y = currentIV.y - 14
        }
        else{// if down arrow was pressed, move current image view, up arrow, and down arrow down
            uparrow.y = uparrow.y +14
            downarrow.y=downarrow.y + 14
            currentIV.y = currentIV.y + 14
        }
    }
}

//function to fix downwards drift if adjusting a note from a whole note.
    fun fromWholeNote(currentIV : ImageView, uparrow : ImageButton, downarrow : ImageButton){
        currentIV.y = currentIV.y-35
        downarrow.y = downarrow.y-35
        uparrow.y = uparrow.y-35
    }
