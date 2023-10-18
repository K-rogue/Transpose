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
            uparrow.x = currentIV.x
            downarrow.x = currentIV.x
            uparrow.bottom = currentIV.top
            downarrow.top = currentIV.bottom
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

    enum class KeySignature(val number: Int) {

        /*
        Legend
        M = major
        m = minor
        s = sharp
        b = flat
        sharps order = FCGDAEB
        flats order = BEADGCF
         */

        //No accidentals
        CM_am(0),

        //Sharps
        GM_em(7), //1 sharp
        DM_bm(2), //2 sharps
        AM_fsm(9), //3 sharps
        EM_csm(4), //4 sharps
        BM_gsm(11), //5 sharps
        FsM_dsm(5), //6 sharps
        CsM(1), //7 sharps

        //Flats
        FM_dm(5), //1 flat
        BbM_gm(10), //2 flats
        EbM_cm(3), //3 flats
        AbM_fm(8), //4 flats
        DbM_bbm(1), //5 flats
        GbM_ebm(6), //6 flats
        CbM(11) //7 flats
    }

    fun transpose(note: Note, fromKey: KeySignature, toKey: KeySignature){
        var difference : Int = toKey.number - fromKey.number;
        note.positionOnScale = note.positionOnScale - difference
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
