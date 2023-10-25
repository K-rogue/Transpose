package com.zybooks.transpose

import Note
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import java.util.Vector


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var detector: GestureDetectorCompat
    //decleration of global variables variables
    var noteCounter : Int = 0 //note counter is used to determine which image view to place the next note in - - should be replaced with vector.size once vector of objects is created
    lateinit var notePlace : ImageView //notePlace will be used as a variable to hold the place of the next note to be placed on the scale
    var currentPage : Int = 0//keeps track of what page you are on from left/right swipe features
    var muse = Vector<Note>()// vector of notes that will be in charge of storing created music
    lateinit var noteoneIV : ImageView
    lateinit var notetwoIV : ImageView
    lateinit var notethreeIV : ImageView
    lateinit var notefourIV : ImageView
    lateinit var uparrowIB : ImageButton
    lateinit var downarrowIB : ImageButton
    lateinit var sharpIB : ImageButton
    lateinit var natrualIB : ImageButton
    lateinit var flatIB : ImageButton
    lateinit var pagesTV : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        detector = GestureDetectorCompat(this,GestureListener())

        // declare vals for buttons and image views
        pagesTV = findViewById<TextView>(R.id.pagesTV)
        val wholenoteIB : ImageButton = findViewById<ImageButton>(R.id.wholenoteIB)
        val halfnoteIB : ImageButton = findViewById<ImageButton>(R.id.halfnoteIB)
        val quarternoteIB : ImageButton = findViewById<ImageButton>(R.id.quarternoteIB)
        val eighthnoteIB : ImageButton = findViewById<ImageButton>(R.id.eighthnoteIB)
        val sixteenthnoteIB : ImageButton = findViewById<ImageButton>(R.id.sixteenthnoteIB)
        noteoneIV = findViewById<ImageView>(R.id.noteoneIV)
        notetwoIV = findViewById<ImageView>(R.id.notetwoIV)
        notethreeIV = findViewById<ImageView>(R.id.notethreeIV)
        notefourIV = findViewById<ImageView>(R.id.notefourIV)
        uparrowIB = findViewById<ImageButton>(R.id.uparrowIB)
        downarrowIB = findViewById<ImageButton>(R.id.downarrowIB)
        val notesrestsBT : Button = findViewById<Button>(R.id.notesrestsBT)
         sharpIB = findViewById<ImageButton>(R.id.sharpIB)
         natrualIB = findViewById<ImageButton>(R.id.natrualIB)
         flatIB = findViewById<ImageButton>(R.id.flatIB)
        val sharpIB : ImageButton = findViewById<ImageButton>(R.id.sharpIB)
        val natrualIB : ImageButton = findViewById<ImageButton>(R.id.natrualIB)
        val flatIB : ImageButton = findViewById<ImageButton>(R.id.flatIB)
        val spin : Spinner = findViewById<Spinner>(R.id.spinner)
        var oldSelectedItem = "CM_am"

        // decleration of variables
        var notes : Boolean = true // used to keep track of if notes or rests are being displayed
        var tempNote = Note("whole", 3) // will be used as a note to edit before adding to vector of notes
        muse.ensureCapacity(100) //sets default minimum capacity to 100(not sure if we need this... vectors in kotlin are weird
        var pages : Int = 0//keeps track of how many pages there are
        var oldAccidental : Char = ' '


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
        //emables accidental buttons
        fun enableAccidentals(){

            sharpIB.isEnabled = true
            natrualIB.isEnabled = true
            flatIB.isEnabled = true
        }
        //disables accidental buttons
        fun disableAccidentals(){

            sharpIB.isEnabled = false
            natrualIB.isEnabled = false
            flatIB.isEnabled = false
        }

        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                var oldKey = KeySignature.valueOf(oldSelectedItem)
                var newKey = KeySignature.valueOf(selectedItem)
                var UIChanges : Pair<Int,Char>
                // Handle the item selected here
                // For example:
                // Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()

                for (i in muse){
                    notePlace = determineIV()
                    UIChanges = transpose(i,oldKey,newKey);
                    if (UIChanges.first > 0){
                        for (j in 0.. UIChanges.first) {
                            moveNote(i, notePlace, R.drawable.uparrowsmall, uparrowIB, downarrowIB)
                        }
                    }
                    else if (UIChanges.first < 0){
                        for (j in 0.. UIChanges.first){
                            moveNote(i, notePlace, R.drawable.downarrowsmall, uparrowIB, downarrowIB)
                            }
                    }
                    else{

                    }
                    if (UIChanges.second == 's'){
                        //have sharp appear
                    }
                    else if (UIChanges.second == 'b'){
                        //have flat appear
                    }

                    noteCounter++
                }
                //Change staff to key signature
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle scenario if needed where no item is selected
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
        uparrowIB.isEnabled = false
        downarrowIB.isEnabled = false
        sharpIB.isEnabled = false
        natrualIB.isEnabled = false
        flatIB.isEnabled = false

        //creation of on click listeners
        sharpIB.setOnClickListener{
            when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental) {
                ' ','n','b' ->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                        "whole"-> notePlace.setImageResource(R.drawable.wholenotesharp)
                        "half" -> notePlace.setImageResource(R.drawable.halfnotesharp)
                        "quarter" -> notePlace.setImageResource(R.drawable.quarternotesharp)
                        "eighth" -> notePlace.setImageResource(R.drawable.eighthnotesharp)
                        "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnotesharp)
                    }
                    oldAccidental = muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = 's'
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.5.toFloat()
                        notePlace.scaleY = 1.5.toFloat()
                    }
                    else{
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                        if(oldAccidental == ' ')
                            notePlace.y= notePlace.y -25
                    }
                }
                's'->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                    "whole"-> notePlace.setImageResource(R.drawable.wholenotesmall)
                    "half" -> notePlace.setImageResource(R.drawable.halfnotesmall)
                    "quarter" -> notePlace.setImageResource(R.drawable.quarternotesmall)
                    "eighth" -> notePlace.setImageResource(R.drawable.eighthnotesmall)
                    "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnotesmall)
                }
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = ' '
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                    }
                    else{
                        notePlace.scaleX = .5.toFloat()
                        notePlace.scaleY = .5.toFloat()
                        notePlace.y= notePlace.y +25
                    }
                }
            }
        }
        natrualIB.setOnClickListener{
            when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental) {
                ' ','s','b' ->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                        "whole"-> notePlace.setImageResource(R.drawable.wholenotenatural)
                        "half" -> notePlace.setImageResource(R.drawable.halfnotenatural)
                        "quarter" -> notePlace.setImageResource(R.drawable.quarternotenatural)
                        "eighth" -> notePlace.setImageResource(R.drawable.eighthnotenatural)
                        "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnotenatural)
                    }
                    oldAccidental = muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = 'n'
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.5.toFloat()
                        notePlace.scaleY = 1.5.toFloat()
                    }
                    else {
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                        if(oldAccidental == ' ')
                            notePlace.y = notePlace.y - 25
                    }
                }
                'n'->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                        "whole"-> notePlace.setImageResource(R.drawable.wholenotesmall)
                        "half" -> notePlace.setImageResource(R.drawable.halfnotesmall)
                        "quarter" -> notePlace.setImageResource(R.drawable.quarternotesmall)
                        "eighth" -> notePlace.setImageResource(R.drawable.eighthnotesmall)
                        "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnotesmall)
                    }
                    oldAccidental = muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = ' '
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                    }
                    else{
                        notePlace.scaleX = .5.toFloat()
                        notePlace.scaleY = .5.toFloat()
                        notePlace.y= notePlace.y +25
                    }
                }
            }
        }
        flatIB.setOnClickListener{
            when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental) {
                ' ','n','s' ->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                        "whole"-> notePlace.setImageResource(R.drawable.wholenoteflat)
                        "half" -> notePlace.setImageResource(R.drawable.halfnoteflat)
                        "quarter" -> notePlace.setImageResource(R.drawable.quarternoteflat)
                        "eighth" -> notePlace.setImageResource(R.drawable.eighthnoteflat)
                        "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnoteflat)
                    }
                    oldAccidental = muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = 'b'
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.5.toFloat()
                        notePlace.scaleY = 1.5.toFloat()
                    }
                    else {
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                        if(oldAccidental == ' ')
                            notePlace.y = notePlace.y - 25
                    }
                }
                'b'->{
                    when(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType){
                        "whole"-> notePlace.setImageResource(R.drawable.wholenotesmall)
                        "half" -> notePlace.setImageResource(R.drawable.halfnotesmall)
                        "quarter" -> notePlace.setImageResource(R.drawable.quarternotesmall)
                        "eighth" -> notePlace.setImageResource(R.drawable.eighthnotesmall)
                        "sixteenth"-> notePlace.setImageResource(R.drawable.sixteenthnotesmall)
                    }
                    muse[((currentPage * 4) + ((noteCounter-1) % 4))].accidental = ' '
                    if(muse[((currentPage * 4) + ((noteCounter-1) % 4))].noteType != "whole") {
                        notePlace.scaleX = 1.toFloat()
                        notePlace.scaleY = 1.toFloat()
                    }
                    else{
                        notePlace.scaleX = .5.toFloat()
                        notePlace.scaleY = .5.toFloat()
                        if(oldAccidental == ' ')
                        notePlace.y= notePlace.y +25
                    }
                }
            }
        }
        wholenoteIB.setOnClickListener{
            notePlace = determineIV()// determines the place
            if(notes) {//if motes are being displayed
                tempNote = createNote(notePlace, R.drawable.wholenotesmall, uparrowIB, downarrowIB, notes) // set tempNote to the result of the createNote function
                enableAccidentals()
            }
            else{//if rests are being displayed
                tempNote = createNote(notePlace, R.drawable.wholerestsmall, uparrowIB, downarrowIB, notes)// set tempNote to result of the createNote function
                disableAccidentals()
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {//if the size of the vector(size equals 0 on first call as no note has been added yet) is larger than the number of pages(with 4 notes each) plus the number of notes on the current page, then a note was edited, not created
                muse[((currentPage * 4) + ((noteCounter - 1) % 4))] = tempNote//set the appropriate vector place to the temp note
            }
            else{// if the number of notes on the page plus the number of pages*4 notes per page is equal to the size of the vector, then a new note was created
                muse.addElement(tempNote)// so add that new note to the vector
            }
        }
        halfnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with halfnote image
            notePlace = determineIV()

            if(notes) {
                tempNote = createNote(notePlace, R.drawable.halfnotesmall, uparrowIB, downarrowIB, notes)
                enableAccidentals()
            }
            else{
                tempNote = createNote(notePlace, R.drawable.halfrestsmall, uparrowIB, downarrowIB, notes)
                disableAccidentals()
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                muse[((currentPage * 4) + ((noteCounter - 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        quarternoteIB.setOnClickListener {//same as wholeNoteIB but createNote is called with quarternote image
            notePlace = determineIV()

            if (notes) {
                tempNote = createNote(notePlace, R.drawable.quarternotesmall, uparrowIB, downarrowIB, notes)
                enableAccidentals()
            }
            else{
                tempNote = createNote(notePlace, R.drawable.quarterrestsmall, uparrowIB, downarrowIB, notes)
                disableAccidentals()
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                muse[((currentPage * 4) + ((noteCounter - 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        eighthnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with eighthNote image
            notePlace = determineIV()

            if(notes) {
                tempNote = createNote(notePlace, R.drawable.eighthnotesmall, uparrowIB, downarrowIB, notes)
                enableAccidentals()
            }
            else{
                tempNote = createNote(notePlace, R.drawable.eighthrestsmall, uparrowIB, downarrowIB, notes)
                disableAccidentals()
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                muse[((currentPage * 4) + ((noteCounter - 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        sixteenthnoteIB.setOnClickListener{//same as wholeNoteIB but createNote is called with sixteenthNote image
            notePlace = determineIV()

            if(notes) {
                tempNote = createNote(notePlace,R.drawable.sixteenthnotesmall,uparrowIB,downarrowIB, notes)
                enableAccidentals()
            }
            else{
                tempNote = createNote(notePlace, R.drawable.sixteenthrestsmall, uparrowIB, downarrowIB, notes)
                disableAccidentals()
            }
            if(muse.size > ((currentPage * 4) + ((noteCounter- 1) % 4))) {
                muse[((currentPage * 4) + ((noteCounter - 1) % 4))] = tempNote
            }
            else{
                muse.addElement(tempNote)
            }
        }
        uparrowIB.setOnClickListener{
            moveNote(muse[((currentPage * 4) + ((noteCounter - 1) % 4))], notePlace, R.drawable.uparrowsmall, uparrowIB, downarrowIB)
        }
        downarrowIB.setOnClickListener {//same as up arrow but inversed top/bottom
            moveNote(muse[((currentPage * 4) + ((noteCounter - 1) % 4))], notePlace, R.drawable.downarrowsmall, uparrowIB, downarrowIB)
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
        var tempNote = Note("half", 17)//create note to be returned
        when(currentImage){//return string version of currentImage
            R.drawable.wholenotesmall -> tempNote.noteType = "whole"
            R.drawable.halfnotesmall -> tempNote.noteType = "half"
            R.drawable.quarternotesmall -> tempNote.noteType = "quarter"
            R.drawable.eighthnotesmall -> tempNote.noteType = "eighth"
            R.drawable.sixteenthnotesmall -> tempNote.noteType = "sixteenth"
        }
        currentIV?.let {//if the current image view exists(if it dosent we have a problem...)
            currentIV.y = 65.toFloat()
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
                    uparrow.isEnabled = true
                    downarrow.isEnabled = true
            }
            else{// if note being created is a rest, make arrows invisible
                    uparrow.visibility = View.INVISIBLE
                    downarrow.visibility = View.INVISIBLE
                    uparrow.isEnabled = false
                    downarrow.isEnabled = false
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
        note.forcePositionOnScale(note.positionOnScale - difference, ' ') //Update note
        return Pair(UIDifference, flag) //Return pair of visual steps with accidental
    }

    fun getUIDifference(note: Note, fromKey: KeySignature, toKey: KeySignature ) : Pair<Int, Char> {
        var UIDifference : Int = 0 //Visual number of steps
        var difference : Int = toKey.number - fromKey.number //Real number of steps
        var flag : Char = ' ' //n = no change. b = flat, s = sharp
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

        var newNoteName : String = availableNotes[((note.realValue % 12) + difference) % 12] //Get new note name from available notes
        val noteToVal : Array<Pair<String, Int>> //Holds visual difference values
        noteToVal = arrayOf(Pair("cb", 0), Pair("c", 0), Pair("cs", 0), Pair("db", 1), Pair("d", 1), Pair("ds", 1), Pair("eb", 2),
                            Pair("e", 2), Pair("es", 2), Pair("fb", 3), Pair("f", 3), Pair("fs", 3), Pair("gb", 4), Pair("g", 4),
                            Pair("gs", 4), Pair("ab", 5), Pair("a", 5), Pair("as", 5), Pair("bb", 6), Pair("b", 6), Pair("bs", 6))

        var firstNoteVal : Int = 0 //Original visual value
        var secondNoteVal : Int = 0 //New visual value

        for (i in noteToVal){
            if (note.getNoteName().substring(0,1) == i.first) //If name matches string in list, then set the visual value
                firstNoteVal = i.second
            if (newNoteName == i.first)
                secondNoteVal = i.second
        }

        UIDifference = secondNoteVal - firstNoteVal //Update visual difference

        if (newNoteName.length == 2){ //Sharp or flat notes have 2 characters, set flag
            if (newNoteName.substring(1,2) == "s")
                flag = 's'
            else
                flag = 'b'
        }

        if (toKey.accidents[secondNoteVal] != 0) //If the key signature holds a flat or sharp at this key, then remove the accidental
            flag = ' '

        return Pair(UIDifference, flag)

    }

    //moves note on up or down arrow button press
    fun moveNote(note: Note, currentIV : ImageView,currentImage : Int, uparrow : ImageButton, downarrow : ImageButton){
        if(currentImage == R.drawable.uparrowsmall)// if up arrow was pressed, move current image view and up and down arrows up
        {
            if (note.positionOnScale == 10) {
                uparrow.y = uparrow.y - (10 * 14)
                downarrow.y = downarrow.y - (10 * 14)
                currentIV.y = currentIV.y - (10 * 14)
                note.forcePositionOnScale(note.positionOnScale + 1, note.accidental)
            }
            else if(note.positionOnScale < 22){
                uparrow.y = uparrow.y - 14
                downarrow.y = downarrow.y - 14
                currentIV.y = currentIV.y - 14
                note.forcePositionOnScale(note.positionOnScale + 1, note.accidental)
            }
        }
        else{// if down arrow was pressed, move current image view, up arrow, and down arrow down
            if (note.positionOnScale == 11) {
                uparrow.y = uparrow.y + (10 * 14)
                downarrow.y = downarrow.y + (10 * 14)
                currentIV.y = currentIV.y + (10 * 14)
                note.forcePositionOnScale(note.positionOnScale - 1, note.accidental)
            }
            else if(note.positionOnScale >= 0){
                uparrow.y = uparrow.y + 14
                downarrow.y = downarrow.y + 14
                currentIV.y = currentIV.y + 14
                note.forcePositionOnScale(note.positionOnScale - 1, note.accidental)
            }
        }
    }
    fun swipeLeft(currentPage : Int, muse : Vector<Note>){

    }
    fun swipe(muse : Vector<Note>, direction : Boolean,uparrow : ImageButton, downarrow : ImageButton, noteOneIV : ImageView, noteTwoIV : ImageView, noteThreeIV : ImageView, noteFourIV : ImageView, flatIB : ImageButton, sharpIB : ImageButton, naturalIB : ImageButton, pagesTV : TextView){
        var noteViews = arrayOf(noteOneIV,noteTwoIV,noteThreeIV,noteFourIV)
        var tempPosOnScale = 17
        if(direction == true){// if swipe is right to left
            if((muse.size / 4) == (currentPage + 1)){// if the last page is full, and are you on it?
                uparrow.isEnabled = false
                downarrow.isEnabled = false
                uparrow.visibility = View.INVISIBLE
                downarrow.visibility = View.INVISIBLE
                noteOneIV.visibility = View.INVISIBLE
                noteTwoIV.visibility = View.INVISIBLE
                noteThreeIV.visibility = View.INVISIBLE
                noteFourIV.visibility = View.INVISIBLE
                sharpIB.isEnabled = false
                flatIB.isEnabled = false
                naturalIB.isEnabled = false
                noteCounter = 0
                currentPage++
                pagesTV.setText("Page:" + (currentPage + 1).toString())
            }
            else if((muse.size % 4) == 0 && muse.size != 0){//if your not on the last page, it is full, so you can swipe right
                currentPage++
                pagesTV.setText("Page:" + (currentPage + 1).toString())
                uparrow.isEnabled = false
                downarrow.isEnabled = false
                uparrow.visibility = View.INVISIBLE
                downarrow.visibility = View.INVISIBLE
                sharpIB.isEnabled = false
                flatIB.isEnabled = false
                naturalIB.isEnabled = false

                noteCounter = 0
                for(i in 0..3){
                    noteViews[i].y = 65F
                    noteViews[i].visibility = View.VISIBLE
                    when(muse[((currentPage * 4) + i)].accidental){
                        's'->{
                            when(muse[(currentPage * 4) + i].noteType) {
                                "whole" -> {
                                    noteViews[i].setImageResource(R.drawable.wholenotesharp)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotesharp)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotesharp)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotesharp)
                                "sixteenth" -> noteViews[i].setImageResource(R.drawable.sixteenthnotesharp)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        'n'->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"->{
                                    noteViews[i].setImageResource(R.drawable.wholenotenatural)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotenatural)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotenatural)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotenatural)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnotenatural)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        'b'->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"->{
                                    noteViews[i].setImageResource(R.drawable.wholenoteflat)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnoteflat)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternoteflat)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnoteflat)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnoteflat)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        ' '->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"-> {
                                    noteViews[i].setImageResource(R.drawable.wholenotesmall)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].scaleX = .5.toFloat()//scale image view to accomedate smaller image of whole note
                                    noteViews[i].scaleY = .5.toFloat()//scale image view to accomedate smaller image of whole note
                                    noteViews[i].y = noteViews[i].y + 35// increase y position of current image to make scaling appear to not happen
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotesmall)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotesmall)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotesmall)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnotesmall)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.toFloat()
                                noteViews[i].scaleY = 1.toFloat()
                            }
                        }
                    }
                    while(tempPosOnScale < muse[(currentPage * 4) + i].positionOnScale) {
                        tempPosOnScale++
                        if (tempPosOnScale == 11) {
                            noteViews[i].y = noteViews[i].y - (10 * 14)
                        }
                        else if(tempPosOnScale >= 0){
                            noteViews[i].y = noteViews[i].y - 14
                        }

                    }
                    tempPosOnScale = 17
                    while(tempPosOnScale > muse[(currentPage * 4) + i].positionOnScale){
                        tempPosOnScale--
                        if (tempPosOnScale == 11) {
                            noteViews[i].y = noteViews[i].y + (10 * 14)
                        }
                        else if(tempPosOnScale >= 0){
                            noteViews[i].y = noteViews[i].y + 14
                        }
                    }
                    tempPosOnScale = 17
                }
            }
        }
        else{// else swipe is left
            if(currentPage != 0 && muse.size % 4 == 0){
                currentPage--
                pagesTV.setText("Page:" + (currentPage + 1).toString())
                uparrow.isEnabled = false
                downarrow.isEnabled = false
                uparrow.visibility = View.INVISIBLE
                downarrow.visibility = View.INVISIBLE
                sharpIB.isEnabled = false
                flatIB.isEnabled = false
                naturalIB.isEnabled = false
                noteCounter = 0
                for(i in 0..3){
                    noteViews[i].y = 65F
                    noteViews[i].visibility = View.VISIBLE
                    when(muse[((currentPage * 4) + i)].accidental){
                        's'->{
                            when(muse[(currentPage * 4) + i].noteType) {
                                "whole" -> {
                                    noteViews[i].setImageResource(R.drawable.wholenotesharp)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotesharp)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotesharp)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotesharp)
                                "sixteenth" -> noteViews[i].setImageResource(R.drawable.sixteenthnotesharp)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        'n'->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"->{
                                    noteViews[i].setImageResource(R.drawable.wholenotenatural)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotenatural)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotenatural)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotenatural)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnotenatural)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        'b'->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"->{
                                    noteViews[i].setImageResource(R.drawable.wholenoteflat)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].y = noteViews[i].y + 10// increase y position of current image to make scaling appear to not happen
                                    noteViews[i].scaleY = 1.toFloat()
                                    noteViews[i].scaleX = 1.toFloat()
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnoteflat)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternoteflat)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnoteflat)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnoteflat)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.5.toFloat()
                                noteViews[i].scaleY = 1.5.toFloat()
                            }
                        }
                        ' '->{
                            when(muse[(currentPage * 4) + i].noteType){
                                "whole"-> {
                                    noteViews[i].setImageResource(R.drawable.wholenotesmall)
                                    uparrow.y = (uparrow.y + 60)//increase y position of uparrow
                                    noteViews[i].scaleX = .5.toFloat()//scale image view to accomedate smaller image of whole note
                                    noteViews[i].scaleY = .5.toFloat()//scale image view to accomedate smaller image of whole note
                                    noteViews[i].y = noteViews[i].y + 35// increase y position of current image to make scaling appear to not happen
                                }
                                "half" -> noteViews[i].setImageResource(R.drawable.halfnotesmall)
                                "quarter" -> noteViews[i].setImageResource(R.drawable.quarternotesmall)
                                "eighth" -> noteViews[i].setImageResource(R.drawable.eighthnotesmall)
                                "sixteenth"-> noteViews[i].setImageResource(R.drawable.sixteenthnotesmall)
                            }
                            if(muse[(currentPage * 4) + i].noteType != "whole") {
                                noteViews[i].scaleX = 1.toFloat()
                                noteViews[i].scaleY = 1.toFloat()
                            }
                        }
                    }
                    while(tempPosOnScale < muse[(currentPage * 4) + i].positionOnScale) {
                        tempPosOnScale++
                        if (tempPosOnScale == 11) {
                            noteViews[i].y = noteViews[i].y - (10 * 14)
                        }
                        else if(tempPosOnScale >= 0){
                            noteViews[i].y = noteViews[i].y - 14
                        }

                    }
                    tempPosOnScale = 17
                    while(tempPosOnScale > muse[(currentPage * 4) + i].positionOnScale){
                        tempPosOnScale--
                        if (tempPosOnScale == 11) {
                            noteViews[i].y = noteViews[i].y + (10 * 14)
                        }
                        else if(tempPosOnScale >= 0){
                            noteViews[i].y = noteViews[i].y + 14
                        }
                    }
                    tempPosOnScale = 17
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event.let{
            if(detector.onTouchEvent(event!!)){
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    inner class GestureListener : GestureDetector.SimpleOnGestureListener(){
        private val SWIPE_THRESHHOLD = 100F
        override fun onFling(down: MotionEvent, move: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var diffX = move?.x?.minus(down!!.x) ?: 0.0F
            return if(Math.abs(diffX) > SWIPE_THRESHHOLD){
                if(diffX > 0){//swipe right
                this@MainActivity.onSwipeRight()
                }
                else{//swipe left
                this@MainActivity.onSwipeLeft()
                }
                true
            }
            else {
                return super.onFling(down, move, velocityX, velocityY)
            }

        }
    }
    fun onSwipeLeft() {
        swipe(muse,true,uparrowIB,downarrowIB,noteoneIV,notetwoIV,notethreeIV,notefourIV,flatIB,sharpIB,natrualIB,pagesTV)
    }

    fun onSwipeRight() {
        swipe(muse,false,uparrowIB,downarrowIB,noteoneIV,notetwoIV,notethreeIV,notefourIV,flatIB,sharpIB,natrualIB,pagesTV)
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
/* Code to play note c2
        var myNote = Note("quarter", 0)
        myNote.loadSound(this)
        println("Requesting playback for note: " + myNote.getNoteName())
 */
