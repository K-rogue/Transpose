package com.zybooks.transpose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    //decleration of variables
    var noteCounter : Int = 1 //note counter is used to determine which image view to place the next note in - - should be replaced with vector.size once vector of objects is created
    var notePlace : ImageView? = null //notePlace will be used as a variable to hold the place of the next note to be placed on the scale


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
        fun determineIV() : ImageView? {
            when(noteCounter){
                1-> return noteoneIV
                2-> return notetwoIV
                3-> return notethreeIV
                4-> return notefourIV
                else -> return null
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
            createNote(notePlace,R.drawable.wholenotesmall)
        }
        halfnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.halfnotesmall)
        }
        quarternoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.quarternotesmall)
        }
        eighthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.eighthnotesmall)
        }
        sixteenthnoteIB.setOnClickListener{
            notePlace = determineIV()
            createNote(notePlace,R.drawable.sixteenthnotesmall)
        }
        uparrowIB.setOnClickListener{
            notePlace = determineIV()
        }
        downarrowIB.setOnClickListener{
            notePlace = determineIV()
        }
    }
    fun createNote(currentIV : ImageView?, currentImage : Int) {
        noteCounter++
        if(currentIV != null) {
            currentIV.setImageDrawable(currentImage.toDrawable())
            currentIV.visibility = View.VISIBLE
        }


    }
}
